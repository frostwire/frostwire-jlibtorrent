namespace libtorrent {
    namespace dht {
        struct dht_state {
            std::vector<std::pair<address, libtorrent::sha1_hash>> nids;
            std::vector<udp::endpoint> nodes;
            std::vector<udp::endpoint> nodes6;
            void clear();
        }; // struct dht_state

    } // namespace dht
} // namespace libtorrent