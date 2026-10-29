package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.frostwire.jlibtorrent.swig.entry;

import java.util.Arrays;

/**
 * Fuzzes {@link Entry#bdecode(byte[])}, walks the decoded tree through the Java wrappers and checks
 * that re-encoding round-trips.
 * <p>
 * Input: bencoded data from peers (DHT items, extension messages) and from disk (resume data,
 * torrents). {@code bdecode} documents {@link IllegalArgumentException} for data it can't decode.
 * Any other exception, a hang, a round trip that changes the bytes, or a native crash (see
 * {@link FuzzInputs}) is a finding.
 */
class EntryBdecodeFuzzTest {

    @FuzzTest
    void decodeWalkAndRoundTrip(FuzzedDataProvider data) {
        byte[] bytes = FuzzInputs.record(data.consumeRemainingAsBytes());
        Entry e;
        try {
            e = Entry.bdecode(bytes);
        } catch (IllegalArgumentException expected) {
            return;
        }
        if (e.swig().type() == entry.data_type.undefined_t) {
            return;
        }
        walk(e, 0);
        e.toString();
        byte[] encoded = e.bencode();
        byte[] again = Entry.bdecode(FuzzInputs.record(encoded)).bencode();
        if (!Arrays.equals(encoded, again)) {
            throw new IllegalStateException("bencode(bdecode(bencode(x))) != bencode(x)");
        }
    }

    private static void walk(Entry e, int depth) {
        if (depth > 200) {
            return;
        }
        entry.data_type type = e.swig().type();
        if (type == entry.data_type.string_t) {
            e.string();
        } else if (type == entry.data_type.int_t) {
            e.integer();
        } else if (type == entry.data_type.list_t) {
            for (Entry child : e.list()) {
                walk(child, depth + 1);
            }
        } else if (type == entry.data_type.dictionary_t) {
            for (Entry child : e.dictionary().values()) {
                walk(child, depth + 1);
            }
        }
    }
}
