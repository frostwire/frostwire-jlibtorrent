namespace libtorrent {
    enum class connection_type : std::uint8_t {
        bittorrent,
        url_seed,
        http_seed
    };
} // namespace libtorrent