namespace libtorrent {
    %extend incoming_connection_alert {
        tcp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // income_connection_alert
} // namespace libtorrent