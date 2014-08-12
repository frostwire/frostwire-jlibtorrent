#include "Session.h"

session *hSession = NULL;

void check_session() {
    if (hSession == NULL) {
        throw runtime_error("Internal libtorrent session is NULL");
    }
}

void session_create() {
    if (hSession != NULL) {
        throw runtime_error("Internal libtorrent session is already created");
    }

    hSession = new session();

    hSession->set_alert_mask(alert::all_categories);
}

void session_release() {
    check_session();
    delete hSession;
}

void session_start_upnp() {
    check_session();
    hSession->start_upnp();
}

void session_start_natpmp() {
    check_session();
    hSession->start_natpmp();
}

void session_start_lsd() {
    check_session();
    hSession->start_lsd();
}

void session_start_dht() {
    check_session();
    hSession->start_dht();
}

void session_stop_upnp() {
    check_session();
    hSession->stop_upnp();
}

void session_stop_natpmp() {
    check_session();
    hSession->stop_natpmp();
}

void session_stop_lsd() {
    check_session();
    hSession->stop_lsd();
}

void session_stop_dht() {
    check_session();
    hSession->stop_dht();
}

alert **session_wait_for_alert(int millis, int *size) {
    check_session();

    deque<alert *> alerts;

    if (hSession->wait_for_alert(milliseconds(millis)) != 0) {
        hSession->pop_alerts(&alerts);
    }

    alert **arr = new alert *[alerts.size()];
    *size = alerts.size();

    for (int i = 0; i < alerts.size(); i++) {
        arr[i] = alerts[i];
    }

    return arr;
}

#ifdef JNI_INTERFACE_ENABLED

JNI_METHOD_BEGIN(void, create)

        session_create();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, release)

        session_release();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startUPnP)

        session_start_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startNATPMP)

        session_start_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startLSD)

        session_start_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startDHT)

        session_start_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopUPnP)

        session_stop_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopNATPMP)

        session_stop_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopLSD)

        session_stop_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopDHT)

        session_stop_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(jobjectArray, waitForAlert, jint millis)

        int n = 0;
        alert **alerts = session_wait_for_alert(millis, &n);

        JNI_NEW_ARRAY("com/frostwire/libtorrent/alerts/Alert", n, arr)

        for (int i = 0; i < n; i++) {
            jstring what = env->NewStringUTF(alerts[i]->what());

            jmethodID mid;
            jclass clazz = env->FindClass("com/frostwire/libtorrent/Alerts");
            if (clazz == NULL) {
                throw NewJavaError(env, "Error getting class com/frostwire/libtorrent/Alerts");
            }
            mid = env->GetStaticMethodID(clazz, "test", "(Ljava/lang/String;)Lcom/frostwire/libtorrent/alerts/Alert;");
            if (mid == NULL) {
                throw NewJavaError(env, "Cant find method \"test\" in com/frostwire/libtorrent/Alerts");
            }

            jobject obj = env->CallStaticObjectMethod(clazz, mid, what);

            delete alerts[i];

            JNI_ARRAY_SET(arr, i, obj)
        }

        delete alerts;

        return arr;

JNI_METHOD_END_RET

#endif //JNI_INTERFACE_ENABLED
