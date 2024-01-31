namespace libtorrent {
    %extend dht_sample_infohashes_alert {
        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }

        std::int64_t get_interval() {
            return libtorrent::total_milliseconds($self->interval);
        }
    } // dht_sample_infohashes_alert
} // namespace libtorrent