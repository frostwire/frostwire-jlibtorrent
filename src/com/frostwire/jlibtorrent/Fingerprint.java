package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.fingerprint;
import com.frostwire.jlibtorrent.swig.libtorrent;

/**
 * The fingerprint class represents information about a client and its version. It is used
 * to encode this information into the client's peer id.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Fingerprint {

    private final fingerprint p;

    public Fingerprint(fingerprint p) {
        this.p = p;
    }

    public Fingerprint() {
        this(new fingerprint("LT", libtorrent.LIBTORRENT_VERSION_MAJOR, libtorrent.LIBTORRENT_VERSION_MINOR, 0, 0));
    }

    /**
     * The constructor takes a ``char const*`` that should point to a string constant containing
     * exactly two characters. These are the characters that should be unique for your client. Make
     * sure not to clash with anybody else. Here are some taken id's:
     * <p/>
     * +----------+-----------------------+
     * | id chars | client                |
     * +==========+=======================+
     * | 'AZ'     | Azureus               |
     * +----------+-----------------------+
     * | 'LT'     | libtorrent            |
     * +----------+-----------------------+
     * | 'BX'     | BittorrentX           |
     * +----------+-----------------------+
     * | 'MT'     | Moonlight Torrent     |
     * +----------+-----------------------+
     * | 'TS'     | Torrent Storm         |
     * +----------+-----------------------+
     * | 'SS'     | Swarm Scope           |
     * +----------+-----------------------+
     * | 'XT'     | Xan Torrent           |
     * +----------+-----------------------+
     * <p/>
     * There's an informal directory of client id's here_.
     * <p/>
     * .. _here: http://wiki.theory.org/BitTorrentSpecification#peer_id
     * <p/>
     * The ``major``, ``minor``, ``revision`` and ``tag`` parameters are used to identify the
     * version of your client.
     *
     * @param id
     * @param major
     * @param minor
     * @param revision
     * @param tag
     */
    public Fingerprint(String id, int major, int minor, int revision, int tag) {
        this(new fingerprint(id, major, minor, revision, tag));
    }

    public fingerprint getSwig() {
        return p;
    }

    public String getName() {
        return p.getName();
    }

    public int getMajorVersion() {
        return p.getMajor_version();
    }

    public int getMinorVersion() {
        return p.getMinor_version();
    }

    public int getRevisionVersion() {
        return p.getRevision_version();
    }

    public int getTagVersion() {
        return p.getTag_version();
    }

    @Override
    public String toString() {
        return p.to_string();
    }
}
