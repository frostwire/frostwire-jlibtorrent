#ifndef _JLIBTORRENT_H
#define _JLIBTORRENT_H

#define JNI_INTERFACE_ENABLED

#ifdef JNI_INTERFACE_ENABLED
#include "jni_util.h"
#endif //JNI_INTERFACE_ENABLED

#define BOOST_ASIO_SEPARATE_COMPILATION

#include <stdexcept>

using namespace std;

#include <libtorrent/version.hpp>
#include <libtorrent/file_storage.hpp>
#include <libtorrent/create_torrent.hpp>
#include <libtorrent/session.hpp>
#include <libtorrent/alert.hpp>

using namespace libtorrent;

#endif //_JLIBTORRENT_H
