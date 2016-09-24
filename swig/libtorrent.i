%module (jniclassname="libtorrent_jni", directors="1") libtorrent

// suppress Warning 317: Specialization of non-template '<name>'.
#pragma SWIG nowarn=317
// suppress Warning 341: The 'using' keyword in type aliasing is not fully supported yet.
#pragma SWIG nowarn=341
// suppress Warning 401: Nothing known about base class '<name>'. Ignored.
#pragma SWIG nowarn=401
// supress Warning 402: Base class '<name>' is incomplete.
#pragma SWIG nowarn=402

%{
// BEGIN common set include ------------------------------------------------------

#include <boost/system/error_code.hpp>

#include "libtorrent/version.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/bitfield.hpp"
#include "libtorrent/peer_request.hpp"
#include "libtorrent/entry.hpp"
#include "libtorrent/file_storage.hpp"
#include "libtorrent/torrent_info.hpp"
#include "libtorrent/torrent_handle.hpp"
#include "libtorrent/add_torrent_params.hpp"
#include "libtorrent/operations.hpp"
#include "libtorrent/session_stats.hpp"
#include "libtorrent/close_reason.hpp"
#include "libtorrent/alert.hpp"
#include "libtorrent/alert_types.hpp"
#include "libtorrent/peer_info.hpp"
#include "libtorrent/session_settings.hpp"
#include "libtorrent/settings_pack.hpp"
#include "libtorrent/peer_class.hpp"
#include "libtorrent/peer_class_type_filter.hpp"
#include "libtorrent/session_handle.hpp"
#include "libtorrent/kademlia/dht_state.hpp"
#include "libtorrent/session.hpp"
#include "libtorrent/peer_connection_handle.hpp"
#include "libtorrent/ip_filter.hpp"
#include "libtorrent/bdecode.hpp"
#include "libtorrent/bencode.hpp"
#include "libtorrent/magnet_uri.hpp"
#include "libtorrent/create_torrent.hpp"
#include "libtorrent/announce_entry.hpp"
#include "libtorrent/torrent_status.hpp"

#include "libtorrent.h"

// END common set include ------------------------------------------------------
%}

#ifdef SWIGJAVA

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

    public static final native long directBufferAddress(java.nio.Buffer buffer);
    public static final native long directBufferCapacity(java.nio.Buffer buffer);
%}

%exception {
    try {
        $action
    } catch (std::exception& e) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }
}

%{
#ifdef __cplusplus
extern "C" {
#endif

SWIGEXPORT jlong JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_directBufferAddress(JNIEnv *jenv, jclass jcls, jobject jbuf) {
    try {
        return reinterpret_cast<jlong>(jenv->GetDirectBufferAddress(jbuf));
    } catch (std::exception& e) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }

    return 0;
}

SWIGEXPORT jlong JNICALL Java_com_frostwire_jlibtorrent_swig_libtorrent_1jni_directBufferCapacity(JNIEnv *jenv, jclass jcls, jobject jbuf) {
    try {
        return reinterpret_cast<jlong>(jenv->GetDirectBufferCapacity(jbuf));
    } catch (std::exception& e) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, e.what());
    } catch (...) {
      SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
    }

    return 0;
}

#ifdef __cplusplus
}
#endif
%}

#endif // SWIGJAVA

%include <stdint.i>
%include <typemaps.i>
%include <std_common.i>
%include <std_string.i>
%include <std_pair.i>

%apply int8_t { char };
%apply int64_t { void* };

namespace std {

    typedef int8_t uint8_t;
    typedef int64_t uint64_t;

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
    %template(string_view_bdecode_node_pair) pair<libtorrent::string_view, libtorrent::bdecode_node>;
    %template(byte_vectors_pair) pair<vector<int8_t>, vector<int8_t>>;

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
    %template(tcp_endpoint_vector) vector<libtorrent::tcp::endpoint>;
    %template(udp_endpoint_vector) vector<libtorrent::udp::endpoint>;

    %template(int_string_map) map<int, std::string>;
    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;
    %template(int_bitfield_map) map<int, libtorrent::bitfield>;

    %template(alert_ptr_vector) vector<libtorrent::alert*>;
};

namespace libtorrent {

    typedef sha1_hash peer_id;

    namespace file {
        struct iovec_t {
            void* iov_base;
            std::int64_t iov_len;
        };
    };

