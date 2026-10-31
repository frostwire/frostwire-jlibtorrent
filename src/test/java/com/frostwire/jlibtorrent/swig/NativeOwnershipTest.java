package com.frostwire.jlibtorrent.swig;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * frostwire/frostwire-jlibtorrent#337: native objects Java owns are freed when their proxy becomes
 * unreachable, exactly once, and never while a borrowed view into them is still reachable.
 * <p>
 * A use after free or a double free can crash the JVM, so every scenario runs in a JVM of its own
 * ({@link OwnershipScenarios}); a crash shows up here as a non-zero exit status.
 */
public class NativeOwnershipTest {

    @Test
    public void aBorrowedFileStorageKeepsItsTorrentInfoAlive() throws Exception {
        String out = run("borrowedFileStorage");
        assertTrue(out, out.contains("numFiles="));
        // the borrowed file_storage must keep the torrent_info proxy reachable, so it's never freed
        assertTrue(out, out.contains("ownerCollected=false"));
    }

    @Test
    public void borrowedEntryViewsKeepTheirEntryAlive() throws Exception {
        String out = run("borrowedEntryViews");
        assertTrue(out, out.contains("numTiers="));
        assertTrue(out, out.contains("ownerCollected=false"));
    }

    @Test
    public void deleteThenCollectFreesOnce() throws Exception {
        String out = run("deleteThenCollect");
        assertTrue(out, out.contains("allCollected=true"));
    }

    @Test
    public void swigReleaseDisarmsTheCleanup() throws Exception {
        String out = run("releaseDisarmsCleanup");
        assertTrue(out, out.contains("proxyCollected=true"));
    }

    @Test
    public void manyThreadsCreatingBorrowingAndDroppingObjects() throws Exception {
        String out = run("manyThreads");
        assertTrue(out, out.contains("ops="));
    }

    private static String run(String scenario) throws IOException, InterruptedException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("jlibtorrent.test.classpath", System.getProperty("java.class.path"));
        File log = File.createTempFile("jlibtorrent-" + scenario, ".log");
        try {
            // a crash report goes next to the log, not into the working directory
            String errorFile = "-XX:ErrorFile=" + log.getAbsolutePath() + ".hs_err_%p.log";
            Process p = new ProcessBuilder(List.of(java, "-Xmx256m", errorFile, "-cp", classpath,
                    OwnershipScenarios.class.getName(), scenario))
                    .redirectErrorStream(true)
                    .redirectOutput(log)
                    .start();
            boolean exited = p.waitFor(2, TimeUnit.MINUTES);
            if (!exited) {
                p.destroyForcibly().waitFor();
            }
            String out = Files.readString(log.toPath(), StandardCharsets.UTF_8);
            if (!exited) {
                fail(scenario + " timed out:\n" + out);
            }
            System.out.println(scenario + " (exit " + p.exitValue() + "):\n" + out);
            assertEquals(scenario + " crashed or failed:\n" + out, 0, p.exitValue());
            assertTrue(out, out.contains("DONE"));
            return out;
        } finally {
            Files.deleteIfExists(log.toPath());
        }
    }
}
