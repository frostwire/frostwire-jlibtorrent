
namespace libtorrent {
    %extend dht_lookup {
        std::string get_type() {
            return std::string($self->type);
        }
    }
} // namespace libtorrent
