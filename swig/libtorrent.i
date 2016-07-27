%module (jniclassname="libtorrent_jni", directors="1") libtorrent

// suppress Warning 317: Specialization of non-template '<name>'.
#pragma SWIG nowarn=317
// suppress Warning 319: No access specifier given for base class 'boost::noncopyable' (ignored).
#pragma SWIG nowarn=319
// suppress Warning 341: The 'using' keyword in type aliasing is not fully supported yet.
#pragma SWIG nowarn=341
// suppress Warning 401: Nothing known about base class '<name>'. Ignored.
#pragma SWIG nowarn=401

%pragma(java) jniclasscode=%{
    static {
        try {
            String path = System.getProperty("jlibtorrent.jni.path", "");
            if ("".equals(path)) {
                System.loadLibrary("jlibtorrent");
            } else {
                System.load(path);
            }
        } catch (LinkageError e) {
            LinkageError le = new LinkageError("Look for your architecture binary instructions at: https://github.com/frostwire/frostwire-jlibtorrent");
            le.initCause(e);
            throw le;
        }
    }
%}

%{
#include <stdexcept>
#include <string>
#include <ios>
#include <list>
#include <vector>
#include <array>
#include <map>
#include <algorithm>

#include <boost/system/error_code.hpp>

#include "libtorrent/version.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/bitfield.hpp"
#include "libtorrent/peer_request.hpp"
#include "libtorrent/entry.hpp"
#include "libtorrent/sha1_hash.hpp"
#include "libtorrent/storage_defs.hpp"
#include "libtorrent/storage.hpp"
#include "libtorrent/file_storage.hpp"
#include "libtorrent/torrent_info.hpp"
#include "libtorrent/torrent_handle.hpp"
#include "libtorrent/add_torrent_params.hpp"
#include "libtorrent/operations.hpp"
#include "libtorrent/session_stats.hpp"
#include "libtorrent/performance_counters.hpp"
#include "libtorrent/close_reason.hpp"
#include "libtorrent/alert.hpp"
#include "libtorrent/alert_types.hpp"
#include "libtorrent/alert_manager.hpp"
#include "libtorrent/peer_info.hpp"
#include "libtorrent/session_status.hpp"
#include "libtorrent/session_settings.hpp"
#include "libtorrent/settings_pack.hpp"
#include "libtorrent/peer_class.hpp"
#include "libtorrent/peer_class_type_filter.hpp"
#include "libtorrent/session_handle.hpp"
#include "libtorrent/session.hpp"
#include "libtorrent/peer_connection_handle.hpp"
#include "libtorrent/extensions.hpp"
#include "libtorrent/ip_filter.hpp"
#include "libtorrent/bdecode.hpp"
#include "libtorrent/bencode.hpp"
#include "libtorrent/magnet_uri.hpp"
#include "libtorrent/create_torrent.hpp"
#include "libtorrent/announce_entry.hpp"
#include "libtorrent/torrent_status.hpp"
#include "libtorrent/ed25519.hpp"

#include "libtorrent/aux_/cpuid.hpp"

using namespace boost;
using namespace boost::system;

using namespace libtorrent;


#include "libtorrent.h"
%}

#ifdef LIBTORRENT_SWIG_JNI
%exception {
    try {
        $action
    } catch (std::exception& e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }
}
#endif // LIBTORRENT_SWIG_JNI

%include <stdint.i>
%include <typemaps.i>
%include <std_common.i>
%include <std_string.i>
%include <std_pair.i>
%include <std_deque.i>

namespace std {

    typedef int8_t uint8_t;

    template<class T> class list {
    public:
        typedef size_t size_type;
        typedef T value_type;
        typedef const value_type& const_reference;
        list();

        bool empty() const;
        size_type size() const;
        size_type max_size() const;

        const_reference front();
        const_reference back();

        void push_front(const value_type& x);
        void pop_front();
        void push_back(const value_type& x);
        void pop_back();
        void clear();

        %extend {
            std::vector<T> to_vector() {
                return std::vector<T>(self->begin(), self->end());
            }
        }
    };

    template<class T> class vector {
    public:
        typedef size_t size_type;
        typedef T value_type;
        typedef const value_type& const_reference;
        vector();
        size_type size() const;
        size_type capacity() const;
        void reserve(size_type n);
        bool empty() const;
        void clear();
        void push_back(const value_type& x);
        %extend {
            const_reference get(int i) throw (std::out_of_range) {
                int size = int(self->size());
                if (i>=0 && i<size)
                    return (*self)[i];
                else
                    throw std::out_of_range("vector index out of range");
            }
            void set(int i, const value_type& val) throw (std::out_of_range) {
                int size = int(self->size());
                if (i>=0 && i<size)
                    (*self)[i] = val;
                else
                    throw std::out_of_range("vector index out of range");
            }
        }
    };

