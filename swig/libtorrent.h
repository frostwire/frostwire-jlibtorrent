#ifndef _WIN32
#include <dlfcn.h>
#endif

#include <stdexcept>
#include <string>
#include <ios>
#include <vector>
#include <array>
#include <map>
#include <algorithm>

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
    return libtorrent::default_storage::disk_write_access_log();
}

void default_storage_disk_write_access_log(bool enable) {
    return libtorrent::default_storage::disk_write_access_log(enable);
}

class alert_notify_callback {
public:
    virtual ~alert_notify_callback() {
    }

    virtual void on_alert() {
    }
};

class add_files_listener {
public:
    virtual ~add_files_listener() {
    }

    virtual bool pred(std::string const& p) {
        return true;
    }
};

void add_files(libtorrent::file_storage& fs, std::string const& file,
                add_files_listener* listener, boost::uint32_t flags) {
    add_files(fs, file, boost::bind(&add_files_listener::pred, listener, _1), flags);
}

class set_piece_hashes_listener {
public:
    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(int i) {
    }
};

void set_piece_hashes_ex(libtorrent::create_torrent& t, std::string const& p,
                        set_piece_hashes_listener* listener, libtorrent::error_code& ec) {
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

void dht_put_item_cb(libtorrent::entry& e, std::array<char, 64>& sig, boost::uint64_t& seq,
    std::string const& salt, char const* public_key, char const* private_key,
    libtorrent::entry& data)
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
