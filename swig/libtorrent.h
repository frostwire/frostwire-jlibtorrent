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

// handling system call wraps

extern "C" {

int __real_open(const char* pathname, int flags, ...);
int __real_open64(const char* pathname, int flags, ...);
int	__real_openat(int dirfd, const char* pathname, int flags, ...);
int	__real_creat(const char* pathname, mode_t mode);
int	__real_mkdir(const char* pathname, mode_t mode);
int __real_rename(const char *oldpath, const char *newpath);
int __real_remove(const char *pathname);
int	__real_lstat(const char* path, struct ::stat* buf);
int	__real_stat(const char* path, struct ::stat* buf);

}

struct swig_posix_stat {
    int     mode;
    long    size;
    long    atime;
    long    mtime;
    long    ctime;
};

class swig_posix_wrapper {
public:
    virtual ~swig_posix_wrapper() {
    }

    virtual int open(const char* pathname, int flags, int mode) {
#ifdef __linux || __ANDROID__
        return __real_open(pathname, flags, mode);
#else
        return -1;
#endif
    }

    virtual int open64(const char* pathname, int flags, int mode) {
#ifdef __linux || __ANDROID__
        return __real_open64(pathname, flags, mode);
#else
        return -1;
#endif
    }

    virtual int openat(int dirfd, const char* pathname, int flags, int mode) {
#ifdef __linux || __ANDROID__
        return __real_openat(dirfd, pathname, flags, mode);
#else
        return -1;
#endif
    }

    virtual int creat(const char* pathname, int mode) {
#ifdef __linux || __ANDROID__
        return __real_creat(pathname, mode);
#else
        return -1;
#endif
    }

    virtual int mkdir(const char* pathname, int mode) {
#ifdef __linux || __ANDROID__
        return __real_mkdir(pathname, mode);
#else
        return -1;
#endif
    }

    virtual int rename(const char *oldpath, const char *newpath) {
#ifdef __linux || __ANDROID__
        return __real_rename(oldpath, newpath);
#else
        return -1;
#endif
    }

    virtual int remove(const char *pathname) {
#ifdef __linux || __ANDROID__
        return __real_remove(pathname);
#else
        return -1;
#endif
    }

    virtual int lstat(const char* path, swig_posix_stat* buf) {
#ifdef __linux || __ANDROID__
        struct ::stat ret;
        int r = __real_lstat(path, &ret);
        buf->mode = ret.st_mode;
        buf->size = ret.st_size;
        buf->atime = ret.st_atime;
        buf->mtime = ret.st_mtime;
        buf->ctime = ret.st_ctime;
        return r;
#else
        return -1;
#endif
    }

    virtual int stat(const char* path, swig_posix_stat* buf) {
#ifdef __linux || __ANDROID__
        struct ::stat ret;
        int r = __real_stat(path, &ret);
        buf->mode = ret.st_mode;
        buf->size = ret.st_size;
        buf->atime = ret.st_atime;
        buf->mtime = ret.st_mtime;
        buf->ctime = ret.st_ctime;
        return r;
#else
        return -1;
#endif
    }
};

swig_posix_wrapper* g_posix_wrapper = NULL;

void set_global_posix_wrapper(swig_posix_wrapper* ctx) {
    g_posix_wrapper = ctx;
}

