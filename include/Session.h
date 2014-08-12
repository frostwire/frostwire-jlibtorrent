#ifndef _SESSION_H
#define _SESSION_H

#include "jlibtorrent.h"

typedef session *session_ptr;
typedef alert *alert_ptr;
typedef alert_ptr *alert_array;

#ifdef __cplusplus
extern "C" {
#endif

session_ptr session_create();
void session_release(session_ptr);

void session_start_upnp(session_ptr);
void session_start_natpmp(session_ptr);
void session_start_lsd(session_ptr);
void session_start_dht(session_ptr);

void session_stop_upnp(session_ptr);
void session_stop_natpmp(session_ptr);
void session_stop_lsd(session_ptr);
void session_stop_dht(session_ptr);

alert_array session_wait_for_alert(session_ptr, int, int *);

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

JNI_METHOD(jobjectArray, waitForAlert, jlong, jint)

#endif //JNI_INTERFACE_ENABLED

#ifdef __cplusplus
}
#endif
#endif //_SESSION_H
