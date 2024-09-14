namespace libtorrent {
    %extend dht_direct_response_alert {
       client_data_t get_userdata() {
           return $self->userdata;
       }

        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // dht_direct_response_alert
} // namespace libtorrent
