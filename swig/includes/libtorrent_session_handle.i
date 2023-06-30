%ignore libtorrent::session_handle::add_default_plugins;
%ignore libtorrent::session_handle::add_extension(std::function<std::shared_ptr<torrent_plugin>(torrent_handle const&, void*)>);
%ignore libtorrent::session_handle::add_extension(std::shared_ptr<plugin>);
%ignore libtorrent::session_handle::add_extension;
%ignore libtorrent::session_handle::add_port_mapping;
%ignore libtorrent::session_handle::add_torrent(add_torrent_params&&, error_code&);
%ignore libtorrent::session_handle::apply_settings(settings_pack&&);
%ignore libtorrent::session_handle::async_add_torrent(add_torrent_params&&);
%ignore libtorrent::session_handle::create_peer_class;
%ignore libtorrent::session_handle::delete_peer_class;
%ignore libtorrent::session_handle::delete_port_mapping;
%ignore libtorrent::session_handle::dht_announce;
%ignore libtorrent::session_handle::dht_direct_request(udp::endpoint const&, entry const&, void*);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>);
%ignore libtorrent::session_handle::dht_get_item(std::array<char, 32>, std::string);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::int64_t&, std::string const&)>);
%ignore libtorrent::session_handle::dht_put_item(std::array<char, 32>, std::function<void(entry&, std::array<char,64>&, std::int64_t&, std::string const&)>, std::string);
%ignore libtorrent::session_handle::get_cache_info;
%ignore libtorrent::session_handle::get_connection_queue;
%ignore libtorrent::session_handle::get_context;
%ignore libtorrent::session_handle::get_io_service;
%ignore libtorrent::session_handle::get_peer_class;
%ignore libtorrent::session_handle::get_torrent_status;
%ignore libtorrent::session_handle::global_peer_class_id;
%ignore libtorrent::session_handle::local_peer_class_id;
%ignore libtorrent::session_handle::native_handle;
%ignore libtorrent::session_handle::session_handle(aux::session_impl*);
%ignore libtorrent::session_handle::session_handle(session_handle&&);
%ignore libtorrent::session_handle::set_alert_notify;
%ignore libtorrent::session_handle::set_dht_storage;
%ignore libtorrent::session_handle::set_load_function;
%ignore libtorrent::session_handle::set_peer_class;
%ignore libtorrent::session_handle::tcp_peer_class_id;
%ignore libtorrent::session_handle::wait_for_alert;

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
            $self->dht_direct_request(ep, e, (void*)userdata);
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