    namespace dht {
        typedef sha1_hash node_id;
    }

    enum storage_mode_t {
        storage_mode_allocate,
        storage_mode_sparse
    };

    enum class connection_type : std::uint8_t {
        bittorrent,
        url_seed,
        http_seed
    };

    template <typename T>
    struct span {

        span();

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
    struct span<char> {

        span();

        size_t size() const;
        bool empty() const;

        int8_t front() const;
        int8_t back() const;

        span<char> first(size_t const n) const;
        span<char> last(size_t const n) const;

        span<char> subspan(size_t const offset) const;
        span<char> subspan(size_t const offset, size_t const count) const;

        %extend {
            int8_t get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    template <>
    struct span<char const> {

        span();

        size_t size() const;
        bool empty() const;

        int8_t front() const;
        int8_t back() const;

        span<char const> first(size_t const n) const;
        span<char const> last(size_t const n) const;

        span<char const> subspan(size_t const offset) const;
        span<char const> subspan(size_t const offset, size_t const count) const;

        %extend {
            int8_t get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    %template(byte_span) span<char>;
    %template(byte_const_span) span<char const>;
    %template(iovec_span) span<file::iovec_t const>;

    class sha1_hash {
    public:

        static size_t size();

        sha1_hash();
        sha1_hash(sha1_hash const&);

        static sha1_hash max();
        static sha1_hash min();

        void clear();
        bool is_all_zeros();
        int count_leading_zeroes();

        %extend {

            sha1_hash(std::vector<int8_t> const& s) {
                return new libtorrent::sha1_hash({reinterpret_cast<char const*>(s.data()), s.size()});
            }

            void assign(std::vector<int8_t> const& s) {
                $self->assign({reinterpret_cast<char const*>(s.data()), s.size()});
            }

            int8_t get(size_t const idx) {
                return (int8_t)(*self)[idx];
            }

            int hash_code() {
                char const* data = $self->data();
                int result = 1;
                for (int i = 0; i < int($self->size()); i++) {
                    result = 31 * result + data[i];
                }
                return result;
            }

            std::vector<int8_t> to_bytes() {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }

            std::string to_hex() {
                return libtorrent::aux::to_hex(*$self);
            }

            bool op_eq(sha1_hash const& n) const {
                return *$self == n;
            }

            bool op_ne(sha1_hash const& n) const {
                return *$self != n;
            }

            bool op_lt(sha1_hash const& n) const {
                return *$self < n;
            }

            static int compare(sha1_hash const& h1, sha1_hash const& h2) const {
                return h1 == h2 ? 0 : (h1 < h2 ? -1 : 1);
            }
        }
    };

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

    class string_view {
    public:
        string_view(std::string s);
        std::string to_string();
    };

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
};

typedef long time_t;

%ignore libtorrent::TORRENT_CFG;
%ignore libtorrent::detail;
%ignore libtorrent::parse_int;
%ignore libtorrent::bdecode;
%ignore libtorrent::set_piece_hashes(create_torrent&, std::string const&, std::function<void(int)> const&, error_code&);
%ignore libtorrent::hash_value;
%ignore libtorrent::internal_file_entry;
%ignore libtorrent::print_entry;
%ignore libtorrent::peer_class;
%ignore libtorrent::peer_class_pool;
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>, std::uint32_t);
%ignore libtorrent::add_files(file_storage&, std::string const&, std::function<bool(std::string)>);
%ignore libtorrent::ip_filter::export_filter;
%ignore libtorrent::add_torrent_params::add_torrent_params;
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::storage;
%ignore libtorrent::add_torrent_params::userdata;
%ignore libtorrent::add_torrent_params::ti;
%ignore libtorrent::add_torrent_params::deprecated1;
%ignore libtorrent::add_torrent_params::deprecated2;
%ignore libtorrent::add_torrent_params::deprecated3;
%ignore libtorrent::add_torrent_params::deprecated4;
%ignore libtorrent::alert::timestamp;
%ignore libtorrent::session_params::session_params(settings_pack, std::vector<std::shared_ptr<plugin>>);
%ignore libtorrent::session_params::session_params(session_params&&);
%ignore libtorrent::session_params::extensions;
%ignore libtorrent::session_params::dht_storage_constructor;
%ignore libtorrent::session::session(session_params, io_service&, int);
%ignore libtorrent::session::session(session_params, io_service&);
%ignore libtorrent::session::session(settings_pack, io_service&, int);
%ignore libtorrent::session::session(settings_pack, io_service&);
%ignore libtorrent::session_handle::session_handle(aux::session_impl*);
%ignore libtorrent::session_handle::get_torrent_status;
%ignore libtorrent::session_handle::get_io_service;
%ignore libtorrent::session_handle::get_connection_queue;
%ignore libtorrent::session_handle::add_extension(std::function<std::shared_ptr<torrent_plugin>(torrent_handle const&, void*)>);
%ignore libtorrent::session_handle::add_extension(std::shared_ptr<plugin>);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::uint64_t&, std::string const&)>, std::string);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::uint64_t&, std::string const&)>);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>, std::string);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>);
%ignore libtorrent::session_handle::dht_direct_request(udp::endpoint, entry const&, void*);
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
%ignore libtorrent::peer_connection_handle::find_plugin;
%ignore libtorrent::peer_connection_handle::time_of_last_unchoke;
%ignore libtorrent::bt_peer_connection_handle::switch_send_crypto;
%ignore libtorrent::bt_peer_connection_handle::switch_recv_crypto;
%ignore libtorrent::bt_peer_connection_handle::native_handle;
%ignore libtorrent::torrent_handle::torrent_handle;
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
%ignore libtorrent::torrent_handle::set_metadata;
%ignore libtorrent::torrent_handle::set_ssl_certificate;
%ignore libtorrent::torrent_handle::set_ssl_certificate_buffer;
%ignore libtorrent::block_info::set_peer;
%ignore libtorrent::partial_piece_info::blocks;
%ignore libtorrent::entry::entry(entry&&);
%ignore libtorrent::entry::entry(preformatted_type);
%ignore libtorrent::entry::integer();
%ignore libtorrent::entry::string();
%ignore libtorrent::entry::dict() const;
%ignore libtorrent::entry::list() const;
%ignore libtorrent::entry::preformatted;
%ignore libtorrent::entry::find_key(string_view) const;
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
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&, int);
%ignore libtorrent::torrent_info::metadata;
%ignore libtorrent::torrent_info::load;
%ignore libtorrent::torrent_info::unload;
%ignore libtorrent::torrent_info::hash_for_piece_ptr;
%ignore libtorrent::torrent_info::parse_info_section;
%ignore libtorrent::torrent_info::swap;
%ignore libtorrent::torrent_info::add_merkle_nodes;
%ignore libtorrent::torrent_info::build_merkle_list;
%ignore libtorrent::torrent_info::ssl_cert;
%ignore libtorrent::torrent_info::parse_torrent_file;
%ignore libtorrent::sanitize_append_path_element;
%ignore libtorrent::verify_encoding;
%ignore libtorrent::read_piece_alert::read_piece_alert;
%ignore libtorrent::read_piece_alert::buffer;
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
%ignore libtorrent::apply_pack;
%ignore libtorrent::load_pack_from_dict;
%ignore libtorrent::save_settings_to_dict;
%ignore libtorrent::error_code;
%ignore libtorrent::settings_pack::settings_pack(settings_pack&&);
%ignore libtorrent::settings_pack::deprecated;
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
%ignore libtorrent::settings_pack::deprecated16;
%ignore libtorrent::settings_pack::deprecated17;
%ignore libtorrent::settings_pack::deprecated18;
%ignore libtorrent::storage_params::pool;
%ignore libtorrent::storage_params::priorities;
%ignore libtorrent::ipv6_peer::addr;
%ignore libtorrent::announce_entry::failed;
%ignore libtorrent::announce_entry::next_announce;
%ignore libtorrent::announce_entry::min_announce;
%ignore libtorrent::announce_entry::can_announce;
%ignore libtorrent::announce_entry::deprecated_send_stats;
%ignore libtorrent::proxy_settings::proxy_settings;
%ignore libtorrent::torrent_status::_dummy_string_;
%ignore libtorrent::torrent_status::torrent_file;
%ignore libtorrent::torrent_status::next_announce;
%ignore libtorrent::torrent_status::deprecated_announce_interval_;
%ignore libtorrent::torrent_status::deprecated_priority;
%ignore libtorrent::torrent_status::unused_enum_for_backwards_compatibility;
%ignore libtorrent::file_storage::file_storage(file_storage&&);
%ignore libtorrent::file_storage::file_path_hash;
%ignore libtorrent::file_storage::all_path_hashes;
%ignore libtorrent::file_storage::file_name_ptr;
%ignore libtorrent::file_storage::file_name_len;
%ignore libtorrent::file_storage::apply_pointer_offset;
%ignore libtorrent::bitfield::bitfield(bitfield const&);
%ignore libtorrent::bitfield::data();
%ignore libtorrent::bitfield::const_iterator;
%ignore libtorrent::bitfield::begin;
%ignore libtorrent::bitfield::end;
%ignore libtorrent::bitfield::swap;
%ignore libtorrent::peer_info::last_request;
%ignore libtorrent::peer_info::last_active;
%ignore libtorrent::peer_info::download_queue_time;
%ignore libtorrent::peer_info::deprecated__;
%ignore libtorrent::peer_info::deprecated_remote_dl_rate;
%ignore libtorrent::create_torrent::set_root_cert;
%ignore libtorrent::stats_metric::name;
%ignore libtorrent::storage_moved_failed_alert::operation;
%ignore libtorrent::file_error_alert::operation;
%ignore libtorrent::fastresume_rejected_alert::operation;
%ignore libtorrent::peer_log_alert::event_type;
%ignore libtorrent::dht_lookup::type;
%ignore libtorrent::error_to_close_reason;
%ignore libtorrent::storage_error;
%ignore libtorrent::user_alert_id;

