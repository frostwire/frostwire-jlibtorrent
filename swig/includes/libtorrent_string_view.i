namespace libtorrent {
    class string_view {
    public:
        %extend {
            std::vector<int8_t> to_bytes() {
                return std::vector<int8_t>($self->begin(), $self->end());
            }
        }
    };
} // namespace libtorrent