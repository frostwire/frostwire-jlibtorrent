namespace libtorrent {
    struct bitfield {
        bitfield();
        bitfield(int bits);
        bitfield(int bits, bool val);
        bitfield(bitfield const& rhs);

        bool operator[](int index) const noexcept;
        bool get_bit(int index) const noexcept;

        void clear_bit(int index) noexcept;
        void set_bit(int index) noexcept;

        bool all_set() const noexcept;

        bool none_set() const noexcept;

        int size() const noexcept;

        int num_words() const noexcept;

        bool empty() const noexcept;

        void swap(bitfield& rhs) noexcept;

        int count() const noexcept;

        int find_first_set() const noexcept;

        int find_last_clear() const noexcept;

        void resize(int bits, bool val);
        void resize(int bits);

        void set_all() noexcept;
        void clear_all() noexcept;

        void clear() noexcept;
    }; // struct bitfield

    %extend bitfield
    {
        bitfield(std::vector<std::int8_t> const& v) {
            return new libtorrent::bitfield({reinterpret_cast<char const*>(v.data()), static_cast<int>(v.size())});
        }

        void assign(std::vector<std::int8_t> const& v)
        {
            $self->assign(reinterpret_cast<char const*>(v.data()), static_cast<int>(v.size()));
        }
    } // bitfield
} // namespace libtorrent