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

    /**
     * Returns true if all bits in the bitfield are set.
     *
     * @return true if all bits are set
     */
    public boolean isAllSet() {
        return f.all_set();
    }

    /**
     * @return true if no bit is set
     */
    public boolean isNoneSet() {
        return f.none_set();
    }

    /**
     * Returns the size of the bitfield in bits.
     *
     * @return the size
     */
    public int size() {
        return f.size();
    }

    /**
     * Returns true if the bitfield has zero size.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return f.empty();
    }

    /**
     * Counts the number of bits in the bitfield that are set to 1.
     *
     * @return the number of bits set
     */
    public int count() {
        return f.count();
    }

    /**
     * @return the bit index
     */
    public int findFirstSet() {
        return f.find_first_set();
    }

    /**
     * @return the bit index
     */
    public int findLastClear() {
        return f.find_last_clear();
    }

    /**
     * Set the size of the bitfield to ``bits`` length. If the bitfield is extended,
     * the new bits are initialized to ``val``.
     *
     * @param bits the number of bits
     * @param val  the bits value
     */
    public void resize(int bits, boolean val) {
        f.resize(bits, val);
    }

    /**
     * @param bits the number of bits
     */
    public void resize(int bits) {
        f.resize(bits);
    }

    /**
     * Set all bits in the bitfield to 1 (set_all) or 0 (clear_all).
     */
    public void setAll() {
        f.set_all();
    }

    /**
     * Set all bits in the bitfield to 1 (set_all) or 0 (clear_all).
     */
    public void clearAll() {
        f.clear_all();
    }

    /**
     * Make the bitfield empty, of zero size.
     */
    public void clear() {
        f.clear();
    }
}
