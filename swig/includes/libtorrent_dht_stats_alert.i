namespace libtorrent {
    %extend dht_stats_alert {
        udp::endpoint get_local_endpoint() {
            return $self->local_endpoint;
        }
    }
} // namespace libtorrent