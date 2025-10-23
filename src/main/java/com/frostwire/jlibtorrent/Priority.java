package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.int_vector;

/**
 * Priority levels for downloading pieces and files in a torrent.
 * <p>
 * When downloading a multi-file torrent, you can specify per-file priorities to control
 * which files are downloaded and in what order. Priorities affect the download scheduler's
 * piece selection algorithm - higher priority pieces are requested more frequently.
 * <p>
 * <b>Understanding Availability:</b>
 * The availability of a piece is the number of peers that have it. Pieces with lower
 * availability (rarer pieces) are more valuable since they're at risk of becoming unavailable
 * if peers disconnect. The scheduling algorithm balances rarity with priority:
 * <ul>
 *   <li>Rare pieces (low availability) are usually prioritized regardless of file priority</li>
 *   <li>Common pieces (high availability) follow the file priority settings</li>
 * </ul>
 * <p>
 * <b>Priority Levels (0-7):</b>
 * <pre>
 * IGNORE   (0) - Don't download this file/piece at all (skip it)
 * NORMAL   (1) - Normal priority, balanced rarity vs progress
 * TWO      (2) - Slightly higher than normal
 * THREE    (3) - Equal priority as partial pieces
 * FOUR     (4) - Preferred over partial pieces
 * FIVE     (5) - Same as FOUR (reserved for future use)
 * SIX      (6) - As likely as pieces with 1 available copy
 * SEVEN    (7) - Maximum priority, ignore availability
 * </pre>
 * <p>
 * <b>Setting File Priorities:</b>
 * <pre>
 * // Download only specific files
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 * Priority[] priorities = new Priority[ti.numFiles()];
 *
 * // Skip all files by default
 * for (int i = 0; i &lt; priorities.length; i++) {
 *     priorities[i] = Priority.IGNORE;
 * }
 *
 * // Download only files 0 and 2
 * priorities[0] = Priority.NORMAL;
 * priorities[2] = Priority.NORMAL;
 *
 * sm.download(ti, saveDir, null, priorities, null, new torrent_flags_t());
 *
 * // Or on an existing torrent handle
 * torrentHandle.prioritizeFiles(priorities);
 * </pre>
 * <p>
 * <b>Selective Download Strategy:</b>
 * <pre>
 * // Download files in order of importance
 * Priority[] priorities = new Priority[ti.numFiles()];
 *
 * for (int i = 0; i &lt; priorities.length; i++) {
 *     if (i &lt; 2) {
 *         priorities[i] = Priority.SEVEN;     // Critical: max priority
 *     } else if (i &lt; 5) {
 *         priorities[i] = Priority.NORMAL;    // Important: balanced
 *     } else {
 *         priorities[i] = Priority.IGNORE;    // Not needed: skip
 *     }
 * }
 *
 * torrentHandle.prioritizeFiles(priorities);
 * </pre>
 * <p>
 * <b>Utility Methods:</b>
 * <pre>
 * // Create array with same priority for all files
 * Priority[] allNormal = Priority.array(Priority.NORMAL, 5);  // 5 files, all NORMAL
 *
 * // Convert to/from native values
 * Priority p = Priority.fromSwig(1);  // NORMAL
 * int nativeValue = p.swig();         // 1
 * </pre>
 *
 * @see TorrentHandle#prioritizeFiles(Priority[]) - To change priorities during download
 * @see SessionManager#download(TorrentInfo, java.io.File, java.io.File, Priority[], java.util.List, com.frostwire.jlibtorrent.swig.torrent_flags_t) - To set initial priorities
 *
 * @author gubatron
 * @author aldenml
 */
public enum Priority {

    /**
     * piece or file is not downloaded at all
     */
    IGNORE(0),

    /**
     * normal priority. Download order is dependent on availability
     */
    NORMAL(1),

    /**
     * higher than normal priority. Pieces are preferred over pieces with
     * the same availability, but not over pieces with lower availability
     */
    TWO(2),

    /**
     * pieces are as likely to be picked as partial pieces.
     */
    THREE(3),

    /**
     * pieces are preferred over partial pieces, but not over pieces with
     * lower availability
     */
    FOUR(4),

    /**
     * *currently the same as 4*
     */
    FIVE(5),

    /**
     * piece is as likely to be picked as any piece with availability 1
     */
    SIX(6),

    /**
     * maximum priority, availability is disregarded, the piece is
     * preferred over any other piece with lower priority
     */
    SEVEN(7);

    Priority(int swigValue) {
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
     * @return the enum corresponding value
     */
    public static Priority fromSwig(int swigValue) {
        Priority[] enumValues = Priority.class.getEnumConstants();
        for (Priority ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("Invalid native value");
    }

    public static Priority[] array(Priority value, int size) {
        Priority[] arr = new Priority[size];

        for (int i = 0; i < size; i++) {
            arr[i] = value;
        }

        return arr;
    }

    static int_vector array2vector(Priority[] arr) {
        int_vector v = new int_vector();

        for (Priority p : arr) {
            v.add(p.swig());
        }

        return v;
    }

    static byte_vector array2byte_vector(Priority[] arr) {
        byte_vector v = new byte_vector();

        for (Priority p : arr) {
            v.add((byte) p.swig());
        }

        return v;
    }
}
