package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
final class JavaStat {

    private final JavaStatChannel[] stat;

    public JavaStat() {
        this.stat = new JavaStatChannel[Stat.NUM_CHANNELS];
    }
}
