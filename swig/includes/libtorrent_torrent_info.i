

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