namespace libtorrent {
    %rename(tcp_endpoint) tcp::endpoint;
    namespace tcp {
        class endpoint {
        public:
            endpoint();
            endpoint(address address, unsigned short port);
            endpoint(endpoint const& other);

            unsigned short port();
            address address();
        };
    } // namespace tcp
} // namespace libtorrent