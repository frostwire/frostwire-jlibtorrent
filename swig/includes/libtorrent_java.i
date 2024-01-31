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

    public static String getMacOSLibraryName() {
      String os_arch = System.getProperty("os.arch");
      if ("aarch64".equals(os_arch)) {
        return "jlibtorrent.arm64";
      }
      return "jlibtorrent." + os_arch;
    }

    static {
        try {
            String path = System.getProperty("jlibtorrent.jni.path", "");
            if ("".equals(path)) {
                try {
                    String jlibtorrentLibraryName = "jlibtorrent";
                    if (isMacOS()) {
                      jlibtorrentLibraryName = getMacOSLibraryName();
                    }
                    System.out.println("jlibtorrent: Trying to load jlibtorrent without version number: " + jlibtorrentLibraryName + ".<so|dylib>...");
                    System.loadLibrary(jlibtorrentLibraryName);
                    System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent.<so|dylib> (version=" +  jlibtorrentVersion() + ")");
                } catch (LinkageError ignored) {
                    // give it a try to the name with version
                    try {
                        System.out.println("jlibtorrent: FAILED: Trying to load jlibtorrent with version number: jlibtorrent-" + jlibtorrentVersion());
                        System.loadLibrary("jlibtorrent-" + jlibtorrentVersion());
                        System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent-" + jlibtorrentVersion());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } else {
                System.out.println("jlibtorrent: Trying to load jlibtorrent through system property jlibtorrent.jni.path=" + path);
                System.load(path);
                System.out.println("jlibtorrent: SUCCESS: Loaded jlibtorrent through jlibtorrent.jni.path parameter");
            }
        } catch (LinkageError e) {
            throw new LinkageError(
                "Look for your architecture binary instructions at: https://github.com/frostwire/frostwire-jlibtorrent", e);
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