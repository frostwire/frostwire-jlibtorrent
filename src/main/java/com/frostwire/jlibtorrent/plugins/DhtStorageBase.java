package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.DhtSettings;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.*;

import java.util.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtStorageBase implements DhtStorage {

    private final Sha1Hash id;
    private final DhtSettings settings;
    private final Counters counters;

    private final HashMap<String, TorrentEntry> map;

    public DhtStorageBase(Sha1Hash id, DhtSettings settings) {
        this.id = id;
        this.settings = settings;
        this.counters = new Counters();

        this.map = new HashMap<String, TorrentEntry>();
    }

    @Override
    public boolean getPeers(Sha1Hash infoHash, boolean noseed, boolean scrape, entry peers) {
        String hex = infoHash.toHex();
        TorrentEntry v = map.get(hex);

        if (v == null) {
            return false;
        }

        if (!v.name.isEmpty()) {
            peers.set("n", v.name);
        }

        if (scrape) {
            bloom_filter_256 downloaders = new bloom_filter_256();
            bloom_filter_256 seeds = new bloom_filter_256();

            for (PeerEntry peer : v.peers) {
                sha1_hash iphash = new sha1_hash();
                libtorrent.sha1_hash_address(peer.addr.address().swig(), iphash);
                if (peer.seed) {
                    seeds.set(iphash);
                } else {
                    downloaders.set(iphash);
                }
            }

            peers.set("BFpe", downloaders.to_bytes());
            peers.set("BFsd", seeds.to_bytes());
        } else {
            int num = Math.min(v.peers.size(), settings.maxPeersReply());
            Iterator<PeerEntry> iter = v.peers.iterator();
            entry_list pe = peers.get("values").list();
            byte_vector endpoint = new byte_vector();

            for (int t = 0, m = 0; m < num && iter.hasNext(); ++t) {
                PeerEntry e = iter.next();
                if ((Math.random() / (Integer.MAX_VALUE + 1.f)) * (num - t) >= num - m) continue;
                if (noseed && e.seed) continue;
                endpoint.resize(18);
                int n = libtorrent.write_tcp_endpoint(e.addr.swig(), endpoint);
                endpoint.resize(n);
                pe.push_back(new entry(endpoint));

                ++m;
            }
        }

        return true;
    }

    @Override
    public void announcePeer(Sha1Hash infoHash, TcpEndpoint endp, String name, boolean seed) {
        String hex = infoHash.toHex();
        TorrentEntry v = map.get(hex);
        if (v == null) {
            // we don't have this torrent, add it
            // do we need to remove another one first?
            if (!map.isEmpty() && map.size() >= settings.maxTorrents()) {
                // we need to remove some. Remove the ones with the
                // fewest peers
                int num_peers = Integer.MAX_VALUE;
                String candidateKey = null;
                for (Map.Entry<String, TorrentEntry> kv : map.entrySet()) {

                    if (kv.getValue().peers.size() > num_peers) {
                        continue;
                    }
                    if (hex.equals(kv.getKey())) {
                        continue;
                    }
                    num_peers = kv.getValue().peers.size();
                    candidateKey = kv.getKey();
                }
                map.remove(candidateKey);
                counters.peers -= num_peers;
                counters.torrents -= 1;
            }
            counters.torrents += 1;
            v = new TorrentEntry();
            map.put(hex, v);
        }

        // the peer announces a torrent name, and we don't have a name
        // for this torrent. Store it.
        if (!name.isEmpty() && v.name.isEmpty()) {
            String tname = name;
            if (tname.length() > 100) {
                tname = tname.substring(0, 99);
            }
            v.name = tname;
        }

        PeerEntry peer = new PeerEntry();
        peer.addr = endp;
        peer.added = System.currentTimeMillis();
        peer.seed = seed;

        if (v.peers.contains(peer)) {
            v.peers.remove(peer);
            counters.peers -= 1;
        } else if (v.peers.size() >= settings.maxPeers()) {
            // when we're at capacity, there's a 50/50 chance of dropping the
            // announcing peer or an existing peer
            if (Math.random() > 0.5) {
                return;
            }
            PeerEntry t = v.peers.lower(peer);
            if (t == null) {
                t = v.peers.first();
            }
            v.peers.remove(t);
            counters.peers -= 1;
        }
        v.peers.add(peer);
        counters.peers += 1;
    }

    @Override
    public boolean getImmutableItem(Sha1Hash target, entry item) {
        return false;
    }

    @Override
    public void putImmutableItem(Sha1Hash target, byte[] buf, Address addr) {

    }

    @Override
    public long getMutableItemSeq(Sha1Hash target) {
        return -1;
    }

    @Override
    public boolean getMutableItem(Sha1Hash target, long seq, boolean forceFill, entry item) {
        return false;
    }

    @Override
    public void putMutableItem(Sha1Hash target, byte[] buf, byte[] sig, long seq, byte[] pk, byte[] salt, Address addr) {

    }

    @Override
    public void tick() {

    }

    @Override
    public Counters counters() {
        return counters;
    }

    static final class PeerEntry {

        public long added;
        public TcpEndpoint addr;
        public boolean seed;

        public static final Comparator<PeerEntry> COMPARATOR = new Comparator<PeerEntry>() {
            @Override
            public int compare(PeerEntry o1, PeerEntry o2) {
                TcpEndpoint a1 = o1.addr;
                TcpEndpoint a2 = o2.addr;

                int r = Address.compare(a1.address(), a2.address());

                return r == 0 ? Integer.compare(a1.port(), a2.port()) : r;
            }
        };
    }

    static final class TorrentEntry {

        public TorrentEntry() {
            this.name = "";
            this.peers = new TreeSet<PeerEntry>(PeerEntry.COMPARATOR);
        }

        public String name;
        public TreeSet<PeerEntry> peers;
    }
}
