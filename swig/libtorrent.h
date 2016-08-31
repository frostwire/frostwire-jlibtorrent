#ifndef _WIN32
#include <dlfcn.h>
#endif

#include <stdexcept>
#include <string>
#include <vector>
#include <array>
#include <map>
#include <algorithm>

#include <boost/version.hpp>

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
#include <libtorrent/extensions.hpp>

#include <libtorrent/kademlia/dht_tracker.hpp>
#include <libtorrent/kademlia/node_entry.hpp>
#include <libtorrent/kademlia/node.hpp>
#include <libtorrent/kademlia/node_id.hpp>
#include <libtorrent/kademlia/get_peers.hpp>
#include <libtorrent/kademlia/item.hpp>
#include <libtorrent/kademlia/ed25519.hpp>

std::vector<int8_t> ed25519_create_seed() {
    std::array<char, 32> seed = libtorrent::dht::ed25519_create_seed();
    return std::vector<int8_t>(seed.data(), seed.end());
}

std::pair<std::vector<int8_t>, std::vector<int8_t>> ed25519_create_keypair(
    std::vector<int8_t>& seed) {
    using namespace libtorrent::dht;

    public_key pk;
    secret_key sk;

    std::array<char, 32> s;
    std::copy_n(seed.begin(), 32, s.begin());

    std::tie(pk, sk) = ed25519_create_keypair(s);

    return std::make_pair(std::vector<int8_t>(pk.bytes.begin(), pk.bytes.end()),
        std::vector<int8_t>(sk.bytes.begin(), sk.bytes.end()));
}

std::vector<int8_t> ed25519_sign(std::vector<int8_t>& msg,
    std::vector<int8_t>& pk, std::vector<int8_t>& sk) {
    using namespace libtorrent::dht;

    public_key pk1((char*)pk.data());
    secret_key sk1((char*)sk.data());

    signature sig = ed25519_sign({reinterpret_cast<char const*>(msg.data()), msg.size()},
        pk1, sk1);
    return std::vector<int8_t>(sig.bytes.begin(), sig.bytes.end());
}

bool ed25519_verify(std::vector<int8_t>& sig,
    std::vector<int8_t>& msg,
    std::vector<int8_t>& pk) {
    using namespace libtorrent::dht;

    signature sig1((char*)sig.data());
    public_key pk1((char*)pk.data());

    return ed25519_verify(sig1, {reinterpret_cast<char const*>(msg.data()), msg.size()}, pk1);
}

std::vector<int8_t> ed25519_add_scalar_public(std::vector<int8_t>& pk,
    std::vector<int8_t>& scalar) {
    using namespace libtorrent::dht;

    public_key pk1((char*)pk.data());

    std::array<char, 32> s;
    std::copy_n(scalar.begin(), 32, s.begin());

    public_key ret = ed25519_add_scalar(pk1, s);
    return std::vector<int8_t>(ret.bytes.begin(), ret.bytes.end());
}

std::vector<int8_t> ed25519_add_scalar_secret(std::vector<int8_t>& sk,
    std::vector<int8_t>& scalar) {
    using namespace libtorrent::dht;

    secret_key sk1((char*)sk.data());

    std::array<char, 32> s;
    std::copy_n(scalar.begin(), 32, s.begin());

    secret_key ret = ed25519_add_scalar(sk1, s);
    return std::vector<int8_t>(ret.bytes.begin(), ret.bytes.end());
}

std::vector<int8_t> ed25519_key_exchange(std::vector<int8_t>& pk,
    std::vector<int8_t>& sk) {
    using namespace libtorrent::dht;

    public_key pk1((char*)pk.data());
    secret_key sk1((char*)sk.data());

    std::array<char, 32> secret = ed25519_key_exchange(pk1, sk1);
    return std::vector<int8_t>(secret.begin(), secret.end());
}

bool default_storage_disk_write_access_log() {
    return libtorrent::default_storage::disk_write_access_log();
}

void default_storage_disk_write_access_log(bool enable) {
    return libtorrent::default_storage::disk_write_access_log(enable);
}

