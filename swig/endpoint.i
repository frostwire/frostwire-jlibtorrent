%{
#include <boost/asio/ip/tcp.hpp>
#include <boost/asio/ip/udp.hpp>
%}

%rename(tcp_endpoint) tcp::endpoint;

namespace tcp {

    class endpoint {
    public:
        unsigned short port();
        %extend {
            std::string address() {
                return $self->address().to_string();
            }
        }
    };
}


