#include "LibTorrent.h"

#include <libtorrent/version.hpp>

JNI_METHOD_BEGIN(LibTorrent, jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END