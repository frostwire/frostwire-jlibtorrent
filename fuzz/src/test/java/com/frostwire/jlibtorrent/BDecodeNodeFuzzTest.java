package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Fuzzes {@link BDecodeNode#bdecode(byte[])} and the typed key lookups a client does on the result.
 * <p>
 * Input: the same untrusted bencode as {@link EntryBdecodeFuzzTest}, through libtorrent's lazy
 * decoder. {@code bdecode} throws {@link IllegalArgumentException} for data it can't decode; the
 * lookups return empty values for a missing key or a different type. Anything else, a hang, or a
 * native crash (see {@link FuzzInputs}) is a finding.
 */
class BDecodeNodeFuzzTest {

    @FuzzTest
    void decodeAndLookUp(FuzzedDataProvider data) {
        String key = data.consumeString(16);
        byte[] bytes = FuzzInputs.record(data.consumeRemainingAsBytes());
        BDecodeNode node;
        try {
            node = BDecodeNode.bdecode(bytes);
        } catch (IllegalArgumentException expected) {
            return;
        }
        node.toString();
        node.hasString(key);
        node.hasInt(key);
        node.hasList(key);
        node.hasDict(key);
        node.getString(key);
        node.getInt(key);
        node.getList(key);
        node.getDict(key);
    }
}
