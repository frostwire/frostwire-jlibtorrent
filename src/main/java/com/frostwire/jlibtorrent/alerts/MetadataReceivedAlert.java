package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.*;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This alert is generated when the metadata has been completely received and the torrent
 * can start downloading. It is not generated on torrents that are started with metadata, but
 * only those that needs to download it from peers (when utilizing the libtorrent extension).
 * <p>
 * Typically, when receiving this alert, you would want to save the torrent file in order
 * to load it back up again when the session is restarted.
 *
 * @author gubatron
 * @author aldenml
 */
public final class MetadataReceivedAlert extends TorrentAlert<metadata_received_alert> {

    private final ReentrantLock sync;

    private int size;
    private byte[] data;
    private boolean invalid;

    /**
     * @param alert the native object
     */
    MetadataReceivedAlert(metadata_received_alert alert) {
        super(alert);
        this.sync = new ReentrantLock();
    }

    /**
     * Returns the size of the metadata (info section).
     * <p>
     * Internally it uses a lock synchronization to make it thread-safe.
     *
     * @return the metadata size
     */
    public int metadataSize() {
        if (invalid) {
            return -1;
        }

        if (size > 0) {
            return size;
        }

        sync.lock();

        try {
            if (invalid) {
                return -1;
            }

            if (size > 0) {
                return size;
            }

            torrent_handle th = alert.getHandle();
            if (th == null || !th.is_valid()) {
                invalid = true;
                return -1;
            }

            torrent_info ti = th.torrent_file_ptr();
            if (ti == null || !ti.is_valid()) {
                invalid = true;
                return -1;
            }

            size = ti.metadata_size();

        } catch (Throwable e) {
            invalid = true;
        } finally {
            sync.unlock();
        }

        return size;
    }

    /**
     * This method construct the torrent lazily. If the metadata
     * is very big it can be a problem in memory constrained devices.
     * <p>
     * Internally it uses a lock synchronization to make it thread-safe.
     *
     * @param extra this controls if extra data, like trackers and web seeds
     *              are included
     * @return the torrent info bencoded data
     */
    public byte[] torrentData(boolean extra) {
        if (invalid) {
            return null;
        }

        if (data != null) {
            return data;
        }

        sync.lock();

        try {
            if (invalid) {
                return null;
            }

            if (data != null) {
                return data;
            }

            torrent_handle th = alert.getHandle();
            if (th == null || !th.is_valid()) {
                invalid = true;
                return null;
            }

            torrent_info ti = th.torrent_file_ptr();
            if (ti == null || !ti.is_valid()) {
                invalid = true;
                return null;
            }

            size = ti.metadata_size();
            data = createTorrent(th, ti, extra);

        } catch (Throwable e) {
            invalid = true;
        } finally {
            sync.unlock();
        }

        return data;
    }

    /**
     * Same as calling {@link #torrentData(boolean)} with {@code false}
     *
     * @return the info-dict data
     */
    public byte[] torrentData() {
        return torrentData(false);
    }

    private static byte[] createTorrent(torrent_handle th, torrent_info ti, boolean extra) {
        create_torrent ct = new create_torrent(ti);

        if (extra) {
            string_vector v = th.get_url_seeds();
            int size = (int) v.size();
            for (int i = 0; i < size; i++) {
                ct.add_url_seed(v.get(i));
            }
            v = th.get_http_seeds();
            size = (int) v.size();
            for (int i = 0; i < size; i++) {
                ct.add_http_seed(v.get(i));
            }
            announce_entry_vector trackers = th.trackers();
            size = (int) trackers.size();
            for (int i = 0; i < size; i++) {
                announce_entry t = trackers.get(i);
                ct.add_tracker(Vectors.byte_vector2ascii(t.get_url()), t.getTier());
            }
        }

        entry e = ct.generate();
        return Vectors.byte_vector2bytes(e.bencode());
    }
}
