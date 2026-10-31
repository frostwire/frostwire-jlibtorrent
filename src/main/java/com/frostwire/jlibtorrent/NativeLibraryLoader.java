package com.frostwire.jlibtorrent;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.jar.JarEntry;

/**
 * Finds and loads the jlibtorrent native library, once per process, without copying it on every
 * start.
 * <p>
 * {@code libtorrent_jni}'s static initializer calls {@link #load(String, ClassLoader)}. The order
 * is:
 * <ol>
 *   <li>{@code -Djlibtorrent.jni.path=/path/to/library}, when set: an app that ships the library
 *   next to itself decides.</li>
 *   <li><b>Android:</b> {@code System.loadLibrary("jlibtorrent")}, nothing else. The APK installs
 *   {@code lib/<abi>/libjlibtorrent.so} from the {@code jlibtorrent-android-*} jar, so copying it
 *   out of the APK again only costs I/O, storage and memory.</li>
 *   <li><b>Desktop:</b> the library bundled in the {@code jlibtorrent-<platform>} jar, extracted
 *   <i>once</i> into a per-user cache ({@code <cache>/jlibtorrent/<version>-<entry crc>/}) and
 *   reused by later starts. It is written to a temporary file and moved into place, so concurrent
 *   first starts can't load a half-written library, and nothing has to be deleted at exit (Windows
 *   can't delete a loaded DLL).</li>
 *   <li><b>Desktop fallback:</b> {@code System.loadLibrary} from {@code java.library.path}.</li>
 * </ol>
 * The bundled copy comes before {@code java.library.path} because it is the build that matches
 * these Java classes; a stale library on the path would load and then fail the version check.
 * <p>
 * Override the cache root with {@code -Djlibtorrent.cache.dir=/some/dir}. The default is
 * {@code %LOCALAPPDATA%} on Windows, {@code ~/Library/Caches} on macOS and
 * {@code $XDG_CACHE_HOME} (or {@code ~/.cache}) on Linux, falling back to {@code java.io.tmpdir}.
 */
public final class NativeLibraryLoader {

    static final String JNI_PATH_PROPERTY = "jlibtorrent.jni.path";
    static final String CACHE_DIR_PROPERTY = "jlibtorrent.cache.dir";

    private static final Logger LOG = Logger.getLogger(NativeLibraryLoader.class);

    private static volatile String loadedLibrary;

    private NativeLibraryLoader() {
    }

    /**
     * Where the native library was loaded from: a file path, or {@code "System.loadLibrary(<name>)"}
     * when the platform's library loader found it. {@code null} before the library is loaded.
     */
    public static String loadedLibrary() {
        return loadedLibrary;
    }

    /**
     * Loads the native library for this platform; later calls return the first result.
     *
     * @param version     the jlibtorrent version of these Java classes, used to key the cache
     * @param classLoader the loader that can see the {@code jlibtorrent-<platform>} jar
     * @return what was loaded, see {@link #loadedLibrary()}
     * @throws UnsatisfiedLinkError when no way of loading the library worked
     */
    public static synchronized String load(String version, ClassLoader classLoader) {
        if (loadedLibrary != null) {
            return loadedLibrary;
        }
        Platform platform = Platform.current();
        StringBuilder failures = new StringBuilder();

        String explicitPath = System.getProperty(JNI_PATH_PROPERTY, "");
        if (!explicitPath.isEmpty()) {
            try {
                System.load(explicitPath);
                return loaded(explicitPath);
            } catch (UnsatisfiedLinkError e) {
                LOG.warn("jlibtorrent: " + JNI_PATH_PROPERTY + "=" + explicitPath + " failed to load: " + e.getMessage());
                failures.append(JNI_PATH_PROPERTY).append(": ").append(e.getMessage()).append("; ");
            }
        }

        if (platform.android) {
            try {
                System.loadLibrary(platform.systemLibraryName());
                return loaded("System.loadLibrary(" + platform.systemLibraryName() + ")");
            } catch (UnsatisfiedLinkError e) {
                throw new UnsatisfiedLinkError("jlibtorrent: libjlibtorrent.so for " + platform.arch
                        + " is not installed with this app; add the jlibtorrent-android jar for that ABI. "
                        + failures + e.getMessage());
            }
        }

        String resourcePath = platform.resourcePath();
        URL resource = resourcePath == null ? null : classLoader.getResource(resourcePath);
        if (resource != null) {
            try {
                return loaded(loadFromCache(resource, cacheRoot(platform), version, platform.fileName()));
            } catch (IOException | UnsatisfiedLinkError e) {
                LOG.warn("jlibtorrent: could not load the bundled " + resourcePath + ": " + e.getMessage());
                failures.append(resourcePath).append(": ").append(e.getMessage()).append("; ");
            }
        } else {
            failures.append("no ").append(resourcePath).append(" on the classpath; ");
        }

        try {
            System.loadLibrary(platform.systemLibraryName());
            return loaded("System.loadLibrary(" + platform.systemLibraryName() + ")");
        } catch (UnsatisfiedLinkError e) {
            failures.append("System.loadLibrary(").append(platform.systemLibraryName()).append("): ").append(e.getMessage());
        }
        throw new UnsatisfiedLinkError("jlibtorrent: could not load the native library for " + platform
                + " (" + failures + "). Add the jlibtorrent-<platform> jar to the classpath, or set -D"
                + JNI_PATH_PROPERTY + ". See https://github.com/frostwire/frostwire-jlibtorrent");
    }

