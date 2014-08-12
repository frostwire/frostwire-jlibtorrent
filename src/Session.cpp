#include "Session.h"

#ifdef JNI_INTERFACE_ENABLED

JNI_METHOD_BEGIN(jlong, create)

    session *s = new session();

    s->set_alert_mask(alert::all_categories);

    return (jlong) s;

JNI_METHOD_END

JNI_METHOD_BEGIN(void, release, jlong handle)

    session *s = (session *) handle;

    delete s;

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startUPnP, jlong handle)

    session *s = (session *) handle;

    s->start_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startNATPMP, jlong handle)

    session *s = (session *) handle;

    s->start_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startLSD, jlong handle)

    session *s = (session *) handle;

    s->start_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startDHT, jlong handle)

    session *s = (session *) handle;

    s->start_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopUPnP, jlong handle)

    session *s = (session *) handle;

    s->stop_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopNATPMP, jlong handle)

    session *s = (session *) handle;

    s->stop_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopLSD, jlong handle)

    session *s = (session *) handle;

    s->stop_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, stopDHT, jlong handle)

    session *s = (session *) handle;

    s->stop_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(jobjectArray, waitForAlerts, jlong handle, jint millis)

    session *s = (session *) handle;

    deque<alert *> alerts;

    if (s->wait_for_alert(milliseconds(millis)) != 0) {
        s->pop_alerts(&alerts);
    }

    JNI_NEW_ARRAY("com/frostwire/libtorrent/alerts/Alert", alerts.size(), arr)

    for (int i = 0; i < alerts.size(); i++) {
        jstring what = env->NewStringUTF(alerts[i]->what());

        jmethodID mid;
        jclass handlerClass = env->FindClass("com/frostwire/libtorrent/Alerts");
        if (handlerClass == NULL) {
            cout << "no clazz" << endl;
        }
        mid = env->GetStaticMethodID(handlerClass, "test", "(Ljava/lang/String;)Lcom/frostwire/libtorrent/alerts/Alert;");
        if (mid == NULL) {
            cout << "no method" << endl;
        }

        jobject obj = env->CallStaticObjectMethod(handlerClass, mid, what);

        JNI_ARRAY_SET(arr, i, obj)
    }

    return arr;

JNI_METHOD_END

#endif //JNI_INTERFACE_ENABLED
