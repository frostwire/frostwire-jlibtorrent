package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.policy;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Policy {


    public static class Peer {

        private final policy.peer p;

        public Peer(policy.peer p) {
            this.p = p;
        }

        public policy.peer getSwig() {
            return p;
        }
    }

    public static class IPv4Peer extends Peer {

        private final policy.ipv4_peer p;

        public IPv4Peer(policy.ipv4_peer p) {
            super(p);
            this.p = p;
        }

        public policy.ipv4_peer getSwig() {
            return p;
        }
    }
}
