%include "libtorrent/create_torrent.hpp"

namespace libtorrent {
    %extend create_torrent {
        void add_url_seed(std::string const& url) {
            $self->add_url_seed(url);
        }

        void add_http_seed(std::string const& url) {
            $self->add_http_seed(url);
        }

        void add_tracker(std::string const& url) {
            $self->add_tracker(url);
        }

        void add_tracker(std::string const& url, int tier) {
            $self->add_tracker(url, tier);
        }

        void add_collection(std::string const& c) {
            $self->add_collection(c);
        }

        void set_root_cert2(std::vector<std::int8_t> const& pem) {
            std::string s{pem.begin(), pem.end()};
            $self->set_root_cert(s);
        }

        void set_root_cert(std::vector<std::int8_t> const& cert) {
            std::string v{cert.begin(), cert.end()};
            $self->set_root_cert(v);
        }

        void set_hash2(file_index_t file, int piece, sha256_hash const& h) {
            $self->set_hash2(file, libtorrent::piece_index_t::diff_type{piece}, h);
        }
    } // create_torrent
} // namespace libtorrent