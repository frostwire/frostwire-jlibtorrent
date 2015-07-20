package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
final class StatChannel {

    // total counters
    private long totalCounter;

    // the accumulator for this second.
    private long counter;

    // sliding average
    private long averageSec5;

    public StatChannel() {
    }

    public void add(StatChannel s) {
        if (counter >= Long.MAX_VALUE - s.counter) {
            throw new IllegalArgumentException("Counter overflow");
        }
        counter += s.counter;
        if (totalCounter >= Long.MAX_VALUE - s.counter) {
            throw new IllegalArgumentException("Counter overflow");
        }
        totalCounter += s.counter;
    }

    public void add(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Counter can't be negative");
        }
        if (counter >= Long.MAX_VALUE - count) {
            throw new IllegalArgumentException("Counter overflow");
        }
        counter += count;
        if (totalCounter >= Long.MAX_VALUE - counter) {
            throw new IllegalArgumentException("Counter overflow");
        }
        totalCounter += count;
    }

    // should be called once every second
    public void secondTick(int tickIntervalMs) {
        long sample = (counter * 1000) / tickIntervalMs;
        if (sample < 0) {
            throw new IllegalStateException("sample can't be negative");
        }
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
        if (totalCounter >= Long.MAX_VALUE - c) {
            throw new IllegalArgumentException("Counter overflow");
        }
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
