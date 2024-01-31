namespace libtorrent {
    %extend dht_pkt_alert {
        udp::endpoint get_node() {
            return $self->node;
        }
    } // dht_pkt_alert
} // namespace libtorrent