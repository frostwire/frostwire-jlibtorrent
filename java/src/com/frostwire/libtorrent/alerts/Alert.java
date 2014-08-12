package com.frostwire.libtorrent.alerts;

public abstract class Alert {

    public Alert(long timestamp, int type, String what, String message, int category, boolean discardable) {
        this.timestamp = timestamp;
        this.type = type;
        this.what = what;
        this.message = message;
        this.category = category;
        this.discardable = discardable;
    }

    public final long timestamp;
    public final int type;
    public final String what;
    public final String message;
    public final int category;
    public final boolean discardable;
}
