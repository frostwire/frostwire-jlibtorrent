package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public class PeerConnection<T extends peer_connection> {

    private final T pc;

    public PeerConnection(T pc) {
        this.pc = pc;
    }

    public T getSwig() {
        return pc;
    }

    public int preferContiguousBlocks() {
        return pc.prefer_contiguous_blocks();
    }

    public boolean onParole() {
        return pc.on_parole();
    }

    /*public int pickerOptions() {

    }*/

    public boolean requestLargeBlocks() {
        return pc.request_large_blocks();
    }

    public boolean endgame() {
        return pc.endgame();
    }

    public boolean noDownload() {
        return pc.no_download();
    }

    public boolean ignoreStats() {
        return pc.ignore_stats();
    }
}
