namespace libtorrent {
    struct ip_filter {
        ip_filter();
        ip_filter(ip_filter const&);

        // the flags defined for an IP range
        enum access_flags
        {
            // indicates that IPs in this range should not be connected
            // to nor accepted as incoming connections
            blocked = 1
        };

        // returns true if the filter does not contain any rules
        bool empty() const;

        // Adds a rule to the filter. ``first`` and ``last`` defines a range of
        // ip addresses that will be marked with the given flags. The ``flags``
        // can currently be 0, which means allowed, or ``ip_filter::blocked``, which
        // means disallowed.
        //
        // precondition:
        // ``first.is_v4() == last.is_v4() && first.is_v6() == last.is_v6()``
        //
        // postcondition:
        // ``access(x) == flags`` for every ``x`` in the range [``first``, ``last``]
        //
        // This means that in a case of overlapping ranges, the last one applied takes
        // precedence.
        void add_rule(address const& first, address const& last, std::uint32_t flags);

        // Returns the access permissions for the given address (``addr``). The permission
        // can currently be 0 or ``ip_filter::blocked``. The complexity of this operation
        // is O(``log`` n), where n is the minimum number of non-overlapping ranges to describe
        // the current filter.
        std::uint32_t access(address const& addr) const;

        // This function will return the current state of the filter in the minimum number of
        // ranges possible. They are sorted from ranges in low addresses to high addresses. Each
        // entry in the returned vector is a range with the access control specified in its
        // ``flags`` field.
        //
        // The return value is a tuple containing two range-lists. One for IPv4 addresses
        // and one for IPv6 addresses.
        //filter_tuple_t export_filter() const;
    };

    // the port filter maps non-overlapping port ranges to flags. This
    // is primarily used to indicate whether a range of ports should
    // be connected to or not. The default is to have the full port
    // range (0-65535) set to flag 0.
    class port_filter {
    public:

        port_filter();
        port_filter(port_filter const&);
        ~port_filter();

        // the defined flags for a port range
        enum access_flags
        {
            // this flag indicates that destination ports in the
            // range should not be connected to
            blocked = 1
        };

        // set the flags for the specified port range (``first``, ``last``) to
        // ``flags`` overwriting any existing rule for those ports. The range
        // is inclusive, i.e. the port ``last`` also has the flag set on it.
        void add_rule(std::uint16_t first, std::uint16_t last, std::uint32_t flags);

        // test the specified port (``port``) for whether it is blocked
        // or not. The returned value is the flags set for this port.
        // see access_flags.
        std::uint32_t access(std::uint16_t port) const;
    };
} // namespace libtorrent