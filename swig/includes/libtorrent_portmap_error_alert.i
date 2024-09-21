namespace libtorrent {
    %extend portmap_error_alert {
        int get_mapping() {
            return static_cast<int>($self->mapping);
        }
    }
} // namespace libtorrent