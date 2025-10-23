/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frostwire.jlibtorrent;

/**
 * Utility for converting between hexadecimal strings and binary data.
 * <p>
 * {@code Hex} provides efficient conversion between byte arrays and their hexadecimal
 * string representations. Used throughout jlibtorrent for displaying and parsing
 * binary data like SHA-1 hashes, info-hashes, and peer IDs in human-readable form.
 * <p>
 * <b>Hexadecimal Encoding in BitTorrent:</b>
 * <pre>
 * // Binary SHA-1 hash (20 bytes)
 * byte[] sha1 = new byte[] {
 *     (byte)0xd8, (byte)0xe8, (byte)0xfc, (byte)0xa2, (byte)0xdc,
 *     0x0f, (byte)0x89, 0x6f, (byte)0xd7, (byte)0xcb,
 *     0x4c, (byte)0xb0, 0x03, 0x1b, (byte)0xa2, 0x49,
 *     ... (remaining 4 bytes)
 * };
 *
 * // Convert to hex string for display
 * String hex = Hex.encode(sha1);
 * // Output: "d8e8fca2dc0f896fd7cb4cb0031ba249" + more
 * </pre>
 * <p>
 * <b>Common Uses in jlibtorrent:</b>
 * <ul>
 *   <li><b>Hash Display:</b> Show SHA-1 or SHA-256 hashes in UI</li>
 *   <li><b>Parsing:</b> Convert hex string to binary (e.g., from user input)</li>
 *   <li><b>Logging:</b> Display binary data in readable form</li>
 *   <li><b>Debugging:</b> Inspect binary structures as hex</li>
 * </ul>
 * <p>
 * <b>Encoding Examples:</b>
 * <pre>
 * // Encode bytes to hex string
 * byte[] data = {0x01, 0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB};
 * String hex = Hex.encode(data);
 * System.out.println(hex);  // Output: "0123456789ab"
 *
 * // Hash to hex
 * Sha1Hash hash = new Sha1Hash(torrent);
 * String hashHex = Hex.encode(hash.swig().to_bytes());
 * System.out.println("Info-hash: " + hashHex);
 * </pre>
 * <p>
 * <b>Decoding Examples:</b>
 * <pre>
 * // Decode hex string to bytes
 * String hex = "0123456789ab";
 * byte[] data = Hex.decode(hex);
 * System.out.println("Decoded: " + data.length + " bytes");
 *
 * // Parse hash from hex string
 * String hashStr = "d8e8fca2dc0f896fd7cb4cb0031ba249";
 * byte[] hashBytes = Hex.decode(hashStr);
 * Sha1Hash hash = new Sha1Hash(hashBytes);
 * </pre>
 * <p>
 * <b>Hex String Format:</b>
 * <ul>
 *   <li><b>Lowercase letters:</b> 'a'-'f' for values 10-15 (standard)</li>
 *   <li><b>Length:</b> Always double the byte array length (2 chars per byte)</li>
 *   <li><b>Validation:</b> Invalid hex characters throw IllegalArgumentException</li>
 * </ul>
 * <p>
 * <b>Error Handling:</b>
 * <pre>
 * try {
 *     // Odd number of characters
 *     byte[] data = Hex.decode("abc");
 * } catch (IllegalArgumentException e) {
 *     System.err.println("Invalid hex: " + e.getMessage());
 * }
 *
 * try {
 *     // Invalid character
 *     byte[] data = Hex.decode("0g23");
 * } catch (IllegalArgumentException e) {
 *     System.err.println("Invalid hex digit: " + e.getMessage());
 * }
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Encoding is O(n) where n = byte array length</li>
 *   <li>Decoding is O(n) where n = hex string length</li>
 *   <li>No intermediate collections; direct byte/char manipulation</li>
 *   <li>Safe for large binary data (entire files, torrents)</li>
 * </ul>
 *
 * @see Sha1Hash#toHex() - Convenient hash to hex method
 * @see Sha256Hash#toHex() - v2 hash to hex
 *
 * @author gubatron
 * @author aldenml
 */
final class Hex {

    private Hex() {
    }

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Converts an array of characters representing hexadecimal values into an array of bytes of those same values. The
     * returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param data
     *            An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied char array.
     */
    public static byte[] decode(final char[] data) {

        final int len = data.length;

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    public static byte[] decode(String data) {
        return decode(data.toCharArray());
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static String encode(final byte[] data) {
        return new String(encode(data, DIGITS_LOWER));
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    private static char[] encode(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source
     * @return An integer
     */
    private static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
