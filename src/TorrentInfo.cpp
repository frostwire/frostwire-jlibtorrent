#include "TorrentInfo.h"

torrent_info_ptr torrent_info_create(const char *filename, int flags) {
    torrent_info_ptr ti = new torrent_info(string(filename), flags);

    return ti;
}

void torrent_info_release(torrent_info_ptr ti) {
    delete ti;
}

long total_size(torrent_info_ptr ti) {
    return ti->total_size();
}

int piece_length(torrent_info_ptr ti) {
    return ti->piece_length();
}

int num_pieces(torrent_info_ptr ti) {
    return ti->num_pieces();
}

const char *info_hash(torrent_info_ptr ti) {
    return ti->info_hash().to_string().c_str();
}

#ifdef JNI_INTERFACE_ENABLED

#define JNI_METHOD_BEGIN_TI(type, name, ...) \
    JNI_METHOD_BEGIN_HANDLE(type, name, torrent_info_ptr, ti, ##__VA_ARGS__)

JNI_METHOD_BEGIN(jlong, create, jstring filename, int flags)

        jboolean isStrCopy;
        const char *cFilename = env->GetStringUTFChars(filename, &isStrCopy);

        torrent_info_ptr ti = torrent_info_create(cFilename, flags);

        env->ReleaseStringUTFChars(filename, cFilename);

        return (jlong) ti;

JNI_METHOD_END_RET(jlong)

JNI_METHOD_BEGIN_TI(void, release)

        torrent_info_release(ti);

JNI_METHOD_END

JNI_METHOD_BEGIN_TI(jlong, totalSize)

        return total_size(ti);

JNI_METHOD_END_RET(jlong)

JNI_METHOD_BEGIN_TI(jint, pieceLength)

        return piece_length(ti);

JNI_METHOD_END_RET(jint)

JNI_METHOD_BEGIN_TI(jint, numPieces)

        return num_pieces(ti);

JNI_METHOD_END_RET(jint)

JNI_METHOD_BEGIN_TI(jstring, infoHash)

        return env->NewStringUTF(info_hash(ti));

JNI_METHOD_END_RET(jstring)

#endif //JNI_INTERFACE_ENABLED
