%module (jniclassname="libtorrent_jni", directors="1") libtorrent

// suppress Warning 317: Specialization of non-template '<name>'.
//#pragma SWIG nowarn=317
// suppress Warning 341: The 'using' keyword in type aliasing is not fully supported yet.
//#pragma SWIG nowarn=341
// suppress Warning 401: Nothing known about base class '<name>'. Ignored.
//#pragma SWIG nowarn=401
// supress Warning 402: Base class '<name>' is incomplete.
//#pragma SWIG nowarn=402
// Overloaded method <name> ignored, using <name> instead.
#pragma SWIG nowarn=516
// Specialization of non-template '<name>'.
#pragma SWIG nowarn=317
	
%{
// BEGIN common set include ------------------------------------------------------
#include "libtorrent/version.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/peer_request.hpp"
#include "libtorrent/file_storage.hpp"
#include "libtorrent/bdecode.hpp"
#include "libtorrent/bencode.hpp"
#include "libtorrent/peer_info.hpp"
#include "libtorrent/torrent_flags.hpp"
#include "libtorrent/pex_flags.hpp"
#include "libtorrent/torrent_status.hpp"
#include "libtorrent/torrent_handle.hpp"
#include "libtorrent/operations.hpp"
#include "libtorrent/session_stats.hpp"
#include "libtorrent/close_reason.hpp"
#include "libtorrent/alert.hpp"
#include "libtorrent/alert_types.hpp"
#include "libtorrent/session_settings.hpp"
#include "libtorrent/settings_pack.hpp"
#include "libtorrent/peer_class.hpp"
#include "libtorrent/peer_class_type_filter.hpp"
#include "libtorrent/session_types.hpp"
#include "libtorrent/ip_filter.hpp"
#include "libtorrent/kademlia/dht_state.hpp"
#include "libtorrent/kademlia/dht_settings.hpp"
#include "libtorrent/session.hpp"
#include "libtorrent/peer_connection_handle.hpp"
#include "libtorrent/magnet_uri.hpp"
#include "libtorrent/create_torrent.hpp"
#include "libtorrent/fingerprint.hpp"

#include "libtorrent.h"

using piece_index_t = libtorrent::piece_index_t;
using file_index_t = libtorrent::file_index_t;
using peer_class_t = libtorrent::peer_class_t;
using port_mapping_t = libtorrent::port_mapping_t;
using queue_position_t = libtorrent::queue_position_t;
using download_priority_t = libtorrent::download_priority_t;
using disconnect_severity_t = libtorrent::disconnect_severity_t;

// END common set include ------------------------------------------------------
%}

%include "includes/libtorrent_java.i"

%include <stdint.i>
%include <typemaps.i>
%include <std_common.i> // maybe remove this one
%include <std_string.i>
%include <std_pair.i>
%include <std_vector.i>
%include <std_map.i>
%include <std_array.i>

%include "boost/boost_map.i"

%apply std::int8_t { char };
%apply std::int64_t { void* };
%apply std::int64_t { std::ptrdiff_t };
%apply std::int64_t { std::time_t };

%define TYPE_INTEGRAL_CONVERSION_EX(name, underlying_type, api_type, java_type)

// don't add deprecated finalize() methods
%typemap(javafinalize) SWIGTYPE ""

%typemap(jni) name, const name& "java_type"
%typemap(jtype) name, const name& "java_type"
%typemap(jstype) name, const name& "java_type"

%typemap(in) name {
    $1 = name(static_cast<underlying_type>($input));
}
%typemap(out) name {
    $result = static_cast<api_type>(static_cast<underlying_type>($1));
}
%typemap(javain) name, const name& "$javainput"
%typemap(javaout) name, const name& {
    return $jnicall;
  }
%enddef

%define TYPE_INTEGRAL_CONVERSION(name, underlying_type, java_type)
TYPE_INTEGRAL_CONVERSION_EX(name, underlying_type, underlying_type, java_type)
%enddef

