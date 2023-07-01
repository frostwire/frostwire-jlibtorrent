%ignore libtorrent::listen_succeeded_alert::address;
namespace libtorrent {
    %extend listen_succeeded_alert {

        address get_address() {
            return $self->address;
        }
    } // listen_succeeded_alert
} // namespace libtorrent