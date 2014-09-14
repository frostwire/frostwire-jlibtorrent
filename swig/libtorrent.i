%module (jniclassname="libtorrent_jni") libtorrent

%{
#include <stdexcept>
#include <string>
#include <ios>

#include <boost/asio/ip/address.hpp>
#include <boost/asio/ip/address_v4.hpp>
#include <boost/asio/ip/address_v6.hpp>
#include <boost/system/error_code.hpp>
    
#include "libtorrent/version.hpp"
#include "libtorrent/ptime.hpp"
#include "libtorrent/size_type.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/fingerprint.hpp"
#include "libtorrent/bitfield.hpp"
#include "libtorrent/stat.hpp"
#include "libtorrent/peer_request.hpp"
#include "libtorrent/address.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/entry.hpp"
#include "libtorrent/sha1_hash.hpp"
#include "libtorrent/piece_picker.hpp"
#include "libtorrent/storage_defs.hpp"
#include "libtorrent/storage.hpp"
#include "libtorrent/policy.hpp"
#include "libtorrent/file_storage.hpp"
#include "libtorrent/torrent_info.hpp"
#include "libtorrent/torrent_handle.hpp"
#include "libtorrent/add_torrent_params.hpp"
#include "libtorrent/rss.hpp"
#include "libtorrent/alert.hpp"
#include "libtorrent/alert_types.hpp"
#include "libtorrent/alert_manager.hpp"
#include "libtorrent/disk_io_thread.hpp"
#include "libtorrent/peer.hpp"
#include "libtorrent/peer_info.hpp"
#include "libtorrent/bandwidth_limit.hpp"
#include "libtorrent/bandwidth_socket.hpp"
#include "libtorrent/peer_connection.hpp"
#include "libtorrent/session_status.hpp"
#include "libtorrent/session_settings.hpp"
#include "libtorrent/torrent.hpp"
#include "libtorrent/session.hpp"
#include "libtorrent/extensions.hpp"
#include "libtorrent/disk_buffer_holder.hpp"
#include "libtorrent/disk_buffer_pool.hpp"
#include "libtorrent/bt_peer_connection.hpp"
#include "libtorrent/ip_voter.hpp"
#include "libtorrent/file_pool.hpp"
#include "libtorrent/ip_filter.hpp"
#include "libtorrent/lazy_entry.hpp"
#include "libtorrent/buffer.hpp"
#include "libtorrent/tracker_manager.hpp"
#include "libtorrent/union_endpoint.hpp"
#include "libtorrent/file.hpp"
#include "libtorrent/time.hpp"
#include "libtorrent/escape_string.hpp"
#include "libtorrent/bencode.hpp"
    
// aditional includes
    
#include <boost/system/error_code.hpp>
    
using namespace boost;
using namespace boost::system;
    
using namespace libtorrent;
    
// dummy implementation due to issues of SWIG with alert type pure virtual functions
namespace libtorrent {
    
    int type() { return 0; }
    int category() { return 0; }
    char* what() { return NULL; }
}
    
// exception helper functions
inline void new_java_exception(JNIEnv *env, const char *type = "", const char *message = "") {
    jclass newExcCls = env->FindClass(type);
    if (newExcCls != NULL) {
        env->ThrowNew(newExcCls, message);
    }
}
    
inline void new_java_error(JNIEnv *env, const char *message = "") {
    new_java_exception(env, "java/lang/Error", message);
}
%}

%exception {
    try {
        $action
    } catch (const std::out_of_range &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaIndexOutOfBoundsException, e.what());
        return $null;
    } catch (const std::bad_alloc &e) {
        //translate OOM C++ exception to a Java exception
        SWIG_JavaThrowException(jenv, SWIG_JavaIndexOutOfBoundsException, e.what());
        return $null;
    } catch (const std::ios_base::failure &e) {
        //translate IO C++ exception to a Java exception
        SWIG_JavaThrowException(jenv, SWIG_JavaIOException, e.what());
        return $null;
    } catch (const std::exception &e) {
        //translate unknown C++ exception to a Java exception
        new_java_error(jenv, e.what());
        return $null;
    } catch (...) {
        //translate unknown C++ exception to a Java exception
        new_java_error(jenv, "Unknown exception type");
        return $null;
    }
}

%include <stdint.i>
%include <typemaps.i>
%include <boost_intrusive_ptr.i>
%include <boost_shared_ptr.i>
%include <std_auto_ptr.i>
%include <std_string.i>
%include <std_pair.i>
%include <std_map.i>
%include <std_deque.i>
%include <enums.swg>

