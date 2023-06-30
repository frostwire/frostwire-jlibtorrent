%ignore boost::system::system_category;
%ignore boost::system::generic_category;

namespace boost {
    namespace system {
        class error_code {
            public:
                error_code();
                void clear();
                int value();
                std::string message();
                bool failed();
                operator bool() const;
        };
        error_category const& system_category();
        error_category const& generic_category();
    }
}