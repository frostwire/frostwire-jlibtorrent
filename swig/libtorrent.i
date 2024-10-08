%module (jniclassname="libtorrent_jni", directors="1") libtorrent

// REMINDER:
// #include vs %include in SWIG:
// - #include -> It literally includes the contents of the file "foo.hpp" in the SWIG interface file
// - %include -> It tells SWIG to parse the .hpp file and generate wrappers for the declarations in it


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

%include "includes/libtorrent_java.i"
  
%{
// BEGIN common set include (just include but don't wrap)----------------------------------------------
#include <boost/container/map.hpp>
#include <libtorrent/flags.hpp>
#include <libtorrent/torrent_handle.hpp>
#include <libtorrent/address.hpp>
#include <libtorrent/socket.hpp>
#include <libtorrent/kademlia/dht_state.hpp>
#include <libtorrent/client_data.hpp>
#include <libtorrent/sha1_hash.hpp>
#include <libtorrent/info_hash.hpp>
#include <libtorrent/storage_defs.hpp>
#include <libtorrent/bitfield.hpp>
#include <libtorrent/operations.hpp>
#include <libtorrent/error_code.hpp>
#include <libtorrent/announce_entry.hpp>
#include <libtorrent/file_storage.hpp>
#include <libtorrent/peer_request.hpp>
#include <libtorrent/bdecode.hpp>
#include <libtorrent/torrent_info.hpp>
#include <libtorrent/torrent_flags.hpp>
#include <libtorrent/add_torrent_params.hpp>
#include <libtorrent/close_reason.hpp>
#include <libtorrent/peer_info.hpp>
#include <libtorrent/pex_flags.hpp>
#include <libtorrent/torrent_status.hpp>
#include <libtorrent/performance_counters.hpp>
#include <libtorrent/portmap.hpp>
#include <libtorrent/piece_block.hpp>
#include <libtorrent/socket_type.hpp>
#include <libtorrent/entry.hpp>
//#include <libtorrent/tracker_event.hpp> // this exists on the 'master' branch
// enum class event_t is defined in tracker_manager.hpp on the 2.0 branch
#include <libtorrent/tracker_manager.hpp>
#include <libtorrent/alert.hpp>
#include <libtorrent/alert_types.hpp>
#include <libtorrent/settings_pack.hpp>
#include <libtorrent/peer_class.hpp>
#include <libtorrent/peer_class_type_filter.hpp>
#include <libtorrent/ip_filter.hpp>
#include <libtorrent/session_types.hpp>
#include <libtorrent/session_params.hpp>
#include <libtorrent/session_handle.hpp>
#include <libtorrent/session.hpp>
#include <libtorrent/create_torrent.hpp>
#include <libtorrent/session_stats.hpp>
#include <libtorrent/version.hpp>
#include <libtorrent/magnet_uri.hpp>
#include <libtorrent/fingerprint.hpp>
#include <libtorrent/read_resume_data.hpp>
#include <libtorrent/write_resume_data.hpp>
#include <libtorrent/posix_disk_io.hpp>

#include <libtorrent/hex.hpp>
#include <libtorrent/bencode.hpp>

using piece_index_t = libtorrent::piece_index_t;
using file_index_t = libtorrent::file_index_t;
using queue_position_t = libtorrent::queue_position_t;

#include "libtorrent.hpp"

%}
%include "includes/libtorrent_typed_bitfield.i"
%{
template <typename IndexType>
using typed_bitfield = libtorrent::typed_bitfield<IndexType>;

using add_torrent_params = libtorrent::add_torrent_params;
using address = libtorrent::address;
using bdecode_node = libtorrent::bdecode_node;
using client_data_t = libtorrent::client_data_t;
using close_reason_t = libtorrent::close_reason_t;
using disconnect_severity_t = libtorrent::disconnect_severity_t;
using download_priority_t = libtorrent::download_priority_t;
using event_t = libtorrent::event_t;
using file_slice = libtorrent::file_slice;
using file_storage = libtorrent::file_storage;
using info_hash_t = libtorrent::info_hash_t;
using operation_t = libtorrent::operation_t;
using peer_connection = libtorrent::peer_connection;
using peer_info = libtorrent::peer_info;
using peer_request = libtorrent::peer_request;
using peer_source_flags_t = libtorrent::peer_source_flags_t;
using pex_flags_t = libtorrent::pex_flags_t;
using portmap_transport = libtorrent::portmap_transport;
using portmap_protocol = libtorrent::portmap_protocol;
using protocol_version = libtorrent::protocol_version;
using reopen_network_flags_t = libtorrent::reopen_network_flags_t;
using save_state_flags_t = libtorrent::save_state_flags_t;
using session_flags_t = libtorrent::session_flags_t;
using session_params = libtorrent::session_params;
using settings_pack = libtorrent::settings_pack;
using sha1_hash_vector = std::vector<libtorrent::digest32<160>>;
using sha256_hash_vector = std::vector<libtorrent::digest32<256>>;
using socket_type_t = libtorrent::socket_type_t;
using storage_mode_t = libtorrent::storage_mode_t;
using tcp = libtorrent::tcp;
using torrent_flags_t = libtorrent::torrent_flags_t;
using torrent_info = libtorrent::torrent_info;
using udp = libtorrent::udp;

// END common set include ------------------------------------------------------
%}

