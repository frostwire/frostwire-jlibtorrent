%ignore libtorrent::session_stats_alert::counters;
namespace libtorrent {
    %extend session_stats_alert {
        long long get_value(int index) {
            return $self->counters()[index];
        }
    } // session_stats_alert
} // namespace libtorrent
