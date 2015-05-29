package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigPlugin extends swig_plugin {

    private static final Logger LOG = Logger.getLogger(SwigPlugin.class);

    private final Plugin p;

    private final List<SwigTorrentPlugin> mem;
    private final Object memLock;

    public SwigPlugin(Plugin p) {
        this.p = p;

        this.mem = new LinkedList<SwigTorrentPlugin>();
        this.memLock = new Object();
    }

    @Override
    public swig_torrent_plugin new_torrent(torrent t) {
        try {
            if (p.handleOperation(Plugin.Operation.NEW_TORRENT)) {
                TorrentPlugin tp = p.newTorrent(new Torrent(t));

                if (p != null) {
                    return pin(new SwigTorrentPlugin(tp, t));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_torrent)", e);
        }

        return super.new_torrent(t);
    }

    @Override
    public void added() {
        try {
            p.added();
        } catch (Throwable e) {
            LOG.error("Error in plugin (added)", e);
        }
    }

    @Override
    public void on_alert(alert a) {
    }

    @Override
    public boolean on_unknown_torrent(sha1_hash info_hash, peer_connection pc, add_torrent_params p) {
        return this.p.onUnknownTorrent(new Sha1Hash(info_hash), new PeerConnection(pc), new AddTorrentParams(p));
    }

    @Override
    public void on_tick() {
        p.onTick();
        cleanup();
    }

    @Override
    public boolean on_optimistic_unchoke(torrent_peer_ptr_vector peers) {
        int size = (int) peers.size();
        TorrentPeer[] arr = new TorrentPeer[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new TorrentPeer(peers.get(i));
        }

        return p.onOptimisticUnchoke(arr);
    }

    @Override
    public void save_state(entry e) {
        p.saveState(new Entry(e));
    }

    @Override
    public void load_state(bdecode_node n) {
        p.loadState(n);
    }

    private SwigTorrentPlugin pin(SwigTorrentPlugin p) {
        mem.add(p);
        return p;
    }

    private void cleanup() {
        synchronized (memLock) {
            Iterator<SwigTorrentPlugin> it = mem.iterator();
            while (it.hasNext()) {
                SwigTorrentPlugin p = it.next();
                if (p.t.is_aborted()) {
                    it.remove();
                }
            }
        }
    }
}
