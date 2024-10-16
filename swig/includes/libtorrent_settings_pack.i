%include "libtorrent/settings_pack.hpp"

namespace libtorrent {

    %extend settings_pack {
        std::vector<std::int8_t> get_bytes(int name) {
            auto v = $self->get_str(name);
            return std::vector<std::int8_t>(v.begin(), v.end());
        }

        void set_bytes(int name, std::vector<std::int8_t> v) {
            $self->set_str(name, std::string(v.begin(), v.end()));
        }
    }

} // namespace libtorrent
