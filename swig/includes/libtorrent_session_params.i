%include "libtorrent/session_params.hpp"

namespace libtorrent {

    %extend session_params {
        static libtorrent::session_params read_session_params(libtorrent::bdecode_node const& e
            , libtorrent::save_state_flags_t flags = libtorrent::save_state_flags_t::all())
        {
            return libtorrent::read_session_params(e, flags);
        }

        static libtorrent::entry write_session_params(libtorrent::session_params const& sp
            , libtorrent::save_state_flags_t flags = libtorrent::save_state_flags_t::all())
        {
            return libtorrent::write_session_params(sp, flags);
        }

        static std::vector<std::int8_t> write_session_params_buf(
            libtorrent::session_params const& sp
            , libtorrent::save_state_flags_t flags = libtorrent::save_state_flags_t::all())
        {
            auto v = libtorrent::write_session_params_buf(sp, flags);
            return {v.begin(), v.end()};
        }

        void set_posix_disk_io_constructor()
        {
            $self->disk_io_constructor = libtorrent::posix_disk_io_constructor;
        }

        void set_default_disk_io_constructor()
        {
            $self->disk_io_constructor = libtorrent::default_disk_io_constructor;
        }
    }

} // namespace libtorrent

