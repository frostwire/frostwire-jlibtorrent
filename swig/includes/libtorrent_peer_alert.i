namespace libtorrent {
    %extend peer_alert {
        tcp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // peer_alert
} // namespace libtorrent
