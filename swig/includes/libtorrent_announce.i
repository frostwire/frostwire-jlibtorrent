%ignore libtorrent::announce_infohash::announce_infohash;
%ignore libtorrent::announce_infohash::next_announce;
%ignore libtorrent::announce_infohash::min_announce;
%ignore libtorrent::announce_entry::announce_entry(string_view);
%ignore libtorrent::announce_endpoint::info_hashes;

%include "libtorrent/announce_entry.hpp"

namespace libtorrent {

    %extend announce_infohash
    {
        std::int64_t get_next_announce()
        {
            return lt::total_milliseconds($self->next_announce.time_since_epoch());
        }

        std::int64_t get_min_announce()
        {
            return lt::total_milliseconds($self->min_announce.time_since_epoch());
        }
    }

    %extend announce_endpoint
    {
        announce_infohash get_infohash_v1()
        {
            return $self->info_hashes[lt::protocol_version::V1];
        }

        announce_infohash get_infohash_v2()
        {
            return $self->info_hashes[lt::protocol_version::V2];
        }
    }

    %extend announce_entry
    {
        announce_entry(std::string url)
        {
            return new lt::announce_entry(url);
        }
    }

    %extend announce_entry
    {
        std::vector<announce_endpoint> endpoints;

        std::uint8_t tier;
        std::uint8_t fail_limit;
        std::uint8_t source;
        bool verified;

        %extend
        {
            announce_entry(std::vector<int8_t> const& u)
            {
                return new libtorrent::announce_entry(
                    {reinterpret_cast<char const*>(u.data()), u.size()});
            }

            std::vector<int8_t> get_url()
            {
                std::string s = $self->url;
                return {s.begin(), s.end()};
            }

            void set_url(std::vector<int8_t> const& s)
            {
                $self->url = {s.begin(), s.end()};
            }

            std::vector<int8_t> get_trackerid()
            {
                std::string s = $self->trackerid;
                return {s.begin(), s.end()};
            }

            void set_trackerid(std::vector<int8_t> const& s)
            {
                $self->trackerid = {s.begin(), s.end()};
            }
        }
    };

}

//     struct announce_endpoint
//     {
//         libtorrent::error_code last_error;
//         tcp::endpoint local_endpoint;
//
//         int scrape_incomplete;
//         int scrape_complete;
//         int scrape_downloaded;
//
//         std::uint8_t fails;
//         bool updating;
//
//         bool is_working() const;
//
//         %extend
//         {
//             std::vector<int8_t> get_message()
//             {
//                 std::string s = $self->message;
//                 return {s.begin(), s.end()};
//             }
//
//             int64_t get_next_announce()
//             {
//                 return libtorrent::total_milliseconds($self->next_announce.time_since_epoch());
//             }
//
//             int64_t get_min_announce()
//             {
//                 return libtorrent::total_milliseconds($self->min_announce.time_since_epoch());
//             }
//         }
//     private:
//         announce_endpoint();
//     };