// libtorrent 2.1 alerts posted by torrent_handle::post_file_status(), post_file_priorities(), and
// for a banned IP address.
namespace libtorrent {
    %extend file_status_alert {
        // the indices of the files the torrent has open
        std::vector<int> get_open_file_indices() {
            std::vector<int> ret;
            ret.reserve($self->state.size());
            for (auto const& s : $self->state) ret.push_back(static_cast<int>(s.file_index));
            return ret;
        }
    } // file_status_alert

    %extend file_priorities_alert {
        // the priority of every file in the torrent, as download_priority_t values
        std::vector<std::int8_t> get_priorities() {
            std::vector<std::int8_t> ret;
            ret.reserve($self->priorities.size());
            for (auto const p : $self->priorities) ret.push_back(static_cast<std::int8_t>(static_cast<std::uint8_t>(p)));
            return ret;
        }
    } // file_priorities_alert

    %extend ip_ban_alert {
        address get_banned_address() {
            return $self->banned_address;
        }
    } // ip_ban_alert
} // namespace libtorrent
