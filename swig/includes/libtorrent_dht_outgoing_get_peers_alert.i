%ignore libtorrent::dht_outgoing_get_peers_alert::endpoint;
namespace libtorrent {
    %extend dht_outgoing_get_peers_alert {
        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // dht_outgoing_get_peers_alert
} // namespace libtorrent