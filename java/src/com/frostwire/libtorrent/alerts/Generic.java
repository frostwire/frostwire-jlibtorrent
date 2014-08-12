package com.frostwire.libtorrent.alerts;

public class Generic extends Alert {

    public Generic(long timestamp, int type, String what, String message, int category, boolean discardable) {
        super(timestamp, type, what, message, category, discardable);
    }
}
