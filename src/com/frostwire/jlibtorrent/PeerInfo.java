package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_info;

/**
 * Holds information and statistics about one peer
 * that libtorrent is connected to.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerInfo {

    private final peer_info p;

    public PeerInfo(peer_info p) {
        this.p = p;
    }

    public peer_info getSwig() {
        return p;
    }

    /**
     * The flags indicating which sources a peer can
     * have come from. A peer may have been seen from
     * multiple sources.
     */
    enum PeerSourceFlags {

        /**
         * This is the first time we see this peer.
         */
        TRACKER(peer_info.peer_source_flags.tracker.swigValue()),

        /**
         * This peer was not added because it was
         * filtered by the IP filter
         */
        DHT(peer_info.peer_source_flags.dht.swigValue()),

        PEX(peer_info.peer_source_flags.pex.swigValue()),

        LSD(peer_info.peer_source_flags.lsd.swigValue()),

        RESUME_DATA(peer_info.peer_source_flags.resume_data.swigValue()),

        INCOMING(peer_info.peer_source_flags.incoming.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        PeerSourceFlags(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static PeerSourceFlags fromSwig(int swigValue) {
            PeerSourceFlags[] enumValues = PeerSourceFlags.class.getEnumConstants();
            for (PeerSourceFlags ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
