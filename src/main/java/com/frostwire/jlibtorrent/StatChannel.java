package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.stat_channel;

/**
 * @author gubatron
 * @author aldenml
 */
public final class StatChannel {

    private final stat_channel s;

    public StatChannel(stat_channel s) {
        this.s = s;
    }

    public stat_channel getSwig() {
        return s;
    }

    public void add(int count) {
        s.add(count);
    }

    public void secondTick(int tickIntervalMs) {
        s.second_tick(tickIntervalMs);
    }

    public int rate() {
        return s.rate();
    }

    public int lowPassRate() {
        return s.low_pass_rate();
    }

    public long total() {
        return s.total();
    }

    public void offset(long c) {
        s.offset(c);
    }

    public int counter() {
        return s.counter();
    }

    public void clear() {
        s.clear();
    }
}
