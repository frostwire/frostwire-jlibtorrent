%ignore libtorrent::listen_failed_alert::address;
namespace libtorrent {
    %extend listen_failed_alert {
        address get_address() {
            return $self->address;
        }
    } // listen_failed_alert
} // namespace libtorrent