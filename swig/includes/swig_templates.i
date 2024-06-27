%template(int_byte_pair) std::pair<int, std::int8_t>;
%template(string_int_pair) std::pair<std::string, int>;
%template(string_string_pair) std::pair<std::string, std::string>;
%template(byte_vector_byte_vector_pair) std::pair<std::vector<std::int8_t>, std::vector<std::int8_t>>;

%template(sha1_hash_udp_endpoint_pair) std::pair<libtorrent::digest32<160>, libtorrent::udp::endpoint>;
%template(bdecode_node_bdecode_node_pair) std::pair<libtorrent::bdecode_node, libtorrent::bdecode_node>;
%template(address_sha1_hash_pair) std::pair<libtorrent::address, libtorrent::digest32<160>>;

%template(string_vector) std::vector<std::string>;
%template(int_vector) std::vector<int>;
%template(int64_vector) std::vector<long long>;
%template(byte_vector) std::vector<std::int8_t>;
%template(bool_vector) std::vector<bool>;
%template(int_byte_pair_vector) std::vector<std::pair<int, std::int8_t>>;
%template(string_int_pair_vector) std::vector<std::pair<std::string, int>>;
%template(string_string_pair_vector) std::vector<std::pair<std::string, std::string>>;

%template(tcp_endpoint_vector) std::vector<libtorrent::tcp::endpoint>;
%template(udp_endpoint_vector) std::vector<libtorrent::udp::endpoint>;
%template(announce_endpoint_vector) std::vector<libtorrent::announce_endpoint>;
%template(announce_entry_vector) std::vector<libtorrent::announce_entry>;
%template(web_seed_entry_vector) std::vector<libtorrent::web_seed_entry>;
%template(file_slice_vector) std::vector<libtorrent::file_slice>;
%template(piece_block_vector) std::vector<libtorrent::piece_block>;
%template(torrent_status_vector) std::vector<libtorrent::torrent_status>;
%template(dht_lookup_vector) std::vector<libtorrent::dht_lookup>;
%template(dht_routing_bucket_vector) std::vector<libtorrent::dht_routing_bucket>;
%template(entry_vector) std::vector<libtorrent::entry>;
%template(partial_piece_info_vector) std::vector<libtorrent::partial_piece_info>;
%template(peer_info_vector) std::vector<libtorrent::peer_info>;
%template(torrent_handle_vector) std::vector<libtorrent::torrent_handle>;
%template(alert_ptr_vector) std::vector<libtorrent::alert*>;
%template(stats_metric_vector) std::vector<libtorrent::stats_metric>;
%template(ip_interface_vector) std::vector<ip_interface>;
%template(ip_route_vector) std::vector<ip_route>;
//%template(create_file_entry_vector) std::vector<libtorrent::create_file_entry>;
%template(block_info_vector) std::vector<libtorrent::block_info>;
%template(bitfield_vector) std::vector<libtorrent::bitfield>;

%template(bool_vector_vector) std::vector<std::vector<bool>>;
%template(sha256_hash_vector_vector) std::vector<std::vector<libtorrent::digest32<256>>>;
%template(sha1_hash_udp_endpoint_pair_vector) std::vector<std::pair<libtorrent::digest32<160>, libtorrent::udp::endpoint>>;
%template(address_sha1_hash_pair_vector) std::vector<std::pair<libtorrent::address, libtorrent::digest32<160>>>;

%template(byte_array_32) std::array<std::int8_t, 32>;
%template(byte_array_64) std::array<std::int8_t, 64>;

%template(int_string_map) std::map<int, std::string>;
%template(string_string_map) std::map<std::string, std::string>;
%template(int_bitfield_map) std::map<int, libtorrent::bitfield>;
%template(string_entry_map) std::map<std::string, libtorrent::entry>;

%template(boost_string_entry_map) boost::container::map<std::string, libtorrent::entry>;

%template(file_index_string_map) std::map<libtorrent::file_index_t, std::string>;
%template(file_index_vector) std::vector<libtorrent::file_index_t>;
%template(piece_index_int_pair) std::pair<piece_index_t, int>;
%template(piece_index_int_pair_vector) std::vector<std::pair<libtorrent::piece_index_t, int>>;
%template(piece_index_vector) std::vector<libtorrent::piece_index_t>;
%template(string_long_map) std::map<std::string, long>;
%template(string_view_bdecode_node_pair) std::pair<libtorrent::string_view, libtorrent::bdecode_node>;

// these are defined in libtorrent_sha1_hash.i, delete these from below when we finally finish the transition
//%template(sha1_hash) libtorrent::digest32<160>;
//%template(sha256_hash) libtorrent::digest32<256>;
//%template(sha1_hash_vector) std::vector<libtorrent::digest32<160>>;
//%template(sha256_hash_vector) std::vector<libtorrent::digest32<256>>;





%template(bitset_128) std::bitset<128>;
%template(bitset_96) std::bitset<96>;