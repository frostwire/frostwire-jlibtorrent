#include "LibTorrent.h"

JNI_METHOD_BEGIN(jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END
