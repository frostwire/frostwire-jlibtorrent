package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;
import com.frostwire.libtorrent.alerts.Generic;

public final class Alerts {

    private Alerts() {
    }

    public static Alert generic(long timestamp, int type, String what, String message, int category, boolean discardable) {
        return new Generic(timestamp, type, what, message, category, discardable);
    }

    public static Alert test(String what) {
        return new Generic(System.currentTimeMillis(), 0, what, what, 0, true);
    }
}
