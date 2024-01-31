namespace std {

    %template(address_sha1_hash_pair) pair<libtorrent::address, libtorrent::sha1_hash>;
    %template(address_sha1_hash_pair_vector) std::vector<std::pair<libtorrent::address, libtorrent::digest32<160>>>;
    %template(alert_ptr_vector) std::vector<libtorrent::alert*>;
    %template(announce_endpoint_vector) std::vector<libtorrent::announce_endpoint>;
    %template(announce_entry_vector) std::vector<libtorrent::announce_entry>;
    %template(bitfield_vector) std::vector<libtorrent::bitfield>;
    %template(bitset_96) bitset<96>;
    %template(block_info_vector) std::vector<libtorrent::block_info>;
    %template(bool_vector) std::vector<bool>;
    %template(bool_vector_vector) std::vector<std::vector<bool>>;
    %template(byte_vector) std::vector<std::int8_t>;
    %template(byte_vector_byte_vector_pair) std::pair<std::vector<std::int8_t>, std::vector<std::int8_t>>;
    %template(dht_lookup_vector) std::vector<libtorrent::dht_lookup>;
    %template(dht_routing_bucket_vector) std::vector<libtorrent::dht_routing_bucket>;
    %template(entry_vector) std::vector<libtorrent::entry>;
    %template(file_index_string_map) map<libtorrent::file_index_t, std::string>;
    %template(file_index_vector) std::vector<libtorrent::file_index_t>;
    %template(file_slice_vector) std::vector<libtorrent::file_slice>;
    %template(int64_vector) std::vector<long long>;
    %template(int_byte_pair_vector) std::vector<std::pair<int, std::int8_t>>;
    %template(int_vector) std::vector<int>;
    %template(ip_interface_vector) std::vector<ip_interface>; // not libtorrent::ip_interface, but ip_interface struct from libtorrent.hpp:~220
    %template(ip_route_vector) std::vector<ip_route>; // not libtorrent::ip_route, but ip_route struct from libtorrent.hpp:~230
    %template(partial_piece_info_vector) std::vector<libtorrent::partial_piece_info>;
    %template(peer_info_vector) std::vector<libtorrent::peer_info>;
    %template(piece_block_vector) std::vector<libtorrent::piece_block>;
    %template(piece_index_int_pair) pair<piece_index_t, int>;
    %template(piece_index_int_pair_vector) vector<std::pair<libtorrent::piece_index_t, int>>;
    %template(piece_index_vector) std::vector<libtorrent::piece_index_t>;
    %template(port_mapping_t_vector) std::vector<libtorrent::port_mapping_t>;
    %template(sha1_hash_udp_endpoint_pair) pair<libtorrent::sha1_hash, libtorrent::udp::endpoint>;
    %template(sha1_hash_udp_endpoint_pair_vector) std::vector<std::pair<libtorrent::digest32<160>, libtorrent::udp::endpoint>>;
    %template(sha1_hash_vector) std::vector<libtorrent::digest32<160>>;
    %template(sha256_hash_vector) std::vector<libtorrent::digest32<256>>;
    %template(sha256_hash_vector_vector) std::vector<std::vector<libtorrent::digest32<256>>>;
    %template(stats_metric_vector) std::vector<libtorrent::stats_metric>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_int_pair_vector) std::vector<std::pair<std::string, int>>;
    %template(string_long_map) map<std::string, long>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(string_string_pair_vector) std::vector<std::pair<std::string, std::string>>;
    %template(string_vector) std::vector<std::string>;
    %template(string_view_bdecode_node_pair) pair<libtorrent::string_view, libtorrent::bdecode_node>;
    %template(tcp_endpoint_vector) std::vector<libtorrent::tcp::endpoint>;
    %template(torrent_handle_vector) std::vector<libtorrent::torrent_handle>;
    %template(torrent_status_vector) std::vector<libtorrent::torrent_status>;
    %template(udp_endpoint_vector) std::vector<libtorrent::udp::endpoint>;
    %template(web_seed_entry_vector) std::vector<libtorrent::web_seed_entry>;
}; // namespace std