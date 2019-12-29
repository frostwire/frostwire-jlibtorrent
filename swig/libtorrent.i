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

#include "libtorrent/version.hpp"
#include "libtorrent/error_code.hpp"
#include "libtorrent/peer_request.hpp"
#include "libtorrent/file_storage.hpp"
#include "libtorrent/bdecode.hpp"
#include "libtorrent/bencode.hpp"
#include "libtorrent/peer_info.hpp"
#include "libtorrent/torrent_flags.hpp"
#include "libtorrent/torrent_info.hpp"
#include "libtorrent/pex_flags.hpp"
#include "libtorrent/torrent_status.hpp"
#include "libtorrent/torrent_handle.hpp"
#include "libtorrent/add_torrent_params.hpp"
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
#include "libtorrent/session_handle.hpp"
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

#ifdef SWIGJAVA

%pragma(java) jniclasscode=%{

    public static String jlibtorrentVersion() {
        // extracted from the gradle with the run-swig step
        return "$JLIBTORRENT_VERSION$";
    }

    static {
        try {
            String path = System.getProperty("jlibtorrent.jni.path", "");
            if ("".equals(path)) {
                try {
                    System.loadLibrary("jlibtorrent-" + jlibtorrentVersion());
                } catch (LinkageError e) {
                    // give it a try to the name without version
                    System.loadLibrary("jlibtorrent");
                }
            } else {
                System.load(path);
            }
        } catch (LinkageError e) {
            throw new LinkageError(
                "Look for your architecture binary instructions at: https://github.com/frostwire/frostwire-jlibtorrent", e);
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
        return $null;
    } catch (...) {
        SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unknown exception type");
        return $null;
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

%apply std::int8_t { char };
%apply std::int64_t { void* };
%apply std::int64_t { std::time_t };

%define TYPE_INTEGRAL_CONVERSION_EX(name, underlying_type, api_type, java_type)
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

namespace std {

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

    template <std::size_t N>
    class bitset
    {
    public:
        bool test(std::size_t pos) const;

        bool all() const;
        bool any() const;
        bool none() const;

        std::size_t count() const;
        std::size_t size() const;

        %extend
        {
            bool get(std::size_t pos) {
                return (*self)[pos];
            }
        }
    };

    %template(piece_index_int_pair) pair<piece_index_t, int>;
    %template(string_int_pair) pair<std::string, int>;
    %template(string_string_pair) pair<std::string, std::string>;
    %template(string_view_bdecode_node_pair) pair<libtorrent::string_view, libtorrent::bdecode_node>;
    %template(byte_vectors_pair) pair<vector<int8_t>, vector<int8_t>>;
    %template(sha1_hash_udp_endpoint_pair) pair<libtorrent::sha1_hash, libtorrent::udp::endpoint>;
    %template(address_sha1_hash_pair) pair<libtorrent::address, libtorrent::sha1_hash>;

    %template(byte_vector) vector<int8_t>;
    %template(string_vector) vector<std::string>;
    %template(string_int_pair_vector) vector<std::pair<std::string, int>>;
    %template(string_string_pair_vector) vector<std::pair<std::string, std::string>>;
    %template(piece_index_int_pair_vector) vector<std::pair<piece_index_t, int>>;

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
    %template(announce_endpoint_vector) vector<libtorrent::announce_endpoint>;
    %template(announce_entry_vector) vector<libtorrent::announce_entry>;
    %template(tcp_endpoint_vector) vector<libtorrent::tcp::endpoint>;
    %template(udp_endpoint_vector) vector<libtorrent::udp::endpoint>;
    %template(piece_index_vector) vector<piece_index_t>;
    %template(file_index_vector) vector<file_index_t>;
    %template(sha1_hash_udp_endpoint_pair_vector) vector<pair<libtorrent::sha1_hash, libtorrent::udp::endpoint>>;
    %template(address_sha1_hash_pair_vector) vector<pair<libtorrent::address, libtorrent::sha1_hash>>;

    %template(file_index_string_map) map<file_index_t, std::string>;
    %template(string_long_map) map<std::string, long>;
    %template(string_entry_map) map<std::string, libtorrent::entry>;

    %template(alert_ptr_vector) vector<libtorrent::alert*>;

    %template(ip_interface_vector) vector<ip_interface>;
    %template(ip_route_vector) vector<ip_route>;
    %template(port_mapping_t_vector) vector<port_mapping_t>;

    %template(bitset_96) bitset<96>;
};

namespace libtorrent {

    namespace flags
    {
        template<typename UnderlyingType, typename Tag>
        struct bitfield_flag
        {
            static bitfield_flag all();

            %extend
            {
                bool nonZero()
                {
                    return $self->operator bool();
                }

                bool eq(bitfield_flag const f)
                {
                    return $self->operator==(f);
                }

                bool ne(bitfield_flag const f)
                {
                    return $self->operator!=(f);
                }

                bitfield_flag or_(bitfield_flag const other)
                {
                    return *$self | other;
                }

                bitfield_flag and_(bitfield_flag const other)
                {
                    return *$self & other;
                }

                bitfield_flag xor(bitfield_flag const other)
                {
                    return *$self ^ other;
                }

                bitfield_flag inv()
                {
                    return $self->operator~();
                }

                int to_int()
                {
                    return static_cast<int>(static_cast<UnderlyingType>(*$self));
                }
            }
        };
    }

    struct alert_category_tag;
    %template(alert_category_t) flags::bitfield_flag<std::uint32_t, alert_category_tag>;
    struct add_piece_flags_tag;
    %template(add_piece_flags_t) flags::bitfield_flag<std::uint8_t, add_piece_flags_tag>;
    struct status_flags_tag;
    %template(status_flags_t) flags::bitfield_flag<std::uint32_t, status_flags_tag>;
    struct deadline_flags_tag;
    %template(deadline_flags_t) flags::bitfield_flag<std::uint8_t, deadline_flags_tag>;
    struct session_flags_tag;
    %template(session_flags_t) flags::bitfield_flag<std::uint8_t, session_flags_tag>;
    struct pause_flags_tag;
    %template(pause_flags_t) flags::bitfield_flag<std::uint8_t, pause_flags_tag>;
    struct resume_data_flags_tag;
    %template(resume_data_flags_t) flags::bitfield_flag<std::uint8_t, resume_data_flags_tag>;
    struct picker_flags_tag;
    %template(picker_flags_t) flags::bitfield_flag<std::uint32_t, picker_flags_tag>;
    struct remove_flags_tag;
    %template(remove_flags_t) flags::bitfield_flag<std::uint8_t, remove_flags_tag>;
    struct save_state_flags_tag;
    %template(save_state_flags_t) flags::bitfield_flag<std::uint32_t, save_state_flags_tag>;
    struct torrent_flags_tag;
    %template(torrent_flags_t) flags::bitfield_flag<std::uint64_t, torrent_flags_tag>;
    struct peer_flags_tag;
    %template(peer_flags_t) flags::bitfield_flag<std::uint32_t, peer_flags_tag>;
    struct peer_source_flags_tag;
    %template(peer_source_flags_t) flags::bitfield_flag<std::uint8_t, peer_source_flags_tag>;
    struct pex_flags_tag;
    %template(pex_flags_t) flags::bitfield_flag<std::uint8_t, pex_flags_tag>;
    struct bandwidth_state_flags_tag;
    %template(bandwidth_state_flags_t) flags::bitfield_flag<std::uint8_t, bandwidth_state_flags_tag>;
    struct file_flags_tag;
    %template(file_flags_t) flags::bitfield_flag<std::uint8_t, file_flags_tag>;
    struct create_flags_tag;
    %template(create_flags_t) flags::bitfield_flag<std::uint32_t, create_flags_tag>;
    struct reopen_network_flags_tag;
    %template(reopen_network_flags_t) flags::bitfield_flag<std::uint8_t, reopen_network_flags_tag>;
    struct reannounce_flags_tag;
    %template(reannounce_flags_t) flags::bitfield_flag<std::uint8_t, reannounce_flags_tag>;

    typedef sha1_hash peer_id;

    namespace dht {
        typedef sha1_hash node_id;
        typedef int announce_flags_t;
    }

    enum storage_mode_t {
        storage_mode_allocate,
        storage_mode_sparse
    };

    enum class move_flags_t : std::uint8_t {
        always_replace_files,
        fail_if_exist,
        dont_replace
    };

    enum class connection_type : std::uint8_t {
        bittorrent,
        url_seed,
        http_seed
    };

    enum class portmap_transport : std::uint8_t
    {
        natpmp, upnp
    };

    enum class portmap_protocol : std::uint8_t
    {
        none, tcp, udp
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
            void set(size_t const idx, int8_t val) {
                (*self)[idx] = val;
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

    class sha1_hash {
    public:

        static size_t size();

        sha1_hash();
        sha1_hash(sha1_hash const& other);

        static sha1_hash max();
        static sha1_hash min();

        void clear();
        bool is_all_zeros();
        int count_leading_zeroes();

        %extend {

            sha1_hash(std::vector<int8_t> const& s) {
                return new libtorrent::sha1_hash({reinterpret_cast<char const*>(s.data()), static_cast<long>(s.size())});
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

            static int compare(sha1_hash const& h1, sha1_hash const& h2) {
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
        %extend {
            std::vector<int8_t> to_bytes() {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }
        }
    };

    class address {
    public:

        address();
        address(address const& other);

        bool is_v4();
        bool is_v6();

        std::string to_string(boost::system::error_code ec);

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

            static address from_string(std::string const& str, boost::system::error_code& ec)
            {
                return boost::asio::ip::make_address(str, ec);
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
            endpoint(endpoint const& other);

            unsigned short port();
            address address();
        };
    }

    namespace udp {

        class endpoint {
        public:
            endpoint();
            endpoint(address address, unsigned short port);
            endpoint(endpoint const& other);

            unsigned short port();
            address address();
        };
    }

    class entry {
    public:
        typedef std::map<std::string, libtorrent::entry> dictionary_type;
        typedef std::string string_type;
        typedef std::vector<libtorrent::entry> list_type;
        typedef std::int64_t integer_type;
        typedef std::vector<char> preformatted_type;

        enum data_type
        {
            int_t,
            string_t,
            list_t,
            dictionary_t,
            undefined_t,
            preformatted_t
        };

        data_type type() const;

        entry(dictionary_type);
        entry(span<char const>);
        entry(string_type);
        entry(list_type);
        entry(integer_type);

        entry(data_type t);

        entry(entry const& e);

        entry();

        const integer_type& integer() const;
        const string_type& string() const;
        list_type& list();
        dictionary_type& dict();

        entry* find_key(string_view key);

        std::string to_string() const;

        %extend {

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
                return libtorrent::bdecode({reinterpret_cast<char const*>(buffer.data()), static_cast<long>(buffer.size())});
            }
        }
    };

    template <typename IndexType>
    struct typed_bitfield {
	    typed_bitfield();
        explicit typed_bitfield(int bits);
        typed_bitfield(int bits, bool val);
        typed_bitfield(typed_bitfield<IndexType> const& rhs);

        bool get_bit(IndexType const index) const;
        void clear_bit(IndexType const index);
        void set_bit(IndexType const index);
        IndexType end_index() const;

        bool all_set() const;
        bool none_set() const;
        int size() const;
        int num_words() const;
        bool empty() const;
        int count() const;
        int find_first_set() const;
        int find_last_clear() const;
        void resize(int bits, bool val);
        void resize(int bits);
        void set_all();
        void clear_all();
        void clear();
	};
    %template(piece_index_bitfield) typed_bitfield<piece_index_t>;

    struct peer_class_info {
        bool ignore_unchoke_slots;
        int connection_limit_factor;
        std::string label;
        int upload_limit;
        int download_limit;
        int upload_priority;
        int download_priority;
    };

    %extend peer_info {

        std::vector<int8_t> get_client() {
            std::string s = $self->client;
            return {s.begin(), s.end()};
        }

        int64_t get_last_request() {
            return libtorrent::total_milliseconds($self->last_request);
        }

        int64_t get_last_active() {
            return libtorrent::total_milliseconds($self->last_active);
        }

        int64_t get_download_queue_time() {
            return libtorrent::total_milliseconds($self->download_queue_time);
        }

        std::int32_t get_flags() {
            return std::int32_t(static_cast<std::uint32_t>($self->flags));
        }

        std::int8_t get_source() {
            return std::int8_t(static_cast<std::uint8_t>($self->source));
        }

        std::int8_t get_read_state() {
            return std::int8_t(static_cast<std::uint8_t>($self->read_state));
        }

        std::int8_t get_write_state() {
            return std::int8_t(static_cast<std::uint8_t>($self->write_state));
        }
    }

    struct announce_endpoint
    {
        libtorrent::error_code last_error;
        tcp::endpoint local_endpoint;

        int scrape_incomplete;
        int scrape_complete;
        int scrape_downloaded;

        std::uint8_t fails;
        bool updating;

        bool is_working() const;

        %extend
        {
            std::vector<int8_t> get_message()
            {
                std::string s = $self->message;
                return {s.begin(), s.end()};
            }

            int64_t get_next_announce()
            {
                return libtorrent::total_milliseconds($self->next_announce.time_since_epoch());
            }

            int64_t get_min_announce()
            {
                return libtorrent::total_milliseconds($self->min_announce.time_since_epoch());
            }
        }
    private:
        announce_endpoint();
    };

    struct announce_entry
    {
        std::vector<announce_endpoint> endpoints;

        std::uint8_t tier;
        std::uint8_t fail_limit;

        enum tracker_source
        {
            source_torrent = 1,
            source_client = 2,
            source_magnet_link = 4,
            source_tex = 8
        };

        std::uint8_t source;
        bool verified;

        %extend
        {
            announce_entry(std::vector<int8_t> const& u)
            {
                return new libtorrent::announce_entry(
                    {reinterpret_cast<char const*>(u.data()), u.size()});
            }

            std::vector<int8_t> get_url()
            {
                std::string s = $self->url;
                return {s.begin(), s.end()};
            }

            void set_url(std::vector<int8_t> const& s)
            {
                $self->url = {s.begin(), s.end()};
            }

            std::vector<int8_t> get_trackerid()
            {
                std::string s = $self->trackerid;
                return {s.begin(), s.end()};
            }

            void set_trackerid(std::vector<int8_t> const& s)
            {
                $self->trackerid = {s.begin(), s.end()};
            }
        }
    };

    // for torrent_status
    struct torrent_handle;
};

%ignore libtorrent::TORRENT_CFG;
%ignore libtorrent::detail;
%ignore libtorrent::aux;
%ignore libtorrent::parse_int;
%ignore libtorrent::bdecode;
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
%ignore libtorrent::add_torrent_params::add_torrent_params;
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::storage;
%ignore libtorrent::add_torrent_params::userdata;
%ignore libtorrent::add_torrent_params::ti;
%ignore libtorrent::add_torrent_params::unfinished_pieces;
%ignore libtorrent::add_torrent_params::renamed_files;
%ignore libtorrent::add_torrent_params::tracker_tiers;
%ignore libtorrent::add_torrent_params::merkle_tree;
%ignore libtorrent::add_torrent_params::banned_peers;
%ignore libtorrent::add_torrent_params::peers;
%ignore libtorrent::add_torrent_params::file_priorities;
%ignore libtorrent::add_torrent_params::dht_nodes;
%ignore libtorrent::add_torrent_params::http_seeds;
%ignore libtorrent::add_torrent_params::url_seeds;
%ignore libtorrent::add_torrent_params::trackers;
%ignore libtorrent::add_torrent_params::piece_priorities;
%ignore libtorrent::add_torrent_params::deprecated1;
%ignore libtorrent::add_torrent_params::deprecated2;
%ignore libtorrent::add_torrent_params::deprecated3;
%ignore libtorrent::add_torrent_params::deprecated4;
%ignore libtorrent::add_torrent_params::deprecated5;
%ignore libtorrent::alert::timestamp;
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
%ignore libtorrent::session_handle::session_handle(aux::session_impl*);
%ignore libtorrent::session_handle::session_handle(session_handle&&);
%ignore libtorrent::session_handle::get_torrent_status;
%ignore libtorrent::session_handle::get_io_service;
%ignore libtorrent::session_handle::get_connection_queue;
%ignore libtorrent::session_handle::add_extension(std::function<std::shared_ptr<torrent_plugin>(torrent_handle const&, void*)>);
%ignore libtorrent::session_handle::add_extension(std::shared_ptr<plugin>);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::int64_t&, std::string const&)>, std::string);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::int64_t&, std::string const&)>);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>, std::string);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>);
%ignore libtorrent::session_handle::dht_direct_request(udp::endpoint const&, entry const&, void*);
%ignore libtorrent::session_handle::set_load_function;
%ignore libtorrent::session_handle::set_alert_notify;
%ignore libtorrent::session_handle::native_handle;
%ignore libtorrent::session_handle::set_dht_storage;
%ignore libtorrent::session_handle::get_cache_info;
%ignore libtorrent::session_handle::wait_for_alert;
%ignore libtorrent::session_handle::add_torrent(add_torrent_params&&, error_code&);
%ignore libtorrent::session_handle::async_add_torrent(add_torrent_params&&);
%ignore libtorrent::session_handle::apply_settings(settings_pack&&);
%ignore libtorrent::session_stats_alert::counters;
%ignore libtorrent::picker_log_alert::blocks;
%ignore libtorrent::peer_connection_handle::peer_connection_handle;
%ignore libtorrent::peer_connection_handle::native_handle;
%ignore libtorrent::peer_connection_handle::add_extension;
%ignore libtorrent::peer_connection_handle::find_plugin;
%ignore libtorrent::peer_connection_handle::time_of_last_unchoke;
%ignore libtorrent::peer_connection_handle::should_log;
%ignore libtorrent::peer_connection_handle::peer_log;
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
%ignore libtorrent::torrent_handle::set_ssl_certificate_buffer;
%ignore libtorrent::torrent_handle::queue_position;
%ignore libtorrent::torrent_handle::queue_position_set;
%ignore libtorrent::torrent_handle::piece_priority;
%ignore libtorrent::torrent_handle::prioritize_pieces;
%ignore libtorrent::torrent_handle::get_piece_priorities;
%ignore libtorrent::torrent_handle::file_priority;
%ignore libtorrent::torrent_handle::prioritize_files;
%ignore libtorrent::torrent_handle::get_file_priorities;
%ignore libtorrent::block_info::set_peer;
%ignore libtorrent::partial_piece_info::blocks;
%ignore libtorrent::partial_piece_info::deprecated_state_t;
%ignore libtorrent::partial_piece_info::deprecated_piece_state;
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
%ignore libtorrent::from_span;
%ignore libtorrent::from_span_t;
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&);
%ignore libtorrent::torrent_info::torrent_info(char const*, int, error_code&, int);
%ignore libtorrent::torrent_info::torrent_info(span<char const>, error_code&, from_span_t);
%ignore libtorrent::torrent_info::metadata;
%ignore libtorrent::torrent_info::load;
%ignore libtorrent::torrent_info::unload;
%ignore libtorrent::torrent_info::hash_for_piece_ptr;
%ignore libtorrent::torrent_info::parse_info_section;
%ignore libtorrent::torrent_info::swap;
%ignore libtorrent::torrent_info::add_merkle_nodes;
%ignore libtorrent::torrent_info::build_merkle_list;
%ignore libtorrent::torrent_info::parse_torrent_file;
%ignore libtorrent::torrent_info::piece_range;
%ignore libtorrent::sanitize_append_path_element;
%ignore libtorrent::verify_encoding;
%ignore libtorrent::read_piece_alert::read_piece_alert;
%ignore libtorrent::read_piece_alert::buffer;
%ignore libtorrent::bdecode_node::bdecode_node(bdecode_node&&);
%ignore libtorrent::bdecode_node::non_owning;
%ignore libtorrent::bdecode_node::data_section;
%ignore libtorrent::bdecode_node::list_string_value_at;
%ignore libtorrent::bdecode_node::dict_find;
%ignore libtorrent::bdecode_node::dict_find_dict;
%ignore libtorrent::bdecode_node::dict_find_list;
%ignore libtorrent::bdecode_node::dict_find_string;
%ignore libtorrent::bdecode_node::dict_find_int;
%ignore libtorrent::bdecode_node::dict_find_string_value;
%ignore libtorrent::bdecode_node::dict_find_int_value;
%ignore libtorrent::bdecode_node::string_value;
%ignore libtorrent::bdecode_node::string_ptr;
%ignore libtorrent::bdecode_node::swap;
%ignore libtorrent::bdecode_node::reserve;
%ignore libtorrent::bdecode_node::switch_underlying_buffer;
%ignore libtorrent::bdecode_node::has_soft_error;
%ignore libtorrent::errors::make_error_code;
%ignore libtorrent::bdecode_errors::make_error_code;
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
%ignore libtorrent::settings_pack::deprecated_dht_upload_rate_limit;
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
%ignore libtorrent::storage_params::pool;
%ignore libtorrent::storage_params::priorities;
%ignore libtorrent::ipv6_peer::addr;
%ignore libtorrent::proxy_settings::proxy_settings;
%ignore libtorrent::torrent_status::torrent_status(torrent_status&&);
%ignore libtorrent::torrent_status::_dummy_string_;
%ignore libtorrent::torrent_status::torrent_file;
%ignore libtorrent::torrent_status::next_announce;
%ignore libtorrent::torrent_status::deprecated_announce_interval_;
%ignore libtorrent::torrent_status::deprecated_priority;
%ignore libtorrent::torrent_status::unused_enum_for_backwards_compatibility;
%ignore libtorrent::torrent_status::deprecated_is_loaded;
%ignore libtorrent::torrent_status::last_upload;
%ignore libtorrent::torrent_status::last_download;
%ignore libtorrent::torrent_status::active_duration;
%ignore libtorrent::torrent_status::finished_duration;
%ignore libtorrent::torrent_status::seeding_duration;
%ignore libtorrent::torrent_status::queue_position;
%ignore libtorrent::torrent_status::deprecated_time_since_upload;
%ignore libtorrent::torrent_status::deprecated_time_since_download;
%ignore libtorrent::torrent_status::deprecated_active_time;
%ignore libtorrent::torrent_status::deprecated_finished_time;
%ignore libtorrent::torrent_status::deprecated_seeding_time;
%ignore libtorrent::torrent_status::deprecated_last_scrape;
%ignore libtorrent::torrent_status::deprecated_ip_filter_applies;
%ignore libtorrent::torrent_status::deprecated_upload_mode;
%ignore libtorrent::torrent_status::deprecated_share_mode;
%ignore libtorrent::torrent_status::deprecated_super_seeding;
%ignore libtorrent::torrent_status::deprecated_paused;
%ignore libtorrent::torrent_status::deprecated_auto_managed;
%ignore libtorrent::torrent_status::deprecated_sequential_download;
%ignore libtorrent::torrent_status::deprecated_seed_mode;
%ignore libtorrent::torrent_status::deprecated_stop_when_ready;
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
%ignore libtorrent::stats_metric::name;
%ignore libtorrent::peer_log_alert::event_type;
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
%ignore libtorrent::peer_info::client;
%ignore libtorrent::peer_info::last_request;
%ignore libtorrent::peer_info::last_active;
%ignore libtorrent::peer_info::download_queue_time;
%ignore libtorrent::peer_info::deprecated_dl_rate;
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
%ignore boost::system::generic_category;
%ignore boost::system::system_category;
%ignore boost::system::error_category;
%ignore boost::system::error_condition;
%ignore boost::system::system_error;
%ignore boost::system::error_code::error_code(int, const error_category&);
%ignore boost::system::error_code::assign;
%ignore boost::system::error_code::category;
%ignore boost::system::error_code::default_error_condition;
%ignore boost::system::error_code::unspecified_bool_true;
%ignore boost::system::error_code::operator std::error_code;
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
%rename(bdecode_no_error) libtorrent::bdecode_errors::no_error;
%rename(bdecode_errors) libtorrent::bdecode_errors::error_code_enum;

