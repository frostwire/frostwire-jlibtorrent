package com.frostwire.libtorrent;

public final class Alerts {

    private Alerts() {
    }

    public abstract static class Alert {

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

    public static class Generic extends Alert {

        public Generic(long timestamp, int type, String what, String message, int category, boolean discardable) {
            super(timestamp, type, what, message, category, discardable);
        }
    }

    public static Alert generic(long timestamp, int type, String what, String message, int category, boolean discardable) {
        return new Generic(timestamp, type, what, message, category, discardable);
    }
}