TYPE_INTEGRAL_CONVERSION(piece_index_t, std::int32_t, int)
TYPE_INTEGRAL_CONVERSION(file_index_t, std::int32_t, int)
TYPE_INTEGRAL_CONVERSION_EX(peer_class_t, std::uint32_t, std::int32_t, int)
TYPE_INTEGRAL_CONVERSION(port_mapping_t, int, int)
TYPE_INTEGRAL_CONVERSION(queue_position_t, int, int)
TYPE_INTEGRAL_CONVERSION(disconnect_severity_t, std::uint8_t, int)

%include "includes/std_vector.i"
%include "includes/std_map.i"
%include "includes/std_bitset.i"
%include "includes/std_swig_templates.i"
%include "includes/libtorrent_flags_bitfield_flag.i"
%include "includes/libtorrent_structs.i"
%include "includes/libtorrent_sha1_hash_type_aliases.i"
%include "includes/libtorrent_storage_mode_t.i"
%include "includes/libtorrent_move_flags_t.i"
%include "includes/libtorrent_connection_type.i"
%include "includes/libtorrent_portmap_transport.i"
%include "includes/libtorrent_portmap_protocol.i"
%include "includes/libtorrent_span.i"
%include "includes/libtorrent_sha1_hash.i"
%include "includes/libtorrent_bloom_filter.i"
%include "includes/libtorrent_string_view.i"
%include "includes/libtorrent_address.i"
%include "includes/libtorrent_tcp_endpoint.i"
%include "includes/libtorrent_udp_endpoint.i"
%include "includes/libtorrent_entry.i"
%include "includes/libtorrent_typed_bitfield.i"
%include "includes/libtorrent_peer_class_info.i"
%include "includes/libtorrent_peer_info.i"
%include "includes/libtorrent_announce.i"
%include "includes/libtorrent_torrent_handle.i"

