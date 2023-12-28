namespace libtorrent {
    namespace flags {

        template<typename UnderlyingType, typename Tag>
        struct bitfield_flag
        {
            bitfield_flag();

            static bitfield_flag all();

            %extend
            {
                bool non_zero()
                {
                    return $self->operator bool();
                }

                bool nonZero()
                {
                    return $self->operator bool();
                }

                bool eq(bitfield_flag const f)
                {
                    return $self->operator==(f);
                }

                bool ne(bitfield_flag const f)
                {
                    return $self->operator!=(f);
                }

                bitfield_flag or_(bitfield_flag const other)
                {
                    return *$self | other;
                }

                bitfield_flag and_(bitfield_flag const other)
                {
                    return *$self & other;
                }

                bitfield_flag xor(bitfield_flag const other)
                {
                    return *$self ^ other;
                }

                bitfield_flag inv()
                {
                    return $self->operator~();
                }

                int to_int()
                {
                    return static_cast<int>(static_cast<UnderlyingType>(*$self));
                }

                static bitfield_flag from_int(int val)
                {
                    return lt::flags::bitfield_flag<UnderlyingType, Tag>(static_cast<UnderlyingType>(val));
                }
            }
        }; // struct bitfield_flag
    } // namespace flags
} // namespace libtorrent
