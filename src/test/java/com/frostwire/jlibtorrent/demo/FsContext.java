package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.TorrentAlertAdapter;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.swig_posix_stat;
import com.frostwire.jlibtorrent.swig.swig_posix_wrapper;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class FsContext {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015.torrent"};

        File torrentFile = new File(args[0]);

        LibTorrent.setPosixWrapper(new swig_posix_wrapper() {
            @Override
            public int open(String pathname, int flags, int mode) {
                System.out.println("OPEN: " + pathname);
                return super.open(pathname, flags, mode);
            }

            @Override
            public int open64(String pathname, int flags, int mode) {
                System.out.println("OPEN64: " + pathname);
                return super.open64(pathname, flags, mode);
            }

            @Override
            public int mkdir(String pathname, int mode) {
                System.out.println("MKDIR: " + pathname);
                return super.mkdir(pathname, mode);
            }

            @Override
            public int rename(String oldpath, String newpath) {
                System.out.println("RENAME: " + oldpath + " -> " + newpath);
                return super.rename(oldpath, newpath);
            }

            @Override
            public int remove(String pathname) {
                System.out.println("REMOVE: " + pathname);
                return super.remove(pathname);
            }

            @Override
            public int lstat(String path, swig_posix_stat buf) {
                System.out.println("LSTAT: " + path);
                return super.lstat(path, buf);
            }

            @Override
            public int stat(String path, swig_posix_stat buf) {
                System.out.println("STAT: " + path);
                return super.stat(path, buf);
            }
        });

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void blockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                System.out.println("Progress: " + p + " for torrent name: " + alert.torrentName());
                System.out.println(s.getStats().download());
            }

            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                signal.countDown();
            }
        });

        th.resume();

        signal.await();
    }
}
