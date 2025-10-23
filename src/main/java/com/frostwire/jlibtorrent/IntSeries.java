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

    private final long[] buffer;

    private int head;
    private int end;
    private int size;

    IntSeries(long[] buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer to hold data can't be null");
        }
        if (buffer.length == 0) {
            throw new IllegalArgumentException("buffer to hold data can't be of size 0");
        }
        this.buffer = buffer;

        head = -1;
        end = -1;
        size = 0;
    }

    IntSeries(int capacity) {
        this(new long[capacity]);
    }

    void add(long value) {
        if (head == -1) { // first element add
            head = end = 0;
            buffer[end] = value;
            size = 1;
            return;
        }

        // update buffer and pointers
        end = (end + 1) % buffer.length;
        buffer[end] = value;
        if (end <= head) {
            head = (head + 1) % buffer.length;
        }

        // update size
        if (head <= end) {
            size = 1 + (end - head);
        } else {
            size = buffer.length;
        }
    }

    /**
     * This method will always returns a value, but keep in mind that
     * due to the nature of the circular buffer internal logic, if you pass
     * past the size, you will get the sames values again.
     * Use  for a proper boundary check.
     * <p>
     * Usage example:
     * <pre>
     * {@code
     *     for (int i = min(series.size(), desired_count); i >= 0; i--) {
     *         plotMyValueAt(i, series.get(i));
     *     }
     * }
     * </pre>
     *
     * @param index the value's index
     * @return the value in the series
     */
    public long get(int index) {
        return buffer[(head + index) % size];
    }

    /**
     * This method will always returns a value, if the series is empty
     * {@code 0} is returned.
     *
     * @return the last value in the series
     */
    public long last() {
        return end != 0 ? buffer[end] : 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < buffer.length; i++) {
            sb.append(buffer[i]);
            if (i != buffer.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
        return "{ head: " + head + ", end: " + end + ", size: " + size() + ", buffer: " + sb.toString() + " }";
    }
}
