%include "libtorrent/peer_class.hpp"

namespace libtorrent {
    struct peer_class_info {
        bool ignore_unchoke_slots;
        int connection_limit_factor;
        std::string label;
        int upload_limit;
        int download_limit;
        int upload_priority;
        int download_priority;
    }; // struct peer_class_info
} // namespace libtorrent