package com.frostwire.jlibtorrent;

public final class PosixTime {

    private PosixTime() {
    }

    public static long time2millis(int time) {
        return ((long) time) * 1000;
    }
}
