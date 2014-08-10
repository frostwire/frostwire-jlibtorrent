#ifndef _SESSION_H
#define _SESSION_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CLASS_NAME Session

JNI_METHOD(jlong, create)
JNI_METHOD(void, release, jlong)

JNI_METHOD(jlong, startUPnP, jlong)
JNI_METHOD(jlong, startNATPMP, jlong)
JNI_METHOD(void, startLSD, jlong)
JNI_METHOD(void, startDHT, jlong)

JNI_METHOD(void, waitForAlerts, jlong, jint)

#ifdef __cplusplus
}
#endif
#endif //_SESSION_H