    template<> class vector<int8_t> {
    public:
        typedef size_t size_type;
        typedef int8_t value_type;
        typedef int8_t const_reference;
        vector();
        size_type size() const;
        size_type capacity() const;
        void reserve(size_type n);
        bool empty() const;
        void clear();
        void push_back(const value_type& x);
        void resize(size_type count);
        %extend {
            int8_t get(int i) throw (std::out_of_range) {
                int size = int(self->size());
                if (i>=0 && i<size)
                    return (*self)[i];
                else
                    throw std::out_of_range("vector index out of range");
            }
            void set(int i, const value_type& val) throw (std::out_of_range) {
                int size = int(self->size());
                if (i>=0 && i<size)
                    (*self)[i] = val;
                else
                    throw std::out_of_range("vector index out of range");
            }
        }
    };

    template<class K, class T> class map {
    // add typemaps here
    public:
        typedef size_t size_type;
        typedef ptrdiff_t difference_type;
        typedef K key_type;
        typedef T mapped_type;
        map();
        map(const map<K,T> &);

        unsigned int size() const;
        bool empty() const;
        void clear();
        %extend {
            const T& get(const K& key) throw (std::out_of_range) {
                std::map<K,T >::iterator i = self->find(key);
                if (i != self->end())
                    return i->second;
                else
                    throw std::out_of_range("key not found");
            }
            void set(const K& key, const T& x) {
                (*self)[key] = x;
            }
            void erase(const K& key) throw (std::out_of_range) {
                std::map<K,T >::iterator i = self->find(key);
                if (i != self->end())
                    self->erase(i);
                else
                    throw std::out_of_range("key not found");
            }
            bool has_key(const K& key) {
                std::map<K,T >::iterator i = self->find(key);
                return i != self->end();
            }
            std::vector<K> keys() {
                std::vector<K> v;
                for(std::map<K, T>::iterator it = self->begin(),
                    end(self->end()); it != end; ++it) {
                    v.push_back(it->first);
                }
                return v;
            }
        }
    };

    %template(int_int_pair) pair<int, int>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(string_bdecode_node_pair) pair<std::string, libtorrent::bdecode_node>;

    %template(byte_vector) vector<int8_t>;
    %template(string_vector) vector<std::string>;
    %template(string_int_pair_vector) vector<std::pair<std::string, int>>;
    %template(string_string_pair_vector) vector<std::pair<std::string, std::string>>;
    %template(int_int_pair_vector) vector<std::pair<int, int>>;

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
    %template(announce_entry_vector) vector<libtorrent::announce_entry>;
    %template(peer_list_entry_vector) vector<libtorrent::peer_list_entry>;
    %template(tcp_endpoint_vector) vector<tcp::endpoint>;

    %template(string_list) list<std::string>;
    %template(entry_list) list<libtorrent::entry>;

    %template(int_string_map) map<int, std::string>;
    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;
    %template(int_bitfield_map) map<int, libtorrent::bitfield>;

    %template(alert_ptr_vector) vector<libtorrent::alert*>;
};

namespace libtorrent {

    class sha1_hash;

    template <int N>
    struct bloom_filter {

        bool find(sha1_hash const& k) const;
        void set(sha1_hash const& k);

        void clear();

        float size() const;

        bloom_filter();

        %extend {
            std::vector<int8_t> to_bytes() const {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }

            void from_bytes(std::vector<int8_t> const& v) {
                $self->from_string(reinterpret_cast<char const*>(&v[0]));
            }
        }
    };

    %template(bloom_filter_128) bloom_filter<128>;
    %template(bloom_filter_256) bloom_filter<256>;

    template <typename T>
    struct span {

        span();
        span(T p);
        span(std::vector<T> const& v);

        size_t size() const;
        bool empty() const;

        T front() const;
        T back() const;

        span<T> first(size_t const n) const;
        span<T> last(size_t const n) const;

        span<T> subspan(size_t const offset) const;
        span<T> subspan(size_t const offset, size_t const count) const;

