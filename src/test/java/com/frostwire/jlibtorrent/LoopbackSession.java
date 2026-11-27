package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.ListenSucceededAlert;
import com.frostwire.jlibtorrent.alerts.SocketType;
import com.frostwire.jlibtorrent.swig.settings_pack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A real {@link SessionManager} bound to the loopback interface with every network discovery
 * mechanism (DHT, local service discovery, UPnP, NAT-PMP) switched off, so tests can run
 * anywhere without touching the network.
 * <p>
 * A libtorrent alert is only valid while the alert thread is delivering it, so nothing keeps the
 * alert objects: tests register an {@link #expect expectation} <em>before</em> they trigger the
 * event, and the alert thread evaluates it on the live alert. Every delivered alert also has all
 * its public no-argument accessors called, which both exercises them and records any accessor
 * that throws ({@link #accessorFailures()}).
 *
 * @author gubatron
 * @author aldenml
 */
final class LoopbackSession implements AutoCloseable {

    static final long TIMEOUT_SECONDS = 60;

    final SessionManager sm;
    private final Map<Class<?>, AtomicInteger> counts = new ConcurrentHashMap<>();
    private final List<String> accessorFailures = new CopyOnWriteArrayList<>();
    private final List<Expectation<?, ?>> expectations = new CopyOnWriteArrayList<>();
    private final CompletableFuture<Integer> listenPort = new CompletableFuture<>();

    private LoopbackSession(SessionManager sm) {
        this.sm = sm;
    }

    private static final class Expectation<T, R> {
        final Class<T> type;
        final Predicate<T> predicate;
        final Function<T, R> extractor;
        final CompletableFuture<R> future = new CompletableFuture<>();

        Expectation(Class<T> type, Predicate<T> predicate, Function<T, R> extractor) {
            this.type = type;
            this.predicate = predicate;
            this.extractor = extractor;
        }

        @SuppressWarnings("unchecked")
        void offer(Object alert) {
            if (!future.isDone() && type.isInstance(alert)) {
                T t = (T) alert;
                try {
                    if (predicate.test(t)) {
                        future.complete(extractor.apply(t));
                    }
                } catch (Throwable e) {
                    future.completeExceptionally(e);
                }
            }
        }
    }

    static LoopbackSession start() {
        return start(false, new SettingsPack());
    }

    static LoopbackSession start(boolean logging, SettingsPack sp) {
        return start(new SessionManager(logging), sp);
    }

    /** Like {@link #start(boolean, SettingsPack)} for a subclass of the manager. */
    static LoopbackSession start(SessionManager sm, SettingsPack sp) {
        sp.listenInterfaces("127.0.0.1:0");
        // an empty list stops the manager from filling in the public bootstrap routers
        sp.setDhtBootstrapNodes("");
        sp.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_lsd.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_upnp.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_natpmp.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.allow_multiple_connections_per_ip.swigValue(), true);
        return startWith(sm, new SessionParams(sp));
    }

    /** Starts the manager with exactly the given params (no loopback settings are applied). */
    static LoopbackSession startWith(SessionManager sm, SessionParams params) {
        LoopbackSession s = new LoopbackSession(sm);
        sm.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                s.deliver(alert);
            }
        });
        // the manager keeps these three out of the catch-all listener
        sm.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.SESSION_STATS.swig(), AlertType.STATE_UPDATE.swig(),
                        AlertType.SESSION_STATS_HEADER.swig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                s.deliver(alert);
            }
        });
        sm.start(params);
        return s;
    }

    private void deliver(Alert<?> alert) {
        counts.computeIfAbsent(alert.getClass(), k -> new AtomicInteger()).incrementAndGet();
        touchAccessors(alert);
        if (alert instanceof ListenSucceededAlert) {
            ListenSucceededAlert l = (ListenSucceededAlert) alert;
            if (l.socketType() == SocketType.TCP && l.address().isLoopback()) {
                listenPort.complete(l.port());
            }
        }
        for (Expectation<?, ?> e : expectations) {
            e.offer(alert);
        }
    }

    private void touchAccessors(Alert<?> alert) {
        accessorFailures.addAll(Accessors.touch(alert));
    }

    /** Accessors that threw when the alert thread touched them. */
    List<String> accessorFailures() {
        return new ArrayList<>(accessorFailures);
    }

    /** The TCP loopback port the session listens on. */
    int listenPort() {
        return get(listenPort, "the listen alert");
    }

    TcpEndpoint endpoint() {
        return new TcpEndpoint("127.0.0.1", listenPort());
    }

    /**
     * Registers an expectation for the next alert of {@code type} matching {@code predicate};
     * the result of {@code extractor} on the live alert is what the future completes with.
     * Register it before triggering the event.
     */
    <T extends Alert<?>, R> CompletableFuture<R> expect(Class<T> type, Predicate<T> predicate, Function<T, R> extractor) {
        Expectation<T, R> e = new Expectation<>(type, predicate, extractor);
        expectations.add(e);
        return e.future;
    }

    <T extends Alert<?>> CompletableFuture<Boolean> expect(Class<T> type, Predicate<T> predicate) {
        return expect(type, predicate, a -> Boolean.TRUE);
    }

    <T extends Alert<?>> CompletableFuture<Boolean> expect(Class<T> type) {
        return expect(type, a -> true, a -> Boolean.TRUE);
    }

    static <R> R get(CompletableFuture<R> f, String what) {
        return get(f, what, TIMEOUT_SECONDS);
    }

    static <R> R get(CompletableFuture<R> f, String what, long seconds) {
        try {
            return f.get(seconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new AssertionError("timed out waiting for " + what);
        } catch (ExecutionException e) {
            throw new AssertionError("failed waiting for " + what + ": " + e.getCause(), e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
    }

    /** How many alerts of the class were delivered so far. */
    int count(Class<? extends Alert<?>> type) {
        AtomicInteger c = counts.get(type);
        return c == null ? 0 : c.get();
    }

    boolean hasSeen(Class<? extends Alert<?>> type) {
        return count(type) > 0;
    }

    /** Polls the condition until it holds or the timeout passes; the poll interval is only a back-off. */
    static void awaitCondition(String what, BooleanSupplier condition) {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
        while (!condition.getAsBoolean()) {
            if (System.nanoTime() > deadline) {
                throw new AssertionError("timed out waiting for " + what);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public void close() {
        sm.stop();
    }
}
