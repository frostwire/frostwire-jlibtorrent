class set_piece_hashes_listener {
public:
    virtual ~set_piece_hashes_listener() {
    }

    virtual void progress(std::string const& id, int num_pieces, int i) {
    }
};

void set_piece_hashes_cb(int i, set_piece_hashes_listener& listener, std::string& id, int num_pieces) {
	listener.progress(id, num_pieces, i);
}

void set_piece_hashes(std::string const& id, libtorrent::create_torrent& t, std::string const& p, libtorrent::error_code& ec, set_piece_hashes_listener& listener) {

    set_piece_hashes(t, p, boost::bind(&set_piece_hashes_cb, _1, listener, id, t.num_pieces()), ec);
}