%ignore libtorrent::TORRENT_CFG;
%ignore libtorrent::detail;
%ignore libtorrent::aux;
%ignore libtorrent::parse_int;
%ignore libtorrent::get_bdecode_category;
%ignore libtorrent::set_piece_hashes(create_torrent&, std::string const&, std::function<void(piece_index_t)> const&, error_code&);
%ignore libtorrent::hash_value;
%ignore libtorrent::internal_file_entry;
%ignore libtorrent::print_entry;
%ignore libtorrent::fingerprint;
%ignore libtorrent::generate_fingerprint(std::string, int, int, int);
%ignore libtorrent::generate_fingerprint(std::string, int, int);
%ignore libtorrent::generate_fingerprint(std::string, int);
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>, create_flags_t);
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>);
%ignore libtorrent::parse_magnet_uri;
%ignore libtorrent::ip_filter::export_filter;
%ignore libtorrent::performance_alert::bittyrant_with_no_uplimit;
%ignore libtorrent::performance_alert::deprecated_bittyrant_with_no_uplimit;
%ignore libtorrent::performance_alert::performance_warning_t::bittyrant_with_no_uplimit;
%ignore libtorrent::session_params::session_params(settings_pack&&, std::vector<std::shared_ptr<plugin>>);
%ignore libtorrent::session_params::session_params(settings_pack const&, std::vector<std::shared_ptr<plugin>>);
%ignore libtorrent::session_params::session_params(session_params&&);
%ignore libtorrent::session_params::session_params(settings_pack&& sp);
%ignore libtorrent::session_params::extensions;
%ignore libtorrent::session_params::dht_storage_constructor;
%ignore libtorrent::session::session(session_params&&, io_service&);
%ignore libtorrent::session::session(session_params const&, io_service&);
%ignore libtorrent::session::session(settings_pack&&, io_service&, session_flags_t const);
%ignore libtorrent::session::session(settings_pack const&, io_service&, session_flags_t const);
%ignore libtorrent::session::session(settings_pack&&, io_service&);
%ignore libtorrent::session::session(settings_pack const&, io_service&);
%ignore libtorrent::session::session(session_params&&);
%ignore libtorrent::session::session(settings_pack&&, session_flags_t const);
%ignore libtorrent::session::session(settings_pack&&);
%ignore libtorrent::session_proxy::session_proxy(session_proxy&&);
%ignore libtorrent::picker_log_alert::blocks;
%ignore libtorrent::bt_peer_connection_handle::switch_send_crypto;
%ignore libtorrent::bt_peer_connection_handle::switch_recv_crypto;
%ignore libtorrent::bt_peer_connection_handle::native_handle;
%ignore libtorrent::block_info::set_peer;
%ignore libtorrent::partial_piece_info::blocks;
%ignore libtorrent::partial_piece_info::deprecated_state_t;
%ignore libtorrent::partial_piece_info::deprecated_piece_state;
%ignore libtorrent::dht_direct_response_alert::dht_direct_response_alert;
%ignore libtorrent::dht_direct_response_alert::userdata;
%ignore libtorrent::from_span;
%ignore libtorrent::from_span_t;
%ignore libtorrent::sanitize_append_path_element;
%ignore libtorrent::verify_encoding;
%ignore libtorrent::errors::make_error_code;
%ignore libtorrent::apply_pack;
%ignore libtorrent::apply_pack_impl;
%ignore libtorrent::load_pack_from_dict;
%ignore libtorrent::save_settings_to_dict;
%ignore libtorrent::run_all_updates;
%ignore libtorrent::error_code;
%ignore libtorrent::settings_pack::settings_pack(settings_pack&&);
%ignore libtorrent::settings_pack::deprecated;
%ignore libtorrent::settings_pack::deprecated_ignore_limits_on_local_network;
%ignore libtorrent::settings_pack::deprecated_rate_limit_utp;
%ignore libtorrent::settings_pack::deprecated_local_upload_rate_limit;
%ignore libtorrent::settings_pack::deprecated_local_download_rate_limit;
%ignore libtorrent::settings_pack::deprecated_half_open_limit;
%ignore libtorrent::settings_pack::deprecated_utp_delayed_ack;
%ignore libtorrent::settings_pack::deprecated_ignore_resume_timestamps;
%ignore libtorrent::settings_pack::deprecated_network_threads;
%ignore libtorrent::settings_pack::deprecated_lock_disk_cache;
%ignore libtorrent::settings_pack::deprecated_use_write_cache;
%ignore libtorrent::settings_pack::deprecated_lazy_bitfield;
%ignore libtorrent::settings_pack::deprecated_guided_read_cache;
%ignore libtorrent::settings_pack::deprecated_hashing_threads;
%ignore libtorrent::settings_pack::deprecated_contiguous_recv_buffer;
%ignore libtorrent::settings_pack::deprecated_default_cache_min_age;
%ignore libtorrent::settings_pack::deprecated_low_prio_disk;
%ignore libtorrent::settings_pack::deprecated_announce_double_nat;
%ignore libtorrent::settings_pack::deprecated_use_disk_read_ahead;
%ignore libtorrent::settings_pack::deprecated_active_loaded_limit;
%ignore libtorrent::settings_pack::deprecated_mmap_cache;
%ignore libtorrent::settings_pack::deprecated_dont_flush_write_cache;
%ignore libtorrent::settings_pack::deprecated_file_checks_delay_per_block;
%ignore libtorrent::settings_pack::deprecated_use_disk_cache_pool;
%ignore libtorrent::settings_pack::deprecated_cache_buffer_chunk_size;
%ignore libtorrent::settings_pack::deprecated_lock_files;
%ignore libtorrent::settings_pack::deprecated_ssl_listen;
%ignore libtorrent::settings_pack::deprecated_force_proxy;
%ignore libtorrent::settings_pack::deprecated_broadcast_lsd;
%ignore libtorrent::settings_pack::deprecated_upnp_ignore_nonrouters;
%ignore libtorrent::settings_pack::deprecated_bittyrant_choker;
%ignore libtorrent::settings_pack::bittyrant_choker;
%ignore libtorrent::storage_params::pool;
%ignore libtorrent::storage_params::priorities;
%ignore libtorrent::ipv6_peer::addr;
%ignore libtorrent::proxy_settings::proxy_settings;
%ignore libtorrent::file_storage::file_storage(file_storage&&);
%ignore libtorrent::file_storage::file_path_hash;
%ignore libtorrent::file_storage::all_path_hashes;
%ignore libtorrent::file_storage::file_name_ptr;
%ignore libtorrent::file_storage::file_name_len;
%ignore libtorrent::file_storage::apply_pointer_offset;
%ignore libtorrent::file_storage::add_file(std::string const&, std::int64_t, std::uint32_t, std::time_t, string_view);
%ignore libtorrent::file_storage::file_range;
%ignore libtorrent::file_storage::piece_range;
%ignore libtorrent::file_storage::sanitize_symlinks;
%ignore libtorrent::create_torrent::add_url_seed(string_view);
%ignore libtorrent::create_torrent::add_http_seed(string_view);
%ignore libtorrent::create_torrent::add_tracker(string_view);
%ignore libtorrent::create_torrent::add_tracker(string_view, int);
%ignore libtorrent::create_torrent::add_collection(string_view);
%ignore libtorrent::create_torrent::set_root_cert;
%ignore libtorrent::get_file_attributes;
%ignore libtorrent::get_symlink_path;
%ignore libtorrent::dht_lookup::type;
%ignore libtorrent::error_to_close_reason;
%ignore libtorrent::storage_error;
%ignore libtorrent::user_alert_id;
%ignore libtorrent::bdecode_category;
%ignore libtorrent::http_category;
%ignore libtorrent::libtorrent_category;
%ignore libtorrent::dht_announce_alert::ip;
%ignore libtorrent::external_ip_alert::external_address;
%ignore libtorrent::listen_failed_alert::address;
%ignore libtorrent::listen_succeeded_alert::address;
%ignore libtorrent::incoming_connection_alert::endpoint;
%ignore libtorrent::peer_alert::endpoint;
%ignore libtorrent::dht_direct_response_alert::endpoint;
%ignore libtorrent::dht_outgoing_get_peers_alert::endpoint;
%ignore libtorrent::dht_pkt_alert::node;
%ignore libtorrent::udp_error_alert::endpoint;
%ignore libtorrent::dht_sample_infohashes_alert::endpoint;
%ignore libtorrent::dht_sample_infohashes_alert::interval;
%ignore libtorrent::tracker_alert::local_endpoint;
%ignore libtorrent::dht::extract_node_ids;
%ignore libtorrent::dht::read_dht_state;
%ignore libtorrent::dht::save_dht_state;
%ignore libtorrent::dht::read_dht_settings;
%ignore libtorrent::dht::save_dht_settings;
%ignore libtorrent::find_metric_idx;

