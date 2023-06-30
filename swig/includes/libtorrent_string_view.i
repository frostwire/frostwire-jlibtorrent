namespace libtorrent {
    class string_view {
    public:
        %extend {
            std::vector<int8_t> to_bytes() {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }
        }
    };
} // namespace libtorrent