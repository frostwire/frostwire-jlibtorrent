#ifndef _SESSION_H
#define _SESSION_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifdef JNI_INTERFACE_ENABLED

#define JNI_CLASS_NAME Session

JNI_METHOD(jlong, create)
JNI_METHOD(void, release, jlong)

JNI_METHOD(void, startUPnP, jlong)
JNI_METHOD(void, startNATPMP, jlong)
JNI_METHOD(void, startLSD, jlong)
JNI_METHOD(void, startDHT, jlong)

JNI_METHOD(void, stopUPnP, jlong)
JNI_METHOD(void, stopNATPMP, jlong)
JNI_METHOD(void, stopLSD, jlong)
JNI_METHOD(void, stopDHT, jlong)

JNI_METHOD(jobjectArray, waitForAlerts, jlong, jint)

#endif //JNI_INTERFACE_ENABLED

#ifdef __cplusplus
}
#endif
#endif //_SESSION_H
