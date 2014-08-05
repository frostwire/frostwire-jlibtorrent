#define BOOST_ASIO_SEPARATE_COMPILATION

#include <libtorrent/file_storage.hpp>
#include <libtorrent/create_torrent.hpp>

using namespace libtorrent;

int test() {

    file_storage fs;

    add_files(fs, "test");

    create_torrent t(fs);

    return 0;
}