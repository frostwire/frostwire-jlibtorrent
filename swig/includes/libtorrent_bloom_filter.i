namespace libtorrent {
    template <int N>
    struct bloom_filter {

        bool find(sha1_hash const& k) const;
        void set(sha1_hash const& k);

        void clear();

        float size() const;

        bloom_filter();

        %extend {
            std::vector<int8_t> to_bytes() const {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }

            void from_bytes(std::vector<int8_t> const& v) {
                $self->from_string(reinterpret_cast<char const*>(&v[0]));
            }
        }
    };

    %template(bloom_filter_128) bloom_filter<128>;
    %template(bloom_filter_256) bloom_filter<256>;
} // namespace libtorrent