package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.swig.ip_ban_alert;

/**
 * Posted when libtorrent 2.1 bans an IP address, e.g. for sending corrupt data (smart ban).
 *
 * @author gubatron
 */
public final class IpBanAlert extends AbstractAlert<ip_ban_alert> {

    IpBanAlert(ip_ban_alert alert) {
        super(alert);
    }

    /**
     * @return the banned address
     */
    public Address bannedAddress() {
        return new Address(alert.get_banned_address());
    }
}
