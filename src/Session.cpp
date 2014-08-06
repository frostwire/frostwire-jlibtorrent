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
