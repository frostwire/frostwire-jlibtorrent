%ignore libtorrent::create_torrent::add_url_seed(string_view);
%ignore libtorrent::create_torrent::add_http_seed(string_view);
%ignore libtorrent::create_torrent::add_tracker(string_view);
%ignore libtorrent::create_torrent::add_tracker(string_view, int);
%ignore libtorrent::create_torrent::add_collection(string_view);
%ignore libtorrent::create_torrent::set_root_cert;

#include "libtorrent/create_torrent.hpp"
//%include "libtorrent/create_torrent.hpp"

namespace libtorrent {
    %extend create_torrent {

        void add_url_seed(std::string const& url) {
            $self->add_url_seed(url);
        }

        void add_http_seed(std::string const& url) {
            $self->add_http_seed(url);
        }

        void add_tracker(std::string const& url, int tier) {
            $self->add_tracker(url, tier);
        }

        void add_collection(std::string const& c) {
            $self->add_collection(c);
        }

        void set_root_cert2(std::vector<int8_t> const& pem)
        {
            std::string s{pem.begin(), pem.end()};
            $self->set_root_cert(s);
        }
    } // create_torrent
} // namespace libtorrent