package com.frostwire.jlibtorrent;

/**
 * @author aldenml
 * @author gubatron
 */
public class CircularArray {

    private final int[] buffer;
    private int head;
    private int end;

    CircularArray(int capacity) {
        buffer = new int[capacity];
        head = -1;
        end = -1;
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

    int get(int i) {
        if (i < 0 || i > size() - 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return buffer[(head + i) % size()];
    }

    int size() {
        if (head == -1) {
            return 0;
        } else if (head <= end) {
            return 1 + (end - head);
        } else {
            return buffer.length;
        }
    }

    /**
     * Returns up to count elements from the head
     */
    public int[] tail(int count) {
        int bufferSize = size();
        if (bufferSize == 0) {
            return new int[0];
        }
        int tailLength = Math.min(count, bufferSize);
        int[] result = new int[tailLength];
        int pointer = 1 + (end - tailLength);
        if (pointer < 0) {
            pointer = (buffer.length + pointer) % bufferSize;
        }
        int i = 0;
        while (i < result.length) {
            result[i] = buffer[pointer];
            pointer = (pointer + 1) % bufferSize;
            i++;
        }
        return result;
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
