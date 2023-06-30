namespace libtorrent {
    class sha1_hash {
    public:

        static size_t size();

        sha1_hash();
        sha1_hash(sha1_hash const& other);

        static sha1_hash max();
        static sha1_hash min();

        void clear();
        bool is_all_zeros();
        int count_leading_zeroes();

        %extend {

            sha1_hash(std::vector<int8_t> const& s) {
                return new libtorrent::sha1_hash({reinterpret_cast<char const*>(s.data()), static_cast<long>(s.size())});
            }

            int hash_code() {
                char const* data = $self->data();
                int result = 1;
                for (int i = 0; i < int($self->size()); i++) {
                    result = 31 * result + data[i];
                }
                return result;
            }

            std::vector<int8_t> to_bytes() {
                std::string s = $self->to_string();
                return std::vector<int8_t>(s.begin(), s.end());
            }

            std::string to_hex() {
                return libtorrent::aux::to_hex(*$self);
            }

            bool op_eq(sha1_hash const& n) const {
                return *$self == n;
            }

            bool op_ne(sha1_hash const& n) const {
                return *$self != n;
            }

            bool op_lt(sha1_hash const& n) const {
                return *$self < n;
            }

            static int compare(sha1_hash const& h1, sha1_hash const& h2) {
                return h1 == h2 ? 0 : (h1 < h2 ? -1 : 1);
            }
        }
    };
}; // namespace libtorrent