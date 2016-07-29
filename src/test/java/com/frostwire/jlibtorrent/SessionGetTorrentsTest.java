package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.torrent_handle_vector;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * This test is for address the issue:
 * https://github.com/frostwire/frostwire-jlibtorrent/issues/81
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionGetTorrentsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    //@Test
    public void testGetTorrents1() throws IOException, InterruptedException {
        final CountDownLatch count = new CountDownLatch(20);


        final Session s = new Session();
        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof PieceFinishedAlert) {
                    System.out.println(alert);

                    count.countDown();

                    torrent_handle_vector torrents = s.swig().get_torrents();
                }
            }
        });

        byte[] tb = Utils.resourceBytes("test1.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(tb);

        AddTorrentParams params = new AddTorrentParams();
        params.flags(params.flags() & ~add_torrent_params.flags_t.flag_auto_managed.swigValue());
        params.torrentInfo(ti);

        File savePath = folder.newFolder("test1_torrent_test");
        params.savePath(savePath.getAbsolutePath());

        ErrorCode ec = new ErrorCode();
        TorrentHandle th = s.addTorrent(params, ec);

        th.resume();

        assertTrue(count.await(20, TimeUnit.MINUTES));

        s.abort();
    }
}
