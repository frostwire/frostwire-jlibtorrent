%ignore libtorrent::TORRENT_CFG;
%ignore libtorrent::apply_pack;
%ignore libtorrent::apply_pack_impl;
%ignore libtorrent::aux;
%ignore libtorrent::aux::filter_impl; // maybe remove this one
%ignore libtorrent::bdecode_category;
%ignore libtorrent::block_info::set_peer;
%ignore libtorrent::bt_peer_connection_handle::native_handle;
%ignore libtorrent::bt_peer_connection_handle::switch_recv_crypto;
%ignore libtorrent::bt_peer_connection_handle::switch_send_crypto;
%ignore libtorrent::detail;
%ignore libtorrent::dht::extract_node_ids;
%ignore libtorrent::dht::read_dht_settings;
%ignore libtorrent::dht::read_dht_state;
%ignore libtorrent::dht::save_dht_settings;
%ignore libtorrent::dht::save_dht_state;
%ignore libtorrent::dht_sample_infohashes_alert::endpoint;
%ignore libtorrent::dht_sample_infohashes_alert::interval;
%ignore libtorrent::error_code;
%ignore libtorrent::error_to_close_reason;
%ignore libtorrent::errors::deprecated_120;
%ignore libtorrent::errors::deprecated_121;
%ignore libtorrent::errors::deprecated_122;
%ignore libtorrent::errors::deprecated_123;
%ignore libtorrent::errors::deprecated_124;
%ignore libtorrent::errors::make_error_code;

// libtorrent_file_storage.i
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>);
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>, create_flags_t);
%ignore libtorrent::file_storage::add_file;
%ignore libtorrent::file_storage::add_file(std::string const&, std::int64_t, std::uint32_t, std::time_t, string_view);
%ignore libtorrent::file_storage::add_file_borrow;
%ignore libtorrent::file_storage::all_path_hashes;
%ignore libtorrent::file_storage::apply_pointer_offset;
%ignore libtorrent::file_storage::file_name;
%ignore libtorrent::file_storage::file_name_len;
%ignore libtorrent::file_storage::file_name_ptr;
%ignore libtorrent::file_storage::file_path_hash;
%ignore libtorrent::file_storage::file_piece_range;
%ignore libtorrent::file_storage::file_range;
%ignore libtorrent::file_storage::file_storage(file_storage&&);
%ignore libtorrent::file_storage::internal_symlink;
%ignore libtorrent::file_storage::paths;
%ignore libtorrent::file_storage::piece_range;
%ignore libtorrent::file_storage::remove_tail_padding;
%ignore libtorrent::file_storage::sanitize_symlinks;