%rename("$ignore", regextarget=1, %$isconstructor) ".*_alert$";

%ignore dht_put_item_cb;

%feature("director") add_files_listener;
%feature("director") set_piece_hashes_listener;

%feature("director") alert_notify_callback;
%feature("director") swig_plugin;
%feature("director") posix_wrapper;

%ignore swig_plugin::implemented_features;
%ignore set_piece_hashes_listener::progress_index;

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
%include "libtorrent/bdecode.hpp"
%include "libtorrent/bencode.hpp"
%include "libtorrent/peer_info.hpp"
%include "libtorrent/torrent_flags.hpp"
%include "libtorrent/torrent_info.hpp"
%include "libtorrent/pex_flags.hpp"
%include "libtorrent/torrent_status.hpp"
%include "libtorrent/torrent_handle.hpp"
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
%include "libtorrent/session_handle.hpp"
%include "libtorrent/kademlia/dht_state.hpp"
%include "libtorrent/kademlia/dht_settings.hpp"
%include "libtorrent/session.hpp"
%include "libtorrent/peer_connection_handle.hpp"
%include "libtorrent/magnet_uri.hpp"
%include "libtorrent/create_torrent.hpp"
%include "libtorrent/fingerprint.hpp"

%include "libtorrent.h"

// END common set include ------------------------------------------------------

