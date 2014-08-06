#ifndef _LIBTORRENT_H
#define _LIBTORRENT_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

JNI_METHOD(Session, jlong, create)

JNI_METHOD(Session, void, release, jlong)

#ifdef __cplusplus
}
#endif
#endif
