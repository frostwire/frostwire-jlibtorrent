#include "libtorrent/file_storage.hpp"

namespace libtorrent {
    struct peer_request;
} // namespace libtorrent

namespace libtorrent {
    struct file_flags_tag;
    %template(file_flags_t) flags::bitfield_flag<std::uint8_t, file_flags_tag>;

    %extend file_storage {
        void add_file(std::string const& path, std::int64_t file_size,
            libtorrent::file_flags_t file_flags, std::time_t mtime, std::string const& symlink_path) {
            $self->add_file(path, file_size, file_flags, mtime, symlink_path);
        }

        void add_file_ex(error_code& ec, std::string const& path, std::int64_t file_size, libtorrent::file_flags_t file_flags = {}, std::time_t mtime = 0, std::string symlink_path = {}) {
            //, char const* root_hash = nullptr) {
            $self->add_file(ec, path, file_size, file_flags, mtime, symlink_path);
        }

        std::string file_name(int index) {
            return std::string{$self->file_name(lt::file_index_t{index})};
        }
    } // file_storage
} // namespace libtorrent