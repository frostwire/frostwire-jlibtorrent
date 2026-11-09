namespace libtorrent {
    struct peer_request;

    %extend renamed_files {
        // the leaf file name of file index in fs, honoring a rename
        std::string get_file_name(file_storage const& fs, file_index_t index) const {
            return std::string($self->file_name(fs, index));
        }

        // the renames, as file index to new path
        std::map<int, std::string> get_renamed_files(file_storage const& fs) const {
            std::map<int, std::string> ret;
            for (auto const& e : $self->export_filenames(fs))
                ret.emplace(static_cast<int>(e.first), e.second);
            return ret;
        }
    } // renamed_files
} // namespace libtorrent

// libtorrent 2.1: renamed_files records a torrent's file renames without changing its file_storage
// (torrent_handle::get_renamed_files(), torrent_status::renamed_files). filenames is a view holding
// references to a file_storage and a renamed_files, which Java can't keep alive: use
// renamed_files::file_path() instead.
%ignore libtorrent::filenames;
%ignore libtorrent::renamed_files::file_name;
%ignore libtorrent::renamed_files::import_filenames;
%ignore libtorrent::renamed_files::export_filenames;

%include "libtorrent/file_storage.hpp"

namespace libtorrent {
    %extend file_storage {
        void add_file_ex(error_code& ec, std::string const& path, std::int64_t file_size, libtorrent::file_flags_t file_flags = {}, std::time_t mtime = 0, std::string symlink_path = {}) {
            $self->add_file(ec, path, file_size, file_flags, mtime, symlink_path);
        }

        std::string file_name_ex(int index) {
            return std::string{$self->file_name(libtorrent::file_index_t{index})};
        }

        std::vector<std::string> file_paths_ex() {
            std::vector<std::string> result;
            int num_files = $self->num_files();
            result.reserve(num_files);

            for (int i = 0; i < num_files; ++i) {
                std::string path = $self->file_path(libtorrent::file_index_t{i});
                result.push_back(path);
            }
            return result;
        }
    } // file_storage
} // namespace libtorrent