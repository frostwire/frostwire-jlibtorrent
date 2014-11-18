%module (jniclassname="libtorrent_jni") libtorrent

%pragma(java) jniclasscode=%{
    static {
        System.loadLibrary("jlibtorrent");
    }
%}

%{
#include <stdexcept>
#include <string>
#include <ios>

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
#include "libtorrent/file_pool.hpp"
#include "libtorrent/ip_filter.hpp"
#include "libtorrent/lazy_entry.hpp"
#include "libtorrent/buffer.hpp"
#include "libtorrent/tracker_manager.hpp"
#include "libtorrent/time.hpp"
#include "libtorrent/escape_string.hpp"
#include "libtorrent/bencode.hpp"
#include "libtorrent/magnet_uri.hpp"
#include "libtorrent/create_torrent.hpp"
#include "libtorrent/upnp.hpp"

#include "libtorrent/extensions/ut_pex.hpp"
#include "libtorrent/extensions/ut_metadata.hpp"
#include "libtorrent/extensions/lt_trackers.hpp"
#include "libtorrent/extensions/smart_ban.hpp"

#include "libtorrent/kademlia/item.hpp"
#include "libtorrent/ed25519.hpp"
    
// aditional includes

using namespace boost;
using namespace boost::system;
    
using namespace libtorrent;
    
// dummy implementation due to issues of SWIG with alert type pure virtual functions
namespace libtorrent {
    
    int type() { return 0; }
    int category() { return 0; }
    char* what() { return NULL; }
}
    
void translate_cpp_exception(JNIEnv *jenv) {
    try {
        throw;
    } catch (const std::out_of_range &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaIndexOutOfBoundsException, e.what());
    } catch (const std::invalid_argument &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaIllegalArgumentException, e.what());
    } catch (const std::bad_alloc &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, e.what());
    } catch (const std::ios_base::failure &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaIOException, e.what());
    } catch (const std::exception &e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaUnknownError, e.what());
    } catch (...) {
        SWIG_JavaThrowException(jenv, SWIG_JavaUnknownError, "Unknown exception type");
    }
}

class ed25519 {
public:

    static const int seed_size = ed25519_seed_size;
    static const int private_key_size = ed25519_private_key_size;
    static const int public_key_size = ed25519_public_key_size;
    static const int signature_size = ed25519_signature_size;
    static const int scalar_size = ed25519_scalar_size;
    static const int shared_secret_size = ed25519_shared_secret_size;

    static int create_seed(std::vector<char>& seed) {
        return ed25519_create_seed((unsigned char*)seed.data());
    }

    static void create_keypair(std::vector<char>& public_key,
                               std::vector<char>& private_key,
                               std::vector<char>& seed) {
        ed25519_create_keypair((unsigned char*)public_key.data(),
                               (unsigned char*)private_key.data(),
                               (unsigned char*)seed.data());
    }

    static void sign(std::vector<char>& signature,
                     std::vector<char>& message,
                     std::vector<char>& public_key,
                     std::vector<char>& private_key) {
        ed25519_sign((unsigned char*)signature.data(),
                     (unsigned char*)message.data(),
                     message.size(),
                     (unsigned char*)public_key.data(),
                     (unsigned char*)private_key.data());
    }

    static int verify(std::vector<char>& signature,
                      std::vector<char>& message,
                      std::vector<char>& private_key) {
        return ed25519_verify((unsigned char*)signature.data(),
                              (unsigned char*)message.data(),
                              message.size(),
                              (unsigned char*)private_key.data());
    }

    static void add_scalar(std::vector<char>& public_key,
                           std::vector<char>& private_key,
                           std::vector<char>& scalar) {
        ed25519_add_scalar((unsigned char*)public_key.data(),
                           (unsigned char*)private_key.data(),
                           (unsigned char*)scalar.data());
    }

    static void key_exchange(std::vector<char>& shared_secret,
                             std::vector<char>& public_key,
                             std::vector<char>& private_key) {
        ed25519_key_exchange((unsigned char*)shared_secret.data(),
                             (unsigned char*)public_key.data(),
                             (unsigned char*)private_key.data());
    }
};

