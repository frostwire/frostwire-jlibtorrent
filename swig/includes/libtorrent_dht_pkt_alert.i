%ignore libtorrent::dht_pkt_alert::node;
namespace libtorrent {
    %extend dht_pkt_alert {
        udp::endpoint get_node() {
            return $self->node;
        }
    } // dht_pkt_alert
} // namespace libtorrent