        %extend {
            T get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    template <>
    struct span<char const> {

        span();
        span(int8_t p);

        size_t size() const;
        bool empty() const;

        int8_t front() const;
        int8_t back() const;

        span<char const> first(size_t const n) const;
        span<char const> last(size_t const n) const;

        span<char const> subspan(size_t const offset) const;
        span<char const> subspan(size_t const offset, size_t const count) const;

        %extend {
            span(std::vector<int8_t> const& v) {
                std::vector<char const> temp(v.begin(), v.end());
                return new span<char const>(temp);
            }

            int8_t get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    %template(byte_span) span<char const>;

};

typedef long time_t;

%ignore clone;
%ignore ssl_ctx;
%ignore default_pred;
%ignore ignore_subdir;
%ignore integer_to_str;
%ignore get_file_attributes;
%ignore get_symlink_path;

%ignore libtorrent::piece_manager;
%ignore libtorrent::request_callback;
%ignore libtorrent::timeout_handler;
%ignore libtorrent::parse_int;
%ignore libtorrent::default_storage_constructor;
%ignore libtorrent::disabled_storage_constructor;
%ignore libtorrent::bdecode;
%ignore libtorrent::url_has_argument;
%ignore libtorrent::set_piece_hashes(create_torrent&, std::string const&, boost::function<void(int)> const&, error_code&);
%ignore libtorrent::hash_value;
%ignore libtorrent::alert_manager;
%ignore libtorrent::plugin;
%ignore libtorrent::disk_job_fence;
%ignore libtorrent::is_read_operation;
%ignore libtorrent::operation_has_buffer;
%ignore libtorrent::internal_file_entry;
%ignore libtorrent::storage_interface;
%ignore libtorrent::time_critical_piece;
%ignore libtorrent::buffer_allocator_interface;
%ignore libtorrent::torrent_hot_members;
%ignore libtorrent::storage_piece_set;
%ignore libtorrent::print_entry;
%ignore libtorrent::type_error;
%ignore libtorrent::peer_class;
%ignore libtorrent::peer_class_pool;
%ignore libtorrent::detail::bdecode_token;

%ignore libtorrent::to_string(size_type);
%ignore libtorrent::read_until;
%ignore libtorrent::convert_to_native;
%ignore libtorrent::convert_from_native;
%ignore libtorrent::trim_path_element;
%ignore libtorrent::request_a_block;
%ignore libtorrent::merkle_num_leafs;
%ignore libtorrent::merkle_num_nodes;
%ignore libtorrent::merkle_get_parent;
%ignore libtorrent::merkle_get_sibling;
%ignore libtorrent::gzip_header;
%ignore libtorrent::convert_path_to_posix;
%ignore libtorrent::hex_to_int;
%ignore libtorrent::nop;
%ignore libtorrent::to_string;
%ignore libtorrent::add_files(file_storage&, std::string const&, boost::function<bool(std::string)>, std::uint32_t);
%ignore libtorrent::add_files(file_storage&, std::string const&, boost::function<bool(std::string)>);
%ignore libtorrent::initialize_file_progress;
%ignore libtorrent::get_filesizes;
%ignore libtorrent::parse_magnet_uri_peers;

%ignore libtorrent::ip_filter::export_filter;
%ignore libtorrent::add_torrent_params::add_torrent_params;
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::storage;
%ignore libtorrent::add_torrent_params::userdata;
%ignore libtorrent::add_torrent_params::flags;
%ignore libtorrent::add_torrent_params::ti;
%ignore libtorrent::add_torrent_params::deprecated1;
%ignore libtorrent::add_torrent_params::deprecated2;
%ignore libtorrent::add_torrent_params::deprecated3;
%ignore libtorrent::add_torrent_params::deprecated4;
%ignore libtorrent::connection_queue::enqueue;
%ignore libtorrent::alert_manager::set_dispatch_function;
%ignore libtorrent::session::session(settings_pack const&, io_service&, int);
%ignore libtorrent::session::session(settings_pack const&, io_service&);
%ignore libtorrent::session_handle::session_handle(aux::session_impl*);
%ignore libtorrent::session_handle::set_alert_dispatch;
%ignore libtorrent::session_handle::get_torrent_status;
%ignore libtorrent::session_handle::get_io_service;
%ignore libtorrent::session_handle::get_connection_queue;
%ignore libtorrent::session_handle::add_extension(boost::function<boost::shared_ptr<torrent_plugin>(torrent_handle const&, void*)>);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, boost::function<void(entry&, std::array<char,64>&, std::uint64_t&, std::string const&)>, std::string);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, boost::function<void(entry&, std::array<char,64>&, std::uint64_t&, std::string const&)>);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>, std::string);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>);
%ignore libtorrent::session_handle::dht_direct_request(udp::endpoint, entry const&, void*);
%ignore libtorrent::session_handle::add_extension;
%ignore libtorrent::session_handle::set_load_function;
%ignore libtorrent::session_handle::set_alert_notify;
%ignore libtorrent::session_handle::native_handle;
%ignore libtorrent::session_handle::set_dht_storage;
%ignore libtorrent::session_handle::get_cache_info;
%ignore libtorrent::session_handle::wait_for_alert;
%ignore libtorrent::session_stats_alert::values;
%ignore libtorrent::save_resume_data_alert::resume_data;
%ignore libtorrent::picker_log_alert::blocks;
%ignore libtorrent::dht_pkt_alert::pkt_buf;
%ignore libtorrent::peer_connection_handle::peer_connection_handle;
%ignore libtorrent::peer_connection_handle::peer_log;
%ignore libtorrent::peer_connection_handle::native_handle;
%ignore libtorrent::peer_connection_handle::add_extension;
%ignore libtorrent::peer_connection_handle::time_of_last_unchoke;
%ignore libtorrent::bt_peer_connection_handle::switch_send_crypto;
%ignore libtorrent::bt_peer_connection_handle::switch_recv_crypto;
%ignore libtorrent::bt_peer_connection_handle::native_handle;
%ignore libtorrent::plugin::added;
%ignore libtorrent::plugin::new_torrent;
%ignore libtorrent::crypto_plugin;
%ignore libtorrent::torrent_plugin::new_connection;
%ignore libtorrent::torrent_handle::add_extension;
%ignore libtorrent::torrent_handle::add_piece;
%ignore libtorrent::torrent_handle::http_seeds;
%ignore libtorrent::torrent_handle::url_seeds;
%ignore libtorrent::torrent_handle::get_storage_impl;
%ignore libtorrent::torrent_handle::file_status;
%ignore libtorrent::torrent_handle::use_interface;
%ignore libtorrent::torrent_handle::native_handle;
%ignore libtorrent::torrent_handle::torrent_file;
%ignore libtorrent::torrent_handle::get_full_peer_list;
%ignore libtorrent::block_info::set_peer;
%ignore libtorrent::partial_piece_info::blocks;
%ignore libtorrent::sha1_hash::sha1_hash(char const*);
%ignore libtorrent::sha1_hash::sha1_hash(std::string const&);
%ignore libtorrent::sha1_hash::begin;
%ignore libtorrent::sha1_hash::end;
%ignore libtorrent::sha1_hash::operator[];
%ignore libtorrent::sha1_hash::assign(char const*);
%ignore libtorrent::sha1_hash::assign(std::string const&);
%ignore libtorrent::sha1_hash::data;
%ignore libtorrent::sha1_hash::to_string;
%ignore libtorrent::entry::entry(entry&&);
%ignore libtorrent::entry::entry(preformatted_type);
%ignore libtorrent::entry::integer();
%ignore libtorrent::entry::string();
%ignore libtorrent::entry::dict() const;
%ignore libtorrent::entry::list() const;
%ignore libtorrent::entry::preformatted;
%ignore libtorrent::entry::find_key(std::string const &) const;
%ignore libtorrent::entry::find_key(char const *);
%ignore libtorrent::entry::find_key(char const *) const;
%ignore libtorrent::entry::operator [];
%ignore libtorrent::entry::m_type_queried;
%ignore libtorrent::stats_alert::transferred;
%ignore libtorrent::stats_alert::deprecated1;
%ignore libtorrent::stats_alert::deprecated2;
%ignore libtorrent::stats_alert::deprecated3;
%ignore libtorrent::stats_alert::deprecated4;
%ignore libtorrent::dht_mutable_item_alert::dht_mutable_item_alert;
%ignore libtorrent::dht_mutable_item_alert::key;
%ignore libtorrent::dht_mutable_item_alert::signature;
%ignore libtorrent::dht_mutable_item_alert::seq;
%ignore libtorrent::dht_mutable_item_alert::salt;
%ignore libtorrent::dht_put_alert::dht_put_alert;
%ignore libtorrent::dht_put_alert::public_key;
%ignore libtorrent::dht_put_alert::signature;
%ignore libtorrent::dht_put_alert::salt;
%ignore libtorrent::dht_put_alert::seq;
%ignore libtorrent::dht_direct_response_alert::dht_direct_response_alert;
%ignore libtorrent::dht_direct_response_alert::userdata;
%ignore libtorrent::torrent_info::torrent_info(char const *, int);
%ignore libtorrent::torrent_info::torrent_info(char const *, int, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, int);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&, int);
%ignore libtorrent::torrent_info::creation_date;
%ignore libtorrent::torrent_info::metadata_size;
%ignore libtorrent::torrent_info::metadata;
%ignore libtorrent::torrent_info::load;
%ignore libtorrent::torrent_info::unload;
%ignore libtorrent::torrent_info::hash_for_piece_ptr;
%ignore libtorrent::torrent_info::parse_info_section;
%ignore libtorrent::torrent_info::swap;
%ignore libtorrent::torrent_info::add_merkle_nodes;
%ignore libtorrent::torrent_info::build_merkle_list;
%ignore libtorrent::torrent_info::ssl_cert;
%ignore libtorrent::sanitize_append_path_element;
%ignore libtorrent::verify_encoding;
%ignore libtorrent::read_piece_alert::read_piece_alert;
%ignore libtorrent::read_piece_alert::buffer;
%ignore libtorrent::peer_plugin::on_extended;
%ignore libtorrent::peer_plugin::on_unknown_message;
%ignore libtorrent::bdecode_node::dict_find(char const *) const;
%ignore libtorrent::bdecode_node::dict_find_dict(char const *) const;
%ignore libtorrent::bdecode_node::non_owning;
%ignore libtorrent::bdecode_node::data_section;
%ignore libtorrent::bdecode_node::string_ptr;
%ignore libtorrent::bdecode_node::clear;
%ignore libtorrent::bdecode_node::swap;
%ignore libtorrent::bdecode_node::reserve;
%ignore libtorrent::bdecode_node::switch_underlying_buffer;
%ignore libtorrent::errors::make_error_code;
%ignore libtorrent::bdecode_errors::make_error_code;
%ignore libtorrent::set_bits;
%ignore libtorrent::has_bits;
%ignore libtorrent::count_zero_bits;
%ignore libtorrent::zero_storage_constructor;
%ignore libtorrent::advance_bufs;
%ignore libtorrent::bufs_size;
%ignore libtorrent::clear_bufs;
%ignore libtorrent::copy_bufs;
%ignore libtorrent::apply_pack;
%ignore libtorrent::load_pack_from_dict;
%ignore libtorrent::save_settings_to_dict;
%ignore libtorrent::error_code;
%ignore libtorrent::settings_pack::deprecated1;
%ignore libtorrent::settings_pack::deprecated2;
%ignore libtorrent::settings_pack::deprecated3;
%ignore libtorrent::settings_pack::deprecated4;
%ignore libtorrent::settings_pack::deprecated5;
%ignore libtorrent::settings_pack::deprecated6;
%ignore libtorrent::settings_pack::deprecated7;
%ignore libtorrent::settings_pack::deprecated8;
%ignore libtorrent::settings_pack::deprecated9;
%ignore libtorrent::settings_pack::deprecated10;
%ignore libtorrent::settings_pack::deprecated11;
%ignore libtorrent::settings_pack::deprecated12;
%ignore libtorrent::settings_pack::deprecated13;
%ignore libtorrent::settings_pack::deprecated14;
%ignore libtorrent::settings_pack::deprecated15;
%ignore libtorrent::settings_pack::deprecated;
%ignore libtorrent::detail::nop;
%ignore libtorrent::storage_params::pool;
%ignore libtorrent::storage_params::priorities;
%ignore libtorrent::ipv6_peer::addr;
%ignore libtorrent::announce_entry::failed;
%ignore libtorrent::announce_entry::next_announce;
%ignore libtorrent::announce_entry::min_announce;
%ignore libtorrent::announce_entry::can_announce;
%ignore libtorrent::proxy_settings::proxy_settings;
%ignore libtorrent::torrent_status::torrent_file;
%ignore libtorrent::torrent_status::_dummy_string_;
%ignore libtorrent::torrent_status::next_announce;
%ignore libtorrent::torrent_status::deprecated_announce_interval_;
%ignore libtorrent::file_storage::file_path_hash;
%ignore libtorrent::file_storage::all_path_hashes;
%ignore libtorrent::file_storage::file_name_ptr;
%ignore libtorrent::file_storage::file_name_len;
%ignore libtorrent::file_storage::apply_pointer_offset;
%ignore libtorrent::default_storage;
%ignore libtorrent::file_status;
%ignore libtorrent::stat_file;
%ignore libtorrent::fileop;
%ignore libtorrent::readwritev;
%ignore libtorrent::bitfield::const_iterator;
%ignore libtorrent::bitfield::begin;
%ignore libtorrent::bitfield::end;
%ignore libtorrent::bitfield::swap;
%ignore libtorrent::peer_info::last_request;
%ignore libtorrent::peer_info::last_active;
%ignore libtorrent::peer_info::download_queue_time;
%ignore libtorrent::peer_info::deprecated__;
%ignore libtorrent::create_torrent::set_root_cert;
%ignore libtorrent::stats_metric::name;
%ignore libtorrent::storage_moved_failed_alert::operation;
%ignore libtorrent::file_error_alert::operation;
%ignore libtorrent::fastresume_rejected_alert::operation;
%ignore libtorrent::peer_log_alert::event_type;
%ignore libtorrent::dht_lookup::type;

