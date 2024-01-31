namespace libtorrent {

    struct client_data_t
    {
        client_data_t();

        client_data_t(void*);
    };

    %extend client_data_t
    {
        void* get()
        {
            return $self->get<void>();
        }
    }

} // namespace libtorrent