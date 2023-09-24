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

%include "includes/ignores.i"

%{
using piece_index_t = libtorrent::piece_index_t;
using file_index_t = libtorrent::file_index_t;
using peer_class_t = libtorrent::peer_class_t;
using port_mapping_t = libtorrent::port_mapping_t;
using queue_position_t = libtorrent::queue_position_t;
using download_priority_t = libtorrent::download_priority_t;
using disconnect_severity_t = libtorrent::disconnect_severity_t;
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
%include "libtorrent/session_params.hpp"
%include "libtorrent/session_handle.hpp"
%include "libtorrent/session.hpp"
//%include "libtorrent/ip_filter.hpp"
%include "libtorrent/kademlia/dht_state.hpp"
%include "libtorrent/kademlia/dht_settings.hpp"
%include "libtorrent/session.hpp"

%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%include "libtorrent/fingerprint.hpp"

// END common set include ------------------------------------------------------

//%include "includes/std_vector.i" // maybe remove this one
//%include "includes/std_map.i"
%include "includes/libtorrent_span.i"
%include "includes/libtorrent_flags_bitfield_flag.i"
%include "includes/libtorrent_move_flags_t.i"
%include "includes/libtorrent_structs.i"
%include "includes/libtorrent_address.i"
%include "includes/std_bitset.i"
%include "includes/std_swig_templates.i"
%include "includes/libtorrent_sha1_hash_type_aliases.i"
%include "includes/libtorrent_storage_mode_t.i"
%include "includes/libtorrent_connection_type.i"
%include "includes/libtorrent_portmap_transport.i"
%include "includes/libtorrent_portmap_protocol.i"
%include "includes/libtorrent_sha1_hash.i"
%include "includes/libtorrent_bloom_filter.i"
%include "includes/libtorrent_string_view.i"
%include "includes/libtorrent_tcp_endpoint.i"
%include "includes/libtorrent_udp_endpoint.i"
%include "includes/libtorrent_entry.i"
%include "includes/libtorrent_typed_bitfield.i"
%include "includes/libtorrent_peer_class_info.i"
%include "includes/libtorrent_ip_filter.i"
%include "includes/libtorrent_peer_info.i"
%include "includes/libtorrent_announce.i"
%include "includes/libtorrent_torrent_handle.i"
%include "includes/libtorrent_alert_casts.i"
%include "includes/libtorrent_session_handle.i"
%include "includes/libtorrent_bdecode.i"
%include "includes/libtorrent_add_torrent_params.i"
%include "includes/libtorrent_torrent_info.i"
%include "includes/libtorrent_torrent_handle.i"
%include "includes/libtorrent_dht_mutable_item_alert.i"
%include "includes/libtorrent_dht_put_alert.i"
%include "includes/libtorrent_session_stats_alert.i"
%include "includes/libtorrent_read_piece_alert.i"
%include "includes/libtorrent_peer_connection_handle.i"
%include "includes/libtorrent_torrent_status.i"
%include "includes/libtorrent_stats_metric.i"
%include "includes/libtorrent_peer_log_alert.i"
%include "includes/libtorrent_dht_lookup.i"
%include "includes/libtorrent_dht_direct_response_alert.i"
%include "includes/libtorrent_create_torrent.i"
%include "includes/libtorrent_file_storage.i"
%include "includes/libtorrent_dht_announce_alert.i"
%include "includes/libtorrent_external_ip_alert.i"
%include "includes/libtorrent_listen_failed_alert.i"
%include "includes/libtorrent_listen_succeeded_alert.i"
%include "includes/libtorrent_incoming_connection_alert.i"
%include "includes/libtorrent_peer_alert.i"
%include "includes/libtorrent_dht_outgoing_get_peers_alert.i"
%include "includes/libtorrent_dht_pkt_alert.i"
%include "includes/libtorrent_udp_error_alert.i"
%include "includes/libtorrent_dht_sample_infohashes_alert.i"
%include "includes/libtorrent_tracker_alert.i"
%include "libtorrent.hpp"