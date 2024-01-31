%ignore libtorrent::bdecode;
%ignore libtorrent::bdecode_category;
%ignore libtorrent::bdecode_errors::make_error_code;
%ignore libtorrent::bdecode_node::bdecode_node(bdecode_node&&);
%ignore libtorrent::bdecode_node::data_section;
%ignore libtorrent::bdecode_node::dict_at;
%ignore libtorrent::bdecode_node::dict_find;
%ignore libtorrent::bdecode_node::dict_find_dict;
%ignore libtorrent::bdecode_node::dict_find_int;
%ignore libtorrent::bdecode_node::dict_find_int_value;
%ignore libtorrent::bdecode_node::dict_find_list;
%ignore libtorrent::bdecode_node::dict_find_string;
%ignore libtorrent::bdecode_node::dict_find_string_value;
%ignore libtorrent::bdecode_node::has_soft_error;
%ignore libtorrent::bdecode_node::list_string_value_at;
%ignore libtorrent::bdecode_node::non_owning;
%ignore libtorrent::bdecode_node::reserve;
%ignore libtorrent::bdecode_node::string_ptr;
%ignore libtorrent::bdecode_node::string_value;
%ignore libtorrent::bdecode_node::swap;
%ignore libtorrent::bdecode_node::switch_underlying_buffer;


%rename(bdecode_no_error) libtorrent::bdecode_errors::no_error;
%rename(bdecode_errors) libtorrent::bdecode_errors::error_code_enum;


%include "libtorrent/bdecode.hpp"

namespace libtorrent {
    %extend bdecode_node {
        std::string list_string_value_at_s(int i, std::string default_val = "") {
            return $self->list_string_value_at(i, default_val).to_string();
        }

        bdecode_node dict_find_s(std::string key) const {
            return $self->dict_find(key);
        }

        bdecode_node dict_find_dict_s(std::string key) const {
            return $self->dict_find_dict(key);
        }

        bdecode_node dict_find_list_s(std::string key) const {
            return $self->dict_find_list(key);
        }

        bdecode_node dict_find_string_s(std::string key) const {
            return $self->dict_find_string(key);
        }

        bdecode_node dict_find_int_s(std::string key) const {
            return $self->dict_find_int(key);
        }

        std::string dict_find_string_value_s(std::string key, std::string default_value = "") const {
            return $self->dict_find_string_value(key, default_value).to_string();
        }

        std::int64_t dict_find_int_value_s(std::string key, std::int64_t default_val = 0) const {
            return $self->dict_find_int_value(key, default_val);
        }

        std::string string_value_s() const {
            return $self->string_value().to_string();
        }

        std::string string_value_ex() const {
                return std::string{$self->string_value()};
        }

        static std::string to_string(bdecode_node const& e, bool single_line, int indent) {
            return libtorrent::print_entry(e, single_line, indent);
        }

        static int bdecode(std::vector<int8_t>& buffer, bdecode_node& ret, error_code& ec) {
            return libtorrent::bdecode((char const*)&buffer[0], (char const*)&buffer[0] + buffer.size(), ret, ec);
        }
    }
} // namespace libtorrent