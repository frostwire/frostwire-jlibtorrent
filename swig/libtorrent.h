#ifndef _WIN32
#include <dlfcn.h>
#endif

#include <boost/version.hpp>
#include <boost/function.hpp>
#include <boost/bind.hpp>

#include <boost/asio/ip/address.hpp>
#include <boost/asio/ip/address_v4.hpp>
#include <boost/asio/ip/address_v6.hpp>

#include <boost/asio/ip/tcp.hpp>
#include <boost/asio/ip/udp.hpp>

#include <libtorrent/buffer.hpp>
#include <libtorrent/utp_stream.hpp>
#include <libtorrent/kademlia/dht_tracker.hpp>
#include <libtorrent/kademlia/node_entry.hpp>
#include <libtorrent/kademlia/node.hpp>
#include <libtorrent/kademlia/get_peers.hpp>
#include <libtorrent/kademlia/item.hpp>

#define LIBTORRENT_REVISION_SHA1 _LIBTORRENT_REVISION_SHA1_
#define JLIBTORRENT_REVISION_SHA1 _JLIBTORRENT_REVISION_SHA1_

boost::chrono::high_resolution_clock::duration to_seconds(long long n) {
    return boost::chrono::seconds(n);
}

boost::chrono::high_resolution_clock::duration to_milliseconds(long long n) {
    return boost::chrono::milliseconds(n);
}

boost::chrono::high_resolution_clock::duration to_microseconds(long long n) {
    return boost::chrono::microseconds(n);
}

boost::chrono::high_resolution_clock::duration to_minutes(long long n) {
    return boost::chrono::minutes(n);
}

boost::chrono::high_resolution_clock::duration to_hours(long long n) {
    return boost::chrono::hours(n);
}

void ed25519_create_seed(std::vector<int8_t>& seed) {
    ed25519_create_seed((unsigned char*)seed.data());
}

void ed25519_create_keypair(std::vector<int8_t>& public_key,
                            std::vector<int8_t>& private_key,
                            std::vector<int8_t>& seed) {
    ed25519_create_keypair((unsigned char*)public_key.data(),
                        (unsigned char*)private_key.data(),
                        (unsigned char*)seed.data());
}

void ed25519_sign(std::vector<int8_t>& signature,
                std::vector<int8_t>& message,
                std::vector<int8_t>& public_key,
                std::vector<int8_t>& private_key) {
    ed25519_sign((unsigned char*)signature.data(),
                (unsigned char*)message.data(),
                message.size(),
                (unsigned char*)public_key.data(),
                (unsigned char*)private_key.data());
}

int ed25519_verify(std::vector<int8_t>& signature,
                std::vector<int8_t>& message,
                std::vector<int8_t>& private_key) {
    return ed25519_verify((unsigned char*)signature.data(),
                        (unsigned char*)message.data(),
                        message.size(),
                        (unsigned char*)private_key.data());
}

void ed25519_add_scalar(std::vector<int8_t>& public_key,
                       std::vector<int8_t>& private_key,
                       std::vector<int8_t>& scalar) {
    ed25519_add_scalar((unsigned char*)public_key.data(),
                    (unsigned char*)private_key.data(),
                    (unsigned char*)scalar.data());
}

void ed25519_key_exchange(std::vector<int8_t>& shared_secret,
                         std::vector<int8_t>& public_key,
                         std::vector<int8_t>& private_key) {
    ed25519_key_exchange((unsigned char*)shared_secret.data(),
                        (unsigned char*)public_key.data(),
                        (unsigned char*)private_key.data());
}

