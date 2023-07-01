%ignore libtorrent::udp_error_alert::endpoint;
namespace libtorrent {
    %extend udp_error_alert {
        udp::endpoint get_endpoint() {
            return $self->endpoint;
        }
    } // udp_error_alert
} // namespace libtorrent