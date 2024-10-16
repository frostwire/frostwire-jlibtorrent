namespace libtorrent {
    %extend udp_error_alert {
        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // udp_error_alert
} // namespace libtorrent