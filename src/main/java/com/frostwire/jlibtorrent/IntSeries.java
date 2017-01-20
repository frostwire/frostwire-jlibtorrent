package com.frostwire.jlibtorrent;

/**
 * This is a limited capability data point series backed by
 * a circular buffer logic. Used to hold equally timed sample
 * of statistics.
 *
 * @author aldenml
 * @author gubatron
 */
public final class IntSeries {

    private final int[] buffer;
    private int head;
    private int end;

    IntSeries(int[] buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer to hold data can't be null");
        }
        if (buffer.length == 0) {
            throw new IllegalArgumentException("buffer to hold data can't be of size 0");
        }
        this.buffer = buffer;

        head = -1;
        end = -1;
    }

    IntSeries(int capacity) {
        this(new int[capacity]);
    }

    void add(int value) {
        if (head == -1) {
            head = end = 0;
            buffer[end] = value;
            return;
        }

        end = (end + 1) % buffer.length;
        buffer[end] = value;
        if (end <= head) {
            head = (head + 1) % buffer.length;
        }
    }

    /**
     * This method will always returns a value, but keep in mind that
     * due to the nature of the circular buffer internal logic, if you pass
     * past the size, you will get the sames values again. Use {@link #size()}
     * for a proper boundary check.
     *
     * @param index the value's index
     * @return the value in the series
     */
    public int get(int index) {
        return buffer[(head + index) % size()];
    }

    public int size() {
        if (head == -1) {
            return 0;
        } else if (head <= end) {
            return 1 + (end - head);
        } else {
            return buffer.length;
        }
    }

    @Override
    public String toString() {
        StringBuffer arrayStr = new StringBuffer();
        arrayStr.append("[ ");
        for (int i = 0; i < buffer.length; i++) {
            arrayStr.append(buffer[i]);
            if (i != buffer.length - 1) {
                arrayStr.append(", ");
            }
        }
        arrayStr.append(" ]");
        return "{ head: " + head + ", end: " + end + ", size: " + size() + ", buffer: " + arrayStr.toString() + " }";
    }
}
