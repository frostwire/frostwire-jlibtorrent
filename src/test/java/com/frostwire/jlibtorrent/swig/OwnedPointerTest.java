package com.frostwire.jlibtorrent.swig;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The exactly-once rules of {@link OwnedPointer} (frostwire/frostwire-jlibtorrent#337), with fake
 * addresses and counting deleters instead of native objects.
 */
public class OwnedPointerTest {

    @Test
    public void freeCallsTheDeleterOnce() {
        AtomicInteger frees = new AtomicInteger();
        Object proxy = new Object();
        OwnedPointer p = OwnedPointer.adopt(proxy, 42, ptr -> {
            assertEquals(42, ptr);
            frees.incrementAndGet();
        });
        assertTrue(p.armed());
        p.free();
        p.free();
        assertFalse(p.armed());
        assertEquals(1, frees.get());
        assertEquals(0, p.release());
    }

    @Test
    public void anUnreachableProxyIsFreedOnce() throws Exception {
        AtomicInteger frees = new AtomicInteger();
        OwnedPointer p = OwnedPointer.adopt(new Object(), 42, ptr -> frees.incrementAndGet());
        assertTrue("the cleaner never freed it", collectUntil(() -> frees.get() == 1));
        settle();
        assertEquals(1, frees.get());
        assertFalse(p.armed());
        p.free(); // delete() after the cleaner: nothing left to free
        assertEquals(1, frees.get());
    }

    @Test
    public void freeThenCollectionDoesNotFreeAgain() throws Exception {
        AtomicInteger frees = new AtomicInteger();
        Object proxy = new Object();
        OwnedPointer p = OwnedPointer.adopt(proxy, 42, ptr -> frees.incrementAndGet());
        p.free();
        proxy = null;
        settle();
        assertEquals(1, frees.get());
    }

    @Test
    public void releaseDisarmsTheCleanup() throws Exception {
        AtomicInteger frees = new AtomicInteger();
        OwnedPointer p = OwnedPointer.adopt(new Object(), 42, ptr -> frees.incrementAndGet());
        assertEquals(42, p.release());
        assertFalse(p.armed());
        assertEquals(0, p.release());
        settle();
        p.free();
        assertEquals(0, frees.get());
    }

    @Test
    public void aNullPointerIsNeverFreed() throws Exception {
        AtomicInteger frees = new AtomicInteger();
        OwnedPointer p = OwnedPointer.adopt(new Object(), 0, ptr -> frees.incrementAndGet());
        assertFalse(p.armed());
        p.free();
        settle();
        assertEquals(0, frees.get());
    }

    // Threads free half of the pointers explicitly while their proxies are dropped and collected:
    // whoever gets there first frees, nobody frees twice.
    @Test
    public void racingDeleteAndTheCleanerFreeEachPointerOnce() throws Exception {
        int n = 200_000;
        AtomicIntegerArray frees = new AtomicIntegerArray(n + 1);
        List<OwnedPointer> pointers = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            pointers.add(OwnedPointer.adopt(new Object(), i, ptr -> frees.incrementAndGet((int) ptr)));
        }
        int threads = 4;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<?>> done = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            int first = t;
            done.add(pool.submit(() -> {
                for (int i = first; i < n; i += threads * 2) {
                    pointers.get(i).free();
                    if (i % 1000 == 0) {
                        System.gc();
                    }
                }
            }));
        }
        for (Future<?> f : done) {
            f.get();
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue("the cleaner didn't free every pointer", collectUntil(() -> {
            for (int i = 1; i <= n; i++) {
                if (frees.get(i) == 0) {
                    return false;
                }
            }
            return true;
        }));
        for (int i = 1; i <= n; i++) {
            assertEquals("pointer " + i, 1, frees.get(i));
        }
    }

    // Several collections, with time for the cleaner to run after each.
    private static void settle() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.gc();
            Thread.sleep(100);
        }
    }

    // GC until the condition holds, up to 10 s, then leaves the cleaner a little more time.
    private static boolean collectUntil(BooleanSupplier done) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        boolean ok;
        while (!(ok = done.getAsBoolean()) && System.nanoTime() < deadline) {
            System.gc();
            Thread.sleep(20);
        }
        if (!ok) {
            return false;
        }
        System.gc();
        Thread.sleep(100);
        return true;
    }
}
