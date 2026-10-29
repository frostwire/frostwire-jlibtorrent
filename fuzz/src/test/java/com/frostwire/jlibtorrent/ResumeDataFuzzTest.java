package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;

/**
 * Fuzzes reading fast-resume data the way {@code SessionManager} restores torrents at startup
 * ({@code libtorrent.read_resume_data_ex}), then writes it back.
 * <p>
 * Input: .resume files on disk, which a crash mid-write, a full disk or another program can leave
 * corrupt. Errors are reported through the {@code error_code}; any exception, a hang, or a native
 * crash (see {@link FuzzInputs}) is a finding.
 */
class ResumeDataFuzzTest {

    @FuzzTest
    void readAndWrite(FuzzedDataProvider data) {
        byte[] bytes = FuzzInputs.record(data.consumeRemainingAsBytes());
        error_code ec = new error_code();
        byte_vector buffer = Vectors.bytes2byte_vector(bytes);
        add_torrent_params p = libtorrent.read_resume_data_ex(buffer, ec);
        if (ec.value() != 0) {
            return;
        }
        AddTorrentParams params = new AddTorrentParams(p);
        params.name();
        params.savePath();
        params.trackers();
        params.urlSeeds();
        params.peers();
        params.getInfoHashes();
        params.torrentInfo();
        libtorrent.write_resume_data_buf_ex(p);
    }
}