%include "includes/ignores.i"

%include <stdint.i>
%include <typemaps.i>
%include <std_common.i> // maybe remove this one
%include <std_string.i>
%include <std_pair.i>
%include <std_vector.i>
%include <std_map.i>
%include <std_array.i>
%include "includes/std_bitset.i"
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
TYPE_INTEGRAL_CONVERSION(queue_position_t, int, int)
TYPE_INTEGRAL_CONVERSION(disconnect_severity_t, std::uint8_t, int)
TYPE_INTEGRAL_CONVERSION_EX(peer_class_t, std::uint32_t, std::int32_t, int)

// swig template definitions
%include "includes/swig_templates.i"

// more structs and templates
%include "includes/libtorrent_flags.i"
%include "includes/libtorrent_structs.i"

%rename(op_eq) operator==;
%rename(op_ne) operator!=;
%rename(op_lt) operator<;
%rename(op_gt) operator>;
%rename(op_lte) operator<=;
%rename(op_gte) operator>=;
%rename(op_bool) operator bool;
%rename(op_inv) operator~;
%rename(op_xor) operator^;
%rename(op_or) operator|;
%rename(op_and) operator&;
%rename(op_at) operator[];

%rename(libtorrent_no_error) libtorrent::errors::no_error;
%rename(libtorrent_errors) libtorrent::errors::error_code_enum;

%rename("$ignore", regextarget=1, %$isconstructor) ".*_alert$";

%ignore dht_put_item_cb;

// directors
%feature("director") add_files_listener;
%feature("director") set_piece_hashes_listener;
%feature("director") alert_notify_callback;
%feature("director") swig_plugin;
%feature("director") posix_wrapper;

%ignore swig_plugin::implemented_features;
%ignore set_piece_hashes_listener::progress_index;

%include "includes/boost_system_error_code.i"

// Includes of what's actually going to be wrapped.
// Order of inclusion matters.
%include "includes/libtorrent_span.i"

// includes/libtorrent_flags.i already included via %include "includes/libtorrent_structs.i"
%include "includes/libtorrent_address.i"
// socket.i ->
%include "includes/libtorrent_tcp_endpoint.i"
%include "includes/libtorrent_udp_endpoint.i"

