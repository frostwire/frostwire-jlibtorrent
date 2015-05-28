package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TorrentHandle;

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

    void onTick();
}
