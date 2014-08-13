#include "LibTorrent.h"

const char *libtorrent_version() {
    return LIBTORRENT_VERSION;
}

#ifdef JNI_INTERFACE_ENABLED

JNI_METHOD_BEGIN(jstring, version)

        return env->NewStringUTF(libtorrent_version());

JNI_METHOD_END_RET(jstring)

#endif //JNI_INTERFACE_ENABLED
