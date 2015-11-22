#include <boost/version.hpp>
#include <boost/function.hpp>
#include <boost/bind.hpp>

#include <libtorrent/utp_stream.hpp>

#define LIBTORRENT_REVISION_SHA1 _LIBTORRENT_REVISION_SHA1_
#define JLIBTORRENT_REVISION_SHA1 _JLIBTORRENT_REVISION_SHA1_

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

void add_files(libtorrent::file_storage& fs, std::string const& file, boost::uint32_t flags, add_files_listener* listener) {
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

void set_piece_hashes(std::string const& id, libtorrent::create_torrent& t, std::string const& p, libtorrent::error_code& ec, set_piece_hashes_listener* listener) {
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
