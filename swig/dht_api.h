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

void dht_get_peers_fun(std::vector<tcp::endpoint> const& v, sha1_hash const& ih) {

    std::cout << "got peers" << std::endl;
}

// search for nodes with ids close to id or with peers
// for info-hash id
void dht_get_peers(session* s, sha1_hash const& info_hash, bool privacy_lookups) {

	boost::shared_ptr<aux::session_impl> s_impl = *s.*result<session_m_impl>::ptr;
	boost::intrusive_ptr<dht::dht_tracker> s_dht_tracker = s_impl->m_dht;
	const reference_wrapper<libtorrent::dht::node_impl> node = boost::ref(*s_dht_tracker.*result<dht_tracker_m_dht>::ptr);

    boost::intrusive_ptr<get_peers> ta;
	if (privacy_lookups)
	{
		ta.reset(new obfuscated_get_peers(node, info_hash
			, boost::bind(&dht_get_peers_fun, _1, info_hash), NULL, 0));
	}
	else
	{
		ta.reset(new get_peers(node, info_hash
			, boost::bind(&dht_get_peers_fun, _1, info_hash), NULL, 0));
	}

	ta->start();
}
