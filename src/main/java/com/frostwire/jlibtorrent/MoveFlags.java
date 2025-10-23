package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.move_flags_t;

/**
 * Flags controlling storage move behavior for torrent files.
 * <p>
 * {@code MoveFlags} specifies how the BitTorrent session should handle file conflicts
 * when moving a torrent's downloaded data to a new location on disk. This is useful
 * for disk space management, organizing downloads, or migrating between storage devices.
 * <p>
 * <b>Understanding Storage Move Operations:</b>
 * <br/>
 * Moving torrent storage involves copying or moving files from current location to a
 * new directory. These flags control what happens if files already exist at the destination:
 * <ul>
 *   <li><b>ALWAYS_REPLACE_FILES:</b> Overwrite any existing destination files</li>
 *   <li><b>FAIL_IF_EXIST:</b> Abort operation if destination files exist (safest)</li>
 *   <li><b>DONT_REPLACE:</b> Use existing destination files instead of copying</li>
 * </ul>
 * <p>
 * <b>Moving Torrent Storage:</b>
 * <pre>
 * // Get handle to active torrent
 * TorrentHandle th = sm.find(infoHash);
 *
 * // Move downloads to new location
 * String newPath = \"/mnt/faster_disk/downloads\";
 * th.moveStorage(newPath, MoveFlags.ALWAYS_REPLACE_FILES);
 *
 * // Operation is asynchronous; listen for completion
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.STORAGE_MOVED.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         StorageMovedAlert sma = (StorageMovedAlert) alert;
 *         System.out.println(\"Storage moved to: \" + sma.path());
 *     }
 * });
 * </pre>
 * <p>
 * <b>MoveFlags Comparison:</b>
 * <table border="1">
 * <caption>Storage Move Flag Behavior</caption>
 * <tr><th>Flag</th><th>Behavior</th><th>Use Case</th><th>Safety</th></tr>
 * <tr>
 *   <td>ALWAYS_REPLACE_FILES</td>
 *   <td>Overwrites existing destination files</td>
 *   <td>Cleanup/reorganization, trusted destinations</td>
 *   <td>Medium - deletes existing data</td>
 * </tr>
 * <tr>
 *   <td>FAIL_IF_EXIST</td>
 *   <td>Aborts if destination files exist</td>
 *   <td>Automated operations, safety-critical</td>
 *   <td>High - preserves existing files</td>
 * </tr>
 * <tr>
 *   <td>DONT_REPLACE</td>
 *   <td>Uses existing destination files</td>
 *   <td>Resume after crash, keep newer versions</td>
 *   <td>Medium - assumes destination is correct</td>
 * </tr>
 * </table>
 * <p>
 * <b>Real-World Scenarios:</b>
 * <pre>
 * // Scenario 1: Disk full - move to larger drive
 * // Use ALWAYS_REPLACE_FILES (trusted new location)
 * String newDisk = \"/media/external/torrents\";
 * th.moveStorage(newDisk, MoveFlags.ALWAYS_REPLACE_FILES);
 *
 * // Scenario 2: Automated organization system
 * // Use FAIL_IF_EXIST (safety check, abort on conflicts)
 * String organized = \"/data/organized/\" + category;
 * th.moveStorage(organized, MoveFlags.FAIL_IF_EXIST);
 *
 * // Scenario 3: Restart after crash
 * // Use DONT_REPLACE (keep partially downloaded files)
 * String resumed = \"/downloads/resumed\";
 * th.moveStorage(resumed, MoveFlags.DONT_REPLACE);
 * </pre>
 * <p>
 * <b>Move Completion Detection:</b>
 * <pre>
 * // Listen for move operations
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {
 *             AlertType.STORAGE_MOVED.swig(),
 *             AlertType.STORAGE_MOVED_FAILED.swig()
 *         };
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         if (alert.type() == AlertType.STORAGE_MOVED) {
 *             StorageMovedAlert sma = (StorageMovedAlert) alert;
 *             System.out.println(\"✓ Moved to: \" + sma.path());
 *         } else {
 *             StorageMovedFailedAlert smfa = (StorageMovedFailedAlert) alert;
 *             System.out.println(\"✗ Move failed: \" + smfa.message());
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>Move operation is asynchronous; check STORAGE_MOVED alert for completion</li>
 *   <li>Destination directory should exist before move</li>
 *   <li>Current downloads continue during move operation</li>
 *   <li>Operation may fail if insufficient disk space</li>
 *   <li>Use FAIL_IF_EXIST for programmatic safety</li>
 * </ul>
 *
 * @see TorrentHandle#moveStorage(String, MoveFlags) - Initiate move operation
 * @see com.frostwire.jlibtorrent.alerts.StorageMovedAlert - Move completion notification
 * @see com.frostwire.jlibtorrent.alerts.StorageMovedFailedAlert - Move failure notification
 *
 * @author gubatron
 * @author aldenml
 */
public enum MoveFlags {

    /**
     * Replace any files in the destination when copying
     * or moving the storage.
     */
    ALWAYS_REPLACE_FILES(move_flags_t.always_replace_files),

    /**
     * If any files that we want to copy exist in the destination,
     * fail the whole operation and don't perform
     * any copy or move. There is an inherent race condition
     * in this mode. The files are checked for existence before
     * the operation starts. In between the check and performing
     * the copy, the destination files may be created, in which
     * case they are replaced.
     */
    FAIL_IF_EXIST(move_flags_t.fail_if_exist),

    /**
     * If any files exist in the target, take those files instead
     * of the ones we may have in the source.
     */
    DONT_REPLACE(move_flags_t.dont_replace);

    MoveFlags(move_flags_t swigValue) {
        this.swigValue = swigValue;
    }

    private final move_flags_t swigValue;

    /**
     * @return the native value
     */
    public move_flags_t swig() {
        return swigValue;
    }

    /**
     * @param swigValue the native value
     */
    public static MoveFlags fromSwig(move_flags_t swigValue) {
        MoveFlags[] enumValues = MoveFlags.class.getEnumConstants();
        for (MoveFlags ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("Enum value not supported");
    }
}
