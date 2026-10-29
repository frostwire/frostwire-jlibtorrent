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

%{
#include <cstdint>
#include <cstring>
#include <string>
// Exception messages reach Java through ThrowNew, which takes modified UTF-8 (JNI): anything else is
// undefined behavior, and Android's CheckJNI aborts. libtorrent messages can embed file paths and
// peer-supplied text, so keep only valid 1-3 byte UTF-8 (no surrogates, no NUL) and replace every
// other byte with '?' (frostwire/frostwire-jlibtorrent#336).
static std::string jlibtorrent_jni_safe_message(char const* s) {
    std::string out;
    if (s == nullptr) return out;
    auto const* p = reinterpret_cast<unsigned char const*>(s);
    while (*p) {
        unsigned char const c = p[0];
        int n = 0;
        std::uint32_t cp = 0;
        if (c < 0x80) { n = 1; cp = c; }
        else if ((c & 0xE0) == 0xC0) { n = 2; cp = c & 0x1F; }
        else if ((c & 0xF0) == 0xE0) { n = 3; cp = c & 0x0F; }
        bool ok = n > 0;
        for (int i = 1; ok && i < n; ++i) {
            ok = (p[i] & 0xC0) == 0x80;
            if (ok) cp = (cp << 6) | (p[i] & 0x3F);
        }
        if (ok && n == 2 && cp < 0x80) ok = false;
        if (ok && n == 3 && (cp < 0x800 || (cp >= 0xD800 && cp <= 0xDFFF))) ok = false;
        if (ok) {
            out.append(reinterpret_cast<char const*>(p), static_cast<std::size_t>(n));
            p += n;
        } else {
            out.push_back('?');
            ++p;
        }
    }
    return out;
}
static std::string jlibtorrent_jni_safe_message(std::string const& s) {
    return jlibtorrent_jni_safe_message(s.c_str());
}
%}

%exception {
    try {
        $action
    } catch (std::exception& e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, jlibtorrent_jni_safe_message(e.what()).c_str());
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
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, jlibtorrent_jni_safe_message(e.what()).c_str());
    } catch (...) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }

    return 0;
}

SWIGEXPORT jlong JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_directBufferCapacity(JNIEnv *jenv, jclass jcls, jobject jbuf) {
    try {
        return reinterpret_cast<jlong>(jenv->GetDirectBufferCapacity(jbuf));
    } catch (std::exception& e) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, jlibtorrent_jni_safe_message(e.what()).c_str());
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
