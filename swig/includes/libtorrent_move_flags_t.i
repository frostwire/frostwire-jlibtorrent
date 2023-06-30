namespace libtorrent {
    enum class move_flags_t : std::uint8_t {
        always_replace_files,
        fail_if_exist,
        dont_replace
    };
} // namespace libtorrent