
namespace libtorrent {
    // dummy tag structs for strong_typedefs
    // they are simply used so that the newly defined strong type definitions have a different id
    struct add_piece_flags_tag;
    struct alert_category_tag;
    struct bandwidth_state_flags_tag;
    struct create_flags_tag;
    struct deadline_flags_tag;
    struct file_flags_tag;
    struct file_progress_flags_tag;
    struct pause_flags_tag;
    struct peer_flags_tag;
    struct peer_source_flags_tag;
    struct pex_flags_tag;
    struct picker_flags_tag;
    struct port_mapping_tag;
    struct reannounce_flags_tag;
    struct resume_data_flags_tag;
    struct status_flags_tag;
    struct write_torrent_flags_tag;

//     namespace aux {
//     template<typename UnderlyingType, typename Tag>
//         struct strong_typedef;
//     }

    %template(add_piece_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, add_piece_flags_tag>;
    %template(alert_category_t) libtorrent::flags::bitfield_flag<std::uint32_t, alert_category_tag>;
    %template(bandwidth_state_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, bandwidth_state_flags_tag>;
    %template(create_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, create_flags_tag>;
    %template(deadline_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, deadline_flags_tag>;
    %template(file_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, file_flags_tag>;
    %template(file_progress_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, file_progress_flags_tag>;
    %template(pause_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, pause_flags_tag>;
    %template(peer_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, peer_flags_tag>;
    %template(peer_source_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, peer_source_flags_tag>;
    %template(pex_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, pex_flags_tag>;
    %template(picker_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, picker_flags_tag>;
    %template(reannounce_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, reannounce_flags_tag>;
    %template(resume_data_flags_t) libtorrent::flags::bitfield_flag<std::uint8_t, resume_data_flags_tag>;
    %template(status_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, status_flags_tag>;
    %template(write_torrent_flags_t) libtorrent::flags::bitfield_flag<std::uint32_t, write_torrent_flags_tag>;

} // namespace libtorrent

using remove_flags_t = libtorrent::remove_flags_t;