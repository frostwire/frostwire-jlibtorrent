%ignore boost::system::error_category;
%ignore boost::system::error_code::assign;
%ignore boost::system::error_code::category;
%ignore boost::system::error_code::default_error_condition;
%ignore boost::system::error_code::error_code(int, const error_category&);
%ignore boost::system::error_code::operator std::error_code;
%ignore boost::system::error_code::unspecified_bool_true;
%ignore boost::system::error_condition;
%ignore boost::system::generic_category;
%ignore boost::system::system_category;
%ignore boost::system::system_error;

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