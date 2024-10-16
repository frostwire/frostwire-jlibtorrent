namespace libtorrent {
    %extend peer_log_alert {
        std::string get_event_type() {
            return std::string($self->event_type);
        }
    } // peer_log_alert
} // namespace libtorrent
