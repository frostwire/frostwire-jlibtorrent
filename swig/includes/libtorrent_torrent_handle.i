%ignore libtorrent::torrent_handle::torrent_handle;
%ignore libtorrent::torrent_handle::add_extension;
%ignore libtorrent::torrent_handle::add_piece;
%ignore libtorrent::torrent_handle::http_seeds;
%ignore libtorrent::torrent_handle::url_seeds;
%ignore libtorrent::torrent_handle::get_storage_impl;
%ignore libtorrent::torrent_handle::file_status;
%ignore libtorrent::torrent_handle::use_interface;
%ignore libtorrent::torrent_handle::native_handle;
%ignore libtorrent::torrent_handle::torrent_file;
%ignore libtorrent::torrent_handle::get_full_peer_list;
%ignore libtorrent::torrent_handle::set_metadata;
%ignore libtorrent::torrent_handle::set_ssl_certificate_buffer;
%ignore libtorrent::torrent_handle::queue_position;
%ignore libtorrent::torrent_handle::queue_position_set;
%ignore libtorrent::torrent_handle::piece_priority;
%ignore libtorrent::torrent_handle::prioritize_pieces;
%ignore libtorrent::torrent_handle::get_piece_priorities;
%ignore libtorrent::torrent_handle::file_priority;
%ignore libtorrent::torrent_handle::prioritize_files;
%ignore libtorrent::torrent_handle::get_file_priorities;

namespace libtorrent {
    // for torrent_status
    struct torrent_handle;
} // namespace libtorrent