%ignore libtorrent::errors::deprecated_120;
%ignore libtorrent::errors::deprecated_121;
%ignore libtorrent::errors::deprecated_122;
%ignore libtorrent::errors::deprecated_123;
%ignore libtorrent::errors::deprecated_124;

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

%rename(op_eq) operator==;
%rename(op_ne) operator!=;
%rename(op_lt) operator<;
%rename(op_gt) operator>;
%rename(op_lte) operator<=;
%rename(op_gte) operator>=;
%rename(op_bool) operator bool;

%rename(libtorrent_no_error) libtorrent::errors::no_error;
%rename(libtorrent_errors) libtorrent::errors::error_code_enum;

%rename("$ignore", regextarget=1, %$isconstructor) ".*_alert$";

%ignore dht_put_item_cb;

%feature("director") add_files_listener;
%feature("director") set_piece_hashes_listener;

%feature("director") alert_notify_callback;
%feature("director") swig_plugin;
%feature("director") posix_wrapper;

%ignore swig_plugin::implemented_features;
%ignore set_piece_hashes_listener::progress_index;

%include "includes/boost_system_error_code.i"

%ignore WRAP_POSIX;
%ignore get_libc;
%ignore posix_open;
%ignore posix_stat;
%ignore posix_mkdir;
%ignore posix_rename;
%ignore posix_remove;
%ignore ::open;
%ignore ::stat;
%ignore ::mkdir;
%ignore ::rename;
%ignore ::remove;
%ignore g_posix_wrapper;

// BEGIN common set include ------------------------------------------------------

%include <boost/system/error_code.hpp>
%include <boost/system/system_error.hpp>

