namespace libtorrent {
    class entry {
    public:
        typedef std::map<std::string, libtorrent::entry> dictionary_type;
        typedef std::string string_type;
        typedef std::vector<libtorrent::entry> list_type;
        typedef std::int64_t integer_type;
        typedef std::vector<char> preformatted_type;

        enum data_type
        {
            int_t,
            string_t,
            list_t,
            dictionary_t,
            undefined_t,
            preformatted_t
        };

        data_type type() const;

        entry(dictionary_type);
        entry(span<char const>);
        entry(string_type);
        entry(list_type);
        entry(integer_type);

        entry(data_type t);

        entry(entry const& e);

        entry();

        const integer_type& integer() const;
        const string_type& string() const;
        list_type& list();
        dictionary_type& dict();

        entry* find_key(string_view key);

        std::string to_string() const;

        %extend {

            entry& get(std::string const& key) {
                return $self->operator[](key);
            }

            void set(std::string const& key, std::string const& value) {
                $self->operator[](key) = value;
            }

            void set(std::string const& key, std::vector<int8_t> const& value) {
                 $self->operator[](key) = std::string(value.begin(), value.end());
            }

            void set(std::string const& key, long long const& value) {
                $self->operator[](key) = value;
            }

            void set(std::string const& key, libtorrent::entry const& value) {
                $self->operator[](key) = value;
            }

            std::vector<int8_t> string_bytes() {
                std::string s = $self->string();
                return std::vector<int8_t>(s.begin(), s.end());
            }

            std::vector<int8_t> preformatted_bytes() {
                std::vector<char> v = $self->preformatted();
                return std::vector<int8_t>(v.begin(), v.end());
            }

            std::vector<int8_t> bencode() {
                std::vector<int8_t> buffer;
                libtorrent::bencode(std::back_inserter(buffer), *$self);
                return buffer;
            }

            static entry from_string_bytes(std::vector<int8_t> const& string_bytes) {
                return libtorrent::entry(std::string(string_bytes.begin(), string_bytes.end()));
            }

            static entry from_preformatted_bytes(std::vector<int8_t> const& preformatted_bytes) {
                return libtorrent::entry(std::vector<char>(preformatted_bytes.begin(), preformatted_bytes.end()));
            }

            static entry bdecode(std::vector<int8_t>& buffer) {
                return libtorrent::bdecode({reinterpret_cast<char const*>(buffer.data()), static_cast<long>(buffer.size())});
            }
        } // %extend
    }; // class entry
} // namespace libtorrent