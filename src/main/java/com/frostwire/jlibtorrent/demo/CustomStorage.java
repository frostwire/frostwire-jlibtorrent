package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.plugins.StoragePlugin;
import com.frostwire.jlibtorrent.plugins.SwigStoragePlugin;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CustomStorage {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        final Session s = new Session();

        final SwigStoragePlugin sp = new SwigStoragePlugin(new StoragePlugin() {
        });

        swig_storage_constructor sc = new swig_storage_constructor() {
            @Override
            public swig_storage create(storage_params params) {
                System.out.println("About to create custom storage");
                return sp;
            }
        };

        final TorrentHandle th = addTorrentSupport(s.getSwig(), new TorrentInfo(torrentFile), torrentFile.getParentFile(), null, null, false, sc);

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

    private static TorrentHandle addTorrentSupport(session s, TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile, boolean async, swig_storage_constructor sc) {

        String savePath = null;
        if (saveDir != null) {
            savePath = saveDir.getAbsolutePath();
        } else if (resumeFile == null) {
            throw new IllegalArgumentException("Both saveDir and resumeFile can't be null at the same time");
        }

        add_torrent_params p = add_torrent_params.create_instance_swig_storage(sc);

        // we used to pass here ti.getSwig(), however this is wrong, as the
        // given TorrentInfo ti reference will be cleaned by the garbage collector
        // higher in the stack. When it cleans, swig code deletes the native torrent_info
        // object, then the torrent engine is in an inconsistent state which WILL cause it to crash.
        p.setTi(ti.getSwig().copy());
        if (savePath != null) {
            p.setSave_path(savePath);
        }

        if (priorities != null) {
            p.setFile_priorities(Vectors.priorities2unsigned_char_vector(priorities));
        }
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        if (resumeFile != null) {
            try {
                byte[] data = Utils.readFileToByteArray(resumeFile);
                p.setResume_data(Vectors.bytes2char_vector(data));

                flags |= add_torrent_params.flags_t.flag_use_resume_save_path.swigValue();
            } catch (Throwable e) {
                System.out.println("Unable to set resume data");
            }
        }

        p.setFlags(flags);

        if (async) {
            s.async_add_torrent(p);
            return null;
        } else {
            torrent_handle th = s.add_torrent(p);
            return new TorrentHandle(th);
        }
    }
}
