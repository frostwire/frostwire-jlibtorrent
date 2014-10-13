%include <std_common.i>

%{
#include <list>
#include <stdexcept>
%}

namespace std {

    template<class T> class list {
        public:
            typedef size_t size_type;
            typedef T value_type;
            typedef const value_type& const_reference;
            list();

            %rename(isEmpty) empty;
            bool empty() const;
            size_type size() const;
            size_type max_size() const;

            const_reference front();
            const_reference back();

            void push_front(const value_type& x);
            void pop_front();
            void push_back(const value_type& x);
            void pop_back();
            void clear();

            %extend {
                std::vector<T> to_vector() {
                    std::vector<T> v(self->begin(), self->end());
                    return v;
                }
            }
    };
}
