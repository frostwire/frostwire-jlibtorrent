package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_torrent_params;

/**
 * The add_torrent_params is a parameter pack for adding torrents to a
 * // session. The key fields when adding a torrent are:
 * //
 * // * ti - when you have a .torrent file
 * // * url - when you have a magnet link or http URL to the .torrent file
 * // * info_hash - when all you have is an info-hash (this is similar to a
 * //   magnet link)
 * //
 * // one of those fields need to be set. Another mandatory field is
 * // ``save_path``. The add_torrent_params object is passed into one of the
 * // ``session::add_torrent()`` overloads or ``session::async_add_torrent()``.
 * //
 * // If you only specify the info-hash, the torrent file will be downloaded
 * // from peers, which requires them to support the metadata extension. For
 * // the metadata extension to work, libtorrent must be built with extensions
 * // enabled (``TORRENT_DISABLE_EXTENSIONS`` must not be defined). It also
 * // takes an optional ``name`` argument. This may be left empty in case no
 * // name should be assigned to the torrent. In case it's not, the name is
 * // used for the torrent as long as it doesn't have metadata. See
 * // ``torrent_handle::name``.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AddTorrentParams {

    private final add_torrent_params p;

    public AddTorrentParams(add_torrent_params p) {
        this.p = p;
    }

    public add_torrent_params getSwig() {
        return p;
    }
}
