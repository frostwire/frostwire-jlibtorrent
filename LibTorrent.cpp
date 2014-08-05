#include "LibTorrent.h"

#include <libtorrent/version.hpp>

JNIEXPORT jstring JNICALL Java_com_frostwire_libtorrent_LibTorrent_version
        (JNIEnv* env, jobject obj) {

    return env->NewStringUTF(LIBTORRENT_VERSION);
}