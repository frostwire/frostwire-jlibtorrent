package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.plugins.AbstractStoragePlugin;
import com.frostwire.jlibtorrent.plugins.SwigStoragePlugin;
import com.frostwire.jlibtorrent.swig.*;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CustomStorage {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};
        //args = new String[]{"/Users/aldenml/Downloads/test.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        final Session s = new Session();

        final HashMap<String, byte[]> memStorage = new HashMap<String, byte[]>();

        final SwigStoragePlugin sp = new SwigStoragePlugin(new AbstractStoragePlugin() {
            @Override
            public int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
                String key = piece + ":" + offset;
                if (!memStorage.containsKey(key)) {
                    System.out.println("Read from non existent key: " + key);
                    return -1;
                }

                byte[] data = memStorage.get(key);

                Unsafe us = getUnsafe();

                for (int i = 0; i < data.length; i++) {
                    us.putByte(iov_base + i, data[i]);
                }

                System.out.println("Read from key: " + key + ", to read=" + data.length);

                return data.length;
            }

            @Override
            public int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
                String key = piece + ":" + offset;
                if (!memStorage.containsKey(key)) {
                    System.out.println("Write to a non existent key: " + key);
                }

                byte[] data = new byte[(int) iov_len];

                Unsafe us = getUnsafe();

                for (int i = 0; i < data.length; i++) {
                    data[i] = us.getByte(iov_base + i);
                }

                memStorage.put(key, data);

                System.out.println("Write to key: " + key + ", to write=" + data.length);

                return data.length;
            }

            @Override
            public boolean tick() {
                System.out.println("Storage tick");
                return true;
            }
        });

        swig_storage_constructor sc = new swig_storage_constructor() {
            @Override
            public swig_storage create(storage_params params) {
                return sp;
            }
        };

        final TorrentHandle th = addTorrentSupport(s.getSwig(), new TorrentInfo(torrentFile), torrentFile.getParentFile(), null, null, false, sc);

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void blockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                //System.out.println("Progress: " + p + " for torrent name: " + alert.torrentName());
                //System.out.println(s.getStats().download());
            }

            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                //signal.countDown();
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

        p.set_ti(ti.getSwig());
        if (savePath != null) {
            p.setSave_path(savePath);
        }

        if (priorities != null) {
            //p.set_file_priorities(Vectors.priorities2unsigned_char_vector(priorities));
        }
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        long flags = p.get_flags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        if (resumeFile != null) {
            try {
                byte[] data = Utils.readFileToByteArray(resumeFile);
                p.set_resume_data(Vectors.bytes2byte_vector(data));

                flags |= add_torrent_params.flags_t.flag_use_resume_save_path.swigValue();
            } catch (Throwable e) {
                System.out.println("Unable to set resume data");
            }
        }

        p.set_flags(flags);

        if (async) {
            s.async_add_torrent(p);
            return null;
        } else {
            error_code ec = new error_code();
            torrent_handle th = s.add_torrent(p, ec);
            return new TorrentHandle(th);
        }
    }

    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
        }
        return null;
    }
}
