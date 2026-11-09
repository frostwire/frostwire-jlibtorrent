// libtorrent 2.1: a peer's endpoint is a std::variant of an IP endpoint and an i2p destination
// hash (peer_endpoint_t); the old tcp::endpoint member is gone.
namespace libtorrent {
    %extend peer_alert {
        // the peer's IP endpoint, or a default (unspecified address, port 0) one for an i2p peer
        tcp::endpoint get_endpoint() {
            if (auto const* e = std::get_if<libtorrent::aux::noexcept_movable<tcp::endpoint>>(&$self->ep))
                return *e;
            return tcp::endpoint();
        }

        // peer_alert::ip_endpoint or peer_alert::i2p_endpoint
        int get_endpoint_type() {
            return static_cast<int>($self->ep.index());
        }

        // the peer's i2p destination hash, or all zeros for an IP peer
        libtorrent::sha256_hash get_i2p_destination() {
            if (auto const* h = std::get_if<libtorrent::sha256_hash>(&$self->ep))
                return *h;
            return libtorrent::sha256_hash();
        }
    } // peer_alert
} // namespace libtorrent