%ignore boost::throws;
%ignore boost::detail::throws;
%ignore boost::system::generic_category;
%ignore boost::system::system_category;
%ignore boost::system::error_code::unspecified_bool_true;
%ignore boost::system::hash_value;
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
%rename(bdecode_no_error) libtorrent::bdecode_errors::no_error;
%rename(bdecode_errors) libtorrent::bdecode_errors::error_code_enum;

%rename("$ignore", regextarget=1, %$isconstructor) ".*_alert$";

%ignore dht_put_item_cb;

%feature("director") alert_notify_callback;
%feature("director") swig_plugin;
%feature("director") posix_wrapper;

%ignore swig_plugin::implemented_features;

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

%include "libtorrent/version.hpp"
%include "libtorrent/error_code.hpp"
%include "libtorrent/bitfield.hpp"
%include "libtorrent/peer_request.hpp"
%include "libtorrent/entry.hpp"
%include "libtorrent/file_storage.hpp"
%include "libtorrent/torrent_info.hpp"
%include "libtorrent/torrent_handle.hpp"
%include "libtorrent/add_torrent_params.hpp"
%include "libtorrent/operations.hpp"
%include "libtorrent/session_stats.hpp"
%include "libtorrent/close_reason.hpp"
%include "libtorrent/alert.hpp"
%include "libtorrent/alert_types.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/session_settings.hpp"
%include "libtorrent/settings_pack.hpp"
%include "libtorrent/peer_class.hpp"
%include "libtorrent/peer_class_type_filter.hpp"
%include "libtorrent/session_handle.hpp"
%include "libtorrent/kademlia/dht_state.hpp"
%include "libtorrent/session.hpp"
%include "libtorrent/peer_connection_handle.hpp"
%include "libtorrent/ip_filter.hpp"
%include "libtorrent/bdecode.hpp"
%include "libtorrent/bencode.hpp"
%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%include "libtorrent/announce_entry.hpp"
%include "libtorrent/torrent_status.hpp"

