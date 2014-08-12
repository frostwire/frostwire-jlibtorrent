#ifndef _JLIBTORRENT_H
#define _JLIBTORRENT_H

#include "jni_util.h"

#define BOOST_ASIO_SEPARATE_COMPILATION

#include <string>

using namespace std;

#include <libtorrent/version.hpp>
#include <libtorrent/file_storage.hpp>
#include <libtorrent/create_torrent.hpp>
#include <libtorrent/session.hpp>
#include <libtorrent/alert.hpp>

using namespace libtorrent;

#endif //_JLIBTORRENT_H
