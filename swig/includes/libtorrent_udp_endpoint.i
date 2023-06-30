namespace libtorrent {
    %rename(udp_endpoint) udp::endpoint;
    namespace udp {
        class endpoint {
        public:
            endpoint();
            endpoint(address address, unsigned short port);
            endpoint(endpoint const& other);

            unsigned short port();
            address address();
        };
    } // namespace udp
} // namespace libtorrent