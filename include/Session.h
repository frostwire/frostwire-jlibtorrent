#ifndef _LIBTORRENT_H
#define _LIBTORRENT_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

JNI_METHOD(Session, jlong, create)
JNI_METHOD(Session, void, release, jlong)

JNI_METHOD(Session, jlong, startUPnP, jlong)
JNI_METHOD(Session, jlong, startNATPMP, jlong)
JNI_METHOD(Session, void, startLSD, jlong)
JNI_METHOD(Session, void, startDHT, jlong)

#ifdef __cplusplus
}
#endif
#endif
