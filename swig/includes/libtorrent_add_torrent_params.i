%ignore libtorrent::add_torrent_params::add_torrent_params;
%ignore libtorrent::add_torrent_params::banned_peers;
%ignore libtorrent::add_torrent_params::deprecated1;
%ignore libtorrent::add_torrent_params::deprecated2;
%ignore libtorrent::add_torrent_params::deprecated3;
%ignore libtorrent::add_torrent_params::deprecated4;
%ignore libtorrent::add_torrent_params::deprecated5;
%ignore libtorrent::add_torrent_params::dht_nodes;
%ignore libtorrent::add_torrent_params::extensions;
%ignore libtorrent::add_torrent_params::file_priorities;
%ignore libtorrent::add_torrent_params::have_pieces;
%ignore libtorrent::add_torrent_params::http_seeds;
%ignore libtorrent::add_torrent_params::merkle_tree_mask;
%ignore libtorrent::add_torrent_params::merkle_trees;
%ignore libtorrent::add_torrent_params::peers;
%ignore libtorrent::add_torrent_params::piece_priorities;
%ignore libtorrent::add_torrent_params::renamed_files;
%ignore libtorrent::add_torrent_params::storage;
%ignore libtorrent::add_torrent_params::ti;
%ignore libtorrent::add_torrent_params::tracker_tiers;
%ignore libtorrent::add_torrent_params::trackers;
%ignore libtorrent::add_torrent_params::unfinished_pieces;
%ignore libtorrent::add_torrent_params::url_seeds;
%ignore libtorrent::add_torrent_params::userdata;
%ignore libtorrent::add_torrent_params::verified_leaf_hashes;
%ignore libtorrent::add_torrent_params::verified_pieces;

%include "libtorrent/add_torrent_params.hpp"

namespace libtorrent {
    %extend add_torrent_params {

        libtorrent::torrent_info const* ti_ptr() {
            return $self->ti.get();
        }

        void set_ti(libtorrent::torrent_info const& ti) {
            $self->ti = std::make_shared<libtorrent::torrent_info>(ti);
        }

        void set_renamed_files(std::map<file_index_t, std::string> const& renamed_files) {
            $self->renamed_files = renamed_files;
        }

        std::vector<int> get_tracker_tiers() {
            return $self->tracker_tiers;
        }

        void set_tracker_tiers(std::vector<int> const& tracker_tiers) {
            $self->tracker_tiers = tracker_tiers;
        }

        std::vector<tcp::endpoint> get_banned_peers() {
            return $self->banned_peers;
        }

        void set_banned_peers(std::vector<tcp::endpoint> const& banned_peers) {
            $self->banned_peers = banned_peers;
        }

        std::vector<tcp::endpoint> get_peers() {
            return $self->peers;
        }

        void set_peers(std::vector<tcp::endpoint> const& peers) {
            $self->peers = peers;
        }

        void set_file_priorities2(std::vector<std::int8_t> const& file_priorities) {
            std::vector<download_priority_t> v(file_priorities.size());
            for (std::size_t i = 0; i < v.size(); i++)
                v[i] = download_priority_t{std::uint8_t(file_priorities[i])};
            $self->file_priorities = v;
        }

        std::vector<std::pair<std::string, int>> get_dht_nodes() {
            return $self->dht_nodes;
        }

        void set_dht_nodes(std::vector<std::pair<std::string, int>> const& dht_nodes) {
            $self->dht_nodes = dht_nodes;
        }

        void set_http_seeds(std::vector<std::string> const& http_seeds) {
            $self->http_seeds = http_seeds;
        }

        std::vector<std::string> get_url_seeds() {
            return $self->url_seeds;
        }

        void set_url_seeds(std::vector<std::string> const& url_seeds) {
            $self->url_seeds = url_seeds;
        }

        std::vector<std::string>  get_trackers() {
            return $self->trackers;
        }

        void set_trackers(std::vector<std::string> const& trackers) {
            $self->trackers = trackers;
        }

        std::vector<std::vector<libtorrent::sha256_hash>> get_merkle_trees() {
            auto* v = &$self->merkle_trees;
            return *reinterpret_cast<std::vector<std::vector<libtorrent::sha256_hash>>*>(v);
        }

        void set_merkle_trees(std::vector<std::vector<libtorrent::sha256_hash>>& v) {
            auto* t = reinterpret_cast<libtorrent::aux::vector<std::vector<libtorrent::sha256_hash>, libtorrent::file_index_t>*>(&v);
            $self->merkle_trees = *t;
        }

        void set_piece_priorities2(std::vector<std::int8_t> const& piece_priorities) {
            std::vector<download_priority_t> v(piece_priorities.size());
            for (std::size_t i = 0; i < v.size(); i++)
                v[i] = download_priority_t{std::uint8_t(piece_priorities[i])};
            $self->piece_priorities = v;
        }

        static libtorrent::add_torrent_params create_instance() {
            return libtorrent::add_torrent_params();
        }

        static libtorrent::add_torrent_params create_instance_disabled_storage() {
            return libtorrent::add_torrent_params(libtorrent::disabled_storage_constructor);
        }

        static libtorrent::add_torrent_params create_instance_zero_storage() {
            return libtorrent::add_torrent_params(libtorrent::zero_storage_constructor);
        }

        void set_default_storage()
        {
            $self->storage = libtorrent::default_storage_constructor;
        }

        void set_disabled_storage()
        {
            $self->storage = libtorrent::disabled_storage_constructor;
        }

        void set_zero_storage()
        {
            $self->storage = libtorrent::zero_storage_constructor;
        }

        static libtorrent::add_torrent_params read_resume_data(libtorrent::bdecode_node const& rd, error_code& ec) {
            return libtorrent::read_resume_data(rd, ec);
        }

        static libtorrent::add_torrent_params read_resume_data(std::vector<int8_t> const& buffer, error_code& ec) {
            return libtorrent::read_resume_data({(char const*)&buffer[0], static_cast<long>(buffer.size())}, ec);
        }

        static libtorrent::entry write_resume_data(add_torrent_params const& atp) {
            return libtorrent::write_resume_data(atp);
        }

        static std::vector<int8_t> write_resume_data_buf(add_torrent_params const& atp) {
            auto v = libtorrent::write_resume_data_buf(atp);
            return {v.begin(), v.end()};
        }

        static add_torrent_params parse_magnet_uri(std::string const& uri, error_code& ec) {
            return libtorrent::parse_magnet_uri(uri, ec);
        }
    } // end of %extend add_torrent_params
} // end of namespace libtorrent
