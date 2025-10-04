package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PeerInfo;
import com.frostwire.jlibtorrent.swig.peer_info;
import com.frostwire.jlibtorrent.swig.peer_info_alert;
import com.frostwire.jlibtorrent.swig.peer_info_vector;

import java.util.ArrayList;

/**
 * posted when torrent_handle::post_peer_info() is called.
 *
 */
public final class PeerInfoAlert extends TorrentAlert<peer_info_alert> {

    PeerInfoAlert(peer_info_alert alert) {
        super(alert);
    }

    /**
     * the list of the currently connected peers.
     */
    public ArrayList<PeerInfo> getPeerInfo() {
        peer_info_vector v = alert.getPeer_info();
        int size = v.size();
        ArrayList<PeerInfo> peerInfo = new ArrayList<>(size);

        for (peer_info e : v) {
            peerInfo.add(new PeerInfo(e));
        }

        return peerInfo;
    }
}