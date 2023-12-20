namespace libtorrent {
    class address {
    public:

        address();
        address(address const& other);

        bool is_v4();
        bool is_v6();

        std::string to_string();

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

            int hash_code()
            {
                if ($self->is_v4())
                {
                    auto data = $self->to_v4().to_bytes();
                    int result = 1;
                    for (int i = 0; i < int(data.size()); i++)
                    {
                        result = 31 * result + data[i];
                    }
                    return result;
                }
                else
                {
                    auto data = $self->to_v6().to_bytes();
                    int result = 1;
                    for (int i = 0; i < int(data.size()); i++)
                    {
                        result = 31 * result + data[i];
                    }
                    return result;
                }
            }
        }
    };
} // namespace libtorrent