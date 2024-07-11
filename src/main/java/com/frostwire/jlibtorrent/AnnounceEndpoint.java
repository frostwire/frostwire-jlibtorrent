package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_endpoint;

/**
 * Announces are sent to each tracker using every listen socket, this class
 * holds information about one listen socket for one tracker.
 * <p>
 * This class is a lightweight version of the native {@link announce_endpoint},
 * and only carries a subset of all the information. However, it's completely
 * open for custom use or optimization to accommodate client necessities.
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceEndpoint {
    private final announce_endpoint h;

    public AnnounceEndpoint(announce_endpoint endpoint) {
        this.h = endpoint;
    }

    /**
     * The local endpoint of the listen interface associated with this endpoint.
     *
     * @return the local endpoint
     */
    public TcpEndpoint localEndpoint() {
        return new TcpEndpoint(h.getLocal_endpoint());
    }

    /**
     * Set to false to not announce from this endpoint.
     */
    public boolean enabled() {
        return h.getEnabled();
    }

    /**
     * Torrents can be announced using multiple info hashes
     * for different protocol versions.
     * <p>
     * This is for version 1 (SHA1).
     *
     * @return the V1 announce infohash
     */
    public AnnounceInfohash infohashV1() {
        return new AnnounceInfohash(h.get_infohash_v1());
    }

    /**
     * Torrents can be announced using multiple info hashes
     * for different protocol versions.
     * <p>
     * This is for version 2 (truncated SHA-256).
     *
     * @return the V2 announce infohash
     */
    public AnnounceInfohash infohashV2() {
        return new AnnounceInfohash(h.get_infohash_v2());
    }
}
