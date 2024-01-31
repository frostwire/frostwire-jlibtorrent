namespace boost {
namespace container {

template<class Key, class T>
class map
{
public:

    bool empty() const;
    void clear();
    std::int64_t size() const;

    %extend
    {
        bool contains(Key const& k)
        {
            return $self->contains(k);
        }

        void put(Key const& k, T& v)
        {
            $self->operator[](k) = v;
        }

        T& get(Key const& k)
        {
            return $self->operator[](k);
        }

        std::vector<Key> keys()
        {
            std::vector<Key> r;

            for (auto const& e : *$self)
            {
                r.emplace_back(e.first);
            }

            return r;
        }

        void remove(Key const& key)
        {
            $self->extract(key);
        }
    }
};

}}