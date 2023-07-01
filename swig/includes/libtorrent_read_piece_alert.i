%ignore libtorrent::read_piece_alert::read_piece_alert;
%ignore libtorrent::read_piece_alert::buffer;

namespace libtorrent {
    %extend read_piece_alert {
        int64_t buffer_ptr() {
            return reinterpret_cast<int64_t>($self->buffer.get());
        }
    } // read_piece_alert
} // namespace libtorrent
