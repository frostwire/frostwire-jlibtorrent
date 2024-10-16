namespace libtorrent {
    %extend listen_failed_alert {
        int get_socket_type() {
            return static_cast<int>($self->socket_type);
        }

        address get_address() {
            return $self->address;
        }
    } // listen_failed_alert
} // namespace libtorrent