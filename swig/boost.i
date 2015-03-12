%{
#include <boost/asio/ip/address.hpp>
#include <boost/asio/ip/address_v4.hpp>
#include <boost/asio/ip/address_v6.hpp>

#include <boost/asio/ip/tcp.hpp>
#include <boost/asio/ip/udp.hpp>

boost::chrono::high_resolution_clock::duration to_seconds(long long n) {
    return boost::chrono::seconds(n);
}

boost::chrono::high_resolution_clock::duration to_milliseconds(long long n) {
    return boost::chrono::milliseconds(n);
}

boost::chrono::high_resolution_clock::duration to_microseconds(long long n) {
    return boost::chrono::microseconds(n);
}

boost::chrono::high_resolution_clock::duration to_minutes(long long n) {
    return boost::chrono::minutes(n);
}

boost::chrono::high_resolution_clock::duration to_hours(long long n) {
    return boost::chrono::hours(n);
}

%}

%rename(posix_time_duration) boost::posix_time::time_duration;
%rename(boost_system_category) boost::system::system_category;

namespace boost {

    namespace date_time {

        enum special_values {not_a_date_time,
                            neg_infin, pos_infin,
                            min_date_time,  max_date_time,
                            not_special, NumSpecialValues};

        enum time_resolutions {
            sec,
            tenth,
            hundreth, // deprecated misspelled version of hundredth
            hundredth = hundreth,
            milli,
            ten_thousandth,
            micro,
            nano,
            NumResolutions
        };
    }

    namespace posix_time {

        class time_duration {
        public:

            time_duration();
            time_duration(boost::int64_t hours_in,
                  boost::int64_t minutes_in,
                  boost::int64_t seconds_in,
                  boost::int64_t frac_sec_in);
            time_duration(time_duration other);
            time_duration(boost::date_time::special_values sv);

            static time_duration unit();
            static boost::int64_t ticks_per_second();
            static boost::date_time::time_resolutions resolution();

            boost::int64_t hours();
            boost::int64_t minutes();
            boost::int64_t seconds();
            boost::int64_t total_seconds();
            boost::int64_t total_milliseconds();
            boost::int64_t total_nanoseconds();
            boost::int64_t total_microseconds();
            boost::int64_t fractional_seconds();

            static unsigned short num_fractional_digits();

            bool is_negative();

            boost::int64_t ticks();

            bool is_special();
            bool is_pos_infinity();
            bool is_neg_infinity();
            bool is_not_a_date_time();
        };
    }

    namespace asio {

        namespace ip {

            class address_v4;
            class address_v6;

            class address {
            public:

                address();
                address(address_v4 ipv4_address);
                address(address_v6 ipv6_address);
                address(address other);

                bool is_v4();
                bool is_v6();

                address_v4 to_v4();
                //address_v6 to_v6();

                std::string to_string();
                std::string to_string(boost::system::error_code ec);

                //static address from_string(std::string str);
                //static address from_string(std::string str, boost::system::error_code ec);

                bool is_loopback();
                bool is_unspecified();
                bool is_multicast();
            };

            class address_v4 {
            public:

                address_v4();
                address_v4(unsigned long addr);
                address_v4(address_v4 other);

                unsigned long to_ulong();
                std::string to_string();
                std::string to_string(boost::system::error_code ec);

                static address_v4 from_string(std::string str);
                static address_v4 from_string(std::string str, boost::system::error_code ec);

                bool is_loopback();
                bool is_unspecified();
                bool is_class_a();
                bool is_class_b();
                bool is_class_c();
                bool is_multicast();

                static address_v4 any();
                static address_v4 loopback();
                static address_v4 broadcast();
                static address_v4 broadcast(address_v4 addr, address_v4 mask);
                static address_v4 netmask(address_v4 addr);
            };

            class address_v6 {
            public:

                address_v6();
                address_v6(address_v6 other);

                unsigned long scope_id();
                void scope_id(unsigned long id);

                std::string to_string();
                std::string to_string(boost::system::error_code ec);

                //static address_v6 from_string(std::string str);
                //static address_v6 from_string(std::string str, boost::system::error_code ec);

                address_v4 to_v4();

                bool is_loopback() const;
                bool is_unspecified() const;
                bool is_link_local() const;
                bool is_site_local() const;
                bool is_v4_mapped() const;
                bool is_v4_compatible() const;
                bool is_multicast() const;
                bool is_multicast_global() const;
                bool is_multicast_link_local() const;
                bool is_multicast_node_local() const;
                bool is_multicast_org_local() const;
                bool is_multicast_site_local() const;

                //static address_v6 any();
                //static address_v6 loopback();
                //static address_v6 v4_mapped(address_v4 addr);
                //static address_v6 v4_compatible(address_v4 addr);
            };
        }
    }

    namespace chrono {

        class high_resolution_clock {
        public:

            class time_point {
            public:
            };

            class duration {
            public:
            };
        };
    }
}

%rename(tcp_endpoint) tcp::endpoint;
%rename(udp_endpoint) udp::endpoint;

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

namespace udp {

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

boost::chrono::high_resolution_clock::duration to_seconds(long long);
boost::chrono::high_resolution_clock::duration to_milliseconds(long long);
boost::chrono::high_resolution_clock::duration to_microseconds(long long);
boost::chrono::high_resolution_clock::duration to_minutes(long long);
boost::chrono::high_resolution_clock::duration to_hours(long long);