%ignore boost::throws;
%ignore boost::detail::throws;
%ignore boost::system::generic_category;
%ignore boost::system::system_category;
%ignore boost::system::error_code::unspecified_bool_true;
%ignore boost::asio::error::get_netdb_category;
%ignore boost::asio::error::get_addrinfo_category;
%ignore boost::asio::error::get_misc_category;
%ignore boost::asio::detail::posix_tss_ptr_create;

%ignore ed25519_create_seed(unsigned char *);
%ignore ed25519_create_keypair(unsigned char *, unsigned char *, const unsigned char *);
%ignore ed25519_sign(unsigned char *, const unsigned char *, size_t , const unsigned char *, const unsigned char *);
%ignore ed25519_verify(const unsigned char *, const unsigned char *, size_t , const unsigned char *);
%ignore ed25519_add_scalar(unsigned char *, unsigned char *, const unsigned char *);
%ignore ed25519_key_exchange(unsigned char *, const unsigned char *, const unsigned char *);

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
%ignore operator[];
%ignore operator unspecified_bool_type;

%rename(op_eq) operator==;
%rename(op_neq) operator!=;
%rename(op_lt) operator<;
%rename(op_gt) operator>;
%rename(op_bool) operator bool;

%rename(libtorrent_no_error) libtorrent::errors::no_error;
%rename(libtorrent_errors) libtorrent::errors::error_code_enum;
%rename(bdecode_no_error) libtorrent::bdecode_errors::no_error;
%rename(bdecode_errors) libtorrent::bdecode_errors::error_code_enum;

