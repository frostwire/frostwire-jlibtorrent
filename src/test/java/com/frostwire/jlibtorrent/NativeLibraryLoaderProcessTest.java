package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * {@code NativeLibraryLoader.load} end to end, one fresh JVM per case (it loads a native library once
 * per JVM). The code coverage agent of this JVM, if any, is handed on so the cases count.
 *
 * @author gubatron
 * @author aldenml
 */
public class NativeLibraryLoaderProcessTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    /** The native library this JVM loaded, to hand to the cases. */
    private static Path loadedLibrary() {
        LibTorrent.versionNum(); // makes this JVM load the native library
        String loaded = NativeLibraryLoader.loadedLibrary();
        assertNotNull("the native library is loaded in this JVM", loaded);
        Path p = new File(loaded).toPath();
        assertTrue(loaded, Files.isRegularFile(p));
        return p;
    }

    private String run(String scenario, String loaderKind, List<String> jvmArgs) throws IOException, InterruptedException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        List<String> command = new ArrayList<>();
        command.add(java);
        command.add("-Xmx128m");
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.startsWith("-javaagent:")) {
                command.add(arg);
            }
        }
        command.addAll(jvmArgs);
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(NativeLoaderScenarios.class.getName());
        command.add(scenario);
        command.add(loaderKind);

        File log = tmp.newFile(scenario + "-" + loaderKind + ".log");
        Process p = new ProcessBuilder(command).redirectErrorStream(true).redirectOutput(log).start();
        if (!p.waitFor(2, TimeUnit.MINUTES)) {
            p.destroyForcibly().waitFor();
            throw new AssertionError(scenario + " timed out:\n" + new String(Files.readAllBytes(log.toPath()), StandardCharsets.UTF_8));
        }
        String out = new String(Files.readAllBytes(log.toPath()), StandardCharsets.UTF_8);
        assertEquals(scenario + ":\n" + out, 0, p.exitValue());
        assertTrue(out, out.contains("DONE"));
        return out;
    }

    private static String line(String out, String key) {
        for (String l : out.split("\\R")) {
            if (l.startsWith(key + "=")) {
                return l.substring(key.length() + 1);
            }
        }
        throw new AssertionError("no " + key + " in:\n" + out);
    }

    @Test
    public void aBadExplicitPathFallsBackToTheBundledLibrary() throws Exception {
        File cache = tmp.newFolder("cache");
        String out = run("badPathThenBundled", "classpath", java.util.Arrays.asList(
                "-D" + NativeLibraryLoader.JNI_PATH_PROPERTY + "=" + new File(tmp.getRoot(), "no-such-library.so"),
                "-D" + NativeLibraryLoader.CACHE_DIR_PROPERTY + "=" + cache));
        String loaded = line(out, "RESULT");
        assertTrue(loaded, loaded.startsWith(cache.getAbsolutePath()));
        assertTrue(out, out.contains("no-such-library"));
    }

    @Test
    public void anExplicitPathIsLoadedAsIs() throws Exception {
        Path library = loadedLibrary();
        String out = run("explicit", "classpath", java.util.Arrays.asList(
                "-D" + NativeLibraryLoader.JNI_PATH_PROPERTY + "=" + library));
        assertEquals(library.toString(), line(out, "RESULT"));
    }

    @Test
    public void withoutABundledLibraryTheSystemPathIsUsed() throws Exception {
        Path library = loadedLibrary();
        File dir = tmp.newFolder("libs");
        NativeLibraryLoader.Platform platform = NativeLibraryLoader.Platform.current();
        Files.copy(library, new File(dir, System.mapLibraryName(platform.systemLibraryName())).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        String out = run("systemPath", "empty", java.util.Arrays.asList("-Djava.library.path=" + dir));
        assertEquals("System.loadLibrary(" + platform.systemLibraryName() + ")", line(out, "RESULT"));
    }

    @Test
    public void whenNothingLoadsTheErrorSaysWhy() throws Exception {
        File empty = tmp.newFolder("empty-libs");
        String out = run("nothing", "empty", java.util.Arrays.asList("-Djava.library.path=" + empty));
        String error = line(out, "ERROR");
        assertTrue(error, error.startsWith("UnsatisfiedLinkError"));
        assertTrue(error, error.contains("could not load the native library"));
        assertTrue(error, error.contains("on the classpath"));
    }

    @Test
    public void androidOnlyUsesTheInstalledLibrary() throws Exception {
        File empty = tmp.newFolder("empty-libs");
        String out = run("android", "classpath", java.util.Arrays.asList(
                "-Djava.library.path=" + empty,
                "-D" + NativeLibraryLoader.JNI_PATH_PROPERTY + "=" + new File(tmp.getRoot(), "missing.so")));
        String error = line(out, "ERROR");
        assertTrue(error, error.startsWith("UnsatisfiedLinkError"));
        assertTrue(error, error.contains("is not installed with this app"));
        assertTrue(error, error.contains(NativeLibraryLoader.JNI_PATH_PROPERTY));
    }

    @Test
    public void theLoadedLibraryIsRememberedAndVersionMismatchesNameIt() throws Exception {
        Path library = loadedLibrary();
        String out = run("twice", "classpath", java.util.Arrays.asList(
                "-D" + NativeLibraryLoader.JNI_PATH_PROPERTY + "=" + library));
        assertEquals(library.toString(), line(out, "RESULT"));
        assertEquals(library.toString(), line(out, "AGAIN"));
        assertEquals(library.toString(), line(out, "REMEMBERED"));
        String linkage = line(out, "LINKAGE");
        assertTrue(linkage, linkage.contains("2.0.15.0") && linkage.contains(library.toString()));
    }
}
