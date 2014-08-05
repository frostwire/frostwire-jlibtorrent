#include "LibTorrent.h"

#include <libtorrent/version.hpp>

JNI_METHOD_BEGIN(LibTorrent, jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END

JNI_METHOD_BEGIN(LibTorrent, void, createTorrent, jobjectArray paths)

    int length = env->GetArrayLength(paths);

    for (int i=0; i<length; i++) {
        jstring str = (jstring) env->GetObjectArrayElement( paths, i);

        jboolean isCopy;
        const char *path = env->GetStringUTFChars(str, &isCopy);

        printf(path, "\n");

        env->ReleaseStringUTFChars(str, path);
    }
JNI_METHOD_END