%ignore libtorrent::alert::timestamp;
%rename("$ignore", regextarget=1, %$isconstructor) ".*_alert$";

%include <boost/system/error_code.hpp>

%include "libtorrent/version.hpp"
%include "libtorrent/error_code.hpp"
%include "libtorrent/bitfield.hpp"
%include "libtorrent/peer_request.hpp"
%include "libtorrent/entry.hpp"
%include "libtorrent/sha1_hash.hpp"
%include "libtorrent/storage_defs.hpp"
%include "libtorrent/storage.hpp"
%include "libtorrent/file_storage.hpp"
%include "libtorrent/torrent_info.hpp"
%include "libtorrent/torrent_handle.hpp"
%include "libtorrent/add_torrent_params.hpp"
%include "libtorrent/operations.hpp"
%include "libtorrent/session_stats.hpp"
%include "libtorrent/performance_counters.hpp"
%include "libtorrent/close_reason.hpp"
%include "libtorrent/alert.hpp"
%include "libtorrent/alert_types.hpp"
%include "libtorrent/alert_manager.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/session_status.hpp"
%include "libtorrent/session_settings.hpp"
%include "libtorrent/settings_pack.hpp"
%include "libtorrent/peer_class.hpp"
%include "libtorrent/peer_class_type_filter.hpp"
%include "libtorrent/session_handle.hpp"
%include "libtorrent/session.hpp"
%include "libtorrent/peer_connection_handle.hpp"
%include "libtorrent/extensions.hpp"
%include "libtorrent/ip_filter.hpp"
%include "libtorrent/bdecode.hpp"
%include "libtorrent/bencode.hpp"
%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%include "libtorrent/announce_entry.hpp"
%include "libtorrent/torrent_status.hpp"
%include "libtorrent/ed25519.hpp"

