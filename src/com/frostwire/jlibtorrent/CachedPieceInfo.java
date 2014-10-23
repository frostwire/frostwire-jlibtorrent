package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.cached_piece_info;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CachedPieceInfo {

    private final cached_piece_info i;

    public CachedPieceInfo(cached_piece_info i) {
        this.i = i;
    }

    public cached_piece_info getSwig() {
        return i;
    }

    /**
     * the piece index for this cache entry.
     *
     * @return
     */
    public int getPiece() {
        return i.getPiece();
    }

    /**
     * holds one entry for each block in this piece. ``true`` represents
     * the data for that block being in the disk cache and ``false`` means it's not.
     *
     * @return
     */
    public boolean[] getBlocks() {
        return Vectors.bool_vector2booleans(i.getBlocks());
    }

    /**
     * the time when a block was last written to this piece. The older
     * a piece is, the more likely it is to be flushed to disk.
     *
     * @return
     */
    public PTime getLastUse() {
        return new PTime(i.getLast_use());
    }

    /**
     * The index of the next block that needs to be hashed.
     * Blocks are hashed as they are downloaded in order to not
     * have to re-read them from disk once the piece is complete, to
     * compare its hash against the hashes in the .torrent file.
     *
     * @return
     */
    public int getNextToHash() {
        return i.getNext_to_hash();
    }

    /**
     * specifies if this piece is part of the read cache or the write cache.
     *
     * @return
     */
    public Kind getKind() {
        return Kind.fromSwig(i.getKind().swigValue());
    }

    public enum Kind {

        /**
         *
         */
        READ_CACHE(cached_piece_info.kind_t.read_cache.swigValue()),

        /**
         *
         */
        WRITE_CACHE(cached_piece_info.kind_t.write_cache.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private Kind(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Kind fromSwig(int swigValue) {
            Kind[] enumValues = Kind.class.getEnumConstants();
            for (Kind ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
