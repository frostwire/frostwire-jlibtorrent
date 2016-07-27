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

#include <openssl/opensslv.h>

#include <libtorrent/config.hpp>
#include <libtorrent/time.hpp>
#include <libtorrent/buffer.hpp>
#include <libtorrent/utp_stream.hpp>
#include <libtorrent/socket_io.hpp>
#include <libtorrent/read_resume_data.hpp>
#include <libtorrent/hex.hpp>

#include <libtorrent/kademlia/dht_tracker.hpp>
#include <libtorrent/kademlia/node_entry.hpp>
#include <libtorrent/kademlia/node.hpp>
#include <libtorrent/kademlia/node_id.hpp>
#include <libtorrent/kademlia/get_peers.hpp>
#include <libtorrent/kademlia/item.hpp>

#define LIBTORRENT_REVISION_SHA1 _LIBTORRENT_REVISION_SHA1_
#define JLIBTORRENT_REVISION_SHA1 _JLIBTORRENT_REVISION_SHA1_

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

void add_files(libtorrent::file_storage& fs, std::string const& file, add_files_listener* listener, boost::uint32_t flags) {
    add_files(fs, file, boost::bind(&add_files_listener::pred, listener, _1), flags);
}

class set_piece_hashes_listener {
public:
    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(int i) {
    }
};

void set_piece_hashes_ex(libtorrent::create_torrent& t, std::string const& p, set_piece_hashes_listener* listener, libtorrent::error_code& ec) {
    set_piece_hashes(t, p, boost::bind(&set_piece_hashes_listener::progress, listener, _1), ec);
}

int boost_version() {
    return BOOST_VERSION;
}

const char* boost_lib_version() {
    return BOOST_LIB_VERSION;
}

int openssl_version_number() {
    return OPENSSL_VERSION_NUMBER;
}

const char* openssl_version_text() {
    return OPENSSL_VERSION_TEXT;
}

struct swig_storage : storage_interface
{
    virtual ~swig_storage() {
    }

    virtual void set_params(libtorrent::storage_params const& params) {
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

    virtual bool verify_resume_data(libtorrent::add_torrent_params const& rd
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

    virtual void delete_files(int options, libtorrent::storage_error& ec) {
    }

    virtual bool tick() { return false; }
};

storage_interface* swig_storage_constructor(swig_storage* s, libtorrent::storage_params const& params) {
    s->set_params(params);
	return s;
}

//------ DHT storage extension -----------------------------

struct swig_dht_storage_counters
{
	boost::int32_t torrents;
	boost::int32_t peers;
	boost::int32_t immutable_data;
	boost::int32_t mutable_data;
};

struct swig_dht_storage /*: dht::dht_storage_interface*/
{
    virtual void set_settings(libtorrent::dht_settings const& settings) {
    }

    virtual void update_node_ids(std::vector<libtorrent::sha1_hash> const& ids) {
    }

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
		, address const& addr) {
		std::vector<int8_t> buf_v(buf, buf + size);
		put_immutable_item(target, buf_v, addr);
	}

	virtual void put_immutable_item(libtorrent::sha1_hash const& target
    	, std::vector<int8_t> const& buf
    	, address const& addr) {
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
		, address const& addr) {
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
    	, address const& addr) {
    }

	virtual void tick() {
	}

	dht::dht_storage_counters counters() const {
	    dht::dht_storage_counters c;
	    swig_dht_storage_counters sc = swig_counters();
	    c.torrents = sc.torrents;
	    c.peers = sc.peers;
	    c.immutable_data = sc.immutable_data;
	    c.mutable_data = sc.mutable_data;
	    return c;
	}

	virtual swig_dht_storage_counters swig_counters() const {
	    return swig_dht_storage_counters();
	}

	virtual ~swig_dht_storage() {}
};

std::unique_ptr<dht::dht_storage_interface> swig_dht_storage_constructor(swig_dht_storage *s,
    libtorrent::dht_settings const& settings) {
	s->set_settings(settings);
	return std::unique_ptr<dht::dht_storage_interface>();//(s);
}

//------------------------------------------------------

void dht_put_item_cb(entry& e, std::array<char, 64>& sig, boost::uint64_t& seq,
    std::string const& salt, char const* public_key, char const* private_key,
    entry& data)
{
	/*using libtorrent::dht::sign_mutable_item;

	e = data;
	std::vector<char> buf;
	bencode(std::back_inserter(buf), e);
	++seq;
	sign_mutable_item(std::pair<char const*, int>(buf.data(), buf.size()),
        std::pair<char const*, int>(salt.data(), salt.size()),
        seq,
        public_key,
        private_key,
        sig.data());*/
}

struct swig_torrent_plugin;
struct swig_peer_plugin;

struct swig_plugin : plugin {

    virtual ~swig_plugin() {
    }

    boost::shared_ptr<torrent_plugin> new_torrent(libtorrent::torrent_handle const&, void*);

    //virtual swig_torrent_plugin* new_torrent(libtorrent::torrent_handle const& t);

    //virtual void added(libtorrent::session_handle s) {
    //}

    virtual void on_alert(libtorrent::alert const* a) {
    }

    virtual bool on_unknown_torrent(libtorrent::sha1_hash const& info_hash, libtorrent::peer_connection_handle const& pc, libtorrent::add_torrent_params& p) {
        return false;
    }

    virtual void on_tick() {
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

    //virtual swig_peer_plugin* new_peer_connection(libtorrent::peer_connection_handle const& pc);

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

    char const* type() const {
        return "swig"; // TODO: pass name at constructor
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
    virtual bool on_piece(libtorrent::peer_request const& piece, char const* buf) { return false; }
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
    swig_torrent_plugin* p = NULL;//new_torrent(t);
    return p != NULL ? boost::shared_ptr<torrent_plugin>(p) : boost::shared_ptr<torrent_plugin>();
}

//swig_torrent_plugin* swig_plugin::new_torrent(libtorrent::torrent_handle const& t) {
//    return NULL;
//}

boost::shared_ptr<peer_plugin> swig_torrent_plugin::new_connection(libtorrent::peer_connection_handle const& pc) {
    swig_peer_plugin* p = NULL;//new_peer_connection(pc);
    return p != NULL ? boost::shared_ptr<peer_plugin>(p) : boost::shared_ptr<peer_plugin>();
}

//swig_peer_plugin* swig_torrent_plugin::new_peer_connection(libtorrent::peer_connection_handle const& pc) {
//    return NULL;
//}

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

#if TORRENT_ANDROID && TORRENT_HAS_ARM && __ANDROID_API__ < 21
#include <stdio.h>
unsigned long getauxval(unsigned long type)  {
    typedef unsigned long getauxval_func_t(unsigned long);

    dlerror();
    void* libc_handle = dlopen("libc.so", RTLD_NOW);
    if (!libc_handle) {
        printf("Could not dlopen() C library: %s\n", dlerror());
        return 0;
    }

    unsigned long ret = 0;
    getauxval_func_t* func = (getauxval_func_t*) dlsym(libc_handle, "getauxval");
    if (!func) {
        printf("Could not find getauxval() in C library\n");
    } else {
        // Note: getauxval() returns 0 on failure. Doesn't touch errno.
        ret = (*func)(type);
    }
    dlclose(libc_handle);
    return ret;
}
#endif