%include "std_vector2.i"

%intrusive_ptr(libtorrent::torrent_info)
%intrusive_ptr(libtorrent::tracker_connection)
%intrusive_ptr(libtorrent::peer_connection)
%intrusive_ptr(libtorrent::file)

%shared_ptr(libtorrent::torrent)
%shared_ptr(libtorrent::entry)
%shared_ptr(libtorrent::plugin)
%shared_ptr(libtorrent::peer_plugin)
%shared_ptr(libtorrent::torrent_plugin)
%shared_ptr(libtorrent::bandwidth_socket)
%shared_ptr(libtorrent::peer_connection)
%shared_ptr(libtorrent::bt_peer_connection)

%auto_ptr(libtorrent::alert)

%apply const std::string & {std::string &};

typedef long time_t;

namespace std {
    %template(void_ptr_int_pair) pair<void*, int>;
    %template(char_const_ptr_int_pair) pair<const char *, int>;
    %template(int_int_pair) pair<int, int>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(long_long_long_1_pair) pair<long, long long>;
    %template(long_long_long_2_pair) pair<long long, long>;
    %template(long_long_long_long_pair) pair<long long, long long>;
    %template(string_lazy_entry_const_ptr_pair) pair<std::string, const libtorrent::lazy_entry *>;
    
    %template(string_vector) vector<std::string>;
    %template(char_vector) vector<char>;
    %template(bool_vector) vector<bool>;
    %template(long_long_long_1_pair_vector) vector<std::pair<long, long long>>;
    %template(long_long_long_2_pair_vector) vector<std::pair<long long, long>>;
    %template(long_long_long_long_pair_vector) vector<std::pair<long long, long long>>;
    %template(string_int_pair_vector) vector<std::pair<std::string, int>>;
    %template(string_string_pair_vector) vector<std::pair<std::string, std::string>>;

    %template(void_ptr_vector) vector<void*>;
    %template(unsigned_char_vector) vector<unsigned char>;
    %template(float_vector) vector<float>;
    %template(int_vector) vector<int>;
    %template(int64_vector) vector<int64_t>;
    %template(sha1_hash_vector) vector<libtorrent::sha1_hash>;
    %template(torrent_status_vector) vector<libtorrent::torrent_status>;
    %template(torrent_handle_vector) vector<libtorrent::torrent_handle>;
    %template(feed_handle_vector) vector<libtorrent::feed_handle>;
    %template(feed_item_vector) vector<libtorrent::feed_item>;
    %template(file_slice_vector) vector<libtorrent::file_slice>;
    %template(peer_request_vector) vector<libtorrent::peer_request>;
    %template(dht_routing_bucket_vector) vector<libtorrent::dht_routing_bucket>;
    %template(dht_lookup_vector) vector<libtorrent::dht_lookup>;
    %template(policy_peer_ptr_vector) vector<libtorrent::policy::peer*>;
    
    %template(partial_piece_info_vector) vector<libtorrent::partial_piece_info>;
    %template(cached_piece_info_vector) vector<libtorrent::cached_piece_info>;
    %template(peer_info_vector) vector<libtorrent::peer_info>;
    
    %template(web_seed_entry_vector) vector<libtorrent::web_seed_entry>;
    %template(peer_entry_vector) vector<libtorrent::peer_entry>;
    %template(announce_entry_vector) vector<libtorrent::announce_entry>;
    %template(peer_list_entry_vector) vector<libtorrent::peer_list_entry>;

    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;
    %template(int_sha1_hash_map) map<int, libtorrent::sha1_hash>;

    %template(alert_ptr_deque) deque<libtorrent::alert*>;
};

%ignore match_filesizes;
%ignore clone;
%ignore new_feed;
%ignore parse_feed;
%ignore ssl_ctx;
%ignore readv;
%ignore writev;
%ignore libtorrent::parse_int;

%ignore libtorrent::partial_hash;
%ignore libtorrent::piece_manager;
%ignore libtorrent::disk_io_thread;
%ignore libtorrent::feed;
%ignore libtorrent::request_callback;
%ignore libtorrent::has_block;
%ignore libtorrent::pending_block;
%ignore libtorrent::timeout_handler;
%ignore libtorrent::connection_queue;

