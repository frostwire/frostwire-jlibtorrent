namespace libtorrent {
    class address {
    public:

        address();
        address(address const& other);

        bool is_v4();
        bool is_v6();

        std::string to_string(boost::system::error_code ec);

        bool is_loopback();
        bool is_unspecified();
        bool is_multicast();

        %extend {
            bool op_lt(const address& a2) {
                return *$self < a2;
            }

            static int compare(const address& a1, const address& a2) {
                return a1 == a2 ? 0 : (a1 < a2 ? -1 : 1);
            }

            static address from_string(std::string const& str, boost::system::error_code& ec)
            {
                return boost::asio::ip::make_address(str, ec);
            }
        }
    };
} // namespace libtorrent