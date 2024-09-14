namespace libtorrent {
    %extend dht_pkt_alert {
        udp::endpoint get_node() {
            return $self->node;
        }

        std::vector<std::int8_t> get_pkt_buf() {
            auto buf = $self->pkt_buf();
            return {buf.begin(), buf.end()};
        }
    } // dht_pkt_alert
} // namespace libtorrent