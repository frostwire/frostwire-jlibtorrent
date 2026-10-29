#ifdef SWIGJAVA

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

    static {
        // See NativeLibraryLoader for the order: no per-start copy of the library on any platform
        // (frostwire/frostwire-jlibtorrent#326).
        ClassLoader classLoader = libtorrent_jni.class.getClassLoader();
        com.frostwire.jlibtorrent.NativeLibraryLoader.load(jlibtorrentVersion(),
                classLoader != null ? classLoader : ClassLoader.getSystemClassLoader());
        String nativeVersion;
        try {
            nativeVersion = jlibtorrentNativeVersion();
        } catch (UnsatisfiedLinkError e) {
            throw new LinkageError("jlibtorrent: the native library loaded from "
                    + com.frostwire.jlibtorrent.NativeLibraryLoader.loadedLibrary()
                    + " predates " + jlibtorrentVersion() + " (it has no version function); use matching jlibtorrent jars", e);
        }
        com.frostwire.jlibtorrent.NativeLibraryLoader.checkNativeVersion(jlibtorrentVersion(), nativeVersion);
    }

    /** The jlibtorrent version the loaded native library was built from (frostwire/frostwire-jlibtorrent#327). */
    public static final native String jlibtorrentNativeVersion();

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

// run-swig.sh replaces the placeholder in the generated libtorrent_jni.cpp, so the native
// library reports the version it was built from.
SWIGEXPORT jstring JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_jlibtorrentNativeVersion(JNIEnv *jenv, jclass jcls) {
    return jenv->NewStringUTF("$JLIBTORRENT_VERSION$");
}

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
