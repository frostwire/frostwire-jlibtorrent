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

    public void add(JavaStatChannel s) {
        counter += s.counter;
        totalCounter += s.counter;
    }

    public void add(int count) {
        counter += count;
        totalCounter += count;
    }

    // should be called once every second
    public void secondTick(int tickIntervalMs) {
        long sample = (counter * 1000) / tickIntervalMs;
        averageSec5 = (averageSec5 * 4) / 5 + sample / 5;
        counter = 0;
    }

    public long rate() {
        return averageSec5;
    }

    public long total() {
        return totalCounter;
    }

    void offset(long c) {
        totalCounter += c;
    }

    public long counter() {
        return counter;
    }

    public void clear() {
        counter = 0;
        averageSec5 = 0;
        totalCounter = 0;
    }
}
