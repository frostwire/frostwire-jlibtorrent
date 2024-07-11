namespace libtorrent {
    struct peer_request;
} // namespace libtorrent

%include "libtorrent/file_storage.hpp"

namespace libtorrent {
    %extend file_storage {
        void add_file_ex(error_code& ec, std::string const& path, std::int64_t file_size, libtorrent::file_flags_t file_flags = {}, std::time_t mtime = 0, std::string symlink_path = {}) {
            //, char const* root_hash = nullptr) {
            $self->add_file(ec, path, file_size, file_flags, mtime, symlink_path);
        }

        std::string file_name_ex(int index) {
            return std::string{$self->file_name(libtorrent::file_index_t{index})};
        }

        std::vector<std::string> file_paths_ex() {
            auto paths = $self->paths();
            std::vector<std::string> result;
            result.clear();
            result.reserve(paths.size());
            for (std::string const& path : paths) {
                result.push_back(path);
            }
            return result;
        }
    } // file_storage
} // namespace libtorrent