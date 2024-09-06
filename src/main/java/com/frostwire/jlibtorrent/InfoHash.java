package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.info_hash_t;

/**
 * Class holding the info-hash of a torrent. It can hold a v1 info-hash
 * (SHA-1) or a v2 info-hash (SHA-256) or both.
 * <p>
 * If !has_v2() then the v1 hash might actually be a truncated v2 hash.
 *
 * @author aldenml@libtorrent4j
 * @author gubatron
 */
public class InfoHash {
    private final info_hash_t ih_t;

    public InfoHash(info_hash_t swig) {
        this.ih_t = swig;
    }

    public InfoHash() {
        this.ih_t = new info_hash_t();
    }

    public info_hash_t swig() {
        return ih_t;
    }

    public boolean hasV1() {
        return ih_t.has_v1();
    }

    public boolean hasV2() {
        return ih_t.has_v2();
    }

    public Sha1Hash getBest() {
        return new Sha1Hash(ih_t.get_best());
    }

    public Sha1Hash getV1() {
        return new Sha1Hash(ih_t.getV1());
    }
}