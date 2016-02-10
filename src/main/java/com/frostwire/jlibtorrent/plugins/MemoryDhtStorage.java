package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.DhtSettings;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.entry;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MemoryDhtStorage implements DhtStorage {

    private final Sha1Hash id;
    private final DhtSettings settings;
    private final Counters counters;

    public MemoryDhtStorage(Sha1Hash id, DhtSettings settings) {
        this.id = id;
        this.settings = settings;
        this.counters = new Counters();
    }

    @Override
    public boolean getPeers(Sha1Hash infoHash, boolean noseed, boolean scrape, entry peers) {
        return false;
    }

    @Override
    public void announcePeer(Sha1Hash infoHash, TcpEndpoint endp, String name, boolean seed) {

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
        return 0;
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
}
