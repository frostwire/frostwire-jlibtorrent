namespace libtorrent {

    template <typename T>
    struct span {

        span();

        size_t size() const;
        bool empty() const;

        T front() const;
        T back() const;

        span<T> first(size_t const n) const;
        span<T> last(size_t const n) const;

        span<T> subspan(size_t const offset) const;
        span<T> subspan(size_t const offset, size_t const count) const;

        %extend {
            T get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    template <>
    struct span<char> {

        span();

        size_t size() const;
        bool empty() const;

        int8_t front() const;
        int8_t back() const;

        span<char> first(size_t const n) const;
        span<char> last(size_t const n) const;

        span<char> subspan(size_t const offset) const;
        span<char> subspan(size_t const offset, size_t const count) const;

        %extend {
            int8_t get(size_t const idx) {
                return (*self)[idx];
            }
            void set(size_t const idx, int8_t val) {
                (*self)[idx] = val;
            }
        }
    };

    template <>
    struct span<char const> {

        span();

        size_t size() const;
        bool empty() const;

        int8_t front() const;
        int8_t back() const;

        span<char const> first(size_t const n) const;
        span<char const> last(size_t const n) const;

        span<char const> subspan(size_t const offset) const;
        span<char const> subspan(size_t const offset, size_t const count) const;

        %extend {
            int8_t get(size_t const idx) {
                return (*self)[idx];
            }
        }
    };

    %template(byte_span) span<char>;
    %template(byte_const_span) span<char const>;

} // namespace libtorrent