// this are ignore until we solve the specific type issues
%ignore libtorrent::to_string(size_type);
%ignore libtorrent::tracker_manager::tracker_manager;
%ignore libtorrent::tracker_manager::queue_request;
%ignore libtorrent::tracker_connection::requester;
%ignore url_has_argument; // global
%ignore export_filter; // ip_filter
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::storage;
%ignore on_peer_name_lookup; // torrent
%ignore on_name_lookup; // torrent
%ignore on_proxy_name_lookup; // torrent
%ignore libtorrent::torrent::async_verify_piece;
%ignore libtorrent::connection_queue::enqueue;
%ignore set_dispatch_function; // alert_manager
%ignore libtorrent::session::set_alert_dispatch;
%ignore libtorrent::session::get_torrent_status;
%ignore libtorrent::session::get_io_service;
%ignore libtorrent::session::get_connection_queue;
%ignore libtorrent::session::add_extension(boost::function<boost::shared_ptr<torrent_plugin>(torrent*, void*)>);
%ignore libtorrent::peer_connection::incoming_piece;
%ignore libtorrent::peer_connection::send_buffer;
%ignore libtorrent::peer_connection::associated_torrent;
%ignore libtorrent::peer_connection::add_request;
%ignore libtorrent::peer_connection::cancel_request;
%ignore libtorrent::peer_connection::make_time_critical;
%ignore libtorrent::peer_connection::download_queue;
%ignore libtorrent::peer_connection::request_queue;
%ignore send_buffer; // bt_peer_connection
%ignore libtorrent::disk_io_job::callback;
%ignore libtorrent::disk_io_job::storage;
%ignore libtorrent::disk_buffer_holder::disk_buffer_holder;
%ignore libtorrent::plugin::added;
%ignore getBlocks;
%ignore setBlocks;
%ignore add_job; // disk_io_thread
%ignore libtorrent::torrent::torrent;
%ignore libtorrent::torrent::filesystem;
%ignore libtorrent::torrent::session;
%ignore libtorrent::torrent::picker;
%ignore libtorrent::torrent::on_torrent_download;
%ignore libtorrent::torrent::tracker_response;
%ignore libtorrent::torrent::begin;
%ignore libtorrent::torrent::end;
%ignore libtorrent::torrent::block_bytes_wanted;
%ignore libtorrent::torrent::cancel_block;
%ignore libtorrent::torrent::to_req;
%ignore libtorrent::torrent::add_extension(boost::function<boost::shared_ptr<torrent_plugin>(torrent*, void*)> const&, void*);
%ignore boost::asio::ip::address_v4::to_bytes;
%ignore boost::asio::ip::address_v6::to_bytes;
%ignore libtorrent::policy::ipv6_peer::addr;
%ignore libtorrent::torrent_handle::add_extension;

%ignore operator=;
%ignore operator!;
%ignore operator<=;
%ignore operator>=;
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
%ignore operator unspecified_bool_type;
%ignore operator udp::endpoint;
%ignore operator tcp::endpoint;

%rename(op_eq) operator==;
%rename(op_neq) operator!=;
%rename(op_lt) operator<;
%rename(op_gt) operator>;
%rename(op_get_at) operator[];

%include "address.hpp"
%include "address_v4.hpp"
%include "address_v6.hpp"
%include "error_code.hpp"

%include "libtorrent/version.hpp"
%include "libtorrent/ptime.hpp"
%include "libtorrent/size_type.hpp"
%include "libtorrent/error_code.hpp"
%include "libtorrent/fingerprint.hpp"
%include "libtorrent/bitfield.hpp"
%include "libtorrent/stat.hpp"
%include "libtorrent/peer_request.hpp"
%include "libtorrent/address.hpp"
%include "libtorrent/error_code.hpp"
%include "libtorrent/entry.hpp"
%include "libtorrent/sha1_hash.hpp"
%include "libtorrent/storage_defs.hpp"
%include "libtorrent/storage.hpp"
%include "libtorrent/file_storage.hpp"
%include "policy.hpp"
%include "libtorrent/torrent_info.hpp"
%include "torrent_handle.hpp"
%include "libtorrent/add_torrent_params.hpp"
%include "rss.hpp"
%include "libtorrent/alert.hpp"
%include "alert_types.hpp"
%include "libtorrent/alert_manager.hpp"
%include "libtorrent/disk_io_thread.hpp"
%include "libtorrent/peer.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/bandwidth_limit.hpp"
%include "libtorrent/bandwidth_socket.hpp"
%include "peer_connection.hpp"
%include "libtorrent/session_status.hpp"
%include "libtorrent/session_settings.hpp"
%include "libtorrent/torrent.hpp"
%include "session.hpp"
%include "extensions.hpp"
%include "libtorrent/disk_buffer_holder.hpp"
%include "libtorrent/disk_buffer_pool.hpp"
%include "bt_peer_connection.hpp"
%include "ip_voter.hpp"
%include "libtorrent/file_pool.hpp"
%include "libtorrent/ip_filter.hpp"
%include "lazy_entry.hpp"
%include "libtorrent/buffer.hpp"
%include "libtorrent/tracker_manager.hpp"
%include "union_endpoint.hpp"
%include "file.hpp"
%include "libtorrent/time.hpp"
%include "libtorrent/escape_string.hpp"
%include "libtorrent/bencode.hpp"

