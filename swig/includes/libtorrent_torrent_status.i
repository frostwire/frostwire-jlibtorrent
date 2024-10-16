%ignore libtorrent::torrent_status::_dummy_string_;
%ignore libtorrent::torrent_status::active_duration;
%ignore libtorrent::torrent_status::allocating;
%ignore libtorrent::torrent_status::deprecated_active_time;
%ignore libtorrent::torrent_status::deprecated_announce_interval_;
%ignore libtorrent::torrent_status::deprecated_auto_managed;
%ignore libtorrent::torrent_status::deprecated_finished_time;
%ignore libtorrent::torrent_status::deprecated_ip_filter_applies;
%ignore libtorrent::torrent_status::deprecated_is_loaded;
%ignore libtorrent::torrent_status::deprecated_last_scrape;
%ignore libtorrent::torrent_status::deprecated_paused;
%ignore libtorrent::torrent_status::deprecated_priority;
%ignore libtorrent::torrent_status::deprecated_seed_mode;
%ignore libtorrent::torrent_status::deprecated_seeding_time;
%ignore libtorrent::torrent_status::deprecated_sequential_download;
%ignore libtorrent::torrent_status::deprecated_share_mode;
%ignore libtorrent::torrent_status::deprecated_stop_when_ready;
%ignore libtorrent::torrent_status::deprecated_super_seeding;
%ignore libtorrent::torrent_status::deprecated_time_since_download;
%ignore libtorrent::torrent_status::deprecated_time_since_upload;
%ignore libtorrent::torrent_status::deprecated_upload_mode;
%ignore libtorrent::torrent_status::finished_duration;
%ignore libtorrent::torrent_status::last_download;
%ignore libtorrent::torrent_status::last_upload;
%ignore libtorrent::torrent_status::next_announce;
%ignore libtorrent::torrent_status::pieces;
%ignore libtorrent::torrent_status::queue_position;
%ignore libtorrent::torrent_status::seeding_duration;
%ignore libtorrent::torrent_status::torrent_file;
%ignore libtorrent::torrent_status::torrent_status(torrent_status&&);
%ignore libtorrent::torrent_status::unused_enum_for_backwards_compatibility;
%ignore libtorrent::torrent_status::unused_enum_for_backwards_compatibility_allocating;
%ignore libtorrent::torrent_status::verified_pieces;

namespace libtorrent {
    struct torrent_handle;
}

%include "libtorrent/torrent_status.hpp"

namespace libtorrent {
    %extend torrent_status {

        torrent_info const* torrent_file_ptr() {
            return $self->torrent_file.lock().get();
        }

        int64_t get_next_announce() {
            return libtorrent::total_milliseconds($self->next_announce);
        }

        int64_t get_last_upload() {
            return libtorrent::total_milliseconds($self->last_upload.time_since_epoch());
        }

        int64_t get_last_download() {
            return libtorrent::total_milliseconds($self->last_download.time_since_epoch());
        }

        int64_t get_active_duration() {
            return libtorrent::total_milliseconds($self->active_duration);
        }

        int64_t get_finished_duration() {
            return libtorrent::total_milliseconds($self->finished_duration);
        }

        int64_t get_seeding_duration() {
            return libtorrent::total_milliseconds($self->seeding_duration);
        }

        int get_queue_position() {
            return static_cast<int>($self->queue_position);
        }

        bitfield get_pieces() {
            auto* v = &$self->pieces;
            return *reinterpret_cast<libtorrent::bitfield*>(v);
        }

        bitfield get_verified_pieces() {
            auto* v = &$self->verified_pieces;
            return *reinterpret_cast<libtorrent::bitfield*>(v);
        }
    } // extend torrent_status
} // namespace libtorrent