%ignore libtorrent::peer_alert::endpoint;

namespace libtorrent {
    %extend peer_alert {
        tcp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    }
}
