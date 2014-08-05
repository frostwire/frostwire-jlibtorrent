#include "LibTorrent.h"

#include <libtorrent/version.hpp>
#include <libtorrent/file_storage.hpp>
#include <libtorrent/create_torrent.hpp>

using namespace std;
using namespace libtorrent;

JNI_METHOD_BEGIN(LibTorrent, jstring, version)

    return env->NewStringUTF(LIBTORRENT_VERSION);

JNI_METHOD_END

JNI_METHOD_BEGIN(LibTorrent, void, createTorrent, jobjectArray paths)

    file_storage fs;

    JNI_STRING_ARRAY_FOREACH_BEGIN(paths, path)

        add_files(fs, path);

    JNI_STRING_ARRAY_FOREACH_END(path)

    create_torrent ct(fs);

    vector<char> torrent;
    bencode(back_inserter(torrent), ct.generate());

    FILE *output = fopen("test.torrent", "wb+");
    fwrite(&torrent[0], 1, torrent.size(), output);
    fclose(output);


JNI_METHOD_END