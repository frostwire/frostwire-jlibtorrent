%ignore libtorrent::stats_alert::transferred;
%ignore libtorrent::stats_alert::deprecated1;
%ignore libtorrent::stats_alert::deprecated2;
%ignore libtorrent::stats_alert::deprecated3;
%ignore libtorrent::stats_alert::deprecated4;

namespace libtorrent {
    %extend stats_alert {
        int get_transferred(int index) {
            return $self->transferred[index];
        }
    } // stats_alert
} // namespace libtorrent
