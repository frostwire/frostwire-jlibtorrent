package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alerts;
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

        this.mem = new LinkedList<>();
        this.memLock = new Object();
    }

    /*
    @Override
    public swig_torrent_plugin new_torrent(torrent_handle t) {
        try {
            if (p.handleOperation(Plugin.Operation.NEW_TORRENT)) {
                TorrentPlugin tp = p.newTorrent(new TorrentHandle(t));

                if (tp != null) {
                    return pin(new SwigTorrentPlugin(tp, t));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_torrent)", e);
        }

        return super.new_torrent(t);
    }*/

    //@Override
    public void added(session_handle s) {
        try {
            p.added(new SessionHandle(s));
        } catch (Throwable e) {
            LOG.error("Error in plugin (added)", e);
        }
    }

    @Override
    public void on_alert(alert a) {
        try {
            if (p.handleOperation(Plugin.Operation.ON_ALERT)) {
                p.onAlert(Alerts.cast(a));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_alert)", e);
        }
    }

    @Override
    public boolean on_unknown_torrent(sha1_hash info_hash, peer_connection_handle pc, add_torrent_params p) {
        try {
            if (this.p.handleOperation(Plugin.Operation.ON_UNKNOWN_TORRENT)) {
                return this.p.onUnknownTorrent(new Sha1Hash(info_hash), new PeerConnectionHandle(pc), new AddTorrentParams(p));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_unknown_torrent)", e);
        }

        return false;
    }

    @Override
    public void on_tick() {
        try {
            p.onTick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_tick)", e);
        }

        cleanup();
    }

    @Override
    public void save_state(entry e) {
        try {
            if (p.handleOperation(Plugin.Operation.SAVE_STATE)) {
                p.saveState(new Entry(e));
            }
        } catch (Throwable t) {
            LOG.error("Error in plugin (save_state)", t);
        }
    }

    @Override
    public void load_state(bdecode_node n) {
        try {
            if (p.handleOperation(Plugin.Operation.LOAD_STATE)) {
                p.loadState(n);
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (load_state)", e);
        }
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
                if (!p.t.is_valid()) {
                    it.remove();
                }
            }
        }
    }
}