extern "C" {

int __wrap_open(const char* pathname, int flags, ...) {
#ifdef __linux || __ANDROID__
    int mode = 0;
    if (flags & O_CREAT) {
        va_list arg;
        va_start(arg, flags);
        mode = va_arg(arg, int);
        va_end(arg);
    }
    return g_posix_wrapper != NULL ? g_posix_wrapper->open(pathname, flags, mode) : __real_open(pathname, flags, mode);
#else
    return -1;
#endif
}

int __wrap_open64(const char* pathname, int flags, ...) {
#ifdef __linux || __ANDROID__
    int mode = 0;
    if (flags & O_CREAT) {
        va_list arg;
        va_start(arg, flags);
        mode = va_arg(arg, int);
        va_end(arg);
    }
    return g_posix_wrapper != NULL ? g_posix_wrapper->open64(pathname, flags, mode) : __real_open64(pathname, flags, mode);
#else
    return -1;
#endif
}

int __wrap_openat(int dirfd, const char* pathname, int flags, ...) {
#ifdef __linux || __ANDROID__
    int mode = 0;
    if (flags & O_CREAT) {
        va_list arg;
        va_start(arg, flags);
        mode = va_arg(arg, int);
        va_end(arg);
    }
    return g_posix_wrapper != NULL ? g_posix_wrapper->openat(dirfd, pathname, flags, mode) : __real_openat(dirfd, pathname, flags, mode);
#else
    return -1;
#endif
}

int __wrap_creat(const char* pathname, mode_t mode) {
#ifdef __linux || __ANDROID__
    return g_posix_wrapper != NULL ? g_posix_wrapper->creat(pathname, mode) : __real_creat(pathname, mode);
#else
    return -1;
#endif
}

int	__wrap_mkdir(const char* pathname, mode_t mode) {
#ifdef __linux || __ANDROID__
    return g_posix_wrapper != NULL ? g_posix_wrapper->mkdir(pathname, mode) : __real_mkdir(pathname, mode);
#else
    return -1;
#endif
}

int __wrap_rename(const char *oldpath, const char *newpath) {
#ifdef __linux || __ANDROID__
    return g_posix_wrapper != NULL ? g_posix_wrapper->rename(oldpath, newpath) : __real_rename(oldpath, newpath);
#else
    return -1;
#endif
}

int __wrap_remove(const char *pathname) {
#ifdef __linux || __ANDROID__
    return g_posix_wrapper != NULL ? g_posix_wrapper->remove(pathname) : __real_remove(pathname);
#else
    return -1;
#endif
}

int	__wrap_lstat(const char* path, struct ::stat* buf) {
#ifdef __linux || __ANDROID__
    if (g_posix_wrapper != NULL) {
        swig_posix_stat ret;
        int r = g_posix_wrapper->lstat(path, &ret);
        buf->st_mode = ret.mode;
        buf->st_size = ret.size;
        buf->st_atime = ret.atime;
        buf->st_mtime = ret.mtime;
        buf->st_ctime = ret.ctime;
        return r;
    } else {
        return __real_lstat(path, buf);
    }
#else
    return -1;
#endif
}

int	__wrap_stat(const char* path, struct ::stat* buf) {
#ifdef __linux || __ANDROID__
    if (g_posix_wrapper != NULL) {
        swig_posix_stat ret;
        int r = g_posix_wrapper->stat(path, &ret);
        buf->st_mode = ret.mode;
        buf->st_size = ret.size;
        buf->st_atime = ret.atime;
        buf->st_mtime = ret.mtime;
        buf->st_ctime = ret.ctime;
        return r;
    } else {
        return __real_stat(path, buf);
    }
#else
    return -1;
#endif
}

}

#ifdef LIBTORRENT_SWIG_NODE

#include <uv.h>

void session_alerts_loop_fn(void *arg) {
    libtorrent::session& s = *((libtorrent::session*) arg);

    libtorrent::time_duration max_wait = std::chrono::milliseconds(500);

    std::vector<alert*> v;
    while (true) {
        alert* ptr = s.wait_for_alert(max_wait);

        if (ptr != NULL) {
            s.pop_alerts(&v);
            printf("alerts (%d)\n", v.size());
            long size = v.size();
            for (int i = 0; i < size; i++) {
                alert* a = v[i];
                printf("%d - %s - %s\n", a->type(), a->what(), a->message().c_str());
            }
        }
    }
}

void session_alerts_loop(libtorrent::session& s) {

    uv_thread_t t;
    uv_thread_create(&t, session_alerts_loop_fn, &s);
}

#endif