%include "libtorrent/version.hpp"
%include "libtorrent/error_code.hpp"
%include "libtorrent/peer_request.hpp"
%include "libtorrent/file_storage.hpp"
%include "libtorrent/bencode.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/torrent_flags.hpp"
%include "libtorrent/pex_flags.hpp"
%include "libtorrent/add_torrent_params.hpp"
%include "libtorrent/operations.hpp"
%include "libtorrent/session_stats.hpp"
%include "libtorrent/close_reason.hpp"
%include "libtorrent/alert.hpp"
%include "libtorrent/alert_types.hpp"
%include "libtorrent/session_settings.hpp"
%include "libtorrent/settings_pack.hpp"
%include "libtorrent/peer_class_type_filter.hpp"
%include "libtorrent/session_types.hpp"
%include "libtorrent/ip_filter.hpp"
%include "libtorrent/kademlia/dht_state.hpp"
%include "libtorrent/kademlia/dht_settings.hpp"
%include "libtorrent/session.hpp"

%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%include "libtorrent/fingerprint.hpp"

%include "libtorrent.h"

// END common set include ------------------------------------------------------

%include "includes/libtorrent_alert_casts.i"
%include "includes/libtorrent_session_handle.i"
%include "includes/libtorrent_bdecode.i"
%include "includes/libtorrent_add_torrent_params.i"
%include "includes/libtorrent_torrent_info.i"
%include "includes/libtorrent_torrent_handle.i"
%include "includes/libtorrent_dht_mutable_item_alert.i"
%include "includes/libtorrent_dht_put_alert.i"
%include "includes/libtorrent_stats_alert.i"
%include "includes/libtorrent_session_stats_alert.i"
%include "includes/libtorrent_read_piece_alert.i"
%include "includes/libtorrent_peer_connection_handle.i"
%include "includes/libtorrent_torrent_status.i"
%include "includes/libtorrent_stats_metric.i"
%include "includes/libtorrent_peer_log_alert.i"

%extend dht_lookup {

    std::string get_type() {
        return std::string($self->type);
    }
}

%extend dht_direct_response_alert {

    int64_t get_userdata() {
        return (int64_t)$self->userdata;
    }
}

%extend create_torrent {

    void add_url_seed(std::string const& url) {
        $self->add_url_seed(url);
    }

    void add_http_seed(std::string const& url) {
        $self->add_http_seed(url);
    }

    void add_tracker(std::string const& url, int tier) {
        $self->add_tracker(url, tier);
    }

    void add_collection(std::string const& c) {
        $self->add_collection(c);
    }

    void set_root_cert2(std::vector<int8_t> const& pem)
    {
        std::string s{pem.begin(), pem.end()};
        $self->set_root_cert(s);
    }
}

%extend file_storage {

    void add_file(std::string const& path, std::int64_t file_size,
        libtorrent::file_flags_t file_flags, std::time_t mtime, std::string const& symlink_path) {
        $self->add_file(path, file_size, file_flags, mtime, symlink_path);
    }
}

%extend dht_announce_alert {

    address get_ip() {
        return $self->ip;
    }
}

%extend external_ip_alert {

    address get_external_address() {
        return $self->external_address;
    }
}

%extend listen_failed_alert {

    address get_address() {
        return $self->address;
    }
}

%extend listen_succeeded_alert {

    address get_address() {
        return $self->address;
    }
}

%extend incoming_connection_alert {

    tcp::endpoint get_endpoint() {
        return $self->endpoint;
    }
}

%extend peer_alert {

    tcp::endpoint get_endpoint() {
        return $self->endpoint;
    }
}

%extend dht_direct_response_alert {

    udp::endpoint get_endpoint() {
        return $self->endpoint;
    }
}

%extend dht_outgoing_get_peers_alert {

    udp::endpoint get_endpoint() {
        return $self->endpoint;
    }
}

%extend dht_pkt_alert {

    udp::endpoint get_node() {
        return $self->node;
    }
}

%extend udp_error_alert {

    udp::endpoint get_endpoint() {
        return $self->endpoint;
    }
}

%extend dht_sample_infohashes_alert {

    udp::endpoint get_endpoint() {
        return $self->endpoint;
    }

    std::int64_t get_interval() {
        return libtorrent::total_milliseconds($self->interval);
    }
}

%extend tracker_alert {

    tcp::endpoint get_local_endpoint() {
        return $self->local_endpoint;
    }
}
