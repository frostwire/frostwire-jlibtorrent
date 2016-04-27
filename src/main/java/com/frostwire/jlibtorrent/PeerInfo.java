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

    PeerInfo(peer_info p) {
        this.p = p;
    }

    public peer_info swig() {
        return p;
    }

    /**
     * The flags indicating which sources a peer can
     * have come from. A peer may have been seen from
     * multiple sources.
     */
    public enum PeerSourceFlags {

        /**
         * The peer was received from the tracker.
         */
        TRACKER(peer_info.peer_source_flags.tracker.swigValue()),

        /**
         * The peer was received from the kademlia DHT.
         */
        DHT(peer_info.peer_source_flags.dht.swigValue()),

        /**
         * The peer was received from the peer exchange
         * extension.
         */
        PEX(peer_info.peer_source_flags.pex.swigValue()),

        /**
         * The peer was received from the local service
         * discovery (The peer is on the local network).
         */
        LSD(peer_info.peer_source_flags.lsd.swigValue()),

        /**
         * The peer was added from the fast resume data.
         */
        RESUME_DATA(peer_info.peer_source_flags.resume_data.swigValue()),

        /**
         * We received an incoming connection from this peer.
         */
        INCOMING(peer_info.peer_source_flags.incoming.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        PeerSourceFlags(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue
         * @return
         */
        public static PeerSourceFlags fromSwig(int swigValue) {
            PeerSourceFlags[] enumValues = PeerSourceFlags.class.getEnumConstants();
            for (PeerSourceFlags ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