namespace libtorrent {
    
// alert types conversion due to lack of polymorphic return type
%extend alert {
    static libtorrent::dht_announce_alert *cast_to_dht_announce_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_announce_alert *>(alert);
    }
    static libtorrent::dht_put_alert *cast_to_dht_put_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_put_alert *>(alert);
    }
    static libtorrent::external_ip_alert *cast_to_external_ip_alert(alert *alert) {
        return dynamic_cast<libtorrent::external_ip_alert *>(alert);
    }
    static libtorrent::listen_failed_alert *cast_to_listen_failed_alert(alert *alert) {
        return dynamic_cast<libtorrent::listen_failed_alert *>(alert);
    }
    static libtorrent::state_update_alert *cast_to_state_update_alert(alert *alert) {
        return dynamic_cast<libtorrent::state_update_alert *>(alert);
    }
    static libtorrent::portmap_alert *cast_to_portmap_alert(alert *alert) {
        return dynamic_cast<libtorrent::portmap_alert *>(alert);
    }
    static libtorrent::torrent_alert *cast_to_torrent_alert(alert *alert) {
        return dynamic_cast<libtorrent::torrent_alert *>(alert);
    }
    static libtorrent::rss_alert *cast_to_rss_alert(alert *alert) {
        return dynamic_cast<libtorrent::rss_alert *>(alert);
    }
    static libtorrent::dht_bootstrap_alert *cast_to_dht_bootstrap_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_bootstrap_alert *>(alert);
    }
    static libtorrent::dht_get_peers_alert *cast_to_dht_get_peers_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_get_peers_alert *>(alert);
    }
    static libtorrent::incoming_connection_alert *cast_to_incoming_connection_alert(alert *alert) {
        return dynamic_cast<libtorrent::incoming_connection_alert *>(alert);
    }
    static libtorrent::i2p_alert *cast_to_i2p_alert(alert *alert) {
        return dynamic_cast<libtorrent::i2p_alert *>(alert);
    }
    static libtorrent::dht_mutable_item_alert *cast_to_dht_mutable_item_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_mutable_item_alert *>(alert);
    }
    static libtorrent::dht_immutable_item_alert *cast_to_dht_immutable_item_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_immutable_item_alert *>(alert);
    }
    static libtorrent::udp_error_alert *cast_to_udp_error_alert(alert *alert) {
        return dynamic_cast<libtorrent::udp_error_alert *>(alert);
    }
    static libtorrent::portmap_error_alert *cast_to_portmap_error_alert(alert *alert) {
        return dynamic_cast<libtorrent::portmap_error_alert *>(alert);
    }
    static libtorrent::portmap_log_alert *cast_to_portmap_log_alert(alert *alert) {
        return dynamic_cast<libtorrent::portmap_log_alert *>(alert);
    }
    static libtorrent::rss_item_alert *cast_to_rss_item_alert(alert *alert) {
        return dynamic_cast<libtorrent::rss_item_alert *>(alert);
    }
    static libtorrent::listen_succeeded_alert *cast_to_listen_succeeded_alert(alert *alert) {
        return dynamic_cast<libtorrent::listen_succeeded_alert *>(alert);
    }
    static libtorrent::dht_error_alert *cast_to_dht_error_alert(alert *alert) {
        return dynamic_cast<libtorrent::dht_error_alert *>(alert);
    }
    static libtorrent::save_resume_data_alert *cast_to_save_resume_data_alert(alert *alert) {
        return dynamic_cast<libtorrent::save_resume_data_alert *>(alert);
    }
};

%extend entry {
    std::vector<char> bencode() {
        std::vector<char> buffer;
        libtorrent::bencode(std::back_inserter(buffer), *$self);
        return buffer;
    }
};
}
