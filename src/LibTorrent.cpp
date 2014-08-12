#include "LibTorrent.h"

#ifdef JNI_INTERFACE_ENABLED

JNI_METHOD_BEGIN(jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END

#endif //JNI_INTERFACE_ENABLED
