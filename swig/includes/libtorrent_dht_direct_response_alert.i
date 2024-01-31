namespace libtorrent {
    %extend dht_direct_response_alert {
        int64_t get_userdata() {
            return (int64_t)$self->userdata;
        }

        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // dht_direct_response_alert
} // namespace libtorrent
