#include "Session.h"

session_ptr session_create() {
    session_ptr s = new session();

    s->set_alert_mask(alert::all_categories);

    return s;
}

void session_release(session_ptr s) {
    delete s;
}

void session_start_upnp(session_ptr s) {
    s->start_upnp();
}

void session_start_natpmp(session_ptr s) {
    s->start_natpmp();
}

void session_start_lsd(session_ptr s) {
    s->start_lsd();
}

void session_start_dht(session_ptr s) {
    s->start_dht();
}

void session_stop_upnp(session_ptr s) {
    s->stop_upnp();
}

void session_stop_natpmp(session_ptr s) {
    s->stop_natpmp();
}

void session_stop_lsd(session_ptr s) {
    s->stop_lsd();
}

void session_stop_dht(session_ptr s) {
    s->stop_dht();
}

alert_array session_wait_for_alert(session_ptr s, int millis, int *size) {
    deque<alert *> alerts;

    if (s->wait_for_alert(milliseconds(millis)) != 0) {
        s->pop_alerts(&alerts);
    }

    alert_array arr = new alert *[alerts.size()];
    *size = alerts.size();

    for (int i = 0; i < alerts.size(); i++) {
        arr[i] = alerts[i];
    }

    return arr;
}

#ifdef JNI_INTERFACE_ENABLED

#define JNI_METHOD_BEGIN_S(type, name, ...) \
    JNI_METHOD_BEGIN(type, name, jlong hSession, ##__VA_ARGS__)\
        session_ptr s = (session_ptr) hSession;

JNI_METHOD_BEGIN(jlong, create)

        return (jlong) session_create();

JNI_METHOD_END_RET(jlong)

JNI_METHOD_BEGIN_S(void, release)

        session_release(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, startUPnP)

        session_start_upnp(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, startNATPMP)

        session_start_natpmp(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, startLSD)

        session_start_lsd(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, startDHT)

        session_start_dht(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, stopUPnP)

        session_stop_upnp(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, stopNATPMP)

        session_stop_natpmp(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, stopLSD)

        session_stop_lsd(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(void, stopDHT)

        session_stop_dht(s);

JNI_METHOD_END

JNI_METHOD_BEGIN_S(jobjectArray, waitForAlert, jint millis)

        int n = 0;
        alert_array alerts = session_wait_for_alert(s, millis, &n);

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

JNI_METHOD_END_RET(jobjectArray)

#endif //JNI_INTERFACE_ENABLED
