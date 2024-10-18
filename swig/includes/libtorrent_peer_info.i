namespace libtorrent {
  typedef sha1_hash peer_id;
}

%include "libtorrent/peer_info.hpp"

namespace libtorrent {
    struct connection_type_tag;
    %template(connection_type_t) libtorrent::flags::bitfield_flag<std::uint8_t, connection_type_tag>;

    %extend peer_info {
        std::vector<int8_t> get_client() {
            std::string s = $self->client;
            return {s.begin(), s.end()};
        }

        int64_t get_last_request() {
            return libtorrent::total_milliseconds($self->last_request);
        }

        int64_t get_last_active() {
            return libtorrent::total_milliseconds($self->last_active);
        }

        int64_t get_download_queue_time() {
            return libtorrent::total_milliseconds($self->download_queue_time);
        }

        std::int32_t get_flags() {
            return std::int32_t(static_cast<std::uint32_t>($self->flags));
        }

        std::int8_t get_source() {
            return std::int8_t(static_cast<std::uint8_t>($self->source));
        }

        std::int8_t get_read_state() {
            return std::int8_t(static_cast<std::uint8_t>($self->read_state));
        }

        std::int8_t get_write_state() {
            return std::int8_t(static_cast<std::uint8_t>($self->write_state));
        }

        bitfield get_pieces() {
            auto* v = &$self->pieces;
            return *reinterpret_cast<libtorrent::bitfield*>(v);
        }
    } // %extend peer_info
} // namespace libtorrent