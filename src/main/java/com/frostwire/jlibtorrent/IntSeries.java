package com.frostwire.jlibtorrent;

/**
 * Circular buffer for storing time-series integer statistics.
 * <p>
 * {@code IntSeries} maintains a fixed-size circular buffer of data points, typically
 * used to store equally-spaced time-series samples of session statistics. Once the buffer
 * fills, new data overwrites the oldest data, maintaining a rolling window of recent samples.
 * <p>
 * <b>Understanding Circular Buffers:</b>
 * <br/>
 * A circular buffer is a fixed-size array where write position wraps around to the start
 * after reaching the end. This is efficient for maintaining a sliding window of data:
 * <pre>
 * // Example: 5-element buffer, adding values 1, 2, 3, 4, 5, 6
 * // After adding: [6, 2, 3, 4, 5]
 * //                ^
 * //                write position wraps around
 * </pre>
 * <p>
 * <b>Creating an IntSeries:</b>
 * <pre>
 * // Create series with capacity for 60 samples (e.g., 60 seconds of data)
 * IntSeries series = new IntSeries(60);
 *
 * // Or create from existing buffer
 * long[] buffer = new long[120];
 * IntSeries series = new IntSeries(buffer);
 * </pre>
 * <p>
 * <b>Adding Data Points:</b>
 * <pre>
 * IntSeries series = new IntSeries(60);
 *
 * // Add statistics samples (typically called once per second)
 * for (int i = 0; i &lt; 65; i++) {
 *     long downloadBytes = sm.stats().totalDownload();
 *     series.add(downloadBytes);
 *
 *     if (i == 60) {
 *         // After 60 samples, oldest data starts getting overwritten
 *         System.out.println(\"Buffer size: \" + series.size());  // = 60
 *     }
 * }
 * </pre>
 * <p>
 * <b>Querying Data:</b>
 * <pre>
 * // Get most recent value
 * long latest = series.last();
 *
 * // Get series size (how many samples stored)
 * int samples = series.size();
 *
 * // Iterate through all samples (newest to oldest)
 * for (int i = 0; i &lt; series.size(); i++) {
 *     long value = series.get(i);
 *     System.out.println(\"Sample \" + i + \": \" + value);
 * }
 * </pre>
 * <p>
 * <b>Practical Use Case - Download Speed Calculation:</b>
 * <pre>
 * // Track download speed over 60 seconds
 * IntSeries downloadSamples = new IntSeries(60);
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * // Sample download bytes every second
 * Timer timer = new Timer();
 * timer.schedule(new TimerTask() {
 *     public void run() {
 *         SessionStats stats = sm.stats();
 *         long totalDownload = stats.totalDownload();
 *         downloadSamples.add(totalDownload);
 *     }
 * }, 0, 1000);  // Every 1000ms
 *
 * // Calculate average speed over last minute
 * long firstSample = downloadSamples.get(downloadSamples.size() - 1);
 * long lastSample = downloadSamples.last();
 * long deltaBytes = lastSample - firstSample;
 * long deltaSeconds = downloadSamples.size();  // One second per sample
 * long avgSpeed = deltaBytes / deltaSeconds;  // bytes per second
 * </pre>
 * <p>
 * <b>Buffer Behavior:</b>
 * <pre>
 * // New series starts empty
 * IntSeries series = new IntSeries(5);
 * System.out.println(series.size());  // 0
 *
 * series.add(100);
 * System.out.println(series.size());  // 1
 * System.out.println(series.last());  // 100
 *
 * // Add more samples
 * for (int i = 0; i &lt; 10; i++) {
 *     series.add(100 + i);
 * }
 *
 * // Buffer now full with 5 most recent samples
 * System.out.println(series.size());  // 5
 * System.out.println(series.last());  // 109 (most recent)
 * System.out.println(series.get(4)); // 105 (oldest in buffer)
 * </pre>
 * <p>
 * <b>Index Wrapping:</b>
 * <pre>
 * // Warning: Index wraps around buffer boundaries
 * IntSeries series = new IntSeries(3);
 * series.add(10);
 * series.add(20);
 * series.add(30);
 *
 * // series.get(0) = 10  (oldest)
 * // series.get(1) = 20  (middle)
 * // series.get(2) = 30  (newest/last)
 *
 * // Be careful! Accessing beyond size wraps around
 * // series.get(series.size() - 1) returns last()
 * // series.get(series.size()) may return wrappedindex values
 * </pre>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>Buffer size is fixed at creation; no growth or shrinking</li>
 *   <li>New series has size() = 0 until first value added</li>
 *   <li>last() returns 0 if series empty or end pointer not set</li>
 *   <li>Access beyond size() causes index wrapping (undefined behavior)</li>
 *   <li>Always check size() before accessing; don't assume capacity</li>
 *   <li>Used internally for sliding-window statistics</li>
 * </ul>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>add(): O(1) - constant time operation</li>
 *   <li>get(): O(1) - direct array access with modulo calculation</li>
 *   <li>last(): O(1) - direct element access</li>
 *   <li>Memory: Fixed by buffer capacity, no dynamic allocation</li>
 * </ul>
 *
 * @see SessionStats - Session statistics including sample data
 * @see java.util.ArrayList - Dynamic alternative (uses more memory)</li>
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
