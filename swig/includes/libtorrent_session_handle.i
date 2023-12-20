%include "libtorrent/session_handle.hpp"

namespace libtorrent {
    %extend session_handle {

        void dht_get_item(std::vector<int8_t>& key, std::vector<int8_t>& salt) {
            if (key.size() != 32) {
                throw std::invalid_argument("Public key must be of size 32");
            }
            std::array<char, 32> pk;
            std::copy_n(key.begin(), 32, pk.begin());

            $self->dht_get_item(pk, std::string(salt.begin(), salt.end()));
        }

        void dht_put_item(std::vector<int8_t>& key, std::vector<int8_t>& sk, entry& data, std::vector<int8_t>& salt) {
            if (key.size() != 32) {
                throw std::invalid_argument("Public key must be of size 32");
            }
            if (sk.size() != 64) {
                throw std::invalid_argument("Private key must be of size 64");
            }
            std::array<char, 32> pk;
            std::copy_n(key.begin(), 32, pk.begin());

            using namespace std::placeholders;
            using namespace libtorrent::dht;

            $self->dht_put_item(pk, std::bind(&dht_put_item_cb, _1, _2, _3, _4,
                public_key((char*)key.data()), secret_key((char*)sk.data()), data),
                std::string(salt.begin(), salt.end()));
        }

        void dht_direct_request(udp::endpoint const& ep, entry const& e, int64_t userdata) {
            $self->dht_direct_request(ep, e, libtorrent::client_data_t{(void*)userdata});
        }

        alert* wait_for_alert_ms(int64_t max_wait) {
            return $self->wait_for_alert(libtorrent::milliseconds(max_wait));
        }

        void set_alert_notify_callback(alert_notify_callback* cb) {
            $self->set_alert_notify(std::bind(&alert_notify_callback::on_alert, cb));
        }

        void add_extension(swig_plugin* ext) {
            $self->add_extension(std::shared_ptr<libtorrent::plugin>(ext));
        }
    }
}