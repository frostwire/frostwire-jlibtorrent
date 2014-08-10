#ifndef _LIBTORRENT_H
#define _LIBTORRENT_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CLASS_NAME LibTorrent

JNI_METHOD(jstring, version)

JNI_METHOD(void, createTorrent, jobjectArray)

#ifdef __cplusplus
}
#endif
#endif //_LIBTORRENT_H
