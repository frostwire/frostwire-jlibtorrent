#include "libtorrent/buffer.hpp"

struct swig_torrent_plugin;
struct swig_peer_plugin;

struct swig_plugin : plugin {

    virtual ~swig_plugin() {
    }

    boost::shared_ptr<torrent_plugin> new_torrent(torrent*, void*);

    virtual swig_torrent_plugin* new_torrent(libtorrent::torrent *t);

    void added(aux::session_impl*) {
        added();
    }

    virtual void added() {
    }

    virtual void on_alert(libtorrent::alert const* a) {
    }

    virtual bool on_unknown_torrent(libtorrent::sha1_hash const& info_hash, libtorrent::peer_connection* pc, libtorrent::add_torrent_params& p) {
         return false;
    }

    virtual void on_tick() {
    }

    virtual bool on_optimistic_unchoke(std::vector<libtorrent::policy::peer*>& peers) {
        return false;
    }

    virtual void save_state(libtorrent::entry& e) const {
    }

    virtual void load_state(libtorrent::lazy_entry const& n) {
    }
};

struct swig_torrent_plugin: torrent_plugin
{
    virtual ~swig_torrent_plugin() {
    }

    boost::shared_ptr<peer_plugin> new_connection(peer_connection* pc);

    virtual swig_peer_plugin* new_peer_connection(libtorrent::peer_connection *pc);
    virtual swig_peer_plugin* new_bt_peer_connection(libtorrent::bt_peer_connection *pc);
    virtual swig_peer_plugin* new_web_peer_connection(libtorrent::web_peer_connection *pc);
    virtual swig_peer_plugin* new_http_seed_connection(libtorrent::http_seed_connection *pc);

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

    virtual bool on_extension_handshake(libtorrent::lazy_entry const& n) {
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

    virtual void on_piece_pass(int index) {}
    virtual void on_piece_failed(int index) {}

    virtual void tick() {}

    virtual bool write_request(libtorrent::peer_request const& r) {
        return false;
    }
};

boost::shared_ptr<torrent_plugin> swig_plugin::new_torrent(torrent* t, void*) {
    return boost::shared_ptr<torrent_plugin>(new_torrent(t));
}

swig_torrent_plugin* swig_plugin::new_torrent(torrent *t) {
    return new swig_torrent_plugin();
}

boost::shared_ptr<peer_plugin> swig_torrent_plugin::new_connection(peer_connection* pc) {

    bt_peer_connection *bt_pc = dynamic_cast<libtorrent::bt_peer_connection *>(pc);
    if (bt_pc != NULL) {
        return boost::shared_ptr<peer_plugin>(new_bt_peer_connection(bt_pc));
    }

    web_peer_connection *web_pc = dynamic_cast<libtorrent::web_peer_connection *>(pc);
    if (web_pc != NULL) {
        return boost::shared_ptr<peer_plugin>(new_web_peer_connection(web_pc));
    }

    http_seed_connection *http_seed_pc = dynamic_cast<libtorrent::http_seed_connection *>(pc);
    if (http_seed_pc != NULL) {
        return boost::shared_ptr<peer_plugin>(new_http_seed_connection(http_seed_pc));
    }

    return boost::shared_ptr<peer_plugin>(new_peer_connection(pc));
}

swig_peer_plugin* swig_torrent_plugin::new_peer_connection(libtorrent::peer_connection *pc) {
    return new swig_peer_plugin();
}

swig_peer_plugin* swig_torrent_plugin::new_bt_peer_connection(libtorrent::bt_peer_connection *pc) {
    return new swig_peer_plugin();
}

swig_peer_plugin* swig_torrent_plugin::new_web_peer_connection(libtorrent::web_peer_connection *pc) {
    return new swig_peer_plugin();
}

swig_peer_plugin* swig_torrent_plugin::new_http_seed_connection(libtorrent::http_seed_connection *pc) {
    return new swig_peer_plugin();
}
