package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

/**
 * Disk storage allocation strategy for torrents.
 * <p>
 * {@code StorageMode} controls how downloaded data is laid out on disk. Different strategies
 * have trade-offs between disk space usage, fragmentation, and I/O performance.
 * <p>
 * <b>Storage Modes:</b>
 * <p>
 * <b>SPARSE (Recommended Default):</b>
 * Files are allocated incrementally as pieces arrive. Uses sparse file support on modern
 * filesystems to avoid pre-allocating entire files. This is the default and recommended mode.
 * <pre>
 * Advantages:
 * - Only uses disk space for downloaded data
 * - Works on all filesystems
 * - Faster download startup (no allocation delay)
 * - Easier to resume on limited storage
 *
 * Disadvantages:
 * - May cause file fragmentation over time
 * - Some filesystems may handle sparse files inefficiently
 *
 * Usage: (default, no explicit setting needed)
 * AddTorrentParams params = new AddTorrentParams();
 * // StorageMode.STORAGE_MODE_SPARSE is default
 * </pre>
 * <p>
 * <b>ALLOCATE (Full Pre-allocation):</b>
 * All disk space for the torrent is allocated upfront using fallocate() or similar OS calls.
 * Files reach their final size immediately, before any data is downloaded.
 * <pre>
 * Advantages:
 * - Zero fragmentation (optimal disk layout)
 * - Faster downloads (no allocation overhead during transfer)
 * - Guarantees contiguous storage on most filesystems
 * - Better predictable I/O performance
 *
 * Disadvantages:
 * - Slower download startup (allocate entire torrent first)
 * - Fails if insufficient disk space available
 * - Wastes space on failed downloads
 * - Not suitable for drives with limited space
 *
 * When to use:
 * - High-performance dedicated seeds/NAS
 * - Very large torrents (minimize fragmentation)
 * - Drives with plenty of free space
 *
 * Usage:
 * AddTorrentParams params = new AddTorrentParams();
 * params.storageMode(StorageMode.STORAGE_MODE_ALLOCATE);
 * sm.download(ti, saveDir, null, null, null, params.flags());
 * </pre>
 * <p>
 * <b>Comparison Table:</b>
 * <pre>
 * ┌─────────────────────────────────────────┬────────────┬──────────┐
 * │ Characteristic                          │   SPARSE   │ ALLOCATE │
 * ├─────────────────────────────────────────┼────────────┼──────────┤
 * │ Startup time                            │ Fast       │ Slow*    │
 * │ Disk space usage                        │ Efficient  │ Full     │
 * │ Fragmentation                           │ Possible   │ None     │
 * │ Speed (during transfer)                 │ Good       │ Better   │
 * │ Suitable for low-space drives           │ Yes        │ No       │
 * │ Suitable for high-speed seeding         │ Good       │ Better   │
 * └─────────────────────────────────────────┴────────────┴──────────┘
 *
 * *Pre-allocation can take seconds to minutes for large files
 * </pre>
 * <p>
 * <b>Practical Example:</b>
 * <pre>
 * // For a 4GB movie download on a laptop
 * // Use SPARSE (default) - quick to start, efficient space use
 * AddTorrentParams params = new AddTorrentParams();
 * // No need to set, SPARSE is default
 *
 * // For a 100GB archive on a dedicated NAS with 1TB free space
 * // Use ALLOCATE - minimize fragmentation, maximize performance
 * AddTorrentParams params = new AddTorrentParams();
 * params.storageMode(StorageMode.STORAGE_MODE_ALLOCATE);
 *
 * // For temporary downloads on small USB drive
 * // Use SPARSE - only allocate what you need
 * </pre>
 *
 * @see AddTorrentParams#storageMode(StorageMode) - To set storage mode
 * @see SessionManager#download(TorrentInfo, java.io.File, java.io.File, Priority[], java.util.List, com.frostwire.jlibtorrent.swig.torrent_flags_t) - Download with custom params
 *
 * @author gubatron
 * @author aldenml
 */
public enum StorageMode {

    /**
     * All pieces will be written to their final position, all files will be
     * allocated in full when the torrent is first started. This is done with
     * {@code fallocate()} and similar calls. This mode minimizes fragmentation.
     */
    STORAGE_MODE_ALLOCATE(storage_mode_t.storage_mode_allocate.swigValue()),

    /**
     * All pieces will be written to the place where they belong and sparse files
     * will be used. This is the recommended, and default mode.
     */
    STORAGE_MODE_SPARSE(storage_mode_t.storage_mode_sparse.swigValue()),

    /**
     * Unknown storage mode (for error handling)
     */
    UNKNOWN(-1);

    StorageMode(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return the native value
     */
    public int swig() {
        return swigValue;
    }

    /**
     * @param swigValue the native value
     * @return the java enum
     */
    public static StorageMode fromSwig(int swigValue) {
        StorageMode[] enumValues = StorageMode.class.getEnumConstants();
        for (StorageMode ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
