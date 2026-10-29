package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Fuzzes the small text parsers clients feed network strings to: hex info hashes, IP addresses,
 * endpoints and tracker URLs. {@code data.consumeInt} picks the parser, so one harness covers them
 * all; split a parser into its own harness before ticketing a finding in it.
 * <p>
 * Input: hashes and addresses from magnet links, web pages, peers and trackers. Each parser throws
 * {@link IllegalArgumentException} for input it rejects ({@code Hex.decode}: an illegal digit or an
 * odd length; the hash constructors: a wrong length; the address and endpoint constructors: an
 * unparseable address). Anything else, a hang, or a native crash (see {@link FuzzInputs}) is a
 * finding.
 */
class TextParsersFuzzTest {

    @FuzzTest
    void parse(FuzzedDataProvider data) {
        int parser = data.consumeInt(0, 6);
        int port = data.consumeInt();
        String text = FuzzInputs.record(data.consumeRemainingAsString());
        try {
            switch (parser) {
                case 0:
                    Hex.decode(text);
                    break;
                case 1:
                    new Sha1Hash(text).toHex();
                    break;
                case 2:
                    new Sha256Hash(text).toHex();
                    break;
                case 3:
                    new Address(text).toString();
                    break;
                case 4:
                    new TcpEndpoint(text, port).toString();
                    break;
                case 5:
                    new UdpEndpoint(text, port).toString();
                    break;
                default:
                    new AnnounceEntry(text).url();
                    break;
            }
        } catch (IllegalArgumentException expected) {
            // documented rejection
        }
    }
}
