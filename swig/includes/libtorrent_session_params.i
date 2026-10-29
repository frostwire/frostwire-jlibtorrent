%include "libtorrent/session_params.hpp"

namespace libtorrent {

    %extend session_params {
        // Explicit overloads instead of C++ default arguments: SWIG 4.5 re-resolves a default
        // argument's typedef and emits `struct save_state_flags_tag` outside namespace libtorrent,
        // which doesn't compile. The Java side gets the same two methods per function as before.
        static libtorrent::session_params read_session_params(libtorrent::bdecode_node const& e
            , libtorrent::save_state_flags_t flags)
        {
            return libtorrent::read_session_params(e, flags);
        }

        static libtorrent::session_params read_session_params(libtorrent::bdecode_node const& e)
        {
            return libtorrent::read_session_params(e, libtorrent::save_state_flags_t::all());
        }

        static libtorrent::entry write_session_params(libtorrent::session_params const& sp
            , libtorrent::save_state_flags_t flags)
        {
            return libtorrent::write_session_params(sp, flags);
        }

        static libtorrent::entry write_session_params(libtorrent::session_params const& sp)
        {
            return libtorrent::write_session_params(sp, libtorrent::save_state_flags_t::all());
        }

        static std::vector<std::int8_t> write_session_params_buf(
            libtorrent::session_params const& sp
            , libtorrent::save_state_flags_t flags)
        {
            auto v = libtorrent::write_session_params_buf(sp, flags);
            return {v.begin(), v.end()};
        }

        static std::vector<std::int8_t> write_session_params_buf(libtorrent::session_params const& sp)
        {
            auto v = libtorrent::write_session_params_buf(sp, libtorrent::save_state_flags_t::all());
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