std::string to_hex(std::vector<char>& v) {
    std::vector<char> s(2 * v.size());
    to_hex(v.data(), v.size(), s.data());
    return std::string(s.begin(), s.end());
}

namespace libtorrent {
namespace dht {
    // code copied from item.cpp
    enum { canonical_length = 1200 };
    int canonical_string(std::pair<char const*, int> v, boost::uint64_t seq
        , std::pair<char const*, int> salt, char out[canonical_length])
    {
        // v must be valid bencoding!
#ifdef TORRENT_DEBUG
        lazy_entry e;
        error_code ec;
        TORRENT_ASSERT(lazy_bdecode(v.first, v.first + v.second, e, ec) == 0);
#endif
        char* ptr = out;

        int left = canonical_length - (ptr - out);
        if (salt.second > 0)
        {
            ptr += snprintf(ptr, left, "4:salt%d:", salt.second);
            left = canonical_length - (ptr - out);
            memcpy(ptr, salt.first, (std::min)(salt.second, left));
            ptr += (std::min)(salt.second, left);
            left = canonical_length - (ptr - out);
        }
        ptr += snprintf(ptr, canonical_length - (ptr - out)
            , "3:seqi%" PRId64 "e1:v", seq);
        left = canonical_length - (ptr - out);
        memcpy(ptr, v.first, (std::min)(v.second, left));
        ptr += (std::min)(v.second, left);
        TORRENT_ASSERT((ptr - out) <= canonical_length);
        return ptr - out;
    }
}
}

class dht_item {
public:

    static int canonical_string(std::vector<char>& v, long seq, std::string& salt, std::vector<char>& out) {
        return dht::canonical_string(std::pair<char const*, int>(v.data(), v.size()),
                                     seq,
                                     std::pair<char const*, int>(salt.data(), salt.size()),
                                     out.data());
    }

    static sha1_hash item_target_id(std::vector<char>& v) {
        return dht::item_target_id(std::pair<char const*, int>(v.data(), v.size()));
    }

    static sha1_hash item_target_id(std::vector<char>& salt, std::vector<char>& pk) {
        return dht::item_target_id(std::pair<char const*, int>(salt.data(), salt.size()), pk.data());
    }

    static bool verify_mutable_item(std::vector<char>& v, std::string& salt, long seq, std::vector<char>& pk, std::vector<char>& sig) {
        return dht::verify_mutable_item(std::pair<char const*, int>(v.data(), v.size()),
                                        std::pair<char const*, int>(salt.data(), salt.size()),
                                        seq,
                                        pk.data(),
                                        sig.data());
    }

    static void sign_mutable_item(std::vector<char>& v, std::string& salt, long seq, std::vector<char>& pk, std::vector<char>& sk, std::vector<char>& sig) {
        dht::sign_mutable_item(std::pair<char const*, int>(v.data(), v.size()),
                               std::pair<char const*, int>(salt.data(), salt.size()),
                               seq,
                               pk.data(),
                               sk.data(),
                               sig.data());
    }
};
%}

