// looks like this is gone
namespace libtorrent {
    template <typename IndexType>
    struct typed_bitfield {
	    typed_bitfield();
        explicit typed_bitfield(int bits);
        typed_bitfield(int bits, bool val);
        typed_bitfield(typed_bitfield<IndexType> const& rhs);

        bool get_bit(IndexType const index) const;
        void clear_bit(IndexType const index);
        void set_bit(IndexType const index);
        IndexType end_index() const;

        bool all_set() const;
        bool none_set() const;
        int size() const;
        int num_words() const;
        bool empty() const;
        int count() const;
        int find_first_set() const;
        int find_last_clear() const;
        void resize(int bits, bool val);
        void resize(int bits);
        void set_all();
        void clear_all();
        void clear();
	};
    //%template(piece_index_bitfield) typed_bitfield<piece_index_t>;
} // namespace libtorrent