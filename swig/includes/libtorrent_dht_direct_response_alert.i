%ignore libtorrent::dht_direct_response_alert::dht_direct_response_alert;
%ignore libtorrent::dht_direct_response_alert::userdata;
%ignore libtorrent::dht_direct_response_alert::endpoint;
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
