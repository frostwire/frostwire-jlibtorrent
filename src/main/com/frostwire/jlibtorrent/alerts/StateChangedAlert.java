package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.swig.state_changed_alert;

/**
 * Generated whenever a torrent changes its state.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StateChangedAlert extends TorrentAlert<state_changed_alert> {

    public StateChangedAlert(state_changed_alert alert) {
        super(alert);
    }

    /**
     * The new state of the torrent.
     *
     * @return
     */
    public TorrentStatus.State getState() {
        return TorrentStatus.State.fromSwig(alert.getState().swigValue());
    }

    /**
     * The previous state.
     *
     * @return
     */
    public TorrentStatus.State getPrevState() {
        return TorrentStatus.State.fromSwig(alert.getPrev_state().swigValue());
    }
}
