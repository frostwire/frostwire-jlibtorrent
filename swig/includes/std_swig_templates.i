namespace std {

    %template(piece_index_int_pair) pair<piece_index_t, int>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(string_view_bdecode_node_pair) pair<libtorrent::string_view, libtorrent::bdecode_node>;
    %template(byte_vectors_pair) pair<vector<int8_t>, vector<int8_t>>;
    %template(sha1_hash_udp_endpoint_pair) pair<libtorrent::sha1_hash, libtorrent::udp::endpoint>;
    %template(address_sha1_hash_pair) pair<libtorrent::address, libtorrent::sha1_hash>;

    %template(byte_vector) vector<int8_t>;
    %template(string_vector) vector<std::string>;
    %template(string_int_pair_vector) vector<std::pair<std::string, int>>;
    %template(string_string_pair_vector) vector<std::pair<std::string, std::string>>;
    %template(piece_index_int_pair_vector) vector<std::pair<piece_index_t, int>>;

    %template(int_vector) vector<int>;
    %template(int64_vector) vector<long long>;
    %template(sha1_hash_vector) vector<libtorrent::sha1_hash>;
    %template(torrent_status_vector) vector<libtorrent::torrent_status>;
    %template(torrent_handle_vector) vector<libtorrent::torrent_handle>;
    %template(file_slice_vector) vector<libtorrent::file_slice>;
    %template(dht_routing_bucket_vector) vector<libtorrent::dht_routing_bucket>;
    %template(dht_lookup_vector) vector<libtorrent::dht_lookup>;

    %template(block_info_vector) vector<libtorrent::block_info>;
    %template(partial_piece_info_vector) vector<libtorrent::partial_piece_info>;
    %template(peer_info_vector) vector<libtorrent::peer_info>;
    %template(stats_metric_vector) vector<libtorrent::stats_metric>;

    %template(entry_vector) vector<libtorrent::entry>;
    %template(web_seed_entry_vector) vector<libtorrent::web_seed_entry>;
    %template(announce_endpoint_vector) vector<libtorrent::announce_endpoint>;
    %template(announce_entry_vector) vector<libtorrent::announce_entry>;
    %template(tcp_endpoint_vector) vector<libtorrent::tcp::endpoint>;
    %template(udp_endpoint_vector) vector<libtorrent::udp::endpoint>;
    %template(piece_index_vector) vector<piece_index_t>;
    %template(file_index_vector) vector<file_index_t>;
    %template(sha1_hash_udp_endpoint_pair_vector) vector<pair<libtorrent::sha1_hash, libtorrent::udp::endpoint>>;
    %template(address_sha1_hash_pair_vector) vector<pair<libtorrent::address, libtorrent::sha1_hash>>;

    %template(file_index_string_map) map<file_index_t, std::string>;
    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;

    %template(alert_ptr_vector) vector<libtorrent::alert*>;

    %template(ip_interface_vector) vector<ip_interface>;
    %template(ip_route_vector) vector<ip_route>;
    %template(port_mapping_t_vector) vector<port_mapping_t>;

    %template(bitset_96) bitset<96>;
}; // namespace std