%include "libtorrent.h"

// END common set include ------------------------------------------------------

namespace libtorrent {
    
// alert types conversion due to lack of polymorphic return type
%extend alert {
#define CAST_ALERT_METHOD(name) \
    static libtorrent::##name const* cast_to_##name(alert const* a) { \
        return libtorrent::alert_cast<libtorrent::##name>(a); \
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
        return libtorrent::total_milliseconds($self->timestamp().time_since_epoch());
    }
}

%extend session_handle {

    void dht_get_item(std::vector<int8_t>& key, std::vector<int8_t>& salt) {
        if (key.size() != 32) {
            throw std::invalid_argument("Public key must be of size 32");
        }
        std::array<char, 32> pk;
        std::copy_n(key.begin(), 32, pk.begin());

        $self->dht_get_item(pk, std::string(salt.begin(), salt.end()));
    }

    void dht_put_item(std::vector<int8_t>& key, std::vector<int8_t>& sk, entry& data, std::vector<int8_t>& salt) {
        if (key.size() != 32) {
            throw std::invalid_argument("Public key must be of size 32");
        }
        if (sk.size() != 64) {
            throw std::invalid_argument("Private key must be of size 64");
        }
        std::array<char, 32> pk;
        std::copy_n(key.begin(), 32, pk.begin());

        using namespace std::placeholders;
        using namespace libtorrent::dht;

        $self->dht_put_item(pk, std::bind(&dht_put_item_cb, _1, _2, _3, _4,
            public_key((char*)key.data()), secret_key((char*)sk.data()), data),
            std::string(salt.begin(), salt.end()));
    }

    void dht_direct_request(udp::endpoint ep, entry const& e, int64_t userdata) {
        $self->dht_direct_request(ep, e, (void*)userdata);
    }

    alert* wait_for_alert_ms(int64_t max_wait) {
        return $self->wait_for_alert(libtorrent::milliseconds(max_wait));
    }

    void set_alert_notify_callback(alert_notify_callback* cb) {
        $self->set_alert_notify(std::bind(&alert_notify_callback::on_alert, cb));
    }

    void add_extension(swig_plugin* ext) {
        $self->add_extension(std::shared_ptr<libtorrent::plugin>(ext));
    }
}

