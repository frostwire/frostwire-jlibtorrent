package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alerts_dropped_alert;

/**
 * This alert is posted to indicate to the client that some alerts were
 * dropped. Dropped meaning that the alert failed to be delivered to the
 * client. The most common cause of such failure is that the internal alert
 * queue grew too big (controlled by alert_queue_size).
 *
 * @author gubatron
 * @author aldenml
 */
public final class AlertsDroppedAlert extends AbstractAlert<alerts_dropped_alert> {

    AlertsDroppedAlert(alerts_dropped_alert alert) {
        super(alert);
    }
}
