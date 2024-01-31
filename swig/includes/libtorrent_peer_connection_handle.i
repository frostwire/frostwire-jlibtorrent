%ignore libtorrent::peer_connection_handle::native_handle;
%ignore libtorrent::peer_connection_handle::add_extension;
%ignore libtorrent::peer_connection_handle::find_plugin;
%ignore libtorrent::peer_connection_handle::time_of_last_unchoke;
%ignore libtorrent::peer_connection_handle::should_log;
%ignore libtorrent::peer_connection_handle::peer_log;

%include "libtorrent/peer_connection_handle.hpp"

namespace libtorrent {
    %extend peer_connection_handle {

        int64_t get_time_of_last_unchoke() {
            return libtorrent::total_milliseconds($self->time_of_last_unchoke() - libtorrent::clock_type::now());
        }
    } // peer_connection_handle
} // namespace libtorrent
