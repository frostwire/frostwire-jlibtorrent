// We have excluded libtorrent::aux, but we need libtorrent::aux::strong_typedef.
// therefore we include units.hpp which is where it's defined.
// %inline %{
// namespace libtorrent {
//     namespace aux {
//         template<typename UnderlyingType, typename Tag
//         		, typename Cond = typename std::enable_if<std::is_integral<UnderlyingType>::value>::type>
//         	struct strong_typedef
//         	{
//         		using underlying_type = UnderlyingType;
//         		using diff_type = strong_typedef<UnderlyingType, difference_tag<Tag>>;
//
//         		constexpr strong_typedef(strong_typedef const& rhs) noexcept = default;
//         		constexpr strong_typedef(strong_typedef&& rhs) noexcept = default;
//         		strong_typedef() noexcept = default;
//         #if TORRENT_ABI_VERSION == 1
//         		constexpr strong_typedef(UnderlyingType val) : m_val(val) {}
//         		constexpr operator UnderlyingType() const { return m_val; }
//         #else
//         		constexpr explicit strong_typedef(UnderlyingType val) : m_val(val) {}
//         		constexpr explicit operator UnderlyingType() const { return m_val; }
//         		constexpr bool operator==(strong_typedef const& rhs) const { return m_val == rhs.m_val; }
//         		constexpr bool operator!=(strong_typedef const& rhs) const { return m_val != rhs.m_val; }
//         		constexpr bool operator<(strong_typedef const& rhs) const { return m_val < rhs.m_val; }
//         		constexpr bool operator>(strong_typedef const& rhs) const { return m_val > rhs.m_val; }
//         		constexpr bool operator>=(strong_typedef const& rhs) const { return m_val >= rhs.m_val; }
//         		constexpr bool operator<=(strong_typedef const& rhs) const { return m_val <= rhs.m_val; }
//         #endif
//         		strong_typedef& operator++() { ++m_val; return *this; }
//         		strong_typedef& operator--() { --m_val; return *this; }
//
//         		strong_typedef operator++(int) & { return strong_typedef{m_val++}; }
//         		strong_typedef operator--(int) & { return strong_typedef{m_val--}; }
//
//         		friend diff_type operator-(strong_typedef lhs, strong_typedef rhs)
//         		{ return diff_type{lhs.m_val - rhs.m_val}; }
//         		friend strong_typedef operator+(strong_typedef lhs, diff_type rhs)
//         		{ return strong_typedef{lhs.m_val + static_cast<UnderlyingType>(rhs)}; }
//         		friend strong_typedef operator+(diff_type lhs, strong_typedef rhs)
//         		{ return strong_typedef{static_cast<UnderlyingType>(lhs) + rhs.m_val}; }
//         		friend strong_typedef operator-(strong_typedef lhs, diff_type rhs)
//         		{ return strong_typedef{lhs.m_val - static_cast<UnderlyingType>(rhs)}; }
//
//         		strong_typedef& operator+=(diff_type rhs) &
//         		{ m_val += static_cast<UnderlyingType>(rhs); return *this; }
//         		strong_typedef& operator-=(diff_type rhs) &
//         		{ m_val -= static_cast<UnderlyingType>(rhs); return *this; }
//
//         		strong_typedef& operator=(strong_typedef const& rhs) & noexcept = default;
//         		strong_typedef& operator=(strong_typedef&& rhs) & noexcept = default;
//
//         #if TORRENT_USE_IOSTREAM
//         		friend std::ostream& operator<<(std::ostream& os, strong_typedef val)
//         		{ return os << static_cast<typename type_to_print_as<UnderlyingType>::type>(static_cast<UnderlyingType>(val)); }
//         #endif
//
//         	private:
//         		UnderlyingType m_val;
//         	};
//     }
// }
// %}

//%include "libtorrent/portmap.hpp"