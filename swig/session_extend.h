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

struct session_impl_m_upnp { typedef boost::shared_ptr<upnp> session_impl::*type; };
template class rob<session_impl_m_upnp, &session_impl::m_upnp>;

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

upnp* get_upnp(session_handle* s) {

    boost::shared_ptr<aux::session_impl> s_impl(s->native_handle());
    boost::shared_ptr<upnp> m_upnp = *s_impl.*result<session_impl_m_upnp>::ptr;

    return m_upnp ? m_upnp.get() : NULL;
}
