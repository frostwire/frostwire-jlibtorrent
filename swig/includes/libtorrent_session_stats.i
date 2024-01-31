%include "libtorrent/session_stats.hpp"

namespace libtorrent {
    %extend stats_metric{
        std::string get_name() {
            return std::string($self->name);
        }
    } // stats_metric
} // namespace libtorrent
