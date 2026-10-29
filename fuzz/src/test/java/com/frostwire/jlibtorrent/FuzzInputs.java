package com.frostwire.jlibtorrent;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Keeps the input being fuzzed in a memory-mapped file before each call into native code.
 * <p>
 * A crash in libtorrent or the SWIG layer kills the JVM, and with it Jazzer, before it can write a
 * {@code crash-*} file. The page cache still holds this mapping, so after a native crash
 * {@code fuzz/last-input.bin} has the input that crashed: a 4-byte big-endian length followed by the
 * bytes. Recording is a copy into mapped memory, cheap enough for every execution.
 * <p>
 * Extract it with {@code python3 -c "import sys,struct; d=open('fuzz/last-input.bin','rb').read();
 * n=struct.unpack('>i',d[:4])[0]; sys.stdout.buffer.write(d[4:4+n])" > crash-native}.
 */
final class FuzzInputs {

    private static final int CAPACITY = 1 << 20;
    private static final MappedByteBuffer LAST = map(System.getProperty("jlibtorrent.fuzz.lastInput"));

    private FuzzInputs() {
    }

    static byte[] record(byte[] input) {
        if (LAST != null) {
            int n = Math.min(input.length, CAPACITY);
            LAST.put(4, input, 0, n);
            LAST.putInt(0, n);
        }
        return input;
    }

    static String record(String input) {
        record(input.getBytes(StandardCharsets.UTF_8));
        return input;
    }

    private static MappedByteBuffer map(String file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Path path = Paths.get(file);
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE,
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            return channel.map(FileChannel.MapMode.READ_WRITE, 0, 4 + CAPACITY);
        } catch (IOException e) {
            throw new IllegalStateException("can't map " + path, e);
        }
    }
}
