package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a lens only functionality.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Downloader {

    private static final Logger LOG = Logger.getLogger(Downloader.class);

    private static final int[] LISTENER_TYPES = new int[]{AlertType.METADATA_RECEIVED.getSwig()};

    private final Session s;

    public Downloader(Session s) {
        this.s = s;
    }

    public TorrentHandle find(Sha1Hash infoHash) {
        return s.findTorrent(infoHash);
    }

    public TorrentHandle find(String infoHash) {
        return s.findTorrent(new Sha1Hash(infoHash));
    }

    public void download(TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile) {

        TorrentHandle th = s.findTorrent(ti.getInfoHash());

        if (th != null) {
            // found a download with the same hash, just adjust the priorities if needed
            if (priorities != null) {
                if (ti.getNumFiles() != priorities.length) {
                    throw new IllegalArgumentException("The priorities length should be equals to the number of files");
                }

                th.prioritizeFiles(priorities);
            } else {
                // did they just add the entire torrent (therefore not selecting any priorities)
                final Priority[] wholeTorrentPriorities = Priority.array(Priority.NORMAL, ti.getNumFiles());
                th.prioritizeFiles(wholeTorrentPriorities);
            }
        } else { // new download
            s.asyncAddTorrent(ti, saveDir, priorities, resumeFile);
        }
    }

    public void download(TorrentInfo ti, File saveDir) {
        download(ti, saveDir, null, null);
    }

    /**
     * @param uri
     * @param timeout in milliseconds
     * @return
     */
    public byte[] fetchMagnet(String uri, long timeout) {
        add_torrent_params p = add_torrent_params.create_instance_disabled_storage();
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, p, ec);

        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }

        final sha1_hash info_hash = p.getInfo_hash();

        boolean add = true;

        torrent_handle th = null;//s.getSwig().find_torrent(info_hash);
        /*if (th != null && th.is_valid()) {
            // we have a download with the same info-hash, let's see if we have the torrent info
            torrent_info ti = th.get_torrent_copy();
            if (ti != null && ti.is_valid()) {
                // ok. we have it, ready to return the data
                return new TorrentInfo(th.get_torrent_copy()).bencode();
            } else {
                add = false;
            }
        }*/

        final CountDownLatch signal = new CountDownLatch(1);
        final byte[][] data = new byte[1][];

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return LISTENER_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                try {
                    if (!(alert instanceof MetadataReceivedAlert)) {
                        return;
                    }

                    MetadataReceivedAlert mr = (MetadataReceivedAlert) alert;
                    torrent_handle th = mr.getHandle().getSwig();

                    if (th.info_hash().op_eq(info_hash)) {
                        try {
                            data[0] = new TorrentInfo(th.get_torrent_copy()).bencode();
                        } catch (Throwable e) {
                            LOG.error("Error encoding torrent info", e);
                        }
                        signal.countDown();
                    }
                } catch (Throwable e) {
                    LOG.error("Error in fetch magnet internal listener", e);
                    signal.countDown();
                }
            }
        };

        s.addListener(l);

        if (add) {
            p.setName("fetch_magnet:" + uri);
            p.setSave_path("fetch_magnet/" + uri);

            long flags = p.get_flags();
            flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
            p.set_flags(flags);

            ec.clear();
            th = s.getSwig().add_torrent(p, ec);
            th.resume();
        }

        try {
            signal.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        s.removeListener(l);

        if (add && th != null && th.is_valid()) {
            s.getSwig().remove_torrent(th);
        }

        return data[0];
    }
}