%extend entry {

    entry(std::string const& s) {
        return new libtorrent::entry(s);
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
        return libtorrent::entry(std::string(string_bytes.begin(), string_bytes.end()));
    }

    static entry from_preformatted_bytes(std::vector<int8_t> const& preformatted_bytes) {
        return libtorrent::entry(std::vector<char>(preformatted_bytes.begin(), preformatted_bytes.end()));
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

    libtorrent::torrent_info const* ti_ptr() {
        return $self->ti.get();
    }

    void set_ti(libtorrent::torrent_info const& ti) {
        $self->ti = std::make_shared<libtorrent::torrent_info>(ti);
    }

    static libtorrent::add_torrent_params create_instance() {
        return libtorrent::add_torrent_params();
    }

    static libtorrent::add_torrent_params create_instance_disabled_storage() {
        return libtorrent::add_torrent_params(libtorrent::disabled_storage_constructor);
    }

    static libtorrent::add_torrent_params create_instance_zero_storage() {
        return libtorrent::add_torrent_params(libtorrent::zero_storage_constructor);
    }

    static libtorrent::add_torrent_params read_resume_data(libtorrent::bdecode_node const& rd, error_code& ec) {
        return libtorrent::read_resume_data(rd, ec);
    }

    static libtorrent::add_torrent_params read_resume_data(std::vector<int8_t> const& buffer, error_code& ec) {
        return libtorrent::read_resume_data((char const*)&buffer[0], buffer.size(), ec);
    }
}

%extend torrent_info {

    torrent_info(int64_t buffer_ptr, int size, error_code& ec, int flags = 0) {
        return new libtorrent::torrent_info(reinterpret_cast<char const*>(buffer_ptr), size, ec, flags);
    }
};

%extend torrent_handle {

    void add_piece_bytes(int piece, std::vector<int8_t> const& data, int flags = 0) {
        $self->add_piece(piece, (char const*)&data[0], flags);
    }

    libtorrent::torrent_info const* torrent_file_ptr() {
        return $self->torrent_file().get();
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
        return libtorrent::total_milliseconds($self->time_of_last_unchoke() - libtorrent::clock_type::now());
    }
};

%extend peer_info {

    int64_t get_last_request() {
        return libtorrent::total_milliseconds($self->last_request);
    }

    int64_t get_last_active() {
        return libtorrent::total_milliseconds($self->last_active);
    }

    int64_t get_download_queue_time() {
        return libtorrent::total_milliseconds($self->download_queue_time);
    }
}

%extend torrent_status {

    torrent_info const* torrent_file_ptr() {
        return $self->torrent_file.lock().get();
    }

    int64_t get_next_announce() {
        return libtorrent::total_milliseconds($self->next_announce);
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

%extend dht_direct_response_alert {

    int64_t get_userdata() {
        return (int64_t)$self->userdata;
    }
}

bool is_utp_stream_logging();
void set_utp_stream_logging(bool enable);

}

%feature("director") add_files_listener;
%feature("director") set_piece_hashes_listener;
