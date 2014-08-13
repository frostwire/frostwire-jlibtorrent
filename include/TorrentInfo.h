#ifndef _TORRENT_INFO_H
#define _TORRENT_INFO_H

#include "jlibtorrent.h"

typedef torrent_info *torrent_info_ptr;

#ifdef __cplusplus
extern "C" {
#endif

torrent_info_ptr torrent_info_create(const char *, int);
void torrent_info_release(torrent_info_ptr);

long total_size(torrent_info_ptr);
int piece_length(torrent_info_ptr);
int num_pieces(torrent_info_ptr);

const char *info_hash(torrent_info_ptr);

#ifdef JNI_INTERFACE_ENABLED

#define JNI_CLASS_NAME TorrentInfo

JNI_METHOD(jlong, create, jstring, int)
JNI_METHOD_HANDLE(void, release)

JNI_METHOD_HANDLE(jlong, totalSize)
JNI_METHOD_HANDLE(jint, pieceLength)
JNI_METHOD_HANDLE(jint, numPieces)

JNI_METHOD_HANDLE(jstring, infoHash)

#endif //JNI_INTERFACE_ENABLED

#ifdef __cplusplus
}
#endif
#endif //_TORRENT_INFO_H
