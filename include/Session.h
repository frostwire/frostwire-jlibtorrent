#ifndef _SESSION_H
#define _SESSION_H

#include "jlibtorrent.h"

#ifdef __cplusplus
extern "C" {
#endif

void session_create();
void session_release();

void session_start_upnp();
void session_start_natpmp();
void session_start_lsd();
void session_start_dht();

void session_stop_upnp();
void session_stop_natpmp();
void session_stop_lsd();
void session_stop_dht();

alert **session_wait_for_alert(int, int *);

#ifdef JNI_INTERFACE_ENABLED

#define JNI_CLASS_NAME Session

JNI_METHOD(void, create)
JNI_METHOD(void, release)

JNI_METHOD(void, startUPnP)
JNI_METHOD(void, startNATPMP)
JNI_METHOD(void, startLSD)
JNI_METHOD(void, startDHT)

JNI_METHOD(void, stopUPnP)
JNI_METHOD(void, stopNATPMP)
JNI_METHOD(void, stopLSD)
JNI_METHOD(void, stopDHT)

JNI_METHOD(jobjectArray, waitForAlert, jint)

#endif //JNI_INTERFACE_ENABLED

#ifdef __cplusplus
}
#endif
#endif //_SESSION_H