struct alert_notify_callback {

    virtual ~alert_notify_callback() {
    }

    virtual void on_alert() {
    }
};

struct add_files_listener {

    virtual ~add_files_listener() {
    }

    virtual bool pred(std::string const& p) {
        return true;
    }
};

void add_files(libtorrent::file_storage& fs, std::string const& file,
                add_files_listener* listener, std::uint32_t flags) {
    using namespace std::placeholders;
    add_files(fs, file, std::bind(&add_files_listener::pred, listener, _1), flags);
}

struct set_piece_hashes_listener {

    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(int i) {
    }
};

void set_piece_hashes_ex(libtorrent::create_torrent& t, std::string const& p,
                        set_piece_hashes_listener* listener, libtorrent::error_code& ec) {
    using namespace std::placeholders;
    set_piece_hashes(t, p, std::bind(&set_piece_hashes_listener::progress, listener, _1), ec);
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

void dht_put_item_cb(libtorrent::entry& e, std::array<char, 64>& sig, std::uint64_t& seq,
    std::string salt, libtorrent::dht::public_key pk, libtorrent::dht::secret_key sk,
    libtorrent::entry data) {
    using namespace libtorrent::dht;

	e = data;
	std::vector<char> buf;
	bencode(std::back_inserter(buf), e);
	signature sign;
	++seq;
	sign = sign_mutable_item(buf, salt, sequence_number(seq), pk, sk);
    sig = sign.bytes;
}

struct swig_plugin : libtorrent::plugin {

    virtual ~swig_plugin() {
    }

    std::uint32_t implemented_features() {
        return libtorrent::plugin::dht_request_feature;
    }

    virtual bool on_dht_request(libtorrent::string_view query,
        libtorrent::udp::endpoint const& source,
        libtorrent::bdecode_node const& message, libtorrent::entry& response) {
        return false;
    }
};

#if TORRENT_ANDROID && TORRENT_HAS_ARM && __ANDROID_API__ < 21
#include <stdio.h>
unsigned long getauxval(unsigned long type) {
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

#define WRAP_POSIX __clang__

struct posix_stat {
    int64_t size;
    int64_t atime;
    int64_t mtime;
    int64_t ctime;
    int mode;
};

int real_open(const char* path, int flags, int mode) {
    typedef int func_t(const char*, int, int);

    dlerror();
    void* h = nullptr;
#if defined TORRENT_ANDROID
    h = dlopen("libc.so", RTLD_NOW);
#elif defined TORRENT_BSD
    h = dlopen("libc.dylib", RTLD_NOW);
#endif
    func_t* f = (func_t*) dlsym(h, "open");
    int ret = (*f)(path, flags, mode);
    dlclose(h);
    return ret;
}

struct posix_wrapper {

    virtual ~posix_wrapper() {
    }

    virtual int open(const char* path, int flags, int mode) {
#if WRAP_POSIX
        return real_open(path, flags, mode);
#else
        return -1;
#endif
    }

    virtual int stat(const char *path, posix_stat *buf) {
#if 0 //WRAP_POSIX
        struct ::stat t;
        int r = ::stat(path, &t);
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
#if 0 //WRAP_POSIX
        return ::mkdir(path, mode);
#else
        return -1;
#endif
    }

    virtual int rename(const char *oldpath, const char *newpath) {
#if 0 //WRAP_POSIX
        return ::rename(oldpath, newpath);
#else
        return -1;
#endif
    }

    virtual int remove(const char *path) {
#if 0 //WRAP_POSIX
        return ::remove(path);
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

int open(const char *path, int flags, ...) {
    mode_t mode = 0;
    //flags |= O_LARGEFILE;
    if (flags & O_CREAT)
    {
        va_list  args;
        va_start(args, flags);
        mode = (mode_t) va_arg(args, int);
        va_end(args);
    }
    return g_posix_wrapper != NULL ?
        g_posix_wrapper->open(path, flags, mode) :
        real_open(path, flags, mode);
}
/*
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
}*/

}
#endif
