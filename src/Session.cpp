#include "Session.h"

#include <libtorrent/session.hpp>
#include <libtorrent/alert.hpp>

using namespace std;
using namespace libtorrent;

JNI_METHOD_BEGIN(Session, jlong, create)

    session *s = new session();

    return (jlong) s;

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, void, release, jlong handle)

    session *s = (session *) handle;

    delete s;

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, jlong, startUPnP, jlong handle)

    session *s = (session *) handle;

    return (jlong) s->start_upnp();

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, jlong, startNATPMP, jlong handle)

    session *s = (session *) handle;

    return (jlong) s->start_natpmp();

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, void, startLSD, jlong handle)

    session *s = (session *) handle;

    s->start_lsd();

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, void, startDHT, jlong handle)

    session *s = (session *) handle;

    s->start_dht();

JNI_METHOD_END

JNI_METHOD_BEGIN(Session, void, waitForAlerts, jlong handle, jint millis)

    session *s = (session *) handle;

    deque<alert *> alerts;

    if (s->wait_for_alert(milliseconds(millis)) != 0) {
        s->pop_alerts(&alerts);
    }

    for (auto it = alerts.begin(); it != alerts.end(); ++it) {
        cout << *it << "\n";
    }

JNI_METHOD_END
