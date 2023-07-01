%ignore libtorrent::tracker_alert::local_endpoint;
namespace libtorrent {
    %extend tracker_alert {
        tcp::endpoint get_local_endpoint() {
            return $self->local_endpoint;
        }
    } // tracker_alert
} // namespace libtorrent