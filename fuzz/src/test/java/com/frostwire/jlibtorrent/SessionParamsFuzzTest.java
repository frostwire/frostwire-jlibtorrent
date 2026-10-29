package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Fuzzes {@link SessionParams#SessionParams(byte[])}, which restores saved session state (settings
 * and the DHT routing table) without starting a session.
 * <p>
 * Input: the session state file on disk, which can be left corrupt. The constructor throws
 * {@link IllegalArgumentException} for data it can't decode; anything else, a hang, or a native
 * crash (see {@link FuzzInputs}) is a finding.
 */
class SessionParamsFuzzTest {

    @FuzzTest
    void restore(FuzzedDataProvider data) {
        byte[] bytes = FuzzInputs.record(data.consumeRemainingAsBytes());
        SessionParams params;
        try {
            params = new SessionParams(bytes);
        } catch (IllegalArgumentException expected) {
            return;
        }
        params.getSettings().toString();
    }
}