%include "includes/libtorrent_kademlia_dht_state.i"
%include "includes/libtorrent_client_data.i"
%include "includes/libtorrent_sha1_hash.i"
%include "includes/libtorrent_info_hash.i"
%include "includes/libtorrent_storage_defs.i"
%include "includes/libtorrent_bitfield.i"
%include "includes/libtorrent_operations.i"
%include "includes/libtorrent_error_code.i"
%include "includes/libtorrent_announce.i"
%include "includes/libtorrent_file_storage.i"
%include "includes/libtorrent_peer_request.i"
%include "includes/libtorrent_bdecode.i"
%include "includes/libtorrent_torrent_info.i"
%include "includes/libtorrent_torrent_flags.i"
%include "includes/libtorrent_add_torrent_params.i"
%include "includes/libtorrent_close_reason.i"
%include "includes/libtorrent_peer_info.i"
%include "includes/libtorrent_torrent_handle.i"
%include "includes/libtorrent_torrent_status.i"
%include "includes/libtorrent_performance_counters.i"
%include "includes/libtorrent_portmap.i"
%include "includes/libtorrent_piece_block.i"
%include "includes/libtorrent_socket_type.i"
%include "includes/libtorrent_entry.i"
//%include "includes/libtorrent_tracker_event.i" // TODO: will need to create when this is merged from master

// ALERTS TYPES
%include "includes/libtorrent_event_t.i"
%include "includes/libtorrent_alert.i"
%include "includes/libtorrent_dht_announce_alert.i"
%include "includes/libtorrent_external_ip_alert.i"
%include "includes/libtorrent_listen_failed_alert.i"
%include "includes/libtorrent_listen_succeeded_alert.i"
%include "includes/libtorrent_incoming_connection_alert.i"
%include "includes/libtorrent_peer_alert.i"
%include "includes/libtorrent_dht_direct_response_alert.i"
%include "includes/libtorrent_dht_outgoing_get_peers_alert.i"
%include "includes/libtorrent_dht_pkt_alert.i"
%include "includes/libtorrent_udp_error_alert.i"
%include "includes/libtorrent_dht_sample_infohashes_alert.i"
%include "includes/libtorrent_tracker_alert.i"
%include "includes/libtorrent_dht_lookup.i"
%include "includes/libtorrent_portmap_alert.i"
%include "includes/libtorrent_portmap_error_alert.i"
%include "includes/libtorrent_dht_mutable_item_alert.i"
%include "includes/libtorrent_dht_put_alert.i"
%include "includes/libtorrent_session_stats_alert.i"
%include "includes/libtorrent_read_piece_alert.i"
%include "includes/libtorrent_peer_log_alert.i"
%include "includes/libtorrent_dht_stats_alert.i"
%include "includes/libtorrent_torrent_conflict_alert.i"
// END OF ALERTS TYPES

%include "includes/libtorrent_settings_pack.i"
%include "includes/libtorrent_peer_class.i"
%include "includes/libtorrent_peer_class_type_filter.i"
%include "includes/libtorrent_ip_filter.i"
%include "includes/libtorrent_session_types.i"
%include "includes/libtorrent_session_params.i"
%include "includes/libtorrent_session_handle.i"
%include "includes/libtorrent_session.i"
%include "includes/libtorrent_file_storage.i"
%include "includes/libtorrent_create_torrent.i"
%include "includes/libtorrent_session_stats.i"
%include "includes/libtorrent_version.i"
%include "includes/libtorrent_magnet_uri.i"
%include "includes/libtorrent_fingerprint.i"
%include "includes/libtorrent_read_resume_data.i"
%include "includes/libtorrent_write_resume_data.i"

//%include "includes/libtorrent_web_seed_entry.i"
// when web_seed_entry.hpp is merged from master, currently web_seed_entry lives in torrent_info.hpp

%include "includes/libtorrent_bloom_filter.i"
%include "includes/libtorrent_connection_type.i"
%include "includes/libtorrent_peer_connection_handle.i"
%include "includes/libtorrent_stats_metric.i"
%include "includes/libtorrent_string_view.i"

// wrap this stuff last.
%include "libtorrent.hpp"