    /**
     * Fails unless the native library was built from the same libtorrent version as these classes.
     * Only the first three components must match: the fourth (the jlibtorrent patch) can change in a
     * Java-only release that reuses the previous natives.
     *
     * @throws LinkageError on a mismatch
     */
    public static void checkNativeVersion(String javaVersion, String nativeVersion) {
        if (!libtorrentPart(javaVersion).equals(libtorrentPart(nativeVersion))) {
            throw new LinkageError("jlibtorrent: the Java classes are " + javaVersion
                    + " but the native library is " + nativeVersion + " (loaded from " + loadedLibrary
                    + "); use matching jlibtorrent jars");
        }
    }

    static String libtorrentPart(String version) {
        String[] parts = version.split("\\.");
        if (parts.length < 3) {
            return version;
        }
        return parts[0] + "." + parts[1] + "." + parts[2];
    }

    private static String loaded(String what) {
        loadedLibrary = what;
        return what;
    }

    private static String loadFromCache(URL resource, Path cacheRoot, String version, String fileName)
            throws IOException {
        return loadFromCache(resource, cacheRoot, version, fileName, System::load);
    }

    /**
     * Loads the cached copy of {@code resource} with {@code systemLoad} ({@code System::load}, or a
     * stand-in in tests), extracting it first if needed.
     */
    static String loadFromCache(URL resource, Path cacheRoot, String version, String fileName,
                                Consumer<String> systemLoad) throws IOException {
        Path library = cachedLibrary(resource, cacheRoot, version, fileName);
        try {
            systemLoad.accept(library.toString());
        } catch (UnsatisfiedLinkError e) {
            // A damaged cached copy of the right size: write it again once.
            LOG.warn("jlibtorrent: cached " + library + " failed to load, extracting it again: " + e.getMessage());
            Files.deleteIfExists(library);
            library = cachedLibrary(resource, cacheRoot, version, fileName);
            systemLoad.accept(library.toString());
        }
        return library.toString();
    }

    /**
     * Returns the cached copy of {@code resource}, extracting it first when it is missing or its size
     * differs. The directory name holds the version and the jar entry's CRC, so a different build of
     * the same version gets its own copy.
     */
    static Path cachedLibrary(URL resource, Path cacheRoot, String version, String fileName) throws IOException {
        ResourceInfo info = ResourceInfo.of(resource);
        Path dir = cacheRoot.resolve("jlibtorrent").resolve(version + "-" + info.key);
        Path target = dir.resolve(fileName);
        if (isComplete(target, info.size)) {
            return target;
        }
        Files.createDirectories(dir);
        Path partial = Files.createTempFile(dir, fileName, ".part");
        try {
            try (InputStream in = resource.openStream()) {
                Files.copy(in, partial, StandardCopyOption.REPLACE_EXISTING);
            }
            try {
                Files.move(partial, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(partial, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Another process won the race, or Windows has the target loaded: use theirs.
                if (!isComplete(target, info.size)) {
                    throw e;
                }
            }
        } finally {
            Files.deleteIfExists(partial);
        }
        return target;
    }

    private static boolean isComplete(Path file, long expectedSize) throws IOException {
        return Files.isRegularFile(file) && (expectedSize < 0 || Files.size(file) == expectedSize);
    }

    static Path cacheRoot(Platform platform) {
        String override = System.getProperty(CACHE_DIR_PROPERTY, "");
        if (!override.isEmpty()) {
            return Paths.get(override);
        }
        Path root = defaultCacheRoot(platform, System.getenv(), System.getProperty("user.home", ""));
        if (root != null && (Files.isWritable(root) || !Files.exists(root))) {
            return root;
        }
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }

    static Path defaultCacheRoot(Platform platform, Map<String, String> env, String userHome) {
        if (platform.os == Os.WINDOWS) {
            String localAppData = env.get("LOCALAPPDATA");
            if (localAppData != null && !localAppData.isEmpty()) {
                return Paths.get(localAppData);
            }
            return userHome.isEmpty() ? null : Paths.get(userHome, "AppData", "Local");
        }
        if (platform.os == Os.MACOS) {
            return userHome.isEmpty() ? null : Paths.get(userHome, "Library", "Caches");
        }
        String xdg = env.get("XDG_CACHE_HOME");
        if (xdg != null && !xdg.isEmpty()) {
            return Paths.get(xdg);
        }
        return userHome.isEmpty() ? null : Paths.get(userHome, ".cache");
    }

    enum Os {
        WINDOWS, MACOS, LINUX, ANDROID, OTHER
    }

    /** The operating system and CPU architecture, as the jar layout names them. */
    static final class Platform {
        final Os os;
        final String arch;
        final boolean android;

        private Platform(Os os, String arch) {
            this.os = os;
            this.arch = arch;
            this.android = os == Os.ANDROID;
        }

        static Platform current() {
            return of(System.getProperty("os.name", ""), System.getProperty("os.arch", ""),
                    System.getProperty("java.runtime.name", ""), System.getProperty("java.vm.vendor", ""));
        }

        static Platform of(String osName, String osArch, String runtimeName, String vmVendor) {
            String os = osName.toLowerCase(Locale.US);
            String arch = osArch.toLowerCase(Locale.US);
            boolean arm64 = arch.equals("aarch64") || arch.equals("arm64");
            boolean x86_64 = arch.equals("amd64") || arch.equals("x86_64");
            boolean onAndroid = runtimeName.toLowerCase(Locale.US).contains("android")
                    || vmVendor.toLowerCase(Locale.US).contains("android");
            if (onAndroid) {
                String abi;
                if (arm64) {
                    abi = "arm64-v8a";
                } else if (x86_64) {
                    abi = "x86_64";
                } else if (arch.startsWith("arm")) {
                    abi = "armeabi-v7a";
                } else if (arch.equals("x86") || arch.equals("i686") || arch.equals("i386")) {
                    abi = "x86";
                } else {
                    abi = arch;
                }
                return new Platform(Os.ANDROID, abi);
            }
            String normalized = arm64 ? "arm64" : x86_64 ? "x86_64" : arch;
            if (os.contains("win")) {
                return new Platform(Os.WINDOWS, normalized);
            }
            if (os.startsWith("mac os") || os.startsWith("darwin")) {
                return new Platform(Os.MACOS, normalized);
            }
            if (os.contains("linux")) {
                return new Platform(Os.LINUX, normalized);
            }
            return new Platform(Os.OTHER, normalized);
        }

        /** The library's file name inside the jar and in the cache. */
        String fileName() {
            switch (os) {
                case WINDOWS:
                    return "jlibtorrent.dll";
                case MACOS:
                    return "libjlibtorrent." + arch + ".dylib";
                default:
                    return "libjlibtorrent.so";
            }
        }

        /** The name {@code System.loadLibrary} takes. */
        String systemLibraryName() {
            return os == Os.MACOS ? "jlibtorrent." + arch : "jlibtorrent";
        }

        /** Where the {@code jlibtorrent-<platform>} jar keeps the library, or null if none is built. */
        String resourcePath() {
            switch (os) {
                case WINDOWS:
                    return arch.equals("x86_64") ? "lib/x86_64/" + fileName() : null;
                case MACOS:
                case LINUX:
                    return arch.equals("x86_64") || arch.equals("arm64") ? "lib/" + arch + "/" + fileName() : null;
                case ANDROID:
                    return "lib/" + arch + "/" + fileName();
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            return os.name().toLowerCase(Locale.US) + "-" + arch;
        }
    }

    /** The cache key and expected size of a library resource, read without extracting it. */
    private static final class ResourceInfo {
        final String key;
        final long size;

        private ResourceInfo(String key, long size) {
            this.key = key;
            this.size = size;
        }

        static ResourceInfo of(URL resource) throws IOException {
            URLConnection connection = resource.openConnection();
            if (connection instanceof JarURLConnection) {
                JarEntry entry = ((JarURLConnection) connection).getJarEntry();
                if (entry != null && entry.getCrc() >= 0) {
                    return new ResourceInfo(Long.toHexString(entry.getCrc()), entry.getSize());
                }
            }
            long size = connection.getContentLengthLong();
            long modified = connection.getLastModified();
            return new ResourceInfo(Long.toHexString(size) + Long.toHexString(modified), size);
        }
    }
}
