namespace libtorrent {
    %extend file_progress_alert {
        std::vector<std::int64_t> get_files() {
            auto* v = &$self->files;
            return *reinterpret_cast<std::vector<std::int64_t>*>(v);
        }
    }
} // namespace libtorrent