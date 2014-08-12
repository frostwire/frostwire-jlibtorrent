#ifndef _LIBTORRENT_H
#define _LIBTORRENT_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifdef JNI_INTERFACE_ENABLED

#define JNI_CLASS_NAME LibTorrent

JNI_METHOD(jstring, version)

#endif //JNI_INTERFACE_ENABLED

#ifdef __cplusplus
}
#endif
#endif //_LIBTORRENT_H
