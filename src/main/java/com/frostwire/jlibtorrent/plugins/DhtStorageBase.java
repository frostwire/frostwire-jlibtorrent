package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

import java.util.*;

/**
 * @author gubatron
 * @author aldenml
 */
public class DhtStorageBase implements DhtStorage {

    private final Sha1Hash id;
    private final DhtSettings settings;
    private final Counters counters;
    private final boolean print;

    private final HashMap<Sha1Hash, TorrentEntry> torrents;
    private final HashMap<Sha1Hash, DhtImmutableItem> immutables;
    private final HashMap<Sha1Hash, DhtMutableItem> mutables;

    public DhtStorageBase(Sha1Hash id, DhtSettings settings, boolean print) {
        this.id = id;
        this.settings = settings;
        this.counters = new Counters();
        this.print = print;

        this.torrents = new HashMap<Sha1Hash, TorrentEntry>();
        this.immutables = new HashMap<Sha1Hash, DhtImmutableItem>();
        this.mutables = new HashMap<Sha1Hash, DhtMutableItem>();
    }

    public DhtStorageBase(Sha1Hash id, DhtSettings settings) {
        this(id, settings, false);
    }

    @Override
    public boolean getPeers(Sha1Hash infoHash, boolean noseed, boolean scrape, entry peers) {
        TorrentEntry v = torrents.get(infoHash);

        if (v == null) {
            if (print) {
                print("get_peers", "no peers for: " + infoHash);
            }
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
                libtorrent.sha1_hash_address(peer.addr.address(), iphash);
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
                int n = libtorrent.write_tcp_endpoint(e.addr, endpoint);
                endpoint.resize(n);
                pe.push_back(new entry(endpoint));

                ++m;
            }
        }

        if (print) {
            print("get_peers", peers);
        }

        return true;
    }

    @Override
    public void announcePeer(Sha1Hash infoHash, TcpEndpoint endp, String name, boolean seed) {
        TorrentEntry v = torrents.get(infoHash);
        if (v == null) {
            // we don't have this torrent, add it
            // do we need to remove another one first?
            if (!torrents.isEmpty() && torrents.size() >= settings.maxTorrents()) {
                // we need to remove some. Remove the ones with the
                // fewest peers
                int num_peers = Integer.MAX_VALUE;
                Sha1Hash candidateKey = null;
                for (Map.Entry<Sha1Hash, TorrentEntry> kv : torrents.entrySet()) {

                    if (kv.getValue().peers.size() > num_peers) {
                        continue;
                    }
                    if (infoHash.equals(kv.getKey())) {
                        continue;
                    }
                    num_peers = kv.getValue().peers.size();
                    candidateKey = kv.getKey();
                }
                torrents.remove(candidateKey);
                counters.peers -= num_peers;
                counters.torrents -= 1;
            }
            counters.torrents += 1;
            v = new TorrentEntry();
            torrents.put(infoHash, v);
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
        peer.addr = endp.swig();
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

        if (print) {
            print("announce_peer", "Name: " + name);
        }
    }

    @Override
    public boolean getImmutableItem(Sha1Hash target, entry item) {
        DhtImmutableItem i = immutables.get(target);
        if (i == null) {
            return false;
        }

        item.set("v", entry.bdecode(Vectors.bytes2byte_vector(i.value)));

        if (print) {
            print("get_immutable_item", item);
        }

        return true;
    }

    @Override
    public void putImmutableItem(Sha1Hash target, byte[] buf, Address addr) {
        DhtImmutableItem i = immutables.get(target);
        if (i == null) {
            // make sure we don't add too many items
            if (immutables.size() >= settings.maxDhtItems()) {
                // delete the least important one (i.e. the one
                // the fewest peers are announcing, and farthest
                // from our node ID)
                Map.Entry<Sha1Hash, DhtImmutableItem> j = Collections.min(immutables.entrySet(), new ImmutableItemComparator(id));
                immutables.remove(j.getKey());
                counters.immutable_data -= 1;
            }
            i = new DhtImmutableItem();
            i.value = buf;
            i.ips = new bloom_filter_128();
            immutables.put(target, i);
            counters.immutable_data += 1;
        }

        touchItem(i, addr.swig());

        if (print) {
            print("put_immutable_item", "From: " + addr);
        }
    }

    @Override
    public long getMutableItemSeq(Sha1Hash target) {
        DhtMutableItem i = mutables.get(target);
        return i != null ? i.seq : -1;
    }

    @Override
    public boolean getMutableItem(Sha1Hash target, long seq, boolean forceFill, entry item) {
        DhtMutableItem f = mutables.get(target);
        if (f == null) {
            return false;
        }

        item.set("seq", f.seq);
        if (forceFill || (0 <= seq && seq < f.seq)) {
            item.set("v", entry.bdecode(Vectors.bytes2byte_vector(f.value)));
            item.set("sig", Vectors.bytes2byte_vector(f.sig));
            item.set("k", Vectors.bytes2byte_vector(f.key));
        }

        if (print) {
            print("get_mutable_item", item);
        }

        return true;
    }

    @Override
    public void putMutableItem(Sha1Hash target, byte[] buf, byte[] sig, long seq, byte[] pk, byte[] salt, Address addr) {
        DhtMutableItem i = mutables.get(target);
        if (i == null) {
            // this is the case where we don't have an item in this slot
            // make sure we don't add too many items
            if (mutables.size() >= settings.maxDhtItems()) {
                // delete the least important one (i.e. the one
                // the fewest peers are announcing)
                Map.Entry<Sha1Hash, DhtMutableItem> j = Collections.min(mutables.entrySet(), new Comparator<Map.Entry<Sha1Hash, DhtMutableItem>>() {
                    @Override
                    public int compare(Map.Entry<Sha1Hash, DhtMutableItem> o1, Map.Entry<Sha1Hash, DhtMutableItem> o2) {
                        return Integer.compare(o1.getValue().num_announcers, o2.getValue().num_announcers);
                    }
                });
                mutables.remove(j.getKey());
                counters.mutable_data -= 1;
            }
            i = new DhtMutableItem();
            i.value = buf;
            i.ips = new bloom_filter_128();
            i.seq = seq;
            i.salt = salt;
            i.sig = sig;
            i.key = pk;

            mutables.put(target, i);
            counters.mutable_data += 1;
        } else {
            // this is the case where we already
            DhtMutableItem item = i;

            if (item.seq < seq) {
                item.seq = seq;
                item.value = buf;
            }
        }

        touchItem(i, addr.swig());

        if (print) {
            print("put_mutable_item", "From: " + addr);
        }
    }

    @Override
    public void tick() {
        long now = System.currentTimeMillis();

        // look through all peers and see if any have timed out
        Iterator<TorrentEntry> torrentsIt = torrents.values().iterator();
        while (torrentsIt.hasNext()) {
            TorrentEntry t = torrentsIt.next();
            purgePeers(t.peers);

            if (!t.peers.isEmpty()) {
                continue;
            }

            // if there are no more peers, remove the entry altogether
            torrentsIt.remove();
            counters.torrents -= 1;// peers is decreased by purge_peers
        }

        if (0 == settings.itemLifetime()) {
            return;
        }

        int lifetime = settings.itemLifetime();
        // item lifetime must >= 120 minutes.
        if (lifetime < 120 * 60) lifetime = 120 * 60;

        Iterator<DhtImmutableItem> immutablesIt = immutables.values().iterator();
        while (immutablesIt.hasNext()) {
            DhtImmutableItem i = immutablesIt.next();
            if (i.last_seen + lifetime * 1000 > now) {
                continue;
            }
            immutablesIt.remove();
            counters.immutable_data -= 1;
        }

        Iterator<DhtMutableItem> mutablesIt = mutables.values().iterator();
        while (mutablesIt.hasNext()) {
            DhtMutableItem i = mutablesIt.next();
            if (i.last_seen + lifetime * 1000 > now) {
                continue;
            }
            mutablesIt.remove();
            counters.mutable_data -= 1;
        }
    }

    @Override
    public Counters counters() {
        return counters;
    }

    private void purgePeers(Set<PeerEntry> peers) {
        int announce_interval = 30;
        Iterator<PeerEntry> it = peers.iterator();
        while (it.hasNext()) {
            PeerEntry i = it.next();
            // the peer has timed out
            if (i.added + (announce_interval * 1.5f * 60 * 1000) < System.currentTimeMillis()) {
                it.remove();
                counters.peers -= 1;
            }
        }
    }

    private static void print(String operation, entry entry) {
        System.out.println("DHT OP: " + operation);
        System.out.println(entry.to_string());
    }

    private static void print(String operation, String msg) {
        System.out.println("DHT OP: " + operation);
        System.out.println(msg);
    }

    private static void touchItem(DhtImmutableItem f, address address) {
        f.last_seen = System.currentTimeMillis();

        // maybe increase num_announcers if we haven't seen this IP before
        sha1_hash iphash = new sha1_hash();
        libtorrent.sha1_hash_address(address, iphash);
        if (!f.ips.find(iphash)) {
            f.ips.set(iphash);
            f.num_announcers++;
        }
    }

    private static final class PeerEntry {

        public long added;
        public tcp_endpoint addr;
        public boolean seed;

        public static final Comparator<PeerEntry> COMPARATOR = new Comparator<PeerEntry>() {
            @Override
            public int compare(PeerEntry o1, PeerEntry o2) {
                tcp_endpoint a1 = o1.addr;
                tcp_endpoint a2 = o2.addr;

                int r = address.compare(a1.address(), a2.address());

                return r == 0 ? Integer.compare(a1.port(), a2.port()) : r;
            }
        };
    }

    private static final class TorrentEntry {

        public TorrentEntry() {
            this.name = "";
            this.peers = new TreeSet<PeerEntry>(PeerEntry.COMPARATOR);
        }

        public String name;
        public TreeSet<PeerEntry> peers;
    }

    private static class DhtImmutableItem {
        // malloced space for the actual value
        public byte[] value;
        // this counts the number of IPs we have seen
        // announcing this item, this is used to determine
        // popularity if we reach the limit of items to store
        public bloom_filter_128 ips;
        // the last time we heard about this
        public long last_seen;
        // number of IPs in the bloom filter
        public int num_announcers;
    }

    private static final class DhtMutableItem extends DhtImmutableItem {
        public byte[] sig;
        public long seq;
        public byte[] key;
        public byte[] salt;
    }

    private static final class ImmutableItemComparator implements Comparator<Map.Entry<Sha1Hash, DhtImmutableItem>> {

        private final Sha1Hash ourId;

        public ImmutableItemComparator(Sha1Hash ourId) {
            this.ourId = ourId;
        }

        @Override
        public int compare(Map.Entry<Sha1Hash, DhtImmutableItem> lhs, Map.Entry<Sha1Hash, DhtImmutableItem> rhs) {
            int l_distance = libtorrent.dht_distance_exp(lhs.getKey().swig(), ourId.swig());
            int r_distance = libtorrent.dht_distance_exp(rhs.getKey().swig(), ourId.swig());

            // this is a score taking the popularity (number of announcers) and the
            // fit, in terms of distance from ideal storing node, into account.
            // each additional 5 announcers is worth one extra bit in the distance.
            // that is, an item with 10 announcers is allowed to be twice as far
            // from another item with 5 announcers, from our node ID. Twice as far
            // because it gets one more bit.
            int a = lhs.getValue().num_announcers / 5 - l_distance;
            int b = rhs.getValue().num_announcers / 5 - r_distance;
            return Integer.compare(a, b);
        }
    }
}
