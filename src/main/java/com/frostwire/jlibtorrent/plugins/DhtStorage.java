package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.entry;

/**
 * @author gubatron
 * @author aldenml
 */
public interface DhtStorage {

    boolean getPeers(Sha1Hash infoHash, boolean noseed, boolean scrape, entry peers);

    void announcePeer(Sha1Hash infoHash, TcpEndpoint endp, String name, boolean seed);

    boolean getImmutableItem(Sha1Hash target, entry item);

    void putImmutableItem(Sha1Hash target, byte[] buf, Address addr);

    long getMutableItemSeq(Sha1Hash target);

    boolean getMutableItem(Sha1Hash target, long seq, boolean forceFill, entry item);

    void putMutableItem(Sha1Hash target, byte[] buf, byte[] sig, long seq, byte[] pk, byte[] salt, Address addr);

    void tick();

    long numTorrents();

    long numPeers();

    long numImmutableData();

    long numMutableData();
}
