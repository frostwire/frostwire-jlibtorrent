#include "libtorrent/kademlia/dht_tracker.hpp"
#include "libtorrent/kademlia/node_entry.hpp"
#include "libtorrent/kademlia/node.hpp"
#include "libtorrent/kademlia/get_peers.hpp"

using namespace libtorrent::dht;

// BEGIN PRIVATE HACK
#include "libtorrent/aux_/session_impl.hpp"

using namespace libtorrent::aux;

template<typename Tag>
struct result {
  /* export it ... */
  typedef typename Tag::type type;
  static type ptr;
};

template<typename Tag>
typename result<Tag>::type result<Tag>::ptr;

template<typename Tag, typename Tag::type p>
struct rob : result<Tag> {
  /* fill it ... */
  struct filler {
    filler() { result<Tag>::ptr = p; }
  };
  static filler filler_obj;
};

template<typename Tag, typename Tag::type p>
typename rob<Tag, p>::filler rob<Tag, p>::filler_obj;

struct session_m_impl { typedef boost::shared_ptr<aux::session_impl> session::*type; };
template class rob<session_m_impl, &session::m_impl>;

struct dht_tracker_m_dht { typedef node_impl dht_tracker::*type; };
template class rob<dht_tracker_m_dht, &dht_tracker::m_dht>;
// END PRIVATE HACK

#define TORRENT_DEFINE_ALERT(name) \
	const static int alert_type = __LINE__; \
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

	TORRENT_DEFINE_ALERT(dht_get_peers_reply_alert); // same line as other alert?

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

	boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;
    boost::intrusive_ptr<dht::dht_tracker> s_dht_tracker = s_impl->m_dht;
    const reference_wrapper<libtorrent::dht::node_impl> node = boost::ref(*s_dht_tracker.*result<dht_tracker_m_dht>::ptr);

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

	boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;
    boost::intrusive_ptr<dht::dht_tracker> s_dht_tracker = s_impl->m_dht;
    const reference_wrapper<libtorrent::dht::node_impl> node = boost::ref(*s_dht_tracker.*result<dht_tracker_m_dht>::ptr);

	node.get().announce(info_hash, port, flags, boost::bind(&dht_get_peers_fun, _1, s_impl, info_hash));
}

void dht_announce(session* s, sha1_hash const& info_hash) {

	int port = s->listen_port();

	int flags = 0;
    // if we allow incoming uTP connections, set the implied_port
    // argument in the announce, this will make the DHT node use
    // our source port in the packet as our listen port, which is
    // likely more accurate when behind a NAT
    if (s->settings().enable_incoming_utp) flags |= dht::dht_tracker::flag_implied_port;

	dht_announce(s, info_hash, port, flags);
}
