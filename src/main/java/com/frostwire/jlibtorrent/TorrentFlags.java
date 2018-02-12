package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentFlags {

    private TorrentFlags() {
    }

    // If ``seed_mode`` is set, libtorrent will assume that all files
    // are present for this torrent and that they all match the hashes in
    // the torrent file. Each time a peer requests to download a block,
    // the piece is verified against the hash, unless it has been verified
    // already. If a hash fails, the torrent will automatically leave the
    // seed mode and recheck all the files. The use case for this mode is
    // if a torrent is created and seeded, or if the user already know
    // that the files are complete, this is a way to avoid the initial
    // file checks, and significantly reduce the startup time.
    //
    // Setting ``seed_mode`` on a torrent without metadata (a
    // .torrent file) is a no-op and will be ignored.
    //
    // If resume data is passed in with this torrent, the seed mode saved
    // in there will override the seed mode you set here.
    public static final torrent_flags_t SEED_MODE = libtorrent.getSeed_mode();

    // If ``upload_mode`` is set, the torrent will be initialized in
    // upload-mode, which means it will not make any piece requests. This
    // state is typically entered on disk I/O errors, and if the torrent
    // is also auto managed, it will be taken out of this state
    // periodically (see ``settings_pack::optimistic_disk_retry``).
    //
    // This mode can be used to avoid race conditions when
    // adjusting priorities of pieces before allowing the torrent to start
    // downloading.
    //
    // If the torrent is auto-managed (``auto_managed``), the torrent
    // will eventually be taken out of upload-mode, regardless of how it
    // got there. If it's important to manually control when the torrent
    // leaves upload mode, don't make it auto managed.
    public static final torrent_flags_t UPLOAD_MODE = libtorrent.getUpload_mode();

    // determines if the torrent should be added in *share mode* or not.
    // Share mode indicates that we are not interested in downloading the
    // torrent, but merely want to improve our share ratio (i.e. increase
    // it). A torrent started in share mode will do its best to never
    // download more than it uploads to the swarm. If the swarm does not
    // have enough demand for upload capacity, the torrent will not
    // download anything. This mode is intended to be safe to add any
    // number of torrents to, without manual screening, without the risk
    // of downloading more than is uploaded.
    //
    // A torrent in share mode sets the priority to all pieces to 0,
    // except for the pieces that are downloaded, when pieces are decided
    // to be downloaded. This affects the progress bar, which might be set
    // to "100% finished" most of the time. Do not change file or piece
    // priorities for torrents in share mode, it will make it not work.
    //
    // The share mode has one setting, the share ratio target, see
    // ``settings_pack::share_mode_target`` for more info.
    public static final torrent_flags_t SHARE_MODE = libtorrent.getShare_mode();

    // determines if the IP filter should apply to this torrent or not. By
    // default all torrents are subject to filtering by the IP filter
    // (i.e. this flag is set by default). This is useful if certain
    // torrents needs to be exempt for some reason, being an auto-update
    // torrent for instance.
    public static final torrent_flags_t APPLY_IP_FILTER = libtorrent.getApply_ip_filter();

    // specifies whether or not the torrent is to be started in a paused
    // state. I.e. it won't connect to the tracker or any of the peers
    // until it's resumed. This is typically a good way of avoiding race
    // conditions when setting configuration options on torrents before
    // starting them.
    public static final torrent_flags_t PAUSED = libtorrent.getPaused();

    // If the torrent is auto-managed (``auto_managed``), the torrent
    // may be resumed at any point, regardless of how it paused. If it's
    // important to manually control when the torrent is paused and
    // resumed, don't make it auto managed.
    //
    // If ``auto_managed`` is set, the torrent will be queued,
    // started and seeded automatically by libtorrent. When this is set,
    // the torrent should also be started as paused. The default queue
    // order is the order the torrents were added. They are all downloaded
    // in that order. For more details, see queuing_.
    //
    // If you pass in resume data, the auto_managed state of the torrent
    // when the resume data was saved will override the auto_managed state
    // you pass in here. You can override this by setting
    // ``override_resume_data``.
    public static final torrent_flags_t AUTO_MANAGED = libtorrent.getAuto_managed();
    public static final torrent_flags_t DUPLICATE_IS_ERROR = libtorrent.getDuplicate_is_error();

    // on by default and means that this torrent will be part of state
    // updates when calling post_torrent_updates().
    public static final torrent_flags_t UPDATE_SUBSCRIBE = libtorrent.getUpdate_subscribe();

    // sets the torrent into super seeding mode. If the torrent is not a
    // seed, this flag has no effect. It has the same effect as calling
    // ``torrent_handle::super_seeding(true)`` on the torrent handle
    // immediately after adding it.
    public static final torrent_flags_t SUPER_SEEDING = libtorrent.getSuper_seeding();

    // sets the sequential download state for the torrent. It has the same
    // effect as calling ``torrent_handle::sequential_download(true)`` on
    // the torrent handle immediately after adding it.
    public static final torrent_flags_t SEQUENTIAL_DOWNLOAD = libtorrent.getSequential_download();

    // When this flag is set, the
    // torrent will *force stop* whenever it transitions from a
    // non-data-transferring state into a data-transferring state (referred to
    // as being ready to download or seed). This is useful for torrents that
    // should not start downloading or seeding yet, but want to be made ready
    // to do so. A torrent may need to have its files checked for instance, so
    // it needs to be started and possibly queued for checking (auto-managed
    // and started) but as soon as it's done, it should be stopped.
    //
    // *Force stopped* means auto-managed is set to false and it's paused. As
    // if auto_manage(false) and pause() were called on the torrent.
    //
    // Note that the torrent may transition into a downloading state while
    // calling this function, and since the logic is edge triggered you may
    // miss the edge. To avoid this race, if the torrent already is in a
    // downloading state when this call is made, it will trigger the
    // stop-when-ready immediately.
    //
    // When the stop-when-ready logic fires, the flag is cleared. Any
    // subsequent transitions between downloading and non-downloading states
    // will not be affected, until this function is used to set it again.
    //
    // The behavior is more robust when setting this flag as part of adding
    // the torrent. See add_torrent_params.
    //
    // The stop-when-ready flag fixes the inherent race condition of waiting
    // for the state_changed_alert and then call pause(). The download/seeding
    // will most likely start in between posting the alert and receiving the
    // call to pause.
    public static final torrent_flags_t STOP_WHEN_READY = libtorrent.getStop_when_ready();

    // when this flag is set, the tracker list in the add_torrent_params
    // object override any trackers from the torrent file. If the flag is
    // not set, the trackers from the add_torrent_params object will be
    // added to the list of trackers used by the torrent.
    public static final torrent_flags_t OVERRIDE_TRACKERS = libtorrent.getOverride_trackers();

    // If this flag is set, the web seeds from the add_torrent_params
    // object will override any web seeds in the torrent file. If it's not
    // set, web seeds in the add_torrent_params object will be added to the
    // list of web seeds used by the torrent.
    public static final torrent_flags_t OVERRIDE_WEB_SEEDS = libtorrent.getOverride_web_seeds();

    /**
     * If this flag is set (which it is by default) the torrent will be
     * considered needing to save its resume data immediately as it's
     * added. New torrents that don't have any resume data should do that.
     * This flag is cleared by a successful call to save_resume_data()
     */
    public static final torrent_flags_t NEED_SAVE_RESUME = libtorrent.getNeed_save_resume();

    public static final torrent_flags_t ALL = libtorrent.getAll();
}
