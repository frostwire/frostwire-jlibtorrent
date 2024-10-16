namespace libtorrent {
    enum class event_t : std::uint8_t {
        none,
        completed,
        started,
        stopped,
        paused
    };
} // namespace libtorrent