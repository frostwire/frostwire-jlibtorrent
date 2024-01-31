namespace libtorrent {
    %extend dht_put_alert {
        std::array<std::int8_t, 32> get_public_key() {
            std::array<char, 32> arr = $self->public_key;
            return *reinterpret_cast<std::array<std::int8_t, 32>*>(&arr);
        }

        std::array<std::int8_t, 64> get_signature() {
            std::array<char, 64> arr = $self->signature;
            return *reinterpret_cast<std::array<std::int8_t, 64>*>(&arr);
        }

        std::vector<int8_t> get_salt() {
            std::string s = $self->salt;
            return std::vector<int8_t>(s.begin(), s.end());
        }

        int64_t get_seq() {
            return int64_t($self->seq);
        }
    }
} // namespace libtorrent