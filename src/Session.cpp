#include "Session.h"

#include <libtorrent/session.hpp>

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
