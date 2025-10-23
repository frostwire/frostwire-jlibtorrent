package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for converting between Java and native C++ vector types.
 * <p>
 * {@code Vectors} provides static helper functions to convert between Java collections and
 * SWIG-generated native vector wrappers. These conversions are essential for interoperability
 * between jlibtorrent's Java API and the underlying libtorrent C++ library.
 * <p>
 * <b>Understanding Vector Conversions:</b>
 * <br/>
 * The native libtorrent library uses C++ vectors (std::vector) for collections. SWIG generates
 * Java wrapper classes for these types (e.g., {@code byte_vector}, {@code string_vector}).
 * This utility class converts between Java native types and SWIG-wrapped vectors:
 * <ul>
 *   <li><b>Byte Vectors:</b> Convert between {@code byte[]}, {@code String}, and native vectors</li>
 *   <li><b>Int Vectors:</b> Convert between {@code int[]}, lists, and native vectors</li>
 *   <li><b>Long Vectors:</b> Convert between {@code long[]}, lists, and native vectors</li>
 *   <li><b>String Vectors:</b> Convert between {@code List<String>} and native vectors</li>
 *   <li><b>Byte Arrays:</b> Convert between fixed-size arrays (32, 64 bytes) and native arrays</li>
 * </ul>
 * <p>
 * <b>Byte Array Conversions:</b>
 * <pre>
 * // Convert native byte_vector to Java byte array
 * byte_vector nativeVec = ...; // From libtorrent
 * byte[] javaArray = Vectors.byte_vector2bytes(nativeVec);
 *
 * // Convert Java byte array to native byte_vector
 * byte[] data = new byte[] {1, 2, 3, 4, 5};
 * byte_vector nativeVec = Vectors.bytes2byte_vector(data);
 *
 * // Batch conversion (more efficient for large arrays)
 * byte_vector source = ...;
 * byte[] destination = new byte[1024];
 * Vectors.byte_vector2bytes(source, destination);
 * </pre>
 * <p>
 * <b>Integer Vector Conversions:</b>
 * <pre>
 * // Convert native int_vector to Java int array
 * int_vector nativeVec = ...; // From libtorrent
 * int[] javaArray = Vectors.int_vector2ints(nativeVec);
 *
 * // Convert native int64_vector to Java long array
 * int64_vector nativeLongVec = ...;
 * long[] javaArray = Vectors.int64_vector2longs(nativeLongVec);
 * </pre>
 * <p>
 * <b>String Vector Conversions:</b>
 * <pre>
 * // Convert native string_vector to Java List<String>
 * string_vector nativeVec = ...; // From libtorrent
 * java.util.List&lt;String&gt; stringList = Vectors.string_vector2list(nativeVec);
 *
 * for (String str : stringList) {
 *     System.out.println(\"String: \" + str);
 * }
 * </pre>
 * <p>
 * <b>String Encoding Conversions:</b>
 * <pre>
 * // Convert byte_vector to String with specific encoding
 * byte_vector data = ...; // From libtorrent
 *
 * // UTF-8 encoding (supports international characters)
 * String utf8String = Vectors.byte_vector2utf8(data);
 *
 * // ASCII encoding (7-bit ASCII only)
 * String asciiString = Vectors.byte_vector2ascii(data);
 *
 * // Custom encoding
 * String customString = Vectors.byte_vector2string(data, \"ISO-8859-1\");
 *
 * // Reverse: convert String to byte_vector
 * String text = \"Hello, World!\";
 * byte_vector utf8Vec = Vectors.ascii2byte_vector(text);\n * byte_vector customVec = Vectors.string2byte_vector(text, \"UTF-8\");
 * </pre>
 * <p>
 * <b>Fixed-Size Byte Array Conversions:</b>
 * <pre>
 * // Convert 32-byte fixed array (e.g., SHA-256 hash)
 * byte[] hash32 = new byte[32];\n * // ... populate hash ...\n * byte_array_32 nativeArray = Vectors.bytes2byte_array_32(hash32);
 *
 * // Convert back
 * byte_array_32 native = ...;
 * byte[] javaArray = Vectors.byte_array2bytes(native);\n *
 * // 64-byte fixed array (e.g., Ed25519 keypair)
 * byte_array_64 keyPair = Vectors.bytes2byte_array_64(new byte[64]);
 * byte[] javaKeyPair = Vectors.byte_array2bytes(keyPair);
 * </pre>
 * <p>
 * <b>Byte Span Conversions (Const References):</b>
 * <pre>
 * // byte_const_span is a read-only reference to byte data
 * byte_const_span constSpan = ...;  // From libtorrent
 * byte[] data = Vectors.byte_span2bytes(constSpan);\n * System.out.println(\"Data size: \" + data.length);
 * </pre>
 * <p>
 * <b>Creating New Native Vectors:</b>
 * <pre>
 * // Create a zero-initialized native byte_vector
 * int size = 1024;
 * byte_vector nativeVec = Vectors.new_byte_vector(size);
 *
 * // Now you can pass it to libtorrent functions that populate it
 * libtorrent.some_function_that_fills_vector(nativeVec);
 *
 * // Convert result back to Java array
 * byte[] result = Vectors.byte_vector2bytes(nativeVec);
 * </pre>
 * <p>
 * <b>Common Use Cases in jlibtorrent:</b>
 * <pre>
 * // Creating a torrent (converting file hashes)
 * java.util.List&lt;String&gt; hashes = ...;
 * string_vector nativeHashes = new string_vector();
 * for (String hash : hashes) {
 *     nativeHashes.add(hash);
 * }
 *
 * // Reading session statistics
 * int64_vector statsVec = session.get_stats();
 * long[] stats = Vectors.int64_vector2longs(statsVec);
 *
 * // Processing piece hashes
 * byte_const_span pieceHashes = torrentInfo.pieces();
 * byte[] hashes = Vectors.byte_span2bytes(pieceHashes);
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Conversions create new Java objects; reuse arrays when possible for efficiency</li>
 *   <li>Batch conversion methods (e.g., {@code byte_vector2bytes(source, dest)}) avoid creating temporary arrays</li>
 *   <li>String encoding conversions have overhead; cache results if used repeatedly</li>
 *   <li>Native vectors are garbage-collected when no longer referenced</li>
 *   <li>Prefer UTF-8 for international text; ASCII for simple byte data</li>
 * </ul>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>String conversions null-terminate at first zero byte (C string convention)</li>
 *   <li>Fixed-size arrays must be exact size (32 or 64 bytes) or conversion will fail</li>
 *   <li>SWIG-generated types are from {@code com.frostwire.jlibtorrent.swig} package</li>
 *   <li>These utilities are internal to jlibtorrent; direct use is rare in application code</li>
 * </ul>
 *
 * @see com.frostwire.jlibtorrent.swig.byte_vector - Native byte vector type
 * @see com.frostwire.jlibtorrent.swig.string_vector - Native string vector type
 * @see java.util.List - Standard Java collection type
 * @see java.nio.ByteBuffer - Alternative for byte data manipulation
 *
 * @author gubatron
 * @author aldenml
 */
public final class Vectors {

    private Vectors() {
    }

    public static byte[] byte_vector2bytes(byte_vector v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static void byte_vector2bytes(byte_vector v, byte[] arr) {
        int size = (int) v.size();

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }
    }

    public static byte_vector bytes2byte_vector(byte[] arr) {
        byte_vector v = new byte_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add(arr[i]);
        }

        return v;
    }

    public static void bytes2byte_vector(byte[] arr, byte_vector v) {
        for (int i = 0; i < arr.length; i++) {
            v.set(i, arr[i]);
        }
    }

    public static int[] int_vector2ints(int_vector v) {
        int size = (int) v.size();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static long[] int64_vector2longs(int64_vector v) {
        int size = (int) v.size();
        long[] arr = new long[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte[] byte_span2bytes(byte_const_span v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte_vector new_byte_vector(int size) {
        byte_vector v = new byte_vector();
        byte z = (byte) 0;
        for (int i = 0; i < size; i++) {
            v.add(z);
        }

        return v;
    }

    public static List<String> string_vector2list(string_vector v) {
        int size = (int) v.size();
        ArrayList<String> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    public static String byte_vector2string(byte_vector v, String encoding) {
        byte[] arr = Vectors.byte_vector2bytes(v);

        int n = 0;
        for (; n < arr.length && arr[n] != 0; ) {
            n++;
        }

        if (n == 0) {
            return "";
        }

        try {
            return new String(arr, 0, n, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String byte_vector2ascii(byte_vector v) {
        return byte_vector2string(v, "US-ASCII");
    }

    public static String byte_vector2utf8(byte_vector v) {
        return byte_vector2string(v, "UTF-8");
    }

    public static byte_vector string2byte_vector(String s, String encoding) {
        try {
            byte[] arr = s.getBytes(encoding);
            return bytes2byte_vector(arr);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte_vector ascii2byte_vector(String s) {
        return string2byte_vector(s, "US-ASCII");
    }

    public static byte_array_32 bytes2byte_array_32(byte[] arr) {
        byte_array_32 v = new byte_array_32();

        for (int i = 0; i < 32; i++) {
            v.set(i, arr[i]);
        }

        return v;
    }

    public static byte[] byte_array2bytes(byte_array_32 v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte[] byte_array2bytes(byte_array_64 v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte_array_64 bytes2byte_array_64(byte[] arr) {
        byte_array_64 v = new byte_array_64();

        for (int i = 0; i < 64; i++) {
            v.set(i, arr[i]);
        }

        return v;
    }
}
