#include "LibTorrent.h"

#include <libtorrent/version.hpp>

JNI_METHOD_BEGIN(LibTorrent, jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END

JNI_METHOD_BEGIN(LibTorrent, void, createTorrent, jobjectArray paths)

    JNI_STRING_ARRAY_FOREACH_BEGIN(paths, path)

        printf(path, "\n");

    JNI_STRING_ARRAY_FOREACH_END(path)

JNI_METHOD_END