%exception {
    try {
        $action
    } catch (...) {
        translate_cpp_exception(jenv);
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
%include <std_deque.i>
%include <enums.swg>

%include "std_vector2.i"
%include "std_map2.i"
%include "std_list.i"
%include "boost.i"

%include "session_extend.i"

%intrusive_ptr(libtorrent::torrent_info)
%intrusive_ptr(libtorrent::tracker_connection)
%intrusive_ptr(libtorrent::peer_connection)

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
%apply const boost::int64_t & {boost::int64_t &};

typedef long time_t;

namespace std {
    %template(char_const_ptr_int_pair) pair<const char *, int>;
    %template(int_int_pair) pair<int, int>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(long_long_long_2_pair) pair<long long, long>;
    %template(string_lazy_entry_const_ptr_pair) pair<std::string, const libtorrent::lazy_entry *>;
    
    %template(string_vector) vector<std::string>;
    %template(char_vector) vector<char>;
    %template(bool_vector) vector<bool>;
    %template(long_long_long_2_pair_vector) vector<std::pair<long long, long>>;
    %template(string_int_pair_vector) vector<std::pair<std::string, int>>;
    %template(string_string_pair_vector) vector<std::pair<std::string, std::string>>;

    %template(unsigned_char_vector) vector<unsigned char>;
    %template(int_vector) vector<int>;
    %template(int64_vector) vector<long long>;
    %template(sha1_hash_vector) vector<libtorrent::sha1_hash>;
    %template(torrent_status_vector) vector<libtorrent::torrent_status>;
    %template(torrent_handle_vector) vector<libtorrent::torrent_handle>;
    %template(feed_handle_vector) vector<libtorrent::feed_handle>;
    %template(feed_item_vector) vector<libtorrent::feed_item>;
    %template(file_slice_vector) vector<libtorrent::file_slice>;
    %template(peer_request_vector) vector<libtorrent::peer_request>;
    %template(dht_routing_bucket_vector) vector<libtorrent::dht_routing_bucket>;
    %template(dht_lookup_vector) vector<libtorrent::dht_lookup>;
    
    %template(partial_piece_info_vector) vector<libtorrent::partial_piece_info>;
    %template(cached_piece_info_vector) vector<libtorrent::cached_piece_info>;
    %template(peer_info_vector) vector<libtorrent::peer_info>;

    %template(entry_vector) vector<libtorrent::entry>;
    %template(web_seed_entry_vector) vector<libtorrent::web_seed_entry>;
    %template(peer_entry_vector) vector<libtorrent::peer_entry>;
    %template(announce_entry_vector) vector<libtorrent::announce_entry>;
    %template(peer_list_entry_vector) vector<libtorrent::peer_list_entry>;
    %template(tcp_endpoint_vector) vector<tcp::endpoint>;

    %template(entry_list) list<libtorrent::entry>;

    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;
    %template(int_sha1_hash_map) map<int, libtorrent::sha1_hash>;

    %template(alert_ptr_deque) deque<libtorrent::alert*>;
};

// this are ignore until we solve the specific type issues

%ignore match_filesizes;
%ignore clone;
%ignore new_feed;
%ignore parse_feed;
%ignore ssl_ctx;
%ignore readv;
%ignore writev;
%ignore default_pred;
%ignore ignore_subdir;
%ignore integer_to_str;
%ignore get_file_attributes;
%ignore get_symlink_path;

%ignore libtorrent::TORRENT_CFG;
%ignore libtorrent::partial_hash;
%ignore libtorrent::piece_manager;
%ignore libtorrent::disk_io_thread;
%ignore libtorrent::feed;
%ignore libtorrent::request_callback;
%ignore libtorrent::has_block;
%ignore libtorrent::pending_block;
%ignore libtorrent::timeout_handler;
%ignore libtorrent::connection_queue;
%ignore libtorrent::parse_int;
%ignore libtorrent::file_pool;
%ignore libtorrent::default_storage;
%ignore libtorrent::default_storage_constructor;
%ignore libtorrent::disabled_storage_constructor;
%ignore libtorrent::lazy_bdecode;
%ignore libtorrent::url_has_argument;
%ignore libtorrent::set_piece_hashes;
%ignore libtorrent::hash_value;
%ignore libtorrent::cork;
%ignore libtorrent::detail::add_files_impl;
%ignore libtorrent::generic_category;
%ignore libtorrent::torrent;
%ignore libtorrent::alert_manager;
%ignore libtorrent::plugin;
%ignore libtorrent::torrent_plugin;
%ignore libtorrent::bandwidth_channel;
%ignore libtorrent::bt_peer_connection;
%ignore libtorrent::disk_io_job;
%ignore libtorrent::is_read_operation;
%ignore libtorrent::operation_has_buffer;
%ignore libtorrent::internal_file_entry;
%ignore libtorrent::peer_plugin;
%ignore libtorrent::libtorrent_exception;
%ignore libtorrent::libtorrent_exception_error;
%ignore libtorrent::libtorrent_exception_what;
%ignore libtorrent::storage_interface;
%ignore libtorrent::time_critical_piece;
%ignore libtorrent::tracker_manager;
%ignore libtorrent::tracker_connection;
%ignore libtorrent::tracker_request;
%ignore libtorrent::type_error;
%ignore libtorrent::buffer;
%ignore libtorrent::disk_buffer_pool;
%ignore libtorrent::disk_buffer_holder;
%ignore libtorrent::upnp;

%ignore libtorrent::to_string(size_type);
%ignore libtorrent::read_until;
%ignore libtorrent::is_hex;
%ignore libtorrent::to_hex(char const*, int, char*);
%ignore libtorrent::from_hex(char const*, int, char*);
%ignore libtorrent::convert_to_native;
%ignore libtorrent::convert_from_native;
%ignore libtorrent::throw_type_error;
%ignore libtorrent::trim_path_element;
%ignore libtorrent::throw_invalid_handle;
%ignore libtorrent::request_a_block;
%ignore libtorrent::merkle_num_leafs;
%ignore libtorrent::merkle_num_nodes;
%ignore libtorrent::merkle_get_parent;
%ignore libtorrent::merkle_get_sibling;
%ignore libtorrent::gzip_header;
%ignore libtorrent::convert_path_to_posix;
%ignore libtorrent::hex_to_int;
%ignore libtorrent::to_hex;

%ignore libtorrent::tracker_manager::tracker_manager;
%ignore libtorrent::tracker_manager::queue_request;
%ignore libtorrent::tracker_manager::incoming_packet;
%ignore libtorrent::tracker_connection::requester;
%ignore libtorrent::tracker_connection::on_receive;
%ignore libtorrent::ip_filter::export_filter;
%ignore libtorrent::add_torrent_params::add_torrent_params;
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::storage;
%ignore libtorrent::add_torrent_params::userdata;
%ignore libtorrent::add_torrent_params::flags;
%ignore libtorrent::torrent::async_verify_piece;
%ignore libtorrent::connection_queue::enqueue;
%ignore libtorrent::alert_manager::set_dispatch_function;
%ignore libtorrent::session::set_alert_dispatch;
%ignore libtorrent::session::get_torrent_status;
%ignore libtorrent::session::get_io_service;
%ignore libtorrent::session::get_connection_queue;
%ignore libtorrent::session::add_extension(boost::function<boost::shared_ptr<torrent_plugin>(torrent*, void*)>);
%ignore libtorrent::session::dht_put_item(boost::array<char, 32>, boost::function<void(entry&, boost::array<char,64>&, boost::uint64_t&, std::string const&)>, std::string);
%ignore libtorrent::session::dht_put_item(boost::array<char, 32>, boost::function<void(entry&, boost::array<char,64>&, boost::uint64_t&, std::string const&)>);
%ignore libtorrent::session::dht_get_item(boost::array<char, 32>, std::string);
%ignore libtorrent::session::dht_get_item(boost::array<char, 32>);
%ignore libtorrent::session::add_extension;
%ignore libtorrent::peer_connection::peer_connection;
%ignore libtorrent::peer_connection::incoming_piece;
%ignore libtorrent::peer_connection::send_buffer;
%ignore libtorrent::peer_connection::associated_torrent;
%ignore libtorrent::peer_connection::add_request;
%ignore libtorrent::peer_connection::cancel_request;
%ignore libtorrent::peer_connection::make_time_critical;
%ignore libtorrent::peer_connection::download_queue;
%ignore libtorrent::peer_connection::request_queue;
%ignore libtorrent::peer_connection::downloading_piece_progress;
%ignore libtorrent::peer_connection::timeout_requests;
%ignore libtorrent::peer_connection::reset_upload_quota;
%ignore libtorrent::peer_connection::m_channel_state;
%ignore libtorrent::peer_connection::can_read;
%ignore libtorrent::peer_connection::get_socket;
%ignore libtorrent::peer_connection::add_extension;
%ignore libtorrent::peer_connection::find_plugin;
%ignore libtorrent::peer_connection::received_listen_port() const;
%ignore libtorrent::bt_peer_connection::send_buffer;
%ignore libtorrent::bt_peer_connection::write_metadata;
%ignore libtorrent::bt_peer_connection::write_metadata_request;
%ignore libtorrent::disk_io_job::callback;
%ignore libtorrent::disk_io_job::storage;
%ignore libtorrent::disk_buffer_holder::disk_buffer_holder;
%ignore libtorrent::disk_buffer_pool::free_multiple_buffers;
%ignore libtorrent::plugin::added;
%ignore libtorrent::plugin::new_torrent;
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
%ignore libtorrent::torrent::connect_to_url_seed;
%ignore libtorrent::torrent::connect_web_seed;
%ignore libtorrent::torrent::remove_web_seed;
%ignore libtorrent::torrent::web_seeds;
%ignore libtorrent::torrent::on_peer_name_lookup;
%ignore libtorrent::torrent::on_name_lookup;
%ignore libtorrent::torrent::on_proxy_name_lookup;
%ignore libtorrent::torrent::read_piece_struct::piece_data;
%ignore libtorrent::policy::policy;
%ignore libtorrent::policy::begin_peer;
%ignore libtorrent::policy::end_peer;
%ignore libtorrent::policy::erase_peer;
%ignore libtorrent::policy::find_peers;
%ignore libtorrent::policy::peer::rank;
%ignore libtorrent::policy::peer::address;
%ignore libtorrent::policy::peer::ip;
%ignore libtorrent::policy::ipv6_peer::addr;
%ignore libtorrent::torrent_handle::add_extension;
%ignore libtorrent::torrent_handle::http_seeds;
%ignore libtorrent::torrent_handle::url_seeds;
%ignore libtorrent::torrent_handle::native_handle;
%ignore libtorrent::torrent_handle::get_storage_impl;
%ignore libtorrent::sha1_hash::sha1_hash(char const *);
%ignore libtorrent::sha1_hash::begin;
%ignore libtorrent::sha1_hash::end;
%ignore libtorrent::sha1_hash::operator[];
%ignore libtorrent::sha1_hash::assign(char const *);
%ignore libtorrent::entry::integer() const;
%ignore libtorrent::entry::string() const;
%ignore libtorrent::entry::dict() const;
%ignore libtorrent::entry::list() const;
%ignore libtorrent::entry::find_key(std::string const &) const;
%ignore libtorrent::entry::find_key(char const *);
%ignore libtorrent::entry::find_key(char const *) const;
%ignore libtorrent::entry::operator [](char const *);
%ignore libtorrent::entry::operator [](char const *) const;
%ignore libtorrent::entry::operator [](std::string const &) const;
%ignore libtorrent::buffer::data() const;
%ignore libtorrent::buffer::begin() const;
%ignore libtorrent::buffer::end() const;
%ignore libtorrent::buffer::operator[];
%ignore libtorrent::buffer::const_interval::begin;
%ignore libtorrent::buffer::const_interval::end;
%ignore libtorrent::stats_alert::transferred;
%ignore libtorrent::dht_mutable_item_alert::dht_mutable_item_alert;
%ignore libtorrent::dht_mutable_item_alert::key;
%ignore libtorrent::dht_mutable_item_alert::signature;
%ignore libtorrent::dht_put_alert::dht_put_alert;
%ignore libtorrent::dht_put_alert::public_key;
%ignore libtorrent::dht_put_alert::signature;
%ignore libtorrent::torrent_info::torrent_info(char const *, int);
%ignore libtorrent::torrent_info::torrent_info(char const *, int, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&, int);
%ignore libtorrent::torrent_info::creation_date;
%ignore libtorrent::torrent_info::metadata;
%ignore libtorrent::read_piece_alert::read_piece_alert;
%ignore libtorrent::read_piece_alert::buffer;
%ignore libtorrent::peer_plugin::on_extended;
%ignore libtorrent::peer_plugin::on_unknown_message;
%ignore libtorrent::lazy_entry::dict_find(char const *) const;
%ignore libtorrent::lazy_entry::list_at(int) const;
%ignore libtorrent::block_info::peer;
%ignore libtorrent::lazy_dict_entry;
%ignore libtorrent::disabled_storage;
%ignore libtorrent::invalid_encoding;
%ignore libtorrent::errors::make_error_code;
%ignore libtorrent::bdecode_errors::make_error_code;
%ignore libtorrent::upnp_errors::make_error_code;

%ignore boost::throws;
%ignore boost::detail::throws;
%ignore boost::asio::ip::address_v4::to_bytes;
%ignore boost::asio::ip::address_v6::to_bytes;

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

%rename(errors) libtorrent::errors::error_code_enum;
%rename(bdecode_errors) libtorrent::bdecode_errors::error_code_enum;
%rename(upnp_errors) libtorrent::upnp_errors::error_code_enum;

%include <boost/system/error_code.hpp>

%include "libtorrent/version.hpp"
%include "libtorrent/ptime.hpp"
%include "libtorrent/size_type.hpp"
%javaconst(1);
%include "libtorrent/error_code.hpp"
%javaconst(0);
%include "libtorrent/fingerprint.hpp"
%include "libtorrent/bitfield.hpp"
%include "libtorrent/stat.hpp"
%include "libtorrent/peer_request.hpp"
%include "libtorrent/address.hpp"
%include "libtorrent/entry.hpp"
%include "libtorrent/sha1_hash.hpp"
%include "libtorrent/storage_defs.hpp"
%include "libtorrent/storage.hpp"
%include "libtorrent/file_storage.hpp"
%include "libtorrent/policy.hpp"
%include "libtorrent/torrent_info.hpp"
%include "libtorrent/torrent_handle.hpp"
%include "libtorrent/add_torrent_params.hpp"
%include "libtorrent/rss.hpp"
%include "libtorrent/alert.hpp"
%include "alert_types.hpp"
%include "libtorrent/alert_manager.hpp"
%include "libtorrent/disk_io_thread.hpp"
%include "libtorrent/peer.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/bandwidth_limit.hpp"
%include "libtorrent/bandwidth_socket.hpp"
%include "libtorrent/peer_connection.hpp"
%include "libtorrent/session_status.hpp"
%include "libtorrent/session_settings.hpp"
%include "libtorrent/torrent.hpp"
%include "libtorrent/session.hpp"
%include "libtorrent/extensions.hpp"
%include "libtorrent/disk_buffer_holder.hpp"
%include "libtorrent/disk_buffer_pool.hpp"
%include "libtorrent/bt_peer_connection.hpp"
%include "libtorrent/file_pool.hpp"
%include "libtorrent/ip_filter.hpp"
%javaconst(1);
%include "libtorrent/lazy_entry.hpp"
%javaconst(0);
%include "libtorrent/buffer.hpp"
%include "libtorrent/tracker_manager.hpp"
%include "libtorrent/time.hpp"
%include "libtorrent/escape_string.hpp"
%include "libtorrent/bencode.hpp"
%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%javaconst(1);
%include "libtorrent/upnp.hpp"
%javaconst(0);

namespace libtorrent {
    
// alert types conversion due to lack of polymorphic return type
%extend alert {
#define CAST_ALERT_METHOD(name) \
    static libtorrent::##name *cast_to_##name(alert *alert) { \
        return dynamic_cast<libtorrent::##name *>(alert); \
    }

    CAST_ALERT_METHOD(torrent_alert)
    CAST_ALERT_METHOD(peer_alert)
    CAST_ALERT_METHOD(tracker_alert)
    CAST_ALERT_METHOD(torrent_added_alert)
    CAST_ALERT_METHOD(torrent_removed_alert)
    CAST_ALERT_METHOD(read_piece_alert)
    CAST_ALERT_METHOD(file_completed_alert)
    CAST_ALERT_METHOD(file_renamed_alert)
    CAST_ALERT_METHOD(file_rename_failed_alert)
    CAST_ALERT_METHOD(performance_alert)
    CAST_ALERT_METHOD(state_changed_alert)
    CAST_ALERT_METHOD(tracker_error_alert)
    CAST_ALERT_METHOD(tracker_warning_alert)
    CAST_ALERT_METHOD(scrape_reply_alert)
    CAST_ALERT_METHOD(scrape_failed_alert)
    CAST_ALERT_METHOD(tracker_reply_alert)
    CAST_ALERT_METHOD(dht_reply_alert)
    CAST_ALERT_METHOD(tracker_announce_alert)
    CAST_ALERT_METHOD(hash_failed_alert)
    CAST_ALERT_METHOD(peer_ban_alert)
    CAST_ALERT_METHOD(peer_unsnubbed_alert)
    CAST_ALERT_METHOD(peer_snubbed_alert)
    CAST_ALERT_METHOD(peer_error_alert)
    CAST_ALERT_METHOD(peer_connect_alert)
    CAST_ALERT_METHOD(peer_disconnected_alert)
    CAST_ALERT_METHOD(invalid_request_alert)
    CAST_ALERT_METHOD(torrent_finished_alert)
    CAST_ALERT_METHOD(piece_finished_alert)
    CAST_ALERT_METHOD(request_dropped_alert)
    CAST_ALERT_METHOD(block_timeout_alert)
    CAST_ALERT_METHOD(block_finished_alert)
    CAST_ALERT_METHOD(block_downloading_alert)
    CAST_ALERT_METHOD(unwanted_block_alert)
    CAST_ALERT_METHOD(storage_moved_alert)
    CAST_ALERT_METHOD(storage_moved_failed_alert)
    CAST_ALERT_METHOD(torrent_deleted_alert)
    CAST_ALERT_METHOD(torrent_delete_failed_alert)
    CAST_ALERT_METHOD(save_resume_data_alert)
    CAST_ALERT_METHOD(save_resume_data_failed_alert)
    CAST_ALERT_METHOD(torrent_paused_alert)
    CAST_ALERT_METHOD(torrent_resumed_alert)
    CAST_ALERT_METHOD(torrent_checked_alert)
    CAST_ALERT_METHOD(url_seed_alert)
    CAST_ALERT_METHOD(file_error_alert)
    CAST_ALERT_METHOD(metadata_failed_alert)
    CAST_ALERT_METHOD(metadata_received_alert)
    CAST_ALERT_METHOD(udp_error_alert)
    CAST_ALERT_METHOD(external_ip_alert)
    CAST_ALERT_METHOD(listen_failed_alert)
    CAST_ALERT_METHOD(listen_succeeded_alert)
    CAST_ALERT_METHOD(portmap_error_alert)
    CAST_ALERT_METHOD(portmap_alert)
    CAST_ALERT_METHOD(portmap_log_alert)
    CAST_ALERT_METHOD(fastresume_rejected_alert)
    CAST_ALERT_METHOD(peer_blocked_alert)
    CAST_ALERT_METHOD(dht_announce_alert)
    CAST_ALERT_METHOD(dht_get_peers_alert)
    CAST_ALERT_METHOD(stats_alert)
    CAST_ALERT_METHOD(cache_flushed_alert)
    CAST_ALERT_METHOD(anonymous_mode_alert)
    CAST_ALERT_METHOD(lsd_peer_alert)
    CAST_ALERT_METHOD(trackerid_alert)
    CAST_ALERT_METHOD(dht_bootstrap_alert)
    CAST_ALERT_METHOD(rss_alert)
    CAST_ALERT_METHOD(torrent_error_alert)
    CAST_ALERT_METHOD(torrent_need_cert_alert)
    CAST_ALERT_METHOD(incoming_connection_alert)
    CAST_ALERT_METHOD(add_torrent_alert)
    CAST_ALERT_METHOD(state_update_alert)
    CAST_ALERT_METHOD(torrent_update_alert)
    CAST_ALERT_METHOD(rss_item_alert)
    CAST_ALERT_METHOD(dht_error_alert)
    CAST_ALERT_METHOD(dht_immutable_item_alert)
    CAST_ALERT_METHOD(dht_mutable_item_alert)
    CAST_ALERT_METHOD(dht_put_alert)
    CAST_ALERT_METHOD(i2p_alert)

    CAST_ALERT_METHOD(dht_get_peers_reply_alert)
};

%extend entry {
    std::vector<char> bencode() {
        std::vector<char> buffer;
        libtorrent::bencode(std::back_inserter(buffer), *$self);
        return buffer;
    }

    static entry bdecode(std::vector<char>& buffer) {
        return bdecode(buffer.begin(), buffer.end());
    }
};

%extend lazy_entry {
    static int bdecode(std::vector<char>& buffer, lazy_entry& e, error_code& ec) {
        return lazy_bdecode(&buffer[0], &buffer[0] + buffer.size(), e, ec);
    }
};

%extend add_torrent_params {
    long long getFlags() {
        return (long long)$self->flags;
    }

    void setFlags(long long flags) {
        $self->flags = flags;
    }

    static add_torrent_params create_instance() {
        return add_torrent_params();
    }

    static add_torrent_params create_instance_no_storage() {
        return add_torrent_params(disabled_storage_constructor);
    }
};

%extend torrent_info {
    time_t get_creation_date() {
        return $self->creation_date().get_value_or(0);
    }
};

%extend sha1_hash {
    std::string to_hex() {
        return to_hex($self->to_string());
    }

    static bool from_hex(char *hex, sha1_hash& h) {
        return from_hex(hex, 40, (char*)&h[0]);
    }
};

%extend dht_mutable_item_alert {
    std::vector<char> key_v() {
        boost::array<char, 32> arr = $self->key;
        return std::vector<char>(arr.begin(), arr.end());
    }

    std::vector<char> signature_v() {
        boost::array<char, 64> arr = $self->signature;
        return std::vector<char>(arr.begin(), arr.end());
    }
};

%extend dht_put_alert {
    std::vector<char> public_key_v() {
        boost::array<char, 32> arr = $self->public_key;
        return std::vector<char>(arr.begin(), arr.end());
    }

    std::vector<char> signature_v() {
        boost::array<char, 64> arr = $self->signature;
        return std::vector<char>(arr.begin(), arr.end());
    }
};

%extend stats_alert {
    std::vector<int> transferred_v() {
        return std::vector<int>($self->transferred, $self->transferred + stats_alert::stats_channel::num_channels);
    }
};

}

class ed25519 {
public:

    static const int seed_size = ed25519_seed_size;
    static const int private_key_size = ed25519_private_key_size;
    static const int public_key_size = ed25519_public_key_size;
    static const int signature_size = ed25519_signature_size;
    static const int scalar_size = ed25519_scalar_size;
    static const int shared_secret_size = ed25519_shared_secret_size;

    static int create_seed(std::vector<char>& seed);

    static void create_keypair(std::vector<char>& public_key,
                               std::vector<char>& private_key,
                               std::vector<char>& seed);

    static void sign(std::vector<char>& signature,
                     std::vector<char>& message,
                     std::vector<char>& public_key,
                     std::vector<char>& private_key);

    static int verify(std::vector<char>& signature,
                      std::vector<char>& message,
                      std::vector<char>& private_key);

    static void add_scalar(std::vector<char>& public_key,
                           std::vector<char>& private_key,
                           std::vector<char>& scalar);

    static void key_exchange(std::vector<char>& shared_secret,
                             std::vector<char>& public_key,
                             std::vector<char>& private_key);
};

std::string to_hex(std::vector<char>& v);

class dht_item {
public:

    static int canonical_string(std::vector<char>& v, long seq, std::string& salt, std::vector<char>& out);

    static libtorrent::sha1_hash item_target_id(std::vector<char>& v);

    static libtorrent::sha1_hash item_target_id(std::vector<char>& salt, std::vector<char>& pk);

    static bool verify_mutable_item(std::vector<char>& v, std::string& salt, long seq, std::vector<char>& pk, std::vector<char>& sig);

    static void sign_mutable_item(std::vector<char>& v, std::string& salt, long seq, std::vector<char>& pk, std::vector<char>& sk, std::vector<char>& sig);
};
