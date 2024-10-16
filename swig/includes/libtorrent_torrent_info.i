
%include "libtorrent/torrent_info.hpp"

%typemap(out) std::string_view libtorrent::torrent_info::ssl_cert {
    $result = jenv->NewStringUTF($1.data());
}

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

        //sha1_hash_vector similar_torrents() {
        std::vector<libtorrent::digest32<160>> similar_torrents() {
            return $self->similar_torrents();
        }

        std::string get_ssl_cert() {
            return std::string($self->ssl_cert());
        }
    }; // %extend torrent_info
} // namespace libtorrent
