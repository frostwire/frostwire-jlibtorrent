namespace libtorrent {
    %extend listen_succeeded_alert {
        address get_address() {
            return $self->address;
        }

        int get_socket_type() {
            return static_cast<int>($self->socket_type);
        }
    } // listen_succeeded_alert
} // namespace libtorrent