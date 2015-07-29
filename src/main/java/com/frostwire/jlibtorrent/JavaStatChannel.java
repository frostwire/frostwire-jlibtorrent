package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
final class JavaStatChannel {

    // total counters
    private long totalCounter;

    // the accumulator for this second.
    private long counter;

    // sliding average
    private long averageSec5;

    public JavaStatChannel() {
    }

    public void add(long count) {
        counter += count;
        totalCounter += count;
    }

    // should be called once every second
    public void secondTick(long tickIntervalMs) {
        if (tickIntervalMs >= 1) {
            long sample = (counter * 1000) / tickIntervalMs;
            averageSec5 = (averageSec5 * 4) / 5 + sample / 5;
            counter = 0;
        }
    }

    public long rate() {
        return averageSec5;
    }

    public long total() {
        return totalCounter;
    }

    public void clear() {
        counter = 0;
        averageSec5 = 0;
        totalCounter = 0;
    }
}