%include "libtorrent/aux_/cpuid.hpp"

class address {
public:

    address();
    address(address other);

    bool is_v4();
    bool is_v6();

    std::string to_string(boost::system::error_code ec);

    static address from_string(std::string str, boost::system::error_code ec);

    bool is_loopback();
    bool is_unspecified();
    bool is_multicast();

    %extend {
        bool op_lt(const address& a2) {
            return *$self < a2;
        }

        static int compare(const address& a1, const address& a2) {
            return a1 == a2 ? 0 : (a1 < a2 ? -1 : 1);
        }
    }
};

%rename(tcp_endpoint) tcp::endpoint;
%rename(udp_endpoint) udp::endpoint;

namespace tcp {

    class endpoint {
    public:
        endpoint();
        endpoint(address address, unsigned short port);

        unsigned short port();
        address address();
    };
}

namespace udp {

    class endpoint {
    public:
        endpoint();
        endpoint(address address, unsigned short port);

        unsigned short port();
        address address();
    };
}

namespace libtorrent {
    
// alert types conversion due to lack of polymorphic return type
%extend alert {
#define CAST_ALERT_METHOD(name) \
    static libtorrent::##name const* cast_to_##name(alert const* a) { \
        return alert_cast<libtorrent::##name>(a); \
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
    CAST_ALERT_METHOD(torrent_error_alert)
    CAST_ALERT_METHOD(torrent_need_cert_alert)
    CAST_ALERT_METHOD(incoming_connection_alert)
    CAST_ALERT_METHOD(add_torrent_alert)
    CAST_ALERT_METHOD(state_update_alert)
    CAST_ALERT_METHOD(mmap_cache_alert)
    CAST_ALERT_METHOD(session_stats_alert)
    CAST_ALERT_METHOD(dht_error_alert)
    CAST_ALERT_METHOD(dht_immutable_item_alert)
    CAST_ALERT_METHOD(dht_mutable_item_alert)
    CAST_ALERT_METHOD(dht_put_alert)
    CAST_ALERT_METHOD(i2p_alert)
    CAST_ALERT_METHOD(dht_outgoing_get_peers_alert)
    CAST_ALERT_METHOD(log_alert)
    CAST_ALERT_METHOD(torrent_log_alert)
    CAST_ALERT_METHOD(peer_log_alert)
    CAST_ALERT_METHOD(lsd_error_alert)
    CAST_ALERT_METHOD(dht_stats_alert)
    CAST_ALERT_METHOD(incoming_request_alert)
    CAST_ALERT_METHOD(dht_log_alert)
    CAST_ALERT_METHOD(dht_pkt_alert)
    CAST_ALERT_METHOD(dht_get_peers_reply_alert)
    CAST_ALERT_METHOD(dht_direct_response_alert)
    CAST_ALERT_METHOD(picker_log_alert)
}

%extend alert {
    int64_t get_timestamp() {
        return total_milliseconds($self->timestamp() - clock_type::now());
    }
}

%extend session_handle {
    void dht_get_item(std::vector<int8_t>& public_key, std::vector<int8_t>& salt) {
        if (public_key.size() != 32) {
            throw std::invalid_argument("Public key must be of size 32");
        }
        std::array<char, 32> key;

        for (int i = 0; i < 32; i++) {
            key[i] = public_key[i];
        }

        $self->dht_get_item(key, std::string(salt.begin(), salt.end()));
    }

    void dht_put_item(std::vector<int8_t>& public_key, std::vector<int8_t>& private_key, entry& data, std::vector<int8_t>& salt) {
        if (public_key.size() != 32) {
            throw std::invalid_argument("Public key must be of size 32");
        }
        if (private_key.size() != 64) {
            throw std::invalid_argument("Private key must be of size 64");
        }
        std::array<char, 32> key;

    	for (int i = 0; i < 32; i++) {
    	    key[i] = public_key[i];
    	}

        $self->dht_put_item(key, boost::bind(&dht_put_item_cb, _1, _2, _3, _4,
            (const char *)public_key.data(), (const char *)private_key.data(), data),
             std::string(salt.begin(), salt.end()));
    }

    void add_swig_extension(swig_plugin *p) {
        $self->add_extension(boost::shared_ptr<plugin>(p));
    }

    void set_swig_dht_storage(swig_dht_storage *s) {
        $self->set_dht_storage(boost::bind(&swig_dht_storage_constructor, s, _1));
    }

    alert* wait_for_alert_ms(int64_t max_wait) {
        return $self->wait_for_alert(milliseconds(max_wait));
    }
}

%extend entry {

    entry(std::string const& s) {
        return new entry(s);
    }

    entry& get(std::string const& key) {
        return $self->operator[](key);
    }

    void set(std::string const& key, std::string const& value) {
        $self->operator[](key) = value;
    }

    void set(std::string const& key, std::vector<int8_t> const& value) {
         $self->operator[](key) = std::string(value.begin(), value.end());
    }

    void set(std::string const& key, long long const& value) {
        $self->operator[](key) = value;
    }

    void set(std::string const& key, libtorrent::entry const& value) {
        $self->operator[](key) = value;
    }

    std::vector<int8_t> string_bytes() {
        std::string s = $self->string();
        return std::vector<int8_t>(s.begin(), s.end());
    }

    std::vector<int8_t> preformatted_bytes() {
        std::vector<char> v = $self->preformatted();
        return std::vector<int8_t>(v.begin(), v.end());
    }

    std::vector<int8_t> bencode() {
        std::vector<int8_t> buffer;
        libtorrent::bencode(std::back_inserter(buffer), *$self);
        return buffer;
    }

    static entry from_string_bytes(std::vector<int8_t> const& string_bytes) {
        return entry(std::string(string_bytes.begin(), string_bytes.end()));
    }

    static entry from_preformatted_bytes(std::vector<int8_t> const& preformatted_bytes) {
        return entry(std::vector<char>(preformatted_bytes.begin(), preformatted_bytes.end()));
    }

    static entry bdecode(std::vector<int8_t>& buffer) {
        return libtorrent::bdecode(buffer.begin(), buffer.end());
    }
}

%extend bdecode_node {
    static std::string to_string(bdecode_node const& e, bool single_line, int indent) {
        return libtorrent::print_entry(e, single_line, indent);
    }

    static int bdecode(std::vector<int8_t>& buffer, bdecode_node& ret, error_code& ec) {
        return libtorrent::bdecode((char const*)&buffer[0], (char const*)&buffer[0] + buffer.size(), ret, ec);
    }
}

%extend add_torrent_params {
    int64_t get_flags() {
        return int64_t($self->flags);
    }

    void set_flags(int64_t flags) {
        $self->flags = flags;
    }

    void set_ti(torrent_info const& ti) {
        $self->ti = boost::make_shared<torrent_info>(ti);
    }

    static add_torrent_params create_instance() {
        return add_torrent_params();
    }

    static add_torrent_params create_instance_disabled_storage() {
        return add_torrent_params(disabled_storage_constructor);
    }

    static add_torrent_params create_instance_zero_storage() {
        return add_torrent_params(zero_storage_constructor);
    }

    static add_torrent_params create_instance_swig_storage(swig_storage* s) {
        return add_torrent_params(boost::bind(&swig_storage_constructor, s, _1));
    }

    static add_torrent_params read_resume_data(bdecode_node const& rd, error_code& ec) {
        return libtorrent::read_resume_data(rd, ec);
    }

    static add_torrent_params read_resume_data(std::vector<int8_t> const& buffer, error_code& ec) {
        return libtorrent::read_resume_data((char const*)&buffer[0], buffer.size(), ec);
    }
}

%extend torrent_info {
    time_t get_creation_date() {
        return $self->creation_date().get_value_or(0);
    }

    std::vector<int8_t> get_ssl_cert() {
        std::string s = $self->ssl_cert();
        return std::vector<int8_t>(s.begin(), s.end());
    }
};

%extend torrent_handle {
    void add_piece_bytes(int piece, std::vector<int8_t> const& data, int flags = 0) {
        $self->add_piece(piece, (char const*)&data[0], flags);
    }

    const torrent_info* get_torrent_copy() {
        boost::shared_ptr<const torrent_info> ti = $self->torrent_file();
        return ti.get();
    }
}

%extend sha1_hash {
    sha1_hash(std::vector<int8_t> const& s) {
        return new sha1_hash(std::string(s.begin(), s.end()));
    }

    int hash_code() {
        char const* data = $self->data();
        int result = 1;
        for (int i = 0; i < 20; i++) {
            result = 31 * result + data[i];
        }
        return result;
    }

    std::vector<int8_t> to_bytes() {
        std::string s = $self->to_string();
        return std::vector<int8_t>(s.begin(), s.end());
    }

    static int compare(const sha1_hash& h1, const sha1_hash& h2) {
        return h1 == h2 ? 0 : (h1 < h2 ? -1 : 1);
    }
}

%extend dht_mutable_item_alert {
    std::vector<int8_t> get_key() {
        std::array<char, 32> arr = $self->key;
        return std::vector<int8_t>(arr.begin(), arr.end());
    }

    std::vector<int8_t> get_signature() {
        std::array<char, 64> arr = $self->signature;
        return std::vector<int8_t>(arr.begin(), arr.end());
    }

    int64_t get_seq() {
        return int64_t($self->seq);
    }

    std::vector<int8_t> get_salt() {
        std::string s = $self->salt;
        return std::vector<int8_t>(s.begin(), s.end());
    }
}

%extend dht_put_alert {
    std::vector<int8_t> get_public_key() {
        std::array<char, 32> arr = $self->public_key;
        return std::vector<int8_t>(arr.begin(), arr.end());
    }

    std::vector<int8_t> get_signature() {
        std::array<char, 64> arr = $self->signature;
        return std::vector<int8_t>(arr.begin(), arr.end());
    }

    std::vector<int8_t> get_salt() {
        std::string s = $self->salt;
        return std::vector<int8_t>(s.begin(), s.end());
    }

    int64_t get_seq() {
        return int64_t($self->seq);
    }
}

%extend stats_alert {
    int get_transferred(int index) {
        return $self->transferred[index];
    }
}

%extend session_stats_alert {
    long long get_value(int index) {
        return $self->values[index];
    }
}

%extend save_resume_data_alert {
    entry get_resume_data() {
        return *($self->resume_data);
    }
}

%extend dht_pkt_alert {
    std::vector<int8_t> get_pkt_buf() {
        return std::vector<int8_t>($self->pkt_buf(), $self->pkt_buf() + $self->pkt_size());
    }
}

%extend read_piece_alert {
    int64_t buffer_ptr() {
        return reinterpret_cast<int64_t>($self->buffer.get());
    }
}

%extend peer_connection_handle {
    int64_t get_time_of_last_unchoke() {
        return total_milliseconds($self->time_of_last_unchoke() - clock_type::now());
    }
};

%extend peer_info {
    int64_t get_last_request() {
        return total_milliseconds($self->last_request);
    }

    int64_t get_last_active() {
        return total_milliseconds($self->last_active);
    }

    int64_t get_download_queue_time() {
        return total_milliseconds($self->download_queue_time);
    }
}

%extend torrent_status {
    int64_t get_next_announce() {
        return total_milliseconds($self->next_announce);
    }
}

%extend create_torrent {
    void set_root_cert_bytes(std::vector<int8_t> const& pem) {
        $self->set_root_cert(std::string(pem.begin(), pem.end()));
    }
}

%extend partial_piece_info {
    std::vector<libtorrent::block_info> get_blocks() {
        return std::vector<libtorrent::block_info>($self->blocks, $self->blocks + $self->blocks_in_piece);
    }
}

%extend stats_metric {
    std::string get_name() {
        return std::string($self->name);
    }
}

%extend storage_moved_failed_alert {
    std::string get_operation() {
        return std::string($self->operation);
    }
}

%extend file_error_alert {
    std::string get_operation() {
        return std::string($self->operation);
    }
}

%extend fastresume_rejected_alert {
    std::string get_operation() {
        return std::string($self->operation);
    }
}

%extend peer_log_alert {
    std::string get_event_type() {
        return std::string($self->event_type);
    }
}

%extend dht_lookup {
    std::string get_type() {
        return std::string($self->type);
    }
}

class stat {
public:
    enum
    {
        upload_payload,
        upload_protocol,
        download_payload,
        download_protocol,
        upload_ip_protocol,
        download_ip_protocol,
        num_channels
    };
};

bool is_utp_stream_logging();
void set_utp_stream_logging(bool enable);

}

