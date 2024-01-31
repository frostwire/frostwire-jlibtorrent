namespace libtorrent {
    %extend torrent_conflict_alert {

        torrent_info get_metadata() {
            return *($self->metadata);
        }
    }
} // namespace libtorrent