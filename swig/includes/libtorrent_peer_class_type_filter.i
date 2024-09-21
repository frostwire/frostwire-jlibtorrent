namespace libtorrent {

    struct peer_class_type_filter {
        peer_class_type_filter();

        enum socket_type_t : std::uint8_t {
            // these match the socket types from socket_type.hpp
            // shifted one down
            tcp_socket = 0,
            utp_socket,
            ssl_tcp_socket,
            ssl_utp_socket,
            i2p_socket,
            num_socket_types
        };

        std::uint32_t apply(socket_type_t const st, std::uint32_t peer_class_mask);

    };

} // namespace libtorrent
