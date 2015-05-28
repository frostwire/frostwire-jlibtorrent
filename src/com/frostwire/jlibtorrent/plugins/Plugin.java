package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.bdecode_node;

/**
 * this is the base class for a session plugin. One primary feature
 * // is that it is notified of all torrents that are added to the session,
 * // and can add its own torrent_plugins.
 */
public interface Plugin {

    /**
     * this is called by the session every time a new torrent is added.
     * // The ``torrent*`` points to the internal torrent object created
     * // for the new torrent. The ``void*`` is the userdata pointer as
     * // passed in via add_torrent_params.
     * //
     * // If the plugin returns a torrent_plugin instance, it will be added
     * // to the new torrent. Otherwise, return an empty shared_ptr to a
     * // torrent_plugin (the default).
     *
     * @param th
     * @return
     */
    TorrentPlugin newTorrent(TorrentHandle th);

    /**
     * called when plugin is added to a session
     */
    void added();

    /**
     * return true if the add_torrent_params should be added.
     *
     * @param infoHash
     * @param pc
     * @param p
     * @return
     */
    boolean onUnknownTorrent(Sha1Hash infoHash, PeerConnection pc, AddTorrentParams p);

    /**
     * called once per second.
     */
    void onTick();

    /**
     * called when choosing peers to optimisticly unchoke
     * // peer's will be unchoked in the order they appear in the given
     * // vector which is initiallity sorted by when they were last
     * // optimistically unchoked.
     * // if the plugin returns true then the ordering provided will be
     * // used and no other plugin will be allowed to change it.
     *
     * @param peers
     * @return
     */
    boolean onOptimisticUnchoke(TorrentPeer[] peers);

    /**
     * called when saving settings state.
     *
     * @param e
     */
    void saveState(Entry e);

    /**
     * called when loading settings state.
     *
     * @param n
     */
    void loadState(bdecode_node n);
}
