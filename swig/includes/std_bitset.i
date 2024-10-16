namespace std {
    template <std::size_t N>
    class bitset
    {
    public:
        bool test(std::size_t pos) const;

        bool all() const;
        bool any() const;
        bool none() const;

        std::size_t count() const;
        std::size_t size() const;

        %extend
        {
            bool get(std::size_t pos) {
                return (*self)[pos];
            }
        }
    };
}; // namespace std