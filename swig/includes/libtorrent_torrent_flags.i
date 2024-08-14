%include "libtorrent/torrent_flags.hpp"

namespace libtorrent {
    struct torrent_flags_tag;
    %template(torrent_flags_t) libtorrent::flags::bitfield_flag<std::uint64_t, torrent_flags_tag>;
}