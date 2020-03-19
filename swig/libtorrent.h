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
#include <libtorrent/write_resume_data.hpp>
#include <libtorrent/hex.hpp>
#include <libtorrent/extensions.hpp>
#include <libtorrent/bloom_filter.hpp>
#include <libtorrent/announce_entry.hpp>
#include <libtorrent/enum_net.hpp>

#include <libtorrent/kademlia/dht_tracker.hpp>
#include <libtorrent/kademlia/node_entry.hpp>
#include <libtorrent/kademlia/node.hpp>
#include <libtorrent/kademlia/node_id.hpp>
#include <libtorrent/kademlia/get_peers.hpp>
#include <libtorrent/kademlia/item.hpp>
#include <libtorrent/kademlia/ed25519.hpp>

#include <libtorrent/aux_/cpuid.hpp>

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

    signature sig = ed25519_sign({reinterpret_cast<char const*>(msg.data()), static_cast<long>(msg.size())},
        pk1, sk1);
    return std::vector<int8_t>(sig.bytes.begin(), sig.bytes.end());
}

bool ed25519_verify(std::vector<int8_t>& sig,
    std::vector<int8_t>& msg,
    std::vector<int8_t>& pk) {
    using namespace libtorrent::dht;

    signature sig1((char*)sig.data());
    public_key pk1((char*)pk.data());

    return ed25519_verify(sig1, {reinterpret_cast<char const*>(msg.data()), static_cast<long>(msg.size())}, pk1);
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

void add_files_ex(libtorrent::file_storage& fs, std::string const& file,
                add_files_listener* listener, libtorrent::create_flags_t flags) {
    add_files(fs, file, std::bind(&add_files_listener::pred, listener, std::placeholders::_1), flags);
}

struct set_piece_hashes_listener {

    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(int i) {
    }

    void progress_index(libtorrent::piece_index_t i) {
        progress(static_cast<int>(i));
    }
};

void set_piece_hashes_ex(libtorrent::create_torrent& t, std::string const& p,
                        set_piece_hashes_listener* listener, libtorrent::error_code& ec) {
    set_piece_hashes(t, p, std::bind(&set_piece_hashes_listener::progress_index, listener, std::placeholders::_1), ec);
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

int find_metric_idx_s(std::string const& name) {
    return libtorrent::find_metric_idx(name);
}

void dht_put_item_cb(libtorrent::entry& e, std::array<char, 64>& sig, std::int64_t& seq,
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

    libtorrent::feature_flags_t implemented_features() {
        return libtorrent::plugin::dht_request_feature;
    }

    virtual bool on_dht_request(libtorrent::string_view query,
        libtorrent::udp::endpoint const& source,
        libtorrent::bdecode_node const& message, libtorrent::entry& response) {
        return false;
    }
};

// enum_net functions, very useful for networking
struct ip_interface
{
    libtorrent::address interface_address;
    libtorrent::address netmask;
    std::vector<std::int8_t> name;
    std::vector<std::int8_t> friendly_name;
    std::vector<std::int8_t> description;
    bool preferred;
};

struct ip_route
{
    libtorrent::address destination;
    libtorrent::address netmask;
    libtorrent::address gateway;
    libtorrent::address source_hint;
    std::vector<std::int8_t> name;
    int mtu;
};

std::vector<ip_interface> enum_net_interfaces(libtorrent::session* s)
{
    std::vector<ip_interface> ret;
    boost::system::error_code ec;
    auto v = libtorrent::enum_net_interfaces(s->get_io_service(), ec);
    for (auto& e : v)
    {
        ip_interface iface;
        iface.interface_address = e.interface_address;
        iface.netmask = e.netmask;
        iface.name = {e.name, e.name + sizeof(e.name)};
        iface.friendly_name = {e.friendly_name, e.friendly_name + sizeof(e.friendly_name)};
        iface.description = {e.description, e.description + sizeof(e.description)};
        iface.preferred = e.preferred;
        ret.push_back(iface);
    }
    return ret;
}

std::vector<ip_route> enum_routes(libtorrent::session* s)
{
    std::vector<ip_route> ret;
    boost::system::error_code ec;
    auto v = libtorrent::enum_routes(s->get_io_service(), ec);
    for (auto& e : v)
    {
        ip_route r;
        r.destination = e.destination;
        r.netmask = e.netmask;
        r.gateway = e.gateway;
        r.name = {e.name, e.name + sizeof(e.name)};
        r.mtu = e.mtu;
        ret.push_back(r);
    }
    return ret;
}

void copy_byte_vector_to_char_array(std::vector<std::int8_t> source, char* target, const unsigned int target_size)
{
  std::memset(target, 0, target_size);
  for (unsigned int i=0; i < source.size() || i <= target_size; i++) {
    target[i] = source[i];
  }
}

//boost::optional<address> get_gateway(ip_interface const& iface, span<ip_route const> routes);
//@see enum_net.hpp -> struct libtorrent::ip_interface
//@see enum_net.hpp -> struct libtorrent::ip_route
libtorrent::address get_gateway(ip_interface const& iface, std::vector<ip_route>& routes)
{
 // convert our libtorrent.h defined types to native libtorrent:: types from enum_net.hpp
 libtorrent::ip_interface lt_iface;
 lt_iface.interface_address = iface.interface_address;
 lt_iface.netmask = iface.netmask;
 lt_iface.preferred = iface.preferred;

 copy_byte_vector_to_char_array(iface.name, lt_iface.name, sizeof(lt_iface.name)); // 64
 copy_byte_vector_to_char_array(iface.friendly_name, lt_iface.friendly_name, sizeof(lt_iface.friendly_name)); // 128
 copy_byte_vector_to_char_array(iface.description, lt_iface.description, sizeof(lt_iface.description)); // 128

 // convert ip_route_vector to vector<libtorrent::ip_route const> (cannot use const, gnu g++ explodes in linux/android thinking there's an ambiguity)
 std::vector<libtorrent::ip_route> lt_routes;
 for (auto const& r : routes) {
   libtorrent::ip_route lt_ip_route;
   lt_ip_route.destination = r.destination;
   lt_ip_route.netmask = r.netmask;
   lt_ip_route.gateway = r.gateway;
   lt_ip_route.source_hint = r.source_hint;
   lt_ip_route.mtu = r.mtu;
   copy_byte_vector_to_char_array(r.name, lt_ip_route.name, sizeof(lt_ip_route.name)); //64
   lt_routes.push_back(lt_ip_route);
 }

 return libtorrent::get_gateway(lt_iface, lt_routes).get();
}

bool arm_neon_support()
{
    return libtorrent::aux::arm_neon_support;
}

#if defined TORRENT_ANDROID || defined TORRENT_BSD
#define WRAP_POSIX 1
#else
#define WRAP_POSIX 0
#endif

struct posix_stat_t {
    int64_t size;
    int64_t atime;
    int64_t mtime;
    int64_t ctime;
    int mode;
};

#if WRAP_POSIX
void* get_libc() {
#if defined TORRENT_ANDROID
    static void* h = dlopen("libc.so", RTLD_NOW);
#elif defined TORRENT_BSD
    static void* h = dlopen("libc.dylib", RTLD_NOW);
#else
    static void* h = nullptr;
#endif
    return h;
}

int posix_open(const char* path, int flags, mode_t mode) {
    typedef int func_t(const char*, int, ...);
    static func_t* f = (func_t*) dlsym(get_libc(), "open");
#if defined TORRENT_ANDROID
    flags |= O_LARGEFILE;
#endif
    return (*f)(path, flags, mode);
}

int posix_stat(const char *path, struct ::stat *buf) {
    typedef int func_t(const char*, struct ::stat*);
#if defined TORRENT_ANDROID && __ANDROID_API__ < 21
    static func_t* f = (func_t*) dlsym(get_libc(), "stat");
#else
    static func_t* f = (func_t*) dlsym(get_libc(), "stat64");
#endif
    return (*f)(path, buf);
}

int posix_mkdir(const char *path, mode_t mode) {
    typedef int func_t(const char*, mode_t);
    static func_t* f = (func_t*) dlsym(get_libc(), "mkdir");
    return (*f)(path, mode);
}

int posix_rename(const char *oldpath, const char *newpath) {
    typedef int func_t(const char*, const char*);
    static func_t* f = (func_t*) dlsym(get_libc(), "rename");
    return (*f)(oldpath, newpath);
}

int posix_remove(const char *path) {
    typedef int func_t(const char*);
    static func_t* f = (func_t*) dlsym(get_libc(), "remove");
    return (*f)(path);
}
#endif

struct posix_wrapper {

    virtual ~posix_wrapper() {
    }

    virtual int open(const char* path, int flags, int mode) {
#if WRAP_POSIX
        return posix_open(path, flags, mode);
#else
        return -1;
#endif
    }

    virtual int stat(const char *path, posix_stat_t *buf) {
#if WRAP_POSIX
        struct ::stat t;
        int r = posix_stat(path, &t);
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
#if WRAP_POSIX
        return posix_mkdir(path, mode);
#else
        return -1;
#endif
    }

    virtual int rename(const char *oldpath, const char *newpath) {
#if WRAP_POSIX
        return posix_rename(oldpath, newpath);
#else
        return -1;
#endif
    }

    virtual int remove(const char *path) {
#if WRAP_POSIX
        return posix_remove(path);
#else
        return -1;
#endif
    }
};

posix_wrapper* g_posix_wrapper = NULL;

void set_posix_wrapper(posix_wrapper *obj) {
    g_posix_wrapper = obj;
}

#if WRAP_POSIX
extern "C" {

int open(const char *path, int flags, ...) {
    mode_t mode = 0;
    if (flags & O_CREAT) {
        va_list v;
        va_start(v, flags);
        mode = (mode_t) va_arg(v, int);
        va_end(v);
    }

    return g_posix_wrapper != NULL ?
           g_posix_wrapper->open(path, flags, mode) :
           posix_open(path, flags, mode);
}

int stat(const char *path, struct ::stat *buf) {
    if (g_posix_wrapper != NULL) {
        posix_stat_t t;
        int r = g_posix_wrapper->stat(path, &t);
        buf->st_size = t.size;
        buf->st_atime = t.atime;
        buf->st_mtime = t.mtime;
        buf->st_ctime = t.ctime;
        buf->st_mode = t.mode;
        return r;
    } else {
        return posix_stat(path, buf);
    }
}

int mkdir(const char *path, mode_t mode) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->mkdir(path, mode) :
           posix_mkdir(path, mode);
}

int rename(const char *oldpath, const char *newpath) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->rename(oldpath, newpath) :
           posix_rename(oldpath, newpath);
}

int remove(const char *path) {
    return g_posix_wrapper != NULL ?
           g_posix_wrapper->remove(path) :
           posix_remove(path);
}

}
#endif
