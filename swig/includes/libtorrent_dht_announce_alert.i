namespace libtorrent {
    %extend dht_announce_alert {
        address get_ip() {
            return $self->ip;
        }
    } // dht_announce_alert
} // namespace libtorrent