%ignore libtorrent::find_metric_idx;
%ignore libtorrent::fingerprint;
%ignore libtorrent::from_span;
%ignore libtorrent::from_span_t;
%ignore libtorrent::generate_fingerprint(std::string, int);
%ignore libtorrent::generate_fingerprint(std::string, int, int);
%ignore libtorrent::generate_fingerprint(std::string, int, int, int);
%ignore libtorrent::get_bdecode_category;
%ignore libtorrent::get_file_attributes;
%ignore libtorrent::get_symlink_path;
%ignore libtorrent::hash_value;
%ignore libtorrent::http_category;
%ignore libtorrent::incoming_connection_alert::endpoint;
%ignore libtorrent::internal_file_entry;
%ignore libtorrent::ip_filter::export_filter;
%ignore libtorrent::ipv6_peer::addr;
%ignore libtorrent::libtorrent_category;
%ignore libtorrent::load_pack_from_dict;
%ignore libtorrent::parse_int;
%ignore libtorrent::parse_magnet_uri;
%ignore libtorrent::partial_piece_info::blocks;
%ignore libtorrent::partial_piece_info::deprecated_piece_state;
%ignore libtorrent::partial_piece_info::deprecated_state_t;
%ignore libtorrent::performance_alert::bittyrant_with_no_uplimit;
%ignore libtorrent::performance_alert::deprecated_bittyrant_with_no_uplimit;
%ignore libtorrent::performance_alert::performance_warning_t::bittyrant_with_no_uplimit;
%ignore libtorrent::picker_log_alert::blocks;
%ignore libtorrent::print_entry;
%ignore libtorrent::proxy_settings::proxy_settings;
%ignore libtorrent::run_all_updates;
%ignore libtorrent::sanitize_append_path_element;
%ignore libtorrent::save_settings_to_dict;
%ignore libtorrent::session::session(session_params const&, io_service&);
%ignore libtorrent::session::session(session_params&&);
%ignore libtorrent::session::session(session_params&&, io_service&);
%ignore libtorrent::session::session(settings_pack const&, io_service&);
%ignore libtorrent::session::session(settings_pack const&, io_service&, session_flags_t const);
%ignore libtorrent::session::session(settings_pack&&);
%ignore libtorrent::session::session(settings_pack&&, io_service&);
%ignore libtorrent::session::session(settings_pack&&, io_service&, session_flags_t const);
%ignore libtorrent::session::session(settings_pack&&, session_flags_t const);
%ignore libtorrent::session_params::dht_storage_constructor;
%ignore libtorrent::session_params::extensions;
%ignore libtorrent::session_params::session_params(session_params&&);
%ignore libtorrent::session_params::session_params(settings_pack const&, std::vector<std::shared_ptr<plugin>>);
%ignore libtorrent::session_params::session_params(settings_pack&& sp);
%ignore libtorrent::session_params::session_params(settings_pack&&, std::vector<std::shared_ptr<plugin>>);
%ignore libtorrent::session_proxy::session_proxy(session_proxy&&);
%ignore libtorrent::set_piece_hashes(create_torrent&, std::string const&, std::function<void(piece_index_t)> const&, error_code&);
%ignore libtorrent::settings_pack::bittyrant_choker;
%ignore libtorrent::settings_pack::deprecated;
%ignore libtorrent::settings_pack::deprecated_active_loaded_limit;
%ignore libtorrent::settings_pack::deprecated_announce_double_nat;
%ignore libtorrent::settings_pack::deprecated_bittyrant_choker;
%ignore libtorrent::settings_pack::deprecated_broadcast_lsd;
%ignore libtorrent::settings_pack::deprecated_cache_buffer_chunk_size;
%ignore libtorrent::settings_pack::deprecated_contiguous_recv_buffer;
%ignore libtorrent::settings_pack::deprecated_default_cache_min_age;
%ignore libtorrent::settings_pack::deprecated_dont_flush_write_cache;
%ignore libtorrent::settings_pack::deprecated_file_checks_delay_per_block;
%ignore libtorrent::settings_pack::deprecated_force_proxy;
%ignore libtorrent::settings_pack::deprecated_guided_read_cache;
%ignore libtorrent::settings_pack::deprecated_half_open_limit;
%ignore libtorrent::settings_pack::deprecated_hashing_threads;
%ignore libtorrent::settings_pack::deprecated_ignore_limits_on_local_network;
%ignore libtorrent::settings_pack::deprecated_ignore_resume_timestamps;
%ignore libtorrent::settings_pack::deprecated_lazy_bitfield;
%ignore libtorrent::settings_pack::deprecated_local_download_rate_limit;
%ignore libtorrent::settings_pack::deprecated_local_upload_rate_limit;
%ignore libtorrent::settings_pack::deprecated_lock_disk_cache;
%ignore libtorrent::settings_pack::deprecated_lock_files;
%ignore libtorrent::settings_pack::deprecated_low_prio_disk;
%ignore libtorrent::settings_pack::deprecated_mmap_cache;
%ignore libtorrent::settings_pack::deprecated_network_threads;
%ignore libtorrent::settings_pack::deprecated_rate_limit_utp;
%ignore libtorrent::settings_pack::deprecated_ssl_listen;
%ignore libtorrent::settings_pack::deprecated_upnp_ignore_nonrouters;
%ignore libtorrent::settings_pack::deprecated_use_disk_cache_pool;
%ignore libtorrent::settings_pack::deprecated_use_disk_read_ahead;
%ignore libtorrent::settings_pack::deprecated_use_write_cache;
%ignore libtorrent::settings_pack::deprecated_utp_delayed_ack;
%ignore libtorrent::settings_pack::settings_pack(settings_pack&&);
%ignore libtorrent::storage_error;
%ignore libtorrent::storage_params::pool;
%ignore libtorrent::storage_params::priorities;
%ignore libtorrent::user_alert_id;
%ignore libtorrent::verify_encoding;

%ignore boost::throws;
%ignore boost::detail::throws;
%ignore boost::system::operator==(const error_code&, const error_condition&);
%ignore boost::system::operator==(const error_condition&, const error_code&);
%ignore boost::system::operator!=(const error_code&, const error_condition&);
%ignore boost::system::operator!=(const error_condition&, const error_code&);
%ignore boost::system::operator!=(const error_condition&, const error_condition&);
%ignore boost::system::hash_value;
%ignore boost::system::errc::make_error_condition;
%ignore boost::asio;

%ignore boost::system::error_category;
%ignore boost::system::error_code::assign;
%ignore boost::system::error_code::category;
%ignore boost::system::error_code::default_error_condition;
%ignore boost::system::error_code::error_code(int, const error_category&);
%ignore boost::system::error_code::operator std::error_code;
%ignore boost::system::error_code::unspecified_bool_true;
%ignore boost::system::error_condition;
%ignore boost::system::generic_category;
%ignore boost::system::system_category;
%ignore boost::system::system_error;

%ignore operator=;
%ignore operator!;
%ignore operator++;
%ignore operator--;
%ignore operator+=;
%ignore operator<<=;
%ignore operator>>=;
%ignore operator~;
%ignore operator^;
%ignore operator^=;
%ignore operator&;
%ignore operator&=;
%ignore operator|=;
%ignore operator int;
%ignore operator();
%ignore operator<<;
%ignore operator>>;
%ignore operator[];
%ignore operator unspecified_bool_type;