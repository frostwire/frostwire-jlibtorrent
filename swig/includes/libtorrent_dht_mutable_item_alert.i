namespace libtorrent {
    %extend dht_mutable_item_alert {

        std::vector<int8_t> get_key() {
            std::array<char, 32> arr = $self->key;
            return std::vector<int8_t>(arr.begin(), arr.end());
        }

        std::vector<int8_t> get_signature() {
            std::array<char, 64> arr = $self->signature;
            return std::vector<int8_t>(arr.begin(), arr.end());
        }

        int64_t get_seq() {
            return int64_t($self->seq);
        }

        std::vector<int8_t> get_salt() {
            std::string s = $self->salt;
            return std::vector<int8_t>(s.begin(), s.end());
        }
    }
}