%feature("director") add_files_listener;
%feature("director") set_piece_hashes_listener;

%ignore swig_storage::set_file_priority;
%ignore swig_storage::readv;
%ignore swig_storage::writev;
%ignore swig_storage_constructor;

%feature("director") swig_storage;

%typemap("javapackage") SwigStorage, SwigStorage *, SwigStorage & "com.frostwire.jlibtorrent.plugins";

// DHT storage
%ignore swig_dht_storage::put_immutable_item(libtorrent::sha1_hash const&, char const*, int, libtorrent::address const&);
%ignore swig_dht_storage::get_mutable_item_seq;
%ignore swig_dht_storage::put_mutable_item(libtorrent::sha1_hash const&, char const*, int, char const*, boost::int64_t, char const*, char const*, int, libtorrent::address const&);
%ignore swig_dht_storage::counters;
%ignore swig_dht_storage_constructor;

%feature("director") swig_dht_storage;

// libtorrent plugins
%feature("director") swig_plugin;
%feature("director") swig_torrent_plugin;
%feature("director") swig_peer_plugin;

%typemap("javapackage") SwigPlugin, SwigPlugin *, SwigPlugin & "com.frostwire.jlibtorrent.plugins";
%typemap("javapackage") SwigDhtPlugin, SwigDhtPlugin *, SwigDhtPlugin & "com.frostwire.jlibtorrent.plugins";

%ignore swig_plugin::new_torrent(libtorrent::torrent_handle const&, void*);
%ignore swig_plugin::register_dht_extensions(libtorrent::dht_extensions_t& dht_extensions);

%ignore swig_torrent_plugin::new_connection;

%ignore swig_peer_plugin::on_extended;
%ignore swig_peer_plugin::on_unknown_message;

%ignore dht_put_item_cb;

%feature("director") posix_wrapper;
%ignore g_posix_wrapper;

%include "libtorrent.h"
