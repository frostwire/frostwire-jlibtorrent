#include "LibTorrent.h"

#include <libtorrent/version.hpp>

JNI_METHOD_BEGIN(LibTorrent, jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END

JNI_METHOD_BEGIN(LibTorrent, void, createTorrent, jobjectArray paths)

    JNI_ARRAY_FOREACH_BEGIN(paths, jstring, str)

        JNI_GET_STRING(str, path)

        printf(path, "\n");

        JNI_RELEASE_STRING(str, path)

    JNI_ARRAY_FOREACH_END

JNI_METHOD_END