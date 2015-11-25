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

    private final List<SwigDhtPlugin> memDht;

    public SwigPlugin(Plugin p) {
        this.p = p;

        this.mem = new LinkedList<SwigTorrentPlugin>();
        this.memLock = new Object();

        this.memDht = new LinkedList<SwigDhtPlugin>();
    }

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
    }

    @Override
    public void added(session_handle s) {
        try {
            p.added(new SessionHandle(s));
        } catch (Throwable e) {
            LOG.error("Error in plugin (added)", e);
        }
    }

    @Override
    public void register_dht_extensions(string_dht_extension_handler_listener_ptr_pair_vector dht_extensions) {
        try {
            if (p.handleOperation(Plugin.Operation.REGISTER_DHT_EXTENSIONS)) {
                List<Pair<String, DhtPlugin>> plugins = new LinkedList<Pair<String, DhtPlugin>>();
                p.registerDhtPlugins(plugins);

                for (Pair<String, DhtPlugin> pp : plugins) {
                    String q = pp.first;
                    SwigDhtPlugin h = pin(new SwigDhtPlugin(pp.second));
                    string_dht_extension_handler_listener_ptr_pair pair = new string_dht_extension_handler_listener_ptr_pair(q, h);
                    dht_extensions.add(pair);
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (register_dht_extensions)", e);
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
    public boolean on_optimistic_unchoke(peer_connection_handle_vector peers) {
        try {
            if (this.p.handleOperation(Plugin.Operation.ON_OPTIMISTIC_UNCHOKE)) {
                int size = (int) peers.size();
                PeerConnectionHandle[] arr = new PeerConnectionHandle[size];

                for (int i = 0; i < size; i++) {
                    arr[i] = new PeerConnectionHandle(peers.get(i));
                }

                return p.onOptimisticUnchoke(arr);
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_optimistic_unchoke)", e);
        }

        return false;
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

    private SwigDhtPlugin pin(SwigDhtPlugin p) {
        memDht.add(p);
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
