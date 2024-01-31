namespace libtorrent {
    %extend external_ip_alert {

        address get_external_address() {
            return $self->external_address;
        }
    } // external_ip_alert
} // namespace libtorrent