%ignore libtorrent::aux::sanitize_append_path_element;
%ignore libtorrent::aux::verify_encoding;
%ignore libtorrent::from_span;
%ignore libtorrent::from_span_t;
%ignore libtorrent::torrent_info::_internal_drain;
%ignore libtorrent::torrent_info::add_merkle_nodes;
%ignore libtorrent::torrent_info::build_merkle_list;
%ignore libtorrent::torrent_info::clear_web_seeds;
%ignore libtorrent::torrent_info::hash_for_piece_ptr;
%ignore libtorrent::torrent_info::info_section;
%ignore libtorrent::torrent_info::internal_load_merkle_trees;
%ignore libtorrent::torrent_info::internal_merkle_trees;
%ignore libtorrent::torrent_info::internal_set_comment;
%ignore libtorrent::torrent_info::internal_set_creation_date;
%ignore libtorrent::torrent_info::internal_set_creator;
%ignore libtorrent::torrent_info::internal_web_seeds;
%ignore libtorrent::torrent_info::load;
%ignore libtorrent::torrent_info::metadata;
%ignore libtorrent::torrent_info::parse_info_section;
%ignore libtorrent::torrent_info::parse_torrent_file;
%ignore libtorrent::torrent_info::piece_layer;
%ignore libtorrent::torrent_info::piece_range;
%ignore libtorrent::torrent_info::set_piece_layers;
%ignore libtorrent::torrent_info::ssl_cert;
%ignore libtorrent::torrent_info::swap;
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&, int);
%ignore libtorrent::torrent_info::torrent_info(span<char const>, error_code&, from_span_t);
%ignore libtorrent::torrent_info::unload;
%ignore libtorrent::torrent_info::v2_piece_hashes_verified;

%include "libtorrent/torrent_info.hpp"

namespace libtorrent {
    %extend torrent_info
    {
        torrent_info(std::int64_t buffer_ptr, int size, error_code& ec)
        {
            return new libtorrent::torrent_info(reinterpret_cast<char const*>(buffer_ptr), size, ec);
        }

        libtorrent::span<std::int8_t const> get_info_section()
        {
            auto v = $self->info_section();
            return libtorrent::span<std::int8_t const>({reinterpret_cast<std::int8_t const*>(v.data()), v.size()});
        }
    }; // %extend torrent_info
} // namespace libtorrent