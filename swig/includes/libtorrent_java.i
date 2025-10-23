#ifdef SWIGJAVA

%pragma(java) jniclassimports=%{
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
%}

%pragma(java) jniclasscode=%{

    public static String jlibtorrentVersion() {
        // extracted from the gradle with the run-swig step
        return "$JLIBTORRENT_VERSION$";
    }

    public static boolean isMacOS() {
      String os = System.getProperty("os.name").toLowerCase(java.util.Locale.US);
      return os.startsWith("mac os");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(java.util.Locale.US).contains("win");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase(java.util.Locale.US).contains("linux");
    }

    public static boolean isAndroid() {
        return System.getProperty("java.runtime.name").toLowerCase(java.util.Locale.US).contains("android");
    }

    public static String getMacOSLibraryName() {
      String os_arch = System.getProperty("os.arch");
      if ("aarch64".equals(os_arch)) {
        return "jlibtorrent.arm64";
      }
      return "jlibtorrent." + os_arch;
    }

/**
     * Loads the jlibtorrent native library from the classpath (e.g., lib/arm64/libjlibtorrent.arm64-${JLIBTORRENT_VERSION}.dylib).
     * @return The absolute path to the extracted temporary file, or null if an IOException occurs.
     * @throws IOException If the library cannot be extracted or loaded.
     */
    private static String loadJlibtorrentJNIFromClassloaderResource() throws java.io.IOException {
        String os = System.getProperty("os.name").toLowerCase(java.util.Locale.US);
        String arch = System.getProperty("os.arch").toLowerCase(java.util.Locale.US);
        boolean isArm64 = arch.equals("aarch64") || arch.equals("arm64");
        String version = jlibtorrentVersion();
        String libraryName;
        String pathToLibraryInJar;

        // Determine platform-specific library path
        if (isWindows()) {
            libraryName = "jlibtorrent-" + version;
            pathToLibraryInJar = "lib/x86_64/" + libraryName + ".dll";
        } else if (isLinux() && !isAndroid()) {
            libraryName = "libjlibtorrent-" + version;
            if (isArm64) {
                pathToLibraryInJar = "lib/arm64/" + libraryName + ".so";
            } else {
                pathToLibraryInJar = "lib/x86_64/" + libraryName + ".so";
            }
        } else if (isMacOS()) {
            if (isArm64) {
                libraryName = "libjlibtorrent.arm64-" + version;
                pathToLibraryInJar = "lib/arm64/" + libraryName + ".dylib";
            } else {
                libraryName = "libjlibtorrent.x86_64-" + version;
                pathToLibraryInJar = "lib/x86_64/" + libraryName + ".dylib";
            }
        } else if (isAndroid()) {
            if (isArm64) {
                libraryName = "libjlibtorrent-" + version;
                pathToLibraryInJar = "lib/arm64-v8a/" + libraryName + ".so";
            } else if (arch.equals("arm")) {
                libraryName = "libjlibtorrent-" + version;
                pathToLibraryInJar = "lib/armeabi-v7a/" + libraryName + ".so";
            } else if (arch.equals("x86_64")) {
                libraryName = "libjlibtorrent-" + version;
                pathToLibraryInJar = "lib/x86_64/" + libraryName + ".so";
            } else if (arch.equals("x86")) {
                libraryName = "libjlibtorrent-" + version;
                pathToLibraryInJar = "lib/x86/" + libraryName + ".so";
            } else {
                throw new java.io.IOException("Unsupported Android architecture: " + arch);
            }
        } else {
            throw new java.io.IOException("Unsupported OS: " + os);
        }

        try {
            // Get the native library resource from the classpath
            InputStream libStream = libtorrent_jni.class.getClassLoader().getResourceAsStream(pathToLibraryInJar);
            if (libStream == null) {
                System.err.println("jlibtorrent: Could not find native library in JAR: " + pathToLibraryInJar);
                throw new FileNotFoundException("Could not find native library in JAR: " + pathToLibraryInJar);
            }

            // Create temp file
            String suffix = pathToLibraryInJar.substring(pathToLibraryInJar.lastIndexOf('.')); // e.g., ".dylib"
            Path tempLib = Files.createTempFile("jlibtorrent-", suffix);
            tempLib.toFile().deleteOnExit();

            // Extract to temp file
            try (InputStream in = libStream) {
                Files.copy(in, tempLib, StandardCopyOption.REPLACE_EXISTING);
            }

            // Load the library
            String absolutePath = tempLib.toAbsolutePath().toString();
            System.out.println("jlibtorrent: Extracted and loading native library from classpath to: " + absolutePath);
            System.load(absolutePath);
            return absolutePath;
        } catch (IOException e) {
            System.err.println("jlibtorrent: Failed to extract/load native library: " + e.getMessage());
            return null;
        }
    }

    static {
        try {
            // Try loading from classpath resource first
            try {
                System.out.println("jlibtorrent: Trying to load jlibtorrent from classpath resource");
                String loadedPath = loadJlibtorrentJNIFromClassloaderResource();
                if (loadedPath != null) {
                    System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent from classpath resource (version=" + jlibtorrentVersion() + ")");
                } else {
                    throw new LinkageError("Failed to load jlibtorrent from classpath resource");
                }
            } catch (Throwable e) {
                System.err.println("jlibtorrent: Failed to load from classpath resource: " + e.getMessage());
                // Fall back to System.loadLibrary
                try {
                    String jlibtorrentLibraryName = "jlibtorrent";
                    if (isMacOS()) {
                        jlibtorrentLibraryName = getMacOSLibraryName();
                    }
                    System.out.println("jlibtorrent: Trying to load jlibtorrent without version number: " + jlibtorrentLibraryName + ".<so|dylib>...");
                    System.loadLibrary(jlibtorrentLibraryName);
                    System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent.<so|dylib> (version=" + jlibtorrentVersion() + ")");
                } catch (LinkageError ignored) {
                    // Try with version number
                    try {
                        String versionedLibraryName = "jlibtorrent-" + jlibtorrentVersion();
                        System.out.println("jlibtorrent: FAILED: Trying to load jlibtorrent with version number: " + versionedLibraryName);
                        System.loadLibrary(versionedLibraryName);
                        System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent-" + jlibtorrentVersion());
                    } catch (LinkageError e2) {
                        // Try jlibtorrent.jni.path
                        String path = System.getProperty("jlibtorrent.jni.path", "");
                        if (!"".equals(path)) {
                            System.out.println("jlibtorrent: Trying to load jlibtorrent through system property jlibtorrent.jni.path=" + path);
                            System.load(path);
                            System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent through jlibtorrent.jni.path parameter");
                        } else {
                            throw new LinkageError("Failed to load jlibtorrent after all attempts");
                        }
                    }
                }
            }
        } catch (LinkageError e) {
            System.err.println("jlibtorrent: FAILED to load native library: " + e.getMessage());
            throw new LinkageError("Look for your architecture binary instructions at: https://github.com/frostwire/frostwire-jlibtorrent", e);
        }
    }

    public static final native long directBufferAddress(java.nio.Buffer buffer);
    public static final native long directBufferCapacity(java.nio.Buffer buffer);
%}

%exception {
    try {
        $action
    } catch (std::exception& e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
        return $null;
    } catch (...) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
        return $null;
    }
}

%{
#ifdef __cplusplus
extern "C" {
#endif

SWIGEXPORT jlong JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_directBufferAddress(JNIEnv *jenv, jclass jcls, jobject jbuf) {
    try {
        return reinterpret_cast<jlong>(jenv->GetDirectBufferAddress(jbuf));
    } catch (std::exception& e) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }

    return 0;
}

SWIGEXPORT jlong JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_directBufferCapacity(JNIEnv *jenv, jclass jcls, jobject jbuf) {
    try {
        return reinterpret_cast<jlong>(jenv->GetDirectBufferCapacity(jbuf));
    } catch (std::exception& e) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }

    return 0;
}

#ifdef __cplusplus
}
#endif
%}

#endif // SWIGJAVA
