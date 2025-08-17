namespace libtorrent {

template <std::ptrdiff_t N>
class digest32
{
public:

    using difference_type = std::ptrdiff_t;
    using index_type = std::ptrdiff_t;

    static constexpr difference_type size();

    digest32() noexcept;

    digest32(digest32 const&);

    static digest32 max();

    static digest32 min();

    void clear() noexcept;

    bool is_all_zeros() const noexcept;

    digest32& operator<<=(int n) noexcept;
    digest32& operator>>=(int n) noexcept;

    bool operator==(digest32 const& n) const noexcept;
    bool operator!=(digest32 const& n) const noexcept;
    bool operator<(digest32 const& n) const noexcept;

    int count_leading_zeroes() const noexcept;

    digest32 operator~() const noexcept;

    digest32 operator^(digest32 const& n) const noexcept;

    digest32& operator^=(digest32 const& n) noexcept;

    digest32 operator&(digest32 const& n) const noexcept;

    digest32& operator&=(digest32 const& n) noexcept;

    digest32& operator|=(digest32 const& n) noexcept;

}; // class digest32

using sha1_hash = digest32<160>;
using sha256_hash = digest32<256>;

%extend digest32<160> {

    digest32<160>(std::vector<std::int8_t> const& v)
    {
        if (static_cast<std::size_t>(libtorrent::digest32<160>::size()) != v.size()) {
            throw std::invalid_argument("digest32<160>(std::vector<std::int8_t> const& v): sha1 size must be 20 bytes");
        }
        return new libtorrent::digest32<160>(libtorrent::span(reinterpret_cast<char const*>(v.data()), static_cast<long>(v.size())));
    }

    void assign(std::vector<std::int8_t> const& v)
    {
        if (static_cast<std::size_t>($self->size()) != v.size()) {
            throw std::invalid_argument("digest32<160>::assign(std::vector<std::int8_t> const& v): sha1 size must be 20 bytes");
        }
        $self->assign(reinterpret_cast<char const*>(v.data()));
    }

    int hash_code()
    {
        char const* data = $self->data();
        int result = 1;
        for (int i = 0; i < int($self->size()); i++)
        {
            // avoid signed-char UB / platform differences
            //result = 31 * result + data[i];
            result = 31 * result + static_cast<unsigned char>(data[i]);
        }
        return result;
    }

    std::vector<std::int8_t> to_bytes()
    {
        std::string s = $self->to_string();
        return std::vector<std::int8_t>(s.begin(), s.end());
    }

    std::string to_hex()
    {
        return libtorrent::aux::to_hex(*$self);
    }

    static digest32<160> from_hex(std::string s)
    {
        libtorrent::digest32<160> hash;
        //libtorrent::aux::from_hex(s, hash.data());
        libtorrent::aux::from_hex(libtorrent::span<char const>(s.data(), static_cast<long>(s.size())),hash.data());
        return hash;
    }

    static int compare(digest32<160> const& h1, digest32<160> const& h2)
    {
        return h1 == h2 ? 0 : (h1 < h2 ? -1 : 1);
    }
} // digest32<160>

%extend digest32<256> {

    digest32<256>(std::vector<std::int8_t> const& v)
    {
        if (static_cast<std::size_t>(libtorrent::digest32<256>::size()) != v.size()) {
            throw std::invalid_argument("digest32<256>(std::vector<std::int8_t> const& v): sha256 size must be 32 bytes");
        }

        return new libtorrent::digest32<256>(libtorrent::span(reinterpret_cast<char const*>(v.data()), static_cast<long>(v.size())));
    }

    void assign(std::vector<std::int8_t> const& v)
    {
        if (static_cast<std::size_t>($self->size()) != v.size()) {
            throw std::invalid_argument("digest32<256>::assign(std::vector<std::size_t> const& v): sha256 size must be 32 bytes");
        }
        $self->assign(reinterpret_cast<char const*>(v.data()));
    }

    int hash_code()
    {
        char const* data = $self->data();
        int result = 1;
        for (int i = 0; i < int($self->size()); i++)
        {
            //result = 31 * result + data[i];
            result = 31 * result + static_cast<unsigned char>(data[i]);
        }
        return result;
    }

    std::vector<std::int8_t> to_bytes()
    {
        std::string s = $self->to_string();
        return std::vector<std::int8_t>(s.begin(), s.end());
    }

    std::string to_hex()
    {
        return libtorrent::aux::to_hex(*$self);
    }

    static digest32<256> from_hex(std::string s)
    {
        libtorrent::digest32<256> hash;
        libtorrent::aux::from_hex(libtorrent::span<char const>(s.data(), static_cast<long>(s.size())),hash.data());
        return hash;
    }

    static int compare(digest32<256> const& h1, digest32<256> const& h2)
    {
        return h1 == h2 ? 0 : (h1 < h2 ? -1 : 1);
    }
} // digest32<256>

} // namespace libtorrent

%template(sha1_hash) libtorrent::digest32<160>;
%template(sha256_hash) libtorrent::digest32<256>;

%template(sha1_hash_vector) std::vector<libtorrent::digest32<160>>;
%template(sha256_hash_vector) std::vector<libtorrent::digest32<256>>;