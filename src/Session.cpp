#include "Session.h"

JNI_METHOD_BEGIN(jlong, create)

    session *s = new session();

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
        cout << alerts[i]->what() << endl;
        jstring what = env->NewStringUTF(alerts[i]->what());

        jmethodID mid;
        jclass handlerClass = env->FindClass("com/frostwire/libtorrent/Alerts");
        if (handlerClass == NULL) {
            /* error handling */
        }
        mid = env->GetMethodID(handlerClass, "test", "(Ljava/lang/String;)com/frostwire/libtorrent/Alerts");
        if (mid == NULL) {
            /* error handling */
        }

        jobject obj = env->CallStaticObjectMethod(handlerClass, mid, what);

        JNI_ARRAY_SET(arr, i, obj)
    }

    return arr;

JNI_METHOD_END
