package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Fuzzes {@link AddTorrentParams#parseMagnetUri(String)} and reads the parsed parameters back.
 * <p>
 * Input: magnet links from web pages, the clipboard and other apps. The method documents
 * {@link IllegalArgumentException} for an invalid URI; anything else, a hang, or a native crash
 * (see {@link FuzzInputs}) is a finding.
 */
class MagnetUriFuzzTest {

    @FuzzTest
    void parse(FuzzedDataProvider data) {
        String uri = FuzzInputs.record(data.consumeRemainingAsString());
        AddTorrentParams params;
        try {
            params = AddTorrentParams.parseMagnetUri(uri);
        } catch (IllegalArgumentException expected) {
            return;
        }
        params.name();
        params.trackers();
        params.trackerTiers();
        params.dhtNodes();
        params.getInfoHashes();
        params.urlSeeds();
        params.peers();
        AddTorrentParams.writeResumeData(params).bencode();
    }
}
