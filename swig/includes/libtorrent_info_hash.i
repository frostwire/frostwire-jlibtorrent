%include "libtorrent/info_hash.hpp"

namespace libtorrent {
    %extend info_hash_t {
        bool op_eq(info_hash_t const& n) const {
            return *$self == n;
        }

        bool op_ne(info_hash_t const& n) const {
            return *$self != n;
        }
    } // info_hash_t
} // namespace libtorrent