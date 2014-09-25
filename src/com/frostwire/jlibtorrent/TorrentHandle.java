package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import com.frostwire.jlibtorrent.swig.torrent_handle.status_flags_t;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentHandle {

    private static final long REQUEST_STATUS_RESOLUTION_MILLIS = 500;

    private final torrent_handle th;

    private long lastStatusRequestTime;
    private TorrentStatus lastStatus;

    public TorrentHandle(torrent_handle th) {
        this.th = th;
    }

    public torrent_handle getSwig() {
        return th;
    }

    public TorrentInfo getTorrentInfo() {
        torrent_info ti = th.torrent_file();
        return ti != null ? new TorrentInfo(th.torrent_file()) : null;
    }

    /**
     * It is important not to call this method for each field in the status
     * for performance reasons.
     *
     * @return
     */
    public TorrentStatus getStatus(boolean force) {
        long now = System.currentTimeMillis();
        if (force || (now - lastStatusRequestTime) >= REQUEST_STATUS_RESOLUTION_MILLIS) {
            lastStatusRequestTime = now;
            lastStatus = new TorrentStatus(th.status());
        }

        return lastStatus;
    }

    public TorrentStatus getStatus() {
        return this.getStatus(false);
    }

    public String getSavePath() {
        torrent_status ts = th.status(status_flags_t.query_save_path.swigValue());
        return ts.getSave_path();
    }

    public String getName() {
        torrent_status ts = th.status(status_flags_t.query_name.swigValue());
        return ts.getName();
    }

    /**
     * returns the info-hash for the torrent.
     *
     * @return
     */
    public Sha1Hash getInfoHash() {
        return new Sha1Hash(th.info_hash());
    }

    // ``pause()``, and ``resume()`` will disconnect all peers and reconnect
    // all peers respectively. When a torrent is paused, it will however
    // remember all share ratios to all peers and remember all potential (not
    // connected) peers. Torrents may be paused automatically if there is a
    // file error (e.g. disk full) or something similar. See
    // file_error_alert.
    //
    // To know if a torrent is paused or not, call
    // ``torrent_handle::status()`` and inspect ``torrent_status::paused``.
    //
    // The ``flags`` argument to pause can be set to
    // ``torrent_handle::graceful_pause`` which will delay the disconnect of
    // peers that we're still downloading outstanding requests from. The
    // torrent will not accept any more requests and will disconnect all idle
    // peers. As soon as a peer is done transferring the blocks that were
    // requested from it, it is disconnected. This is a graceful shut down of
    // the torrent in the sense that no downloaded bytes are wasted.
    //
    // torrents that are auto-managed may be automatically resumed again. It
    // does not make sense to pause an auto-managed torrent without making it
    // not automanaged first. Torrents are auto-managed by default when added
    // to the session. For more information, see queuing_.
    public void pause() {
        th.pause();
    }

    public void resume() {
        th.resume();
    }

    // This function returns true if any whole chunk has been downloaded
    // since the torrent was first loaded or since the last time the resume
    // data was saved. When saving resume data periodically, it makes sense
    // to skip any torrent which hasn't downloaded anything since the last
    // time.
    //
    //.. note::
    //	A torrent's resume data is considered saved as soon as the alert is
    //	posted. It is important to make sure this alert is received and
    //	handled in order for this function to be meaningful.
    public boolean needSaveResumeData() {
        return th.need_save_resume_data();
    }

    // ``save_resume_data()`` generates fast-resume data and returns it as an
    // entry. This entry is suitable for being bencoded. For more information
    // about how fast-resume works, see fast-resume_.
    //
    // The ``flags`` argument is a bitmask of flags ORed together. see
    // save_resume_flags_t
    //
    // This operation is asynchronous, ``save_resume_data`` will return
    // immediately. The resume data is delivered when it's done through an
    // save_resume_data_alert.
    //
    // The fast resume data will be empty in the following cases:
    //
    //	1. The torrent handle is invalid.
    //	2. The torrent is checking (or is queued for checking) its storage, it
    //	   will obviously not be ready to write resume data.
    //	3. The torrent hasn't received valid metadata and was started without
    //	   metadata (see libtorrent's metadata-from-peers_ extension)
    //
    // Note that by the time you receive the fast resume data, it may already
    // be invalid if the torrent is still downloading! The recommended
    // practice is to first pause the session, then generate the fast resume
    // data, and then close it down. Make sure to not remove_torrent() before
    // you receive the save_resume_data_alert though. There's no need to
    // pause when saving intermittent resume data.
    //
    //.. warning::
    //   If you pause every torrent individually instead of pausing the
    //   session, every torrent will have its paused state saved in the
    //   resume data!
    //
    //.. warning::
    //   The resume data contains the modification timestamps for all files.
    //   If one file has been modified when the torrent is added again, the
    //   will be rechecked. When shutting down, make sure to flush the disk
    //   cache before saving the resume data. This will make sure that the
    //   file timestamps are up to date and won't be modified after saving
    //   the resume data. The recommended way to do this is to pause the
    //   torrent, which will flush the cache and disconnect all peers.
    //
    //.. note::
    //   It is typically a good idea to save resume data whenever a torrent
    //   is completed or paused. In those cases you don't need to pause the
    //   torrent or the session, since the torrent will do no more writing to
    //   its files. If you save resume data for torrents when they are
    //   paused, you can accelerate the shutdown process by not saving resume
    //   data again for paused torrents. Completed torrents should have their
    //   resume data saved when they complete and on exit, since their
    //   statistics might be updated.
    //
    //	In full allocation mode the reume data is never invalidated by
    //	subsequent writes to the files, since pieces won't move around. This
    //	means that you don't need to pause before writing resume data in full
    //	or sparse mode. If you don't, however, any data written to disk after
    //	you saved resume data and before the session closed is lost.
    //
    // It also means that if the resume data is out dated, libtorrent will
    // not re-check the files, but assume that it is fairly recent. The
    // assumption is that it's better to loose a little bit than to re-check
    // the entire file.
    //
    // It is still a good idea to save resume data periodically during
    // download as well as when closing down.
    //
    // Example code to pause and save resume data for all torrents and wait
    // for the alerts:
    //
    // .. code:: c++
    //
    //	extern int outstanding_resume_data; // global counter of outstanding resume data
    //	std::vector<torrent_handle> handles = ses.get_torrents();
    //	ses.pause();
    //	for (std::vector<torrent_handle>::iterator i = handles.begin();
    //		i != handles.end(); ++i)
    //	{
    //		torrent_handle& h = *i;
    //		if (!h.is_valid()) continue;
    //		torrent_status s = h.status();
    //		if (!s.has_metadata) continue;
    //		if (!s.need_save_resume_data()) continue;
    //
    //		h.save_resume_data();
    //		++outstanding_resume_data;
    //	}
    //
    //	while (outstanding_resume_data > 0)
    //	{
    //		alert const* a = ses.wait_for_alert(seconds(10));
    //
    //		// if we don't get an alert within 10 seconds, abort
    //		if (a == 0) break;
    //
    //		std::auto_ptr<alert> holder = ses.pop_alert();
    //
    //		if (alert_cast<save_resume_data_failed_alert>(a))
    //		{
    //			process_alert(a);
    //			--outstanding_resume_data;
    //			continue;
    //		}
    //
    //		save_resume_data_alert const* rd = alert_cast<save_resume_data_alert>(a);
    //		if (rd == 0)
    //		{
    //			process_alert(a);
    //			continue;
    //		}
    //
    //		torrent_handle h = rd->handle;
    //		torrent_status st = h.status(torrent_handle::query_save_path | torrent_handle::query_name);
    //		std::ofstream out((st.save_path
    //			+ "/" + st.name + ".fastresume").c_str()
    //			, std::ios_base::binary);
    //		out.unsetf(std::ios_base::skipws);
    //		bencode(std::ostream_iterator<char>(out), *rd->resume_data);
    //		--outstanding_resume_data;
    //	}
    //
    //.. note::
    //	Note how ``outstanding_resume_data`` is a global counter in this
    //	example. This is deliberate, otherwise there is a race condition for
    //	torrents that was just asked to save their resume data, they posted
    //	the alert, but it has not been received yet. Those torrents would
    //	report that they don't need to save resume data again, and skipped by
    //	the initial loop, and thwart the counter otherwise.
    public void saveResumeData() {
        th.save_resume_data(torrent_handle.save_resume_flags_t.save_info_dict.swigValue());
    }

    public String getDisplayName() {
        torrent_info ti = null;
        if (!th.is_valid() || (ti = th.torrent_file()) == null) {
            return "Unknown";
        }

        String name;

        if (ti.num_files() == 1) {
            name = ti.files().file_name(0);
        } else {
            name = ti.name();
        }

        return name;
    }

    public boolean isPartial() {
        if (!th.is_valid()) {
            return false;
        }

        int_vector v = th.file_priorities();

        long size = v.size();
        for (int i = 0; i < size; i++) {
            if (v.get(i) == 0) {
                return true;
            }
        }

        return false;
    }

    // Returns true if this handle refers to a valid torrent and false if it
    // hasn't been initialized or if the torrent it refers to has been
    // aborted. Note that a handle may become invalid after it has been added
    // to the session. Usually this is because the storage for the torrent is
    // somehow invalid or if the filenames are not allowed (and hence cannot
    // be opened/created) on your filesystem. If such an error occurs, a
    // file_error_alert is generated and all handles that refers to that
    // torrent will become invalid.
    public boolean isValid() {
        return th.is_valid();
    }

    // Generates a magnet URI from the specified torrent. If the torrent
    // handle is invalid, null is returned.
    //
    // For more information about magnet links, see magnet-links_.
    //
    public String makeMagnetUri() {
        return th.is_valid() ? libtorrent.make_magnet_uri(th) : null;
    }

    // ``set_upload_limit`` will limit the upload bandwidth used by this
    // particular torrent to the limit you set. It is given as the number of
    // bytes per second the torrent is allowed to upload.
    // ``set_download_limit`` works the same way but for download bandwidth
    // instead of upload bandwidth. Note that setting a higher limit on a
    // torrent then the global limit
    // (``session_settings::upload_rate_limit``) will not override the global
    // rate limit. The torrent can never upload more than the global rate
    // limit.
    //
    // ``upload_limit`` and ``download_limit`` will return the current limit
    // setting, for upload and download, respectively.
    public int getUploadLimit() {
        return th.upload_limit();
    }

    // ``set_upload_limit`` will limit the upload bandwidth used by this
    // particular torrent to the limit you set. It is given as the number of
    // bytes per second the torrent is allowed to upload.
    // ``set_download_limit`` works the same way but for download bandwidth
    // instead of upload bandwidth. Note that setting a higher limit on a
    // torrent then the global limit
    // (``session_settings::upload_rate_limit``) will not override the global
    // rate limit. The torrent can never upload more than the global rate
    // limit.
    //
    // ``upload_limit`` and ``download_limit`` will return the current limit
    // setting, for upload and download, respectively.
    public void setUploadLimit(int limit) {
        th.set_upload_limit(limit);
    }

    // ``set_upload_limit`` will limit the upload bandwidth used by this
    // particular torrent to the limit you set. It is given as the number of
    // bytes per second the torrent is allowed to upload.
    // ``set_download_limit`` works the same way but for download bandwidth
    // instead of upload bandwidth. Note that setting a higher limit on a
    // torrent then the global limit
    // (``session_settings::upload_rate_limit``) will not override the global
    // rate limit. The torrent can never upload more than the global rate
    // limit.
    //
    // ``upload_limit`` and ``download_limit`` will return the current limit
    // setting, for upload and download, respectively.
    public int getDownloadLimit() {
        return th.download_limit();
    }

    // ``set_upload_limit`` will limit the upload bandwidth used by this
    // particular torrent to the limit you set. It is given as the number of
    // bytes per second the torrent is allowed to upload.
    // ``set_download_limit`` works the same way but for download bandwidth
    // instead of upload bandwidth. Note that setting a higher limit on a
    // torrent then the global limit
    // (``session_settings::upload_rate_limit``) will not override the global
    // rate limit. The torrent can never upload more than the global rate
    // limit.
    //
    // ``upload_limit`` and ``download_limit`` will return the current limit
    // setting, for upload and download, respectively.
    public void setDownloadLimit(int limit) {
        th.set_download_limit(limit);
    }

    // ``force_recheck`` puts the torrent back in a state where it assumes to
    // have no resume data. All peers will be disconnected and the torrent
    // will stop announcing to the tracker. The torrent will be added to the
    // checking queue, and will be checked (all the files will be read and
    // compared to the piece hashes). Once the check is complete, the torrent
    // will start connecting to peers again, as normal.
    public void forceRecheck() {
        th.force_recheck();
    }

    // ``force_reannounce()`` will force this torrent to do another tracker
    // request, to receive new peers. The ``seconds`` argument specifies how
    // many seconds from now to issue the tracker announces.
    //
    // If the tracker's ``min_interval`` has not passed since the last
    // announce, the forced announce will be scheduled to happen immediately
    // as the ``min_interval`` expires. This is to honor trackers minimum
    // re-announce interval settings.
    //
    // The ``tracker_index`` argument specifies which tracker to re-announce.
    // If set to -1 (which is the default), all trackers are re-announce.
    //
    public void forceReannounce(int seconds, int tracker_index) {
        th.force_reannounce(seconds, tracker_index);
    }

    // ``force_reannounce()`` will force this torrent to do another tracker
    // request, to receive new peers. The ``seconds`` argument specifies how
    // many seconds from now to issue the tracker announces.
    //
    // If the tracker's ``min_interval`` has not passed since the last
    // announce, the forced announce will be scheduled to happen immediately
    // as the ``min_interval`` expires. This is to honor trackers minimum
    // re-announce interval settings.
    //
    // The ``tracker_index`` argument specifies which tracker to re-announce.
    // If set to -1 (which is the default), all trackers are re-announce.
    //
    public void forceReannounce(int seconds) {
        th.force_reannounce(seconds);
    }

    // ``force_reannounce()`` will force this torrent to do another tracker
    // request, to receive new peers. The ``seconds`` argument specifies how
    // many seconds from now to issue the tracker announces.
    //
    // If the tracker's ``min_interval`` has not passed since the last
    // announce, the forced announce will be scheduled to happen immediately
    // as the ``min_interval`` expires. This is to honor trackers minimum
    // re-announce interval settings.
    //
    // The ``tracker_index`` argument specifies which tracker to re-announce.
    // If set to -1 (which is the default), all trackers are re-announce.
    //
    public void forceReannounce() {
        th.force_reannounce();
    }

    /**
     * Announce the torrent to the DHT immediately.
     */
    public void forceDHTAnnounce() {
        th.force_dht_announce();
    }

    // ``scrape_tracker()`` will send a scrape request to the tracker. A
    // scrape request queries the tracker for statistics such as total number
    // of incomplete peers, complete peers, number of downloads etc.
    //
    // This request will specifically update the ``num_complete`` and
    // ``num_incomplete`` fields in the torrent_status struct once it
    // completes. When it completes, it will generate a scrape_reply_alert.
    // If it fails, it will generate a scrape_failed_alert.
    public void scrapeTracker() {
        th.scrape_tracker();
    }

    // ``trackers()`` will return the list of trackers for this torrent. The
    // announce entry contains both a string ``url`` which specify the
    // announce url for the tracker as well as an int ``tier``, which is
    // specifies the order in which this tracker is tried.
    public List<AnnounceEntry> getTrackers() {
        announce_entry_vector v = th.trackers();
        int size = (int) v.size();
        List<AnnounceEntry> list = new ArrayList<AnnounceEntry>(size);

        for (int i = 0; i < size; i++) {
            list.add(new AnnounceEntry(v.get(i)));
        }

        return list;
    }

    // If you want
    // libtorrent to use another list of trackers for this torrent, you can
    // use ``replace_trackers()`` which takes a list of the same form as the
    // one returned from ``trackers()`` and will replace it. If you want an
    // immediate effect, you have to call force_reannounce(). See
    // announce_entry.
    //
    // The updated set of trackers will be saved in the resume data, and when
    // a torrent is started with resume data, the trackers from the resume
    // data will replace the original ones.
    public void replaceTrackers(List<AnnounceEntry> trackers) {
        announce_entry_vector v = new announce_entry_vector();

        for (AnnounceEntry e : trackers) {
            v.add(e.getSwig());
        }

        th.replace_trackers(v);
    }

    // ``add_tracker()`` will look if the specified tracker is already in the
    // set. If it is, it doesn't do anything. If it's not in the current set
    // of trackers, it will insert it in the tier specified in the
    // announce_entry.
    //
    // The updated set of trackers will be saved in the resume data, and when
    // a torrent is started with resume data, the trackers from the resume
    // data will replace the original ones.
    public void addTracker(AnnounceEntry tracker) {
        th.add_tracker(tracker.getSwig());
    }

    /**
     * index must be in the range [0, number_of_files).
     * <p/>
     * The priority values are the same as for piece_priority().
     * <p/>
     * Whenever a file priority is changed, all other piece priorities are
     * reset to match the file priorities. In order to maintain sepcial
     * priorities for particular pieces, piece_priority() has to be called
     * again for those pieces.
     * <p/>
     * You cannot set the file priorities on a torrent that does not yet have
     * metadata or a torrent that is a seed. ``file_priority(int, int)`` and
     * prioritize_files() are both no-ops for such torrents.
     *
     * @param index
     * @param priority
     */
    public void setFilePriority(int index, int priority) {
        th.file_priority(index, priority);
    }

    /**
     * index must be in the range [0, number_of_files).
     * <p/>
     * queries or sets the priority of file index.
     *
     * @param index
     * @return
     */
    public int getFilePriority(int index) {
        return th.file_priority(index);
    }

    /**
     * Takes a vector that has at as many elements as
     * there are files in the torrent. Each entry is the priority of that
     * file. The function sets the priorities of all the pieces in the
     * torrent based on the vector.
     *
     * @param priorities
     */
    public void prioritizeFiles(Priority[] priorities) {
        int[] arr = new int[priorities.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = priorities[i] != Priority.UNKNOWN ? priorities[i].getSwig() : Priority.ZERO.getSwig();
        }
        th.prioritize_files(Vectors.ints2int_vector(arr));
    }

    /**
     * Returns a vector with the priorities of all files.
     *
     * @return
     */
    public int[] getFilePriorities() {
        return Vectors.int_vector2ints(th.file_priorities());
    }
}
