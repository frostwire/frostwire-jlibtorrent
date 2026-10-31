package com.frostwire.jlibtorrent.swig;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.function.LongConsumer;

/**
 * The native object a SWIG proxy owns, freed exactly once (frostwire/frostwire-jlibtorrent#337).
 * <p>
 * This file is hand-written: run-swig.sh only replaces the files SWIG generates. The typemaps in
 * {@code swig/includes/libtorrent_ownership.i} give every proxy one of three states:
 * <ul>
 * <li><b>Owned</b> ({@code swigCMemOwn} true): the proxy holds an armed {@code OwnedPointer}. The
 * native object is freed by whichever comes first: {@code delete()}, or the cleaner thread once
 * the proxy is unreachable. Java's {@code new}, by-value returns and {@code %newobject} results are
 * owned.</li>
 * <li><b>Borrowed</b> ({@code swigCMemOwn} false): the proxy points into memory something else
 * owns, such as {@code torrent_info.files()} or a vector element. It holds no
 * {@code OwnedPointer}, and keeps the proxy it was borrowed from reachable
 * ({@code swigBorrowedFrom}), so the owner can't be freed under it. {@code delete()} on the owner
 * still frees it right away: like dropping a value in Rust, that ends every borrow of it.</li>
 * <li><b>Transferred</b>: {@code swigRelease()} hands the object to C++. {@link #release()}
 * disarms the cleanup, and the proxy becomes unusable.</li>
 * </ul>
 * Invariant: {@code ptr} goes from the native address to 0 once, through {@code getAndSet}; only
 * the caller that gets the address frees it ({@link #free()}) or hands it over
 * ({@link #release()}). So {@code delete()}, {@code swigRelease()} and the cleaner can race in any
 * order and the object is freed at most once, never after it was handed over.
 * <p>
 * The cleanup holds the address and a static deleter, never the proxy, so the proxy can become
 * phantom reachable. It works on every Java and Android version jlibtorrent supports
 * ({@code java.lang.ref.Cleaner} needs Android API 33).
 */
final class OwnedPointer extends PhantomReference<Object> {

    private static final ReferenceQueue<Object> QUEUE = new ReferenceQueue<>();
    // A PhantomReference that is itself unreachable is never enqueued: keep the armed ones here.
    private static final Set<OwnedPointer> ARMED = ConcurrentHashMap.newKeySet();
    private static final AtomicLongFieldUpdater<OwnedPointer> PTR =
            AtomicLongFieldUpdater.newUpdater(OwnedPointer.class, "ptr");

    static {
        Thread cleaner = new Thread(OwnedPointer::run, "jlibtorrent-cleaner");
        cleaner.setDaemon(true);
        cleaner.start();
    }

    private volatile long ptr;
    private final LongConsumer deleter;

    private OwnedPointer(Object proxy, long ptr, LongConsumer deleter) {
        super(proxy, QUEUE);
        this.ptr = ptr;
        this.deleter = deleter;
    }

    /**
     * Makes {@code proxy} the owner of the native object at {@code ptr}: {@code deleter} frees it
     * once, after {@link #free()} or after {@code proxy} becomes unreachable, whichever is first.
     *
     * @param proxy   the proxy that owns the object; it is not referenced strongly
     * @param ptr     the address of the native object (0: nothing to free)
     * @param deleter frees the object at an address; must not reference {@code proxy}
     * @return the owned pointer, for the proxy to keep
     */
    static OwnedPointer adopt(Object proxy, long ptr, LongConsumer deleter) {
        OwnedPointer p = new OwnedPointer(proxy, ptr, deleter);
        if (ptr != 0) {
            ARMED.add(p);
        }
        return p;
    }

    /**
     * Frees the native object now, unless it was already freed or released. Called by
     * {@code delete()} and by the cleaner thread.
     */
    void free() {
        long p = take();
        if (p != 0) {
            deleter.accept(p);
        }
    }

    /**
     * Gives up ownership without freeing: the native object now belongs to C++.
     *
     * @return the address, or 0 if the object was already freed or released
     */
    long release() {
        return take();
    }

    /**
     * @return true until the object is freed or released
     */
    boolean armed() {
        return ptr != 0;
    }

    private long take() {
        long p = PTR.getAndSet(this, 0);
        if (p != 0) {
            ARMED.remove(this);
            clear();
        }
        return p;
    }

    private static void run() {
        while (true) {
            try {
                ((OwnedPointer) QUEUE.remove()).free();
            } catch (InterruptedException e) {
                // nothing else frees unreachable proxies: keep going
            } catch (Throwable e) {
                // A destructor failed. Report it without stopping the thread.
                Thread t = Thread.currentThread();
                t.getUncaughtExceptionHandler().uncaughtException(t, e);
            }
        }
    }
}
