#include "jlibtorrent.h"

#include <JavaVM/jni.h>

#ifndef _LIBTORRENT_H
#define _LIBTORRENT_H
#ifdef __cplusplus
extern "C" {
#endif

JNI_METHOD(LibTorrent, jstring, version)

JNI_METHOD(LibTorrent, void, createTorrent, jobjectArray)

#ifdef __cplusplus
}
#endif
#endif
