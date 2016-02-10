package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
final class SwigDhtStorage extends swig_dht_storage {

    private static final Logger LOG = Logger.getLogger(SwigDhtStorage.class);

    private final DhtStorage s;

    public SwigDhtStorage(DhtStorage s) {
        this.s = s;
    }

    @Override
    public boolean get_peers(sha1_hash info_hash, boolean noseed, boolean scrape, entry peers) {
        return s.getPeers(new Sha1Hash(info_hash), noseed, scrape, peers);
    }

    @Override
    public void announce_peer(sha1_hash info_hash, tcp_endpoint endp, String name, boolean seed) {
        s.announcePeer(new Sha1Hash(info_hash), new TcpEndpoint(endp), name, seed);
    }

    @Override
    public boolean get_immutable_item(sha1_hash target, entry item) {
        return s.getImmutableItem(new Sha1Hash(target), item);
    }

    @Override
    public void put_immutable_item(sha1_hash target, byte_vector buf, address addr) {
        s.putImmutableItem(new Sha1Hash(target),
                Vectors.byte_vector2bytes(buf),
                new Address(addr));
    }

    @Override
    public long get_mutable_item_seq_num(sha1_hash target) {
        return s.getMutableItemSeq(new Sha1Hash(target));
    }

    @Override
    public boolean get_mutable_item(sha1_hash target, long seq, boolean force_fill, entry item) {
        return s.getMutableItem(new Sha1Hash(target), seq, force_fill, item);
    }

    @Override
    public void put_mutable_item(sha1_hash target, byte_vector buf, byte_vector sig, long seq, byte_vector pk, byte_vector salt, address addr) {
        s.putMutableItem(new Sha1Hash(target),
                Vectors.byte_vector2bytes(buf),
                Vectors.byte_vector2bytes(sig),
                seq,
                Vectors.byte_vector2bytes(pk),
                Vectors.byte_vector2bytes(salt),
                new Address(addr));
    }

    @Override
    public void tick() {
        s.tick();
    }

    @Override
    public long num_torrents() {
        return s.numTorrents();
    }

    @Override
    public long num_peers() {
        return s.numPeers();
    }

    @Override
    public long num_immutable_data() {
        return s.numImmutableData();
    }

    @Override
    public long num_mutable_data() {
        return s.numMutableData();
    }
}
