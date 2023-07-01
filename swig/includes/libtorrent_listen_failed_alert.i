%ignore libtorrent::listen_failed_alert::address;
%extend listen_failed_alert {

    address get_address() {
        return $self->address;
    }
}