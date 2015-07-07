#include "session_plugins.h"

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

struct session_impl_m_upnp { typedef boost::shared_ptr<upnp> session_impl::*type; };
template class rob<session_impl_m_upnp, &session_impl::m_upnp>;

// END PRIVATE HACK

#define TORRENT_DEFINE_ALERT_IMPL(name, seq, prio) \
	static const int priority = prio; \
	static const int alert_type = seq; \
	virtual int type() const { return alert_type; } \
	virtual int category() const { return static_category; } \
	virtual char const* what() const { return #name; }

namespace libtorrent {

struct set_piece_hashes_alert: alert {

	set_piece_hashes_alert(aux::stack_allocator& alloc, std::string const& id, int progress, int num_pieces)
		: id(id),
		  progress(progress),
		  num_pieces(num_pieces){
	}

	TORRENT_DEFINE_ALERT_IMPL(set_piece_hashes_alert, user_alert_id + 101, 0);

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

// search for nodes with ids close to id or with peers
// for info-hash id
void dht_get_peers(session* s, sha1_hash const& info_hash) {

    boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;

    s_impl->dht_get_peers(info_hash);
}

void dht_announce(session* s, sha1_hash const& info_hash, int port, int flags) {

    boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;

    s_impl->dht_announce(info_hash, port, flags);
}

void set_piece_hashes_fun(int i, boost::shared_ptr<aux::session_impl> s, std::string& id, int num_pieces) {
	if (s->alerts().should_post<set_piece_hashes_alert>()) {
        s->alerts().emplace_alert<set_piece_hashes_alert>(id, i + 1, num_pieces);
    }
}

void set_piece_hashes(session* s, std::string const& id, libtorrent::create_torrent& t, std::string const& p, error_code& ec) {

    boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;

	set_piece_hashes(t, p, boost::bind(&set_piece_hashes_fun, _1, s_impl, id, t.num_pieces()), ec);
}

upnp* get_upnp(session* s) {

    boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;
    boost::shared_ptr<upnp> m_upnp = *s_impl.*result<session_impl_m_upnp>::ptr;

    return m_upnp ? m_upnp.get() : NULL;
}
