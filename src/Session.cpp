#include "Session.h"

#include <libtorrent/session.hpp>
#include <libtorrent/alert.hpp>

#include <boost/foreach.hpp>

using namespace std;
using namespace libtorrent;

JNI_METHOD_BEGIN(jlong, create)

    session *s = new session();

    return (jlong) s;

JNI_METHOD_END

JNI_METHOD_BEGIN(void, release, jlong handle)

    session *s = (session *) handle;

    delete s;

JNI_METHOD_END

JNI_METHOD_BEGIN(jlong, startUPnP, jlong handle)

    session *s = (session *) handle;

    return (jlong) s->start_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(jlong, startNATPMP, jlong handle)

    session *s = (session *) handle;

    return (jlong) s->start_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startLSD, jlong handle)

    session *s = (session *) handle;

    s->start_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, startDHT, jlong handle)

    session *s = (session *) handle;

    s->start_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(void, waitForAlerts, jlong handle, jint millis)

    session *s = (session *) handle;

    deque<alert *> alerts;

    if (s->wait_for_alert(milliseconds(millis)) != 0) {
        s->pop_alerts(&alerts);
    }

    for (alert *a: alerts) {
        //cout << *a << "\n";
    }

JNI_METHOD_END
