%include "libtorrent/session_types.hpp"

namespace libtorrent {
    struct remove_flags_tag;
    %template(remove_flags_t) flags::bitfield_flag<std::uint8_t, remove_flags_tag>;

    struct save_state_flags_tag;
    %template(save_state_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, save_state_flags_tag>;

    struct session_flags_tag;
    %template(session_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, session_flags_tag>;

    struct reopen_network_flags_tag;
    %template(reopen_network_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, reopen_network_flags_tag>;

} // namespace libtorrent