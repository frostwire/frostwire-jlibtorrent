package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.NativeLibraryLoader.Os;
import com.frostwire.jlibtorrent.NativeLibraryLoader.Platform;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.Assert.*;

/**
 * The loader never copies the library on every start (frostwire/frostwire-jlibtorrent#326): Android
 * uses the installed library, desktop extracts the bundled one once into a cache.
 */
public class NativeLibraryLoaderTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void desktopPlatformsMapToTheJarLayout() {
        assertPlatform(Platform.of("Mac OS X", "aarch64", "OpenJDK Runtime Environment", "Oracle"),
                Os.MACOS, "lib/arm64/libjlibtorrent.arm64.dylib", "jlibtorrent.arm64");
        assertPlatform(Platform.of("Mac OS X", "x86_64", "OpenJDK Runtime Environment", "Oracle"),
                Os.MACOS, "lib/x86_64/libjlibtorrent.x86_64.dylib", "jlibtorrent.x86_64");
        assertPlatform(Platform.of("Windows 11", "amd64", "Java(TM) SE Runtime Environment", "Oracle"),
                Os.WINDOWS, "lib/x86_64/jlibtorrent.dll", "jlibtorrent");
        assertPlatform(Platform.of("Linux", "amd64", "OpenJDK Runtime Environment", "Eclipse Adoptium"),
                Os.LINUX, "lib/x86_64/libjlibtorrent.so", "jlibtorrent");
        assertPlatform(Platform.of("Linux", "aarch64", "OpenJDK Runtime Environment", "Eclipse Adoptium"),
                Os.LINUX, "lib/arm64/libjlibtorrent.so", "jlibtorrent");
    }

    @Test
    public void androidIsDetectedBeforeLinuxAndMapsToAbis() {
        assertPlatform(Platform.of("Linux", "aarch64", "Android Runtime", "The Android Project"),
                Os.ANDROID, "lib/arm64-v8a/libjlibtorrent.so", "jlibtorrent");
        assertPlatform(Platform.of("Linux", "armv8l", "Android Runtime", "The Android Project"),
                Os.ANDROID, "lib/armeabi-v7a/libjlibtorrent.so", "jlibtorrent");
        assertPlatform(Platform.of("Linux", "x86_64", "Android Runtime", "The Android Project"),
                Os.ANDROID, "lib/x86_64/libjlibtorrent.so", "jlibtorrent");
        assertPlatform(Platform.of("Linux", "i686", "Android Runtime", "The Android Project"),
                Os.ANDROID, "lib/x86/libjlibtorrent.so", "jlibtorrent");
        assertTrue(Platform.of("Linux", "aarch64", "", "The Android Project").android);
    }

    @Test
    public void unbuiltPlatformsHaveNoBundledLibrary() {
        assertNull(Platform.of("Windows 11", "aarch64", "", "").resourcePath());
        assertNull(Platform.of("FreeBSD", "amd64", "", "").resourcePath());
        assertNull(Platform.of("Linux", "riscv64", "", "").resourcePath());
    }

    @Test
    public void cacheRootFollowsEachPlatformsConvention() {
        Map<String, String> env = new HashMap<>();
        env.put("LOCALAPPDATA", "C:\\Users\\u\\AppData\\Local");
        assertEquals(Paths.get("C:\\Users\\u\\AppData\\Local"),
                NativeLibraryLoader.defaultCacheRoot(Platform.of("Windows 10", "amd64", "", ""), env, "C:\\Users\\u"));
        assertEquals(Paths.get("/Users/u", "Library", "Caches"),
                NativeLibraryLoader.defaultCacheRoot(Platform.of("Mac OS X", "aarch64", "", ""), env, "/Users/u"));
        assertEquals(Paths.get("/home/u", ".cache"),
                NativeLibraryLoader.defaultCacheRoot(Platform.of("Linux", "amd64", "", ""), Collections.emptyMap(), "/home/u"));
        env.put("XDG_CACHE_HOME", "/var/cache/u");
        assertEquals(Paths.get("/var/cache/u"),
                NativeLibraryLoader.defaultCacheRoot(Platform.of("Linux", "amd64", "", ""), env, "/home/u"));
    }

    @Test
    public void theBundledLibraryIsExtractedOnceAndReused() throws Exception {
        URL resource = resourceInJar("lib/x86_64/libjlibtorrent.so", new byte[]{1, 2, 3, 4, 5});
        Path cache = tmp.newFolder("cache").toPath();

        Path first = NativeLibraryLoader.cachedLibrary(resource, cache, "2.0.15.0", "libjlibtorrent.so");
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, Files.readAllBytes(first));
        assertTrue(first.startsWith(cache.resolve("jlibtorrent")));
        FileTime written = FileTime.fromMillis(1_000_000L);
        Files.setLastModifiedTime(first, written);

        Path second = NativeLibraryLoader.cachedLibrary(resource, cache, "2.0.15.0", "libjlibtorrent.so");
        assertEquals(first, second);
        assertEquals("a second start must reuse the copy, not write it again",
                written, Files.getLastModifiedTime(second));
        try (java.util.stream.Stream<Path> files = Files.list(first.getParent())) {
            assertEquals("no leftover partial files", 1, files.count());
        }
    }

    @Test
    public void aTruncatedCopyIsReplaced() throws Exception {
        URL resource = resourceInJar("lib/x86_64/libjlibtorrent.so", new byte[]{9, 8, 7, 6});
        Path cache = tmp.newFolder("cache").toPath();
        Path library = NativeLibraryLoader.cachedLibrary(resource, cache, "2.0.15.0", "libjlibtorrent.so");
        Files.write(library, new byte[]{9, 8});

        Path again = NativeLibraryLoader.cachedLibrary(resource, cache, "2.0.15.0", "libjlibtorrent.so");
        assertArrayEquals(new byte[]{9, 8, 7, 6}, Files.readAllBytes(again));
    }

    @Test
    public void aDifferentBuildOfTheSameVersionGetsItsOwnCopy() throws Exception {
        Path cache = tmp.newFolder("cache").toPath();
        Path a = NativeLibraryLoader.cachedLibrary(
                resourceInJar("lib/x86_64/libjlibtorrent.so", new byte[]{1, 1, 1}), cache, "2.0.15.0", "libjlibtorrent.so");
        Path b = NativeLibraryLoader.cachedLibrary(
                resourceInJar("lib/x86_64/libjlibtorrent.so", new byte[]{2, 2, 2}), cache, "2.0.15.0", "libjlibtorrent.so");
        assertNotEquals(a.getParent(), b.getParent());
        assertArrayEquals(new byte[]{1, 1, 1}, Files.readAllBytes(a));
        assertArrayEquals(new byte[]{2, 2, 2}, Files.readAllBytes(b));
    }

    @Test
    public void nativeVersionMustMatchTheLibtorrentPart() {
        NativeLibraryLoader.checkNativeVersion("2.0.15.0", "2.0.15.0");
        NativeLibraryLoader.checkNativeVersion("2.0.15.1", "2.0.15.0");
        try {
            NativeLibraryLoader.checkNativeVersion("2.0.15.0", "2.0.12.9");
            fail("a native library built from another libtorrent version must not be used");
        } catch (LinkageError expected) {
            assertTrue(expected.getMessage(), expected.getMessage().contains("2.0.12.9"));
        }
    }

    private static void assertPlatform(Platform p, Os os, String resourcePath, String systemName) {
        assertEquals(os, p.os);
        assertEquals(resourcePath, p.resourcePath());
        assertEquals(systemName, p.systemLibraryName());
    }

    private URL resourceInJar(String entryName, byte[] content) throws IOException {
        File jar = tmp.newFile();
        try (OutputStream out = Files.newOutputStream(jar.toPath());
             JarOutputStream jarOut = new JarOutputStream(out)) {
            jarOut.putNextEntry(new JarEntry(entryName));
            jarOut.write(content);
            jarOut.closeEntry();
        }
        URLClassLoader loader = new URLClassLoader(new URL[]{jar.toURI().toURL()}, null);
        URL url = loader.getResource(entryName);
        assertNotNull(url);
        return url;
    }
}
