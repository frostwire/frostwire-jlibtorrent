#include "libtorrent/kademlia/dht_tracker.hpp"
#include "libtorrent/kademlia/node_entry.hpp"
#include "libtorrent/kademlia/node.hpp"
#include "libtorrent/kademlia/get_peers.hpp"

using namespace libtorrent::dht;

// IMPORTANT NOTE: To use this code, you need to make public m_impl in session
// and m_dht in dht_tracker
#include "libtorrent/aux_/session_impl.hpp"

#define TORRENT_DEFINE_ALERT(name, seq) \
	static const int alert_type = seq; \
	virtual int type() const { return alert_type; } \
	virtual std::auto_ptr<alert> clone() const \
	{ return std::auto_ptr<alert>(new name(*this)); } \
	virtual int category() const { return static_category; } \
	virtual char const* what() const { return #name; }

namespace libtorrent {

struct dht_get_peers_reply_alert: alert {

	dht_get_peers_reply_alert(sha1_hash const& ih, std::vector<tcp::endpoint> const& v)
		: info_hash(ih), peers(v) {
	}

	TORRENT_DEFINE_ALERT(dht_get_peers_reply_alert, user_alert_id + 100);

	const static int static_category = alert::dht_notification;

	std::string message() const {
    	char ih_hex[41];
    	to_hex((const char*)&info_hash[0], 20, ih_hex);
    	char msg[200];
    	snprintf(msg, sizeof(msg), "incoming dht get_peers reply: %s, peers %ld", ih_hex, peers.size());
    	return msg;
    }

	sha1_hash info_hash;
	std::vector<tcp::endpoint> peers;
};

struct set_piece_hashes_alert: alert {

	set_piece_hashes_alert(std::string const& id, int progress, int num_pieces)
		: id(id),
		  progress(progress),
		  num_pieces(num_pieces){
	}

	TORRENT_DEFINE_ALERT(set_piece_hashes_alert, user_alert_id + 101);

	const static int static_category = alert::progress_notification;

	std::string message() const {
    	char msg[200];
    	snprintf(msg, sizeof(msg), "creating torrent %s, piece hash progress %d/%d", id.c_str(), progress, num_pieces);
    	return msg;
    }

	std::string id;
	int progress;
	int num_pieces;
};

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

void dht_get_peers_fun(std::vector<tcp::endpoint> const& peers,
						boost::shared_ptr<aux::session_impl> s, sha1_hash const& ih) {

	if (s->m_alerts.should_post<dht_get_peers_reply_alert>()) {
		s->m_alerts.post_alert(dht_get_peers_reply_alert(ih, peers));
	}
}

// search for nodes with ids close to id or with peers
// for info-hash id
void dht_get_peers(session* s, sha1_hash const& info_hash) {

	boost::shared_ptr<aux::session_impl> s_impl = s->m_impl;
    dht::dht_tracker* s_dht_tracker = s_impl->dht();
    const reference_wrapper<libtorrent::dht::node_impl> node = boost::ref(s_dht_tracker->m_dht);

	bool privacy_lookups = node.get().settings().privacy_lookups;

    boost::intrusive_ptr<get_peers> ta;
	if (privacy_lookups)
	{
		ta.reset(new obfuscated_get_peers(node, info_hash,
			boost::bind(&dht_get_peers_fun, _1, s_impl, info_hash), NULL, 0));
	}
	else
	{
		ta.reset(new get_peers(node, info_hash,
			boost::bind(&dht_get_peers_fun, _1, s_impl, info_hash), NULL, 0));
	}

	ta->start();
}

void dht_announce(session* s, sha1_hash const& info_hash, int port, int flags) {

	boost::shared_ptr<aux::session_impl> s_impl = s->m_impl;
    dht::dht_tracker* s_dht_tracker = s_impl->dht();
    const reference_wrapper<libtorrent::dht::node_impl> node = boost::ref(s_dht_tracker->m_dht);

	node.get().announce(info_hash, port, flags, boost::bind(&dht_get_peers_fun, _1, s_impl, info_hash));
}

void dht_announce(session* s, sha1_hash const& info_hash) {

	int port = s->listen_port();

	int flags = 0;
    // if we allow incoming uTP connections, set the implied_port
    // argument in the announce, this will make the DHT node use
    // our source port in the packet as our listen port, which is
    // likely more accurate when behind a NAT
    if (s->get_settings().get_bool(settings_pack::enable_incoming_utp)) flags |= dht::dht_tracker::flag_implied_port;

	dht_announce(s, info_hash, port, flags);
}

void set_piece_hashes_fun(int i, boost::shared_ptr<aux::session_impl> s, std::string& id, int num_pieces) {
	if (s->m_alerts.should_post<set_piece_hashes_alert>()) {
        s->m_alerts.post_alert(set_piece_hashes_alert(id, i + 1, num_pieces));
    }
}

void set_piece_hashes(session* s, std::string const& id, libtorrent::create_torrent& t, std::string const& p, error_code& ec) {

    boost::shared_ptr<aux::session_impl> s_impl = s->m_impl;

	set_piece_hashes(t, p, boost::bind(&set_piece_hashes_fun, _1, s_impl, id, t.num_pieces()), ec);
}
