package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.piece_index_bitfield;
import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * The bitfield type stores any number of bits as a bitfield
 * in a heap allocated array.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PieceIndexBitfield {

    private final piece_index_bitfield f;
    private final torrent_status ts;

    /**
     * @param f the native object
     */
    public PieceIndexBitfield(piece_index_bitfield f) {
        this(f, null);
    }

    /**
     * Used to keep the torrent status reference around.
     *
     * @param f  the native object
     * @param ts the torrent status to pin
     */
    PieceIndexBitfield(piece_index_bitfield f, torrent_status ts) {
        this.f = f;
        this.ts = ts;
    }

    /**
     * @return the native object
     */
    public piece_index_bitfield swig() {
        return f;
    }

    /**
     * This methods returns the internal torrent status or null
     * if it was constructed without one.
     * <p>
     * This also prevent premature garbage collection in case
     * the storage was created from a torrent status.
     *
     * @return the pinned torrent info
     */
    public torrent_status ts() {
        return ts;
    }

    /**
     * @param index the bit index
     * @return the bit value
     */
    public boolean getBit(int index) {
        return f.get_bit(index);
    }

    /**
     * @param index the bit index
     */
    public void clearBit(int index) {
        f.clear_bit(index);
    }

    /**
     * @param index the bit index
     */
    public void setBit(int index) {
        f.set_bit(index);
    }

    public int endIndex() {
        return f.end_index();
    }
}
