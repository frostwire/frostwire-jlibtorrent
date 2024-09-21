namespace libtorrent {
    %extend portmap_alert {
        int get_mapping() {
            return static_cast<int>($self->mapping);
        }
    }
} // namespace libtorrent