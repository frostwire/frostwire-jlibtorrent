%ignore libtorrent::stats_metric::name;
namespace libtorrent {
    %extend stats_metric {
        std::string get_name() {
            return std::string($self->name);
        }
    } // stats_metric
} // namespace libtorrent
