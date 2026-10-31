package com.frostwire.jlibtorrent.swig;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.TorrentInfo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Scenarios for {@link NativeOwnershipTest} that can crash the JVM (use after free, double free), so
 * the test runs each one in its own JVM: {@code java ... OwnershipScenarios <name>}.
 * <p>
 * A scenario prints {@code key=value} facts on stdout and exits 0, or exits 1 after printing
 * {@code FAIL ...} when it reads wrong data. A native crash ends the process with a signal.
 */
public final class OwnershipScenarios {

    private OwnershipScenarios() {
    }

    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "borrowedFileStorage" -> borrowedFileStorage();
            case "borrowedEntryViews" -> borrowedEntryViews();
            case "deleteThenCollect" -> deleteThenCollect();
            case "releaseDisarmsCleanup" -> releaseDisarmsCleanup();
            case "manyThreads" -> manyThreads();
            default -> throw new IllegalArgumentException(args[0]);
        }
        System.out.println("DONE");
        System.out.flush();
    }

    // torrent_info.files() returns a file_storage that points into the torrent_info. Dropping the
    // torrent_info while keeping the file_storage must not free the memory under it.
    private static void borrowedFileStorage() throws Exception {
        byte[] data = resource("/noaa-ncei-national-marine-sanctuary-program-academic-torrents.torrent");
        torrent_info ti = new TorrentInfo(data).swig();
        file_storage fs = ti.files();
        int numFiles = fs.num_files();
        long totalSize = fs.total_size();
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < numFiles; i++) {
            paths.add(fs.file_path(i));
        }
        WeakReference<torrent_info> owner = new WeakReference<>(ti);
        ti = null;

        boolean collected = collect(() -> owner.get() == null);
        churn(data);

        for (int round = 0; round < 200; round++) {
            check(fs.num_files() == numFiles, "num_files " + fs.num_files() + " != " + numFiles);
            check(fs.total_size() == totalSize, "total_size " + fs.total_size() + " != " + totalSize);
            for (int i = 0; i < numFiles; i++) {
                String p = fs.file_path(i);
                check(paths.get(i).equals(p), "file_path(" + i + ") " + p + " != " + paths.get(i));
            }
            if (round % 50 == 0) {
                churn(data);
            }
        }
        System.out.println("numFiles=" + numFiles);
        System.out.println("ownerCollected=" + collected);
    }

    // entry.dict() returns the entry's own map, entry.list() its own vector, and entry_vector.get()
    // an element of the vector: all three point into the entry.
    private static void borrowedEntryViews() throws Exception {
        byte[] data = resource("/noaa-ncei-national-marine-sanctuary-program-academic-torrents.torrent");
        entry e = Entry.bdecode(data).swig();
        string_entry_map dict = e.dict();
        entry announceList = e.get("announce-list");
        entry_vector tiers = announceList.list();
        entry firstTier = tiers.get(0);
        String announce = dict.get("announce").string();
        int numTiers = tiers.size();
        String firstTracker = firstTier.list().get(0).string();
        WeakReference<entry> owner = new WeakReference<>(e);
        e = null;
        announceList = null;

        boolean collected = collect(() -> owner.get() == null);
        churn(data);

        for (int round = 0; round < 2000; round++) {
            check(announce.equals(dict.get("announce").string()), "announce");
            check(tiers.size() == numTiers, "tiers " + tiers.size() + " != " + numTiers);
            check(firstTracker.equals(firstTier.list().get(0).string()), "first tracker");
            if (round % 500 == 0) {
                churn(data);
            }
        }
        System.out.println("numTiers=" + numTiers);
        System.out.println("ownerCollected=" + collected);
    }

    // delete() frees the object; the cleaner must not free it again once the proxy is collected.
    private static void deleteThenCollect() throws Exception {
        byte[] data = resource("/bittorrent-v2-test.torrent");
        List<WeakReference<Object>> refs = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            torrent_info ti = new TorrentInfo(data).swig();
            entry e = Entry.bdecode(data).swig();
            refs.add(new WeakReference<>(ti));
            refs.add(new WeakReference<>(e));
            ti.delete();
            ti.delete(); // idempotent
            e.delete();
        }
        boolean collected = collect(() -> refs.stream().allMatch(r -> r.get() == null));
        churn(data);
        System.out.println("allCollected=" + collected);
    }

    // swigRelease() hands the object to C++: the cleaner must not free it after the proxy is
    // collected. Here "C++" is this scenario, which reads it and frees it once, at the end.
    private static void releaseDisarmsCleanup() throws Exception {
        byte[] data = resource("/noaa-ncei-national-marine-sanctuary-program-academic-torrents.torrent");
        torrent_info ti = new TorrentInfo(data).swig();
        int numFiles = ti.num_files();
        WeakReference<torrent_info> proxy = new WeakReference<>(ti);
        long ptr = torrent_info.swigRelease(ti);
        check(ptr != 0, "swigRelease returned 0");
        check(!ti.swigCMemOwn, "still owned after swigRelease");
        try {
            torrent_info.swigRelease(ti);
            check(false, "a second swigRelease must throw");
        } catch (RuntimeException expected) {
            // not owned any more
        }
        ti = null;
        boolean collected = collect(() -> proxy.get() == null);
        churn(data);

        torrent_info view = new torrent_info(ptr, false);
        for (int round = 0; round < 1000; round++) {
            check(view.num_files() == numFiles, "num_files after release");
        }
        new torrent_info(ptr, true).delete();
        System.out.println("proxyCollected=" + collected);
    }

    // Threads create, borrow from, read, drop and delete objects while others force collections.
    private static void manyThreads() throws Exception {
        byte[] data = resource("/noaa-ncei-national-marine-sanctuary-program-academic-torrents.torrent");
        TorrentInfo reference = new TorrentInfo(data);
        int numFiles = reference.numFiles();
        String firstPath = reference.files().filePath(0);
        int threads = Math.max(4, Runtime.getRuntime().availableProcessors());
        ExecutorService pool = Executors.newFixedThreadPool(threads + 1);
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(8);
        List<Future<Integer>> results = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            results.add(pool.submit(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                List<file_storage> kept = new ArrayList<>();
                int ops = 0;
                while (System.nanoTime() < deadline) {
                    torrent_info ti = new TorrentInfo(data).swig();
                    file_storage fs = ti.files();
                    check(fs.num_files() == numFiles, "num_files");
                    if (random.nextInt(4) == 0) {
                        kept.add(fs); // outlives ti, which is dropped without delete()
                    } else if (random.nextBoolean()) {
                        // delete() frees right away, so only when no view into ti is kept
                        ti.delete();
                    }
                    entry e = Entry.bdecode(data).swig();
                    string_entry_map dict = e.dict();
                    check(dict.containsKey("info"), "info key");
                    if (random.nextBoolean()) {
                        e.delete();
                    }
                    if (kept.size() > 32) {
                        for (file_storage k : kept) {
                            check(k.num_files() == numFiles, "kept num_files");
                            check(firstPath.equals(k.file_path(0)), "kept file_path");
                        }
                        kept.clear();
                    }
                    ops++;
                }
                return ops;
            }));
        }
        pool.submit(() -> {
            while (System.nanoTime() < deadline) {
                System.gc();
                Thread.sleep(20);
            }
            return 0;
        });
        int total = 0;
        for (Future<Integer> f : results) {
            total += f.get();
        }
        pool.shutdown();
        check(pool.awaitTermination(30, TimeUnit.SECONDS), "pool did not stop");
        collect(() -> false);
        System.out.println("threads=" + threads);
        System.out.println("ops=" + total);
    }

    // Runs GC until the condition holds, up to 50 rounds, then gives the cleaner time to run.
    private static boolean collect(BooleanSupplier done) throws InterruptedException {
        boolean ok = false;
        for (int i = 0; i < 50 && !(ok = done.getAsBoolean()); i++) {
            System.gc();
            Thread.sleep(20);
        }
        System.gc();
        Thread.sleep(200);
        return ok;
    }

    // Allocates and drops native objects of similar sizes, so that freed memory gets reused.
    private static void churn(byte[] data) throws InterruptedException {
        for (int i = 0; i < 300; i++) {
            new TorrentInfo(data).files().numFiles();
            Entry.bdecode(data).bencode();
        }
        System.gc();
        Thread.sleep(100);
    }

    private static void check(boolean condition, String what) {
        if (!condition) {
            System.out.println("FAIL " + what);
            System.out.flush();
            Runtime.getRuntime().halt(1);
        }
    }

    private static byte[] resource(String name) throws IOException {
        try (InputStream in = OwnershipScenarios.class.getResourceAsStream(name)) {
            return in.readAllBytes();
        }
    }
}