namespace libtorrent {
    
// alert types conversion due to lack of polymorphic return type
%extend alert {
#define CAST_ALERT_METHOD(name) \
    static libtorrent::##name const* cast_to_##name(alert const* a) { \
        return libtorrent::alert_cast<libtorrent::##name>(a); \
    }

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
    CAST_ALERT_METHOD(lsd_peer_alert)
    CAST_ALERT_METHOD(trackerid_alert)
    CAST_ALERT_METHOD(dht_bootstrap_alert)
    CAST_ALERT_METHOD(torrent_error_alert)
    CAST_ALERT_METHOD(torrent_need_cert_alert)
    CAST_ALERT_METHOD(incoming_connection_alert)
    CAST_ALERT_METHOD(add_torrent_alert)
    CAST_ALERT_METHOD(state_update_alert)
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
    CAST_ALERT_METHOD(session_error_alert)
    CAST_ALERT_METHOD(dht_live_nodes_alert)
    CAST_ALERT_METHOD(session_stats_header_alert)
    CAST_ALERT_METHOD(dht_sample_infohashes_alert)
    CAST_ALERT_METHOD(block_uploaded_alert)
    CAST_ALERT_METHOD(alerts_dropped_alert)
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

    void dht_direct_request(udp::endpoint const& ep, entry const& e, int64_t userdata) {
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

%extend bdecode_node {

    std::string list_string_value_at_s(int i, std::string default_val = "") {
        return $self->list_string_value_at(i, default_val).to_string();
    }

    bdecode_node dict_find_s(std::string key) const {
        return $self->dict_find(key);
    }

    bdecode_node dict_find_dict_s(std::string key) const {
        return $self->dict_find_dict(key);
    }

    bdecode_node dict_find_list_s(std::string key) const {
        return $self->dict_find_list(key);
    }

    bdecode_node dict_find_string_s(std::string key) const {
        return $self->dict_find_string(key);
    }

    bdecode_node dict_find_int_s(std::string key) const {
        return $self->dict_find_int(key);
    }

    std::string dict_find_string_value_s(std::string key, std::string default_value = "") const {
        return $self->dict_find_string_value(key, default_value).to_string();
    }

    std::int64_t dict_find_int_value_s(std::string key, std::int64_t default_val = 0) const {
        return $self->dict_find_int_value(key, default_val);
    }

    std::string string_value_s() const {
        return $self->string_value().to_string();
    }

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

    void set_renamed_files(std::map<file_index_t, std::string> const& renamed_files) {
        $self->renamed_files = renamed_files;
    }

    std::vector<int> get_tracker_tiers() {
        return $self->tracker_tiers;
    }

    void set_tracker_tiers(std::vector<int> const& tracker_tiers) {
        $self->tracker_tiers = tracker_tiers;
    }

    void set_merkle_tree(std::vector<sha1_hash> const& merkle_tree) {
        $self->merkle_tree = merkle_tree;
    }

    std::vector<tcp::endpoint> get_banned_peers() {
        return $self->banned_peers;
    }

    void set_banned_peers(std::vector<tcp::endpoint> const& banned_peers) {
        $self->banned_peers = banned_peers;
    }

    std::vector<tcp::endpoint> get_peers() {
        return $self->peers;
    }

    void set_peers(std::vector<tcp::endpoint> const& peers) {
        $self->peers = peers;
    }

    void set_file_priorities2(std::vector<std::int8_t> const& file_priorities) {
        std::vector<download_priority_t> v(file_priorities.size());
        for (std::size_t i = 0; i < v.size(); i++)
            v[i] = download_priority_t{std::uint8_t(file_priorities[i])};
        $self->file_priorities = v;
    }

    std::vector<std::pair<std::string, int>> get_dht_nodes() {
        return $self->dht_nodes;
    }

    void set_dht_nodes(std::vector<std::pair<std::string, int>> const& dht_nodes) {
        $self->dht_nodes = dht_nodes;
    }

    void set_http_seeds(std::vector<std::string> const& http_seeds) {
        $self->http_seeds = http_seeds;
    }

    std::vector<std::string> get_url_seeds() {
        return $self->url_seeds;
    }

    void set_url_seeds(std::vector<std::string> const& url_seeds) {
        $self->url_seeds = url_seeds;
    }

    std::vector<std::string>  get_trackers() {
        return $self->trackers;
    }

    void set_trackers(std::vector<std::string> const& trackers) {
        $self->trackers = trackers;
    }

    void set_piece_priorities2(std::vector<std::int8_t> const& piece_priorities) {
        std::vector<download_priority_t> v(piece_priorities.size());
        for (std::size_t i = 0; i < v.size(); i++)
            v[i] = download_priority_t{std::uint8_t(piece_priorities[i])};
        $self->piece_priorities = v;
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

    void set_default_storage()
    {
        $self->storage = libtorrent::default_storage_constructor;
    }

    void set_disabled_storage()
    {
        $self->storage = libtorrent::disabled_storage_constructor;
    }

    void set_zero_storage()
    {
        $self->storage = libtorrent::zero_storage_constructor;
    }

    static libtorrent::add_torrent_params read_resume_data(libtorrent::bdecode_node const& rd, error_code& ec) {
        return libtorrent::read_resume_data(rd, ec);
    }

    static libtorrent::add_torrent_params read_resume_data(std::vector<int8_t> const& buffer, error_code& ec) {
        return libtorrent::read_resume_data({(char const*)&buffer[0], static_cast<long>(buffer.size())}, ec);
    }

    static libtorrent::entry write_resume_data(add_torrent_params const& atp) {
        return libtorrent::write_resume_data(atp);
    }

    static std::vector<int8_t> write_resume_data_buf(add_torrent_params const& atp) {
        auto v = libtorrent::write_resume_data_buf(atp);
        return {v.begin(), v.end()};
    }

    static add_torrent_params parse_magnet_uri(std::string const& uri, error_code& ec) {
        return libtorrent::parse_magnet_uri(uri, ec);
    }
}

%extend torrent_info {

    torrent_info(int64_t buffer_ptr, int size, error_code& ec) {
        return new libtorrent::torrent_info(reinterpret_cast<char const*>(buffer_ptr), size, ec);
    }
};

%extend torrent_handle {

    void add_piece_bytes(int piece, std::vector<int8_t> const& data, add_piece_flags_t flags = {}) {
        $self->add_piece(piece_index_t(piece), (char const*)&data[0], flags);
    }

    libtorrent::torrent_info const* torrent_file_ptr() {
        return $self->torrent_file().get();
    }

    std::vector<std::string> get_url_seeds() const {
        std::set<std::string> s = $self->url_seeds();
        return {s.begin(), s.end()};
    }

    std::vector<std::string> get_http_seeds() const {
        std::set<std::string> s = $self->http_seeds();
        return {s.begin(), s.end()};
    }

    void set_ssl_certificate_buffer2(std::vector<int8_t> const& certificate
        , std::vector<int8_t> const& private_key
        , std::vector<int8_t> const& dh_params)
    {
        std::string cert{certificate.begin(), certificate.end()};
        std::string pk{private_key.begin(), private_key.end()};
        std::string dh{dh_params.begin(), dh_params.end()};
        $self->set_ssl_certificate_buffer(cert, pk, dh);
    }

    int queue_position2() const
    {
        return static_cast<int>($self->queue_position());
    }

    void queue_position_set2(int p)
    {
        $self->queue_position_set(queue_position_t{p});
    }

    int piece_priority2(piece_index_t index)
    {
        return int(static_cast<std::uint8_t>($self->piece_priority(index)));
    }

    void piece_priority2(piece_index_t index, int priority)
    {
        $self->piece_priority(index, download_priority_t{std::uint8_t(priority)});
    }

    void prioritize_pieces2(std::vector<int> const& pieces)
    {
        std::vector<download_priority_t> v(pieces.size());
        for (std::size_t i = 0; i < v.size(); i++)
            v[i] = download_priority_t{std::uint8_t(pieces[i])};
        $self->prioritize_pieces(v);
    }

    void prioritize_pieces2(std::vector<std::pair<piece_index_t, int>> const& pieces)
    {
        std::vector<std::pair<piece_index_t, download_priority_t>> v(pieces.size());
        for (std::size_t i = 0; i < v.size(); i++)
            v[i] = std::pair<piece_index_t, download_priority_t>(pieces[i].first, download_priority_t{std::uint8_t(pieces[i].second)});
        $self->prioritize_pieces(v);
    }

    std::vector<int> get_piece_priorities2() const
    {
        std::vector<download_priority_t> v = $self->get_piece_priorities();
        std::vector<int> r(v.size());
        for (std::size_t i = 0; i < v.size(); i++)
            r[i] = int(static_cast<std::uint8_t>(v[i]));
        return r;
    }

    int file_priority2(file_index_t index)
    {
        return int(static_cast<std::uint8_t>($self->file_priority(index)));
    }

    void file_priority2(file_index_t index, int priority)
    {
        $self->file_priority(index, download_priority_t{std::uint8_t(priority)});
    }

    void prioritize_files2(std::vector<int> const& files)
    {
        std::vector<download_priority_t> v(files.size());
        for (std::size_t i = 0; i < v.size(); i++)
            v[i] = download_priority_t{std::uint8_t(files[i])};
        $self->prioritize_files(v);
    }

    std::vector<int> get_file_priorities2() const
    {
        std::vector<download_priority_t> v = $self->get_file_priorities();
        std::vector<int> r(v.size());
        for (std::size_t i = 0; i < v.size(); i++)
            r[i] = int(static_cast<std::uint8_t>(v[i]));
        return r;
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
        return $self->counters()[index];
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

%extend torrent_status {

    torrent_info const* torrent_file_ptr() {
        return $self->torrent_file.lock().get();
    }

    int64_t get_next_announce() {
        return libtorrent::total_milliseconds($self->next_announce);
    }

    int64_t get_last_upload() {
        return libtorrent::total_milliseconds($self->last_upload.time_since_epoch());
    }

    int64_t get_last_download() {
        return libtorrent::total_milliseconds($self->last_download.time_since_epoch());
    }

    int64_t get_active_duration() {
        return libtorrent::total_milliseconds($self->active_duration);
    }

    int64_t get_finished_duration() {
        return libtorrent::total_milliseconds($self->finished_duration);
    }

    int64_t get_seeding_duration() {
        return libtorrent::total_milliseconds($self->seeding_duration);
    }

    int get_queue_position()
    {
        return static_cast<int>($self->queue_position);
    }
}

%extend stats_metric {
    std::string get_name() {
        return std::string($self->name);
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

}