// code copied from item.cpp
enum { dht_item_canonical_length = 1200 };
int dht_item_canonical_string0(std::pair<char const*, int> v, boost::uint64_t seq
    , std::pair<char const*, int> salt, char out[dht_item_canonical_length])
{
    int canonical_length = dht_item_canonical_length;
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

int dht_item_canonical_string(std::vector<int8_t>& v, long seq,
                            const std::string& salt, std::vector<int8_t>& out) {
    return dht_item_canonical_string0(std::pair<char const*, int>((const char *)v.data(), v.size()),
                                seq,
                                std::pair<char const*, int>((const char *)salt.data(), salt.size()),
                                (char *)out.data());
}

libtorrent::sha1_hash dht_item_target_id(std::vector<int8_t>& v) {
    return dht::item_target_id(std::pair<char const*, int>((const char *)v.data(), v.size()));
}

libtorrent::sha1_hash dht_item_target_id(std::vector<int8_t>& salt, std::vector<int8_t>& pk) {
    return dht::item_target_id(std::pair<char const*, int>((const char *)salt.data(), salt.size()), (char *)pk.data());
}

bool dht_verify_mutable_item(std::vector<int8_t>& v, const std::string& salt, long seq,
                            std::vector<int8_t>& pk, std::vector<int8_t>& sig) {
    return dht::verify_mutable_item(std::pair<char const*, int>((const char *)v.data(), v.size()),
                                    std::pair<char const*, int>((const char *)salt.data(), salt.size()),
                                    seq,
                                    (char *)pk.data(),
                                    (char *)sig.data());
}

void dht_sign_mutable_item(std::vector<int8_t>& v, const std::string& salt, long seq,
                        std::vector<int8_t>& pk, std::vector<int8_t>& sk, std::vector<int8_t>& sig) {
    dht::sign_mutable_item(std::pair<char const*, int>((const char *)v.data(), v.size()),
                        std::pair<char const*, int>((const char *)salt.data(), salt.size()),
                        seq,
                        (char *)pk.data(),
                        (char *)sk.data(),
                        (char *)sig.data());
}

bool default_storage_disk_write_access_log() {
    return default_storage::disk_write_access_log();
}

void default_storage_disk_write_access_log(bool enable) {
    return default_storage::disk_write_access_log(enable);
}

class add_files_listener {
public:
    virtual ~add_files_listener() {
    }

    virtual bool pred(std::string const& p) {
        return true;
    }
};

bool add_files_cb(std::string const& p, add_files_listener* listener) {
	return listener->pred(p);
}

void add_files_ex(libtorrent::file_storage& fs, std::string const& file, boost::uint32_t flags, add_files_listener* listener) {
    add_files(fs, file, boost::bind(&add_files_cb, _1, listener), flags);
}

class set_piece_hashes_listener {
public:
    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(int i) {
    }
};

void set_piece_hashes_cb(int i, set_piece_hashes_listener* listener) {
	listener->progress(i);
}

void set_piece_hashes_ex(std::string const& id, libtorrent::create_torrent& t, std::string const& p, libtorrent::error_code& ec, set_piece_hashes_listener* listener) {
    set_piece_hashes(t, p, boost::bind(&set_piece_hashes_cb, _1, listener), ec);
}

int boost_version() {
    return BOOST_VERSION;
}

char* boost_lib_version() {
    return BOOST_LIB_VERSION;
}

class dht_extension_handler_listener {
public:
    virtual ~dht_extension_handler_listener() {
    }

    virtual bool on_message(udp::endpoint const& source,
                            libtorrent::bdecode_node const& request, libtorrent::entry& response) {
        return false;
    }
};

bool dht_extension_handler_cb(udp::endpoint const& source,
                            libtorrent::bdecode_node const& request, libtorrent::entry& response,
                            dht_extension_handler_listener* listener) {
	return listener->on_message(source, request, response);
}

struct swig_storage : storage_interface
{
    virtual ~swig_storage() {
    }

    virtual void initialize(libtorrent::storage_error& ec) {
    }

    int readv(libtorrent::file::iovec_t const* bufs, int num_bufs
        , int piece, int offset, int flags, libtorrent::storage_error& ec) {
        int n = 0;
        for (int i = 0; i < num_bufs; i++) {
            n += read((boost::int64_t)bufs[i].iov_base, bufs[i].iov_len,
                       piece, offset, flags, ec);
            if (ec) {
                return -1;
            }

            offset += bufs[i].iov_len;
        }
        return n;
    }

    virtual int read(boost::int64_t iov_base, size_t iov_len
        , int piece, int offset, int flags, libtorrent::storage_error& ec) {
        return 0;
    }

    int writev(libtorrent::file::iovec_t const* bufs, int num_bufs
        , int piece, int offset, int flags, libtorrent::storage_error& ec) {
        int n = 0;
        for (int i = 0; i < num_bufs; i++) {
            n += write((boost::int64_t)bufs[i].iov_base, bufs[i].iov_len,
                        piece, offset, flags, ec);
            if (ec) {
                return -1;
            }

            offset += bufs[i].iov_len;
        }
        return n;
    }

    virtual int write(boost::int64_t iov_base, size_t iov_len
        , int piece, int offset, int flags, libtorrent::storage_error& ec) {
        return 0;
    }

    virtual bool has_any_file(libtorrent::storage_error& ec) {
        return false;
    }

    virtual void set_file_priority(std::vector<boost::uint8_t> const& prio
        , libtorrent::storage_error& ec) {
    }

    virtual int move_storage(std::string const& save_path, int flags
        , libtorrent::storage_error& ec) {
        return 0;
    }

    virtual bool verify_resume_data(libtorrent::bdecode_node const& rd
        , std::vector<std::string> const* links
        , libtorrent::storage_error& ec) {
        return false;
    }

    virtual void write_resume_data(libtorrent::entry& rd, libtorrent::storage_error& ec) const {
    }

    virtual void release_files(libtorrent::storage_error& ec) {
    }

    virtual void rename_file(int index, std::string const& new_filename
        , libtorrent::storage_error& ec) {
    }

    virtual void delete_files(libtorrent::storage_error& ec) {
    }

    virtual bool tick() { return false; }
};

class swig_storage_constructor {
public:
    virtual ~swig_storage_constructor() {
    }

    virtual swig_storage* create(libtorrent::storage_params const& params) {
        return NULL;
    }
};

storage_interface* swig_storage_constructor_cb(libtorrent::storage_params const& params, swig_storage_constructor* sc) {
	return sc->create(params);
}

//------ DHT storage extension -----------------------------

struct swig_dht_storage : dht::dht_storage_interface
{
	virtual bool get_peers(libtorrent::sha1_hash const& info_hash
		, bool noseed, bool scrape
		, libtorrent::entry& peers) const {
		return false;
	}

	virtual void announce_peer(libtorrent::sha1_hash const& info_hash
		, tcp::endpoint const& endp
		, std::string const& name, bool seed) {
	}

	virtual bool get_immutable_item(libtorrent::sha1_hash const& target
		, libtorrent::entry& item) const {
		return false;
	}

	void put_immutable_item(libtorrent::sha1_hash const& target
		, char const* buf, int size
		, libtorrent::address const& addr) {
		std::vector<int8_t> buf_v(buf, buf + size);
		put_immutable_item(target, buf_v, addr);
	}

	virtual void put_immutable_item(libtorrent::sha1_hash const& target
    	, std::vector<int8_t> const& buf
    	, libtorrent::address const& addr) {
    }

	bool get_mutable_item_seq(libtorrent::sha1_hash const& target
		, boost::int64_t& seq) const {
		seq = get_mutable_item_seq_num(target);
		return seq >= 0;
	}

	virtual boost::int64_t get_mutable_item_seq_num(libtorrent::sha1_hash const& target) const {
    	return -1;
    }

	virtual bool get_mutable_item(libtorrent::sha1_hash const& target
		, boost::int64_t seq, bool force_fill
		, libtorrent::entry& item) const {
		return false;
	}

	void put_mutable_item(libtorrent::sha1_hash const& target
		, char const* buf, int size
		, char const* sig
		, boost::int64_t seq
		, char const* pk
		, char const* salt, int salt_size
		, libtorrent::address const& addr) {
		std::vector<int8_t> buf_v(buf, buf + size);
		std::vector<int8_t> sig_v(sig, sig + 64);
		std::vector<int8_t> pk_v(pk, pk + 32);
		std::vector<int8_t> salt_v(salt, salt + salt_size);
		put_mutable_item(target, buf_v, sig_v, seq, pk_v, salt_v, addr);
	}

	virtual void put_mutable_item(libtorrent::sha1_hash const& target
    	, std::vector<int8_t> const& buf
    	, std::vector<int8_t> const& sig
    	, boost::int64_t seq
    	, std::vector<int8_t> const& pk
    	, std::vector<int8_t> const& salt
    	, libtorrent::address const& addr) {
    }

	virtual void tick() {
	}

	dht::dht_storage_counters counters() const {
	    dht::dht_storage_counters c;
	    c.torrents = num_torrents();
	    c.peers = num_peers();
	    c.immutable_data = num_immutable_data();
	    c.mutable_data = num_mutable_data();
	    return c;
	}

	virtual size_t num_torrents() const {
	    return 0;
	}

    virtual size_t num_peers() const {
        return 0;
    }

    virtual size_t num_immutable_data() const {
        return 0;
    }

    virtual size_t num_mutable_data() const {
        return 0;
    }

	virtual ~swig_dht_storage() {}
};

//------------------------------------------------------

void dht_put_item_cb(entry& e, boost::array<char, 64>& sig, boost::uint64_t& seq,
    std::string const& salt, char const* public_key, char const* private_key,
    entry& data)
{
	using libtorrent::dht::sign_mutable_item;

	e = data;
	std::vector<char> buf;
	bencode(std::back_inserter(buf), e);
	++seq;
	sign_mutable_item(std::pair<char const*, int>(buf.data(), buf.size()),
        std::pair<char const*, int>(salt.data(), salt.size()),
        seq,
        public_key,
        private_key,
        sig.data());
}

struct swig_torrent_plugin;
struct swig_peer_plugin;

struct swig_plugin : plugin {

    virtual ~swig_plugin() {
    }

    boost::shared_ptr<torrent_plugin> new_torrent(libtorrent::torrent_handle const&, void*);

    virtual swig_torrent_plugin* new_torrent(libtorrent::torrent_handle const& t);

    virtual void added(libtorrent::session_handle s) {
    }

    void register_dht_extensions(libtorrent::dht_extensions_t& dht_extensions) {
        std::vector<std::pair<std::string, dht_extension_handler_listener*> > swig_dht_extensions;
        register_dht_extensions(swig_dht_extensions);
        for (int i = 0; i < swig_dht_extensions.size(); i++) {
            std::pair<std::string, dht_extension_handler_listener*> ext = swig_dht_extensions[i];
            dht_extensions.push_back(std::pair<std::string, dht_extension_handler_t>(
                ext.first,
                boost::bind(&dht_extension_handler_cb, _1, _2, _3, ext.second)));
        }
    }

    virtual void register_dht_extensions(std::vector<std::pair<std::string, dht_extension_handler_listener*> >& dht_extensions) {
    }

    virtual void on_alert(libtorrent::alert const* a) {
    }

    virtual bool on_unknown_torrent(libtorrent::sha1_hash const& info_hash, libtorrent::peer_connection_handle const& pc, libtorrent::add_torrent_params& p) {
        return false;
    }

    virtual void on_tick() {
    }

    virtual bool on_optimistic_unchoke(std::vector<libtorrent::peer_connection_handle>& peers) {
        return false;
    }

    virtual void save_state(libtorrent::entry& e) const {
    }

    virtual void load_state(libtorrent::bdecode_node const& n) {
    }
};

struct swig_torrent_plugin: torrent_plugin {

    virtual ~swig_torrent_plugin() {
    }

    boost::shared_ptr<peer_plugin> new_connection(libtorrent::peer_connection_handle const& pc);

    virtual swig_peer_plugin* new_peer_connection(libtorrent::peer_connection_handle const& pc);

    virtual void on_piece_pass(int index) {
    }

    virtual void on_piece_failed(int index) {
    }

    virtual void tick() {
    }

    virtual bool on_pause() {
        return false;
    }

    virtual bool on_resume() {
        return false;
    }

    virtual void on_files_checked() {
    }

    virtual void on_state(int s) {
    }

    virtual void on_unload() {
    }

    virtual void on_load() {
    }

    virtual void on_add_peer(tcp::endpoint const& endp, int src, int flags) {
    }
};

struct swig_peer_plugin : peer_plugin
{
    virtual ~swig_peer_plugin() {
    }

    virtual char const* type() const {
        return "swig";
    }

    virtual void add_handshake(libtorrent::entry& e) {
    }

    virtual void on_disconnect(boost::system::error_code const& ec) {
    }

    virtual void on_connected() {
    }

    virtual bool on_handshake(char const* reserved_bits) {
        return true;
    }

    virtual bool on_extension_handshake(libtorrent::bdecode_node const& n) {
        return true;
    }

    virtual bool on_choke() { return false; }
    virtual bool on_unchoke() { return false; }
    virtual bool on_interested() { return false; }
    virtual bool on_not_interested() { return false; }
    virtual bool on_have(int index) { return false; }
    virtual bool on_dont_have(int index) { return false; }
    virtual bool on_bitfield(libtorrent::bitfield const& bitfield) { return false; }
    virtual bool on_have_all() { return false; }
    virtual bool on_have_none() { return false; }
    virtual bool on_allowed_fast(int index) { return false; }
    virtual bool on_request(libtorrent::peer_request const& r) { return false; }
    virtual bool on_piece(libtorrent::peer_request const& piece, libtorrent::disk_buffer_holder& data) { return false; }
    virtual bool on_cancel(libtorrent::peer_request const& r) { return false; }
    virtual bool on_reject(libtorrent::peer_request const& r) { return false; }
    virtual bool on_suggest(int index) { return false; }

    virtual void sent_unchoke() {
    }

    virtual void sent_payload(int bytes) {
    }

    virtual bool can_disconnect(boost::system::error_code const& ec) {
        return true;
    }

    bool on_extended(int length, int msg, libtorrent::buffer::const_interval body) {
        return false;
    }

    bool on_unknown_message(int length, int msg, libtorrent::buffer::const_interval body) {
        return false;
    }

    virtual void on_piece_pass(int index) {
    }

    virtual void on_piece_failed(int index) {
    }

    virtual void tick() {
    }

    virtual bool write_request(libtorrent::peer_request const& r) {
        return false;
    }
};

boost::shared_ptr<torrent_plugin> swig_plugin::new_torrent(libtorrent::torrent_handle const& t, void*) {
    swig_torrent_plugin* p = new_torrent(t);
    return p != NULL ? boost::shared_ptr<torrent_plugin>(p) : boost::shared_ptr<torrent_plugin>();
}

swig_torrent_plugin* swig_plugin::new_torrent(libtorrent::torrent_handle const& t) {
    return NULL;
}

boost::shared_ptr<peer_plugin> swig_torrent_plugin::new_connection(libtorrent::peer_connection_handle const& pc) {
    swig_peer_plugin* p = new_peer_connection(pc);
    return p != NULL ? boost::shared_ptr<peer_plugin>(p) : boost::shared_ptr<peer_plugin>();
}

swig_peer_plugin* swig_torrent_plugin::new_peer_connection(libtorrent::peer_connection_handle const& pc) {
    return NULL;
}

struct posix_stat {
    boost::int64_t size;
	boost::int64_t atime;
	boost::int64_t mtime;
	boost::int64_t ctime;
    int mode;
};

#ifdef WRAP_POSIX
extern "C" {
int __real_open(const char *path, int flags, ...);
int __real_stat(const char *path, struct ::stat *buf);
int __real_mkdir(const char *path, mode_t mode);
int __real_rename(const char *oldpath, const char *newpath);
int __real_remove(const char *path);
}
#endif

class posix_wrapper {
public:
    virtual ~posix_wrapper() {
    }

    virtual int open(const char* path, int flags, int mode) {
#ifdef WRAP_POSIX
        return __real_open(path, flags, mode);
#else
        return -1;
#endif
    }

    virtual int stat(const char *path, posix_stat *buf) {
#ifdef WRAP_POSIX
        struct ::stat t;
        int r = __real_stat(path, &t);
        buf->size = t.st_size;
        buf->atime = t.st_atime;
        buf->mtime = t.st_mtime;
        buf->ctime = t.st_ctime;
        buf->mode = t.st_mode;
        return r;
#else
        return -1;
#endif
    }

    virtual int mkdir(const char *path, int mode) {
#ifdef WRAP_POSIX
        return __real_mkdir(path, mode);
#else
        return -1;
#endif
    }

    virtual int rename(const char *oldpath, const char *newpath) {
#ifdef WRAP_POSIX
        return __real_rename(oldpath, newpath);
#else
        return -1;
#endif
    }

    virtual int remove(const char *path) {
#ifdef WRAP_POSIX
        return __real_remove(path);
#else
        return -1;
#endif
    }
};

posix_wrapper* g_posix_wrapper = NULL;

void set_posix_wrapper(posix_wrapper *obj) {
    g_posix_wrapper = obj;
}

#ifdef WRAP_POSIX
extern "C" {

int __wrap_open(const char *path, int flags, ...) {
    mode_t mode = 0;
    flags |= O_LARGEFILE;
    if (flags & O_CREAT)
    {
        va_list  args;
        va_start(args, flags);
        mode = (mode_t) va_arg(args, int);
        va_end(args);
    }
    return g_posix_wrapper != NULL ?
        g_posix_wrapper->open(path, flags, mode) :
        __real_open(path, flags, mode);
}

int __wrap_stat(const char *path, struct ::stat *buf) {
    if (g_posix_wrapper != NULL) {
        posix_stat t;
        int r = g_posix_wrapper->stat(path, &t);
        buf->st_size = t.size;
        buf->st_atime = t.atime;
        buf->st_mtime = t.mtime;
        buf->st_ctime = t.ctime;
        buf->st_mode = t.mode;
        return r;
    } else {
        return __real_stat(path, buf);
    }
}

int __wrap_mkdir(const char *path, mode_t mode) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->mkdir(path, mode) :
           __real_mkdir(path, mode);
}
int __wrap_rename(const char *oldpath, const char *newpath) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->rename(oldpath, newpath) :
           __real_rename(oldpath, newpath);
}
int __wrap_remove(const char *path) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->remove(path) :
           __real_remove(path);
}

}
#endif
