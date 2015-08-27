#include <boost/version.hpp>

#define LIBTORRENT_REVISION_SHA1 _LIBTORRENT_REVISION_SHA1_
#define JLIBTORRENT_REVISION_SHA1 _JLIBTORRENT_REVISION_SHA1_

class add_files_listener {
public:
    virtual ~add_files_listener() {
    }

    virtual bool pred(std::string const& id, std::string const& p) {
        return true;
    }
};

bool add_files_cb(std::string const& p, add_files_listener* listener, std::string& id) {
	return listener->pred(id, p);
}

void add_files(std::string const& id, libtorrent::file_storage& fs, std::string const& file, boost::uint32_t flags, add_files_listener* listener) {
    add_files(fs, file, boost::bind(&add_files_cb, _1, listener, id), flags);
}

class set_piece_hashes_listener {
public:
    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(std::string const& id, int num_pieces, int i) {
    }
};

void set_piece_hashes_cb(int i, set_piece_hashes_listener* listener, std::string& id, int num_pieces) {
	listener->progress(id, num_pieces, i);
}

void set_piece_hashes(std::string const& id, libtorrent::create_torrent& t, std::string const& p, libtorrent::error_code& ec, set_piece_hashes_listener* listener) {
    set_piece_hashes(t, p, boost::bind(&set_piece_hashes_cb, _1, listener, id, t.num_pieces()), ec);
}

int get_boost_version() {
    return BOOST_VERSION;
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
