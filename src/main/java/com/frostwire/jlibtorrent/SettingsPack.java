package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.settings_pack.string_types;

/**
 * The ``settings_pack`` struct, contains the names of all settings as
 * enum values. These values are passed in to the ``set_str()``,
 * ``set_int()``, ``set_bool()`` functions, to specify the setting to
 * change.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SettingsPack {

    private final settings_pack sp;

    public SettingsPack(settings_pack sp) {
        this.sp = sp;
    }

    /**
     * Example, how to turn on the DHT using SettingsPack.
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     * pack.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), on);
     * s.applySettings(pack);
     * }</pre>
     */
    public SettingsPack() {
        this(new settings_pack());
    }

    /**
     * @return
     */
    public settings_pack swig() {
        return sp;
    }

    /**
     * @param name
     * @return
     */
    public boolean getBoolean(int name) {
        return sp.get_bool(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setBoolean(int name, boolean value) {
        sp.set_bool(name, value);
        return this;
    }

    /**
     * @param name
     * @return
     */
    public int getInteger(int name) {
        return sp.get_int(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setInteger(int name, int value) {
        sp.set_int(name, value);
        return this;
    }

    /**
     * @param name
     * @return
     */
    public String getString(int name) {
        return sp.get_str(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setString(int name, String value) {
        sp.set_str(name, value);
        return this;
    }

    public byte[] getBytes(int name) {
        byte_vector v = sp.get_bytes(name);
        return Vectors.byte_vector2bytes(v);
    }

    public void setBytes(int name, byte[] value) {
        byte_vector v = Vectors.bytes2byte_vector(value);
        sp.set_bytes(name, v);
    }

    public void clear() {
        sp.clear();
    }

    public void clear(int name) {
        sp.clear(name);
    }

    /**
     * Queries whether the specified configuration option has a value set in
     * this pack. ``name`` can be any enumeration value from string_types,
     * int_types or bool_types.
     *
     * @param name name id of the setting
     * @return true if present
     */
    public boolean hasValue(int name) {
        return sp.has_val(name);
    }

    /**
     * The fingerprint for the client.
     *
     * It will be used as the prefix to the peer-id. If this is 20 bytes (or longer)
     * it will be truncated to 20 bytes and used as the entire peer-id.
     */
    public byte[] getPeerFingerprint() {
        return getBytes(string_types.peer_fingerprint.swigValue());
    }

    /**
     * The fingerprint for the client.
     *
     * It will be used as the prefix to the peer-id. If this is 20 bytes (or longer)
     * it will be truncated to 20 bytes and used as the entire peer-id.
     */
    public void setPeerFingerprint(byte[] value) {
        setBytes(string_types.peer_fingerprint.swigValue(), value);
    }

    /**
     * @return the session-global download rate limit in bytes per second. (0 for unlimited)
     */
    public int downloadRateLimit() {
        return sp.get_int(settings_pack.int_types.download_rate_limit.swigValue());
    }

    /**
     * Sets the session-global limits of download rate limit, in
     * bytes per second.
     * <p>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public SettingsPack downloadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.download_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * @return the session-global upload rate limit in bytes per second. (0 for unlimited)
     */
    public int uploadRateLimit() {
        return sp.get_int(settings_pack.int_types.upload_rate_limit.swigValue());
    }

    /**
     * Sets the session-global limits of upload rate limit, in
     * bytes per second.
     * <p>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public SettingsPack uploadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.upload_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_downloads} controls how many active
     * downloading torrents the queuing mechanism allows.
     * <p>
     * The target number of active torrents is {@code min(active_downloads +
     * active_seeds, active_limit)}. {@code active_downloads} and
     * {@code active_seeds} are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @return
     */
    public int activeDownloads() {
        return sp.get_int(settings_pack.int_types.active_downloads.swigValue());
    }

    /**
     * @param value
     * @see #activeDownloads()
     */
    public SettingsPack activeDownloads(int value) {
        sp.set_int(settings_pack.int_types.active_downloads.swigValue(), value);
        return this;
    }

    /**
     * {@code active_seeds} controls how many active seeding
     * torrents the queuing mechanism allows.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeSeeds() {
        return sp.get_int(settings_pack.int_types.active_seeds.swigValue());
    }

    /**
     * @param value
     * @see #activeSeeds()
     */
    public SettingsPack activeSeeds(int value) {
        sp.set_int(settings_pack.int_types.active_seeds.swigValue(), value);
        return this;
    }

    /**
     * {@code active_checking} is the limit of number of simultaneous checking
     * torrents.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeChecking() {
        return sp.get_int(settings_pack.int_types.active_checking.swigValue());
    }

    /**
     * @param value
     * @see #activeChecking()
     */
    public SettingsPack activeChecking(int value) {
        sp.set_int(settings_pack.int_types.active_checking.swigValue(), value);
        return this;
    }

    /**
     * {@code active_dht_limit} is the max number of torrents to announce to
     * the DHT. By default this is set to 88, which is no more than one
     * DHT announce every 10 seconds.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeDhtLimit() {
        return sp.get_int(settings_pack.int_types.active_dht_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeDhtLimit()
     */
    public SettingsPack activeDhtLimit(int value) {
        sp.set_int(settings_pack.int_types.active_dht_limit.swigValue(), value);
        return this;
    }

    public int dhtUploadRate() {
        return sp.get_int(settings_pack.int_types.dht_upload_rate_limit.swigValue());
    }

    public SettingsPack dhtUploadRate(int value) {
        sp.set_int(settings_pack.int_types.dht_upload_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_tracker_limit} is the max number of torrents to announce
     * to their trackers. By default this is 360, which is no more than
     * one announce every 5 seconds.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeTrackerLimit() {
        return sp.get_int(settings_pack.int_types.active_tracker_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeTrackerLimit()
     */
    public SettingsPack activeTrackerLimit(int value) {
        sp.set_int(settings_pack.int_types.active_tracker_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_lsd_limit} is the max number of torrents to announce to
     * the local network over the local service discovery protocol. By
     * default this is 80, which is no more than one announce every 5
     * seconds (assuming the default announce interval of 5 minutes).
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeLsdLimit() {
        return sp.get_int(settings_pack.int_types.active_lsd_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeLsdLimit()
     */
    public SettingsPack activeLsdLimit(int value) {
        sp.set_int(settings_pack.int_types.active_lsd_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_limit} is a hard limit on the number of active (auto
     * managed) torrents. This limit also applies to slow torrents.
     *
     * @return the value
     * @see #activeDownloads()
     */
    public int activeLimit() {
        return sp.get_int(settings_pack.int_types.active_limit.swigValue());
    }

    /**
     * {@code active_limit} is a hard limit on the number of active (auto
     * managed) torrents. This limit also applies to slow torrents.
     *
     * @param value the value
     * @see #activeLimit()
     */
    public SettingsPack activeLimit(int value) {
        sp.set_int(settings_pack.int_types.active_limit.swigValue(), value);
        return this;
    }

    /**
     * @return global limit on the number of connections opened.
     */
    public int connectionsLimit() {
        return sp.get_int(settings_pack.int_types.connections_limit.swigValue());
    }

    /**
     * Sets a global limit on the number of connections opened. The number of
     * connections is set to a hard minimum of at least two per torrent, so
     * if you set a too low connections limit, and open too many torrents,
     * the limit will not be met.
     *
     * @param value
     */
    public SettingsPack connectionsLimit(int value) {
        sp.set_int(settings_pack.int_types.connections_limit.swigValue(), value);
        return this;
    }

    /**
     * @return the maximum number of peers in the list of known peers. (0 for unlimited)
     */
    public int maxPeerlistSize() {
        return sp.get_int(settings_pack.int_types.max_peerlist_size.swigValue());
    }

    /**
     * Sets the maximum number of peers in the list of known peers. These peers
     * are not necessarily connected, so this number should be much greater
     * than the maximum number of connected peers. Peers are evicted from the
     * cache when the list grows passed 90% of this limit, and once the size
     * hits the limit, peers are no longer added to the list. If this limit
     * is set to 0, there is no limit on how many peers we'll keep in the
     * peer list.
     *
     * @param value
     */
    public SettingsPack maxPeerlistSize(int value) {
        sp.set_int(settings_pack.int_types.max_peerlist_size.swigValue(), value);
        return this;
    }

    /**
     * @return the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled.
     */
    public int maxQueuedDiskBytes() {
        return sp.get_int(settings_pack.int_types.max_queued_disk_bytes.swigValue());
    }

    /**
     * Sets the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled. This prevents
     * fast downloads to slow medias to allocate more memory indefinitely.
     * This should be set to at least 16 kB to not completely disrupt normal
     * downloads. If it's set to 0, you will be starving the disk thread and
     * nothing will be written to disk. this is a per session setting.
     * <p>
     * When this limit is reached, the peer connections will stop reading
     * data from their sockets, until the disk thread catches up. Setting
     * this too low will severely limit your download rate.
     *
     * @param value
     */
    public SettingsPack maxQueuedDiskBytes(int value) {
        sp.set_int(settings_pack.int_types.max_queued_disk_bytes.swigValue(), value);
        return this;
    }

    /**
     * @return the upper limit of the send buffer low-watermark.
     */
    public int sendBufferWatermark() {
        return sp.get_int(settings_pack.int_types.send_buffer_watermark.swigValue());
    }

    /**
     * Sets the upper limit of the send buffer low-watermark.
     * <p>
     * if the send buffer has fewer bytes than this, we'll read another 16kB
     * block onto it. If set too small, upload rate capacity will suffer. If
     * set too high, memory will be wasted. The actual watermark may be lower
     * than this in case the upload rate is low, this is the upper limit.
     *
     * @param value
     */
    public SettingsPack sendBufferWatermark(int value) {
        sp.set_int(settings_pack.int_types.send_buffer_watermark.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public int tickInterval() {
        return sp.get_int(settings_pack.int_types.tick_interval.swigValue());
    }

    /**
     * Specifies the number of milliseconds between internal ticks. This is
     * the frequency with which bandwidth quota is distributed to peers. It
     * should not be more than one second (i.e. 1000 ms). Setting this to a
     * low value (around 100) means higher resolution bandwidth quota
     * distribution, setting it to a higher value saves CPU cycles.
     *
     * @param value
     */
    public SettingsPack tickInterval(int value) {
        sp.set_int(settings_pack.int_types.tick_interval.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public int inactivityTimeout() {
        return sp.get_int(settings_pack.int_types.inactivity_timeout.swigValue());
    }

    /**
     * if a peer is uninteresting and uninterested for longer than this
     * number of seconds, it will be disconnected. default is 10 minutes
     *
     * @param value
     */
    public SettingsPack inactivityTimeout(int value) {
        sp.set_int(settings_pack.int_types.inactivity_timeout.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public boolean seedingOutgoingConnections() {
        return sp.get_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue());
    }

    /**
     * Determines if seeding (and finished) torrents should attempt to make
     * outgoing connections or not. By default this is true. It may be set to
     * false in very specific applications where the cost of making outgoing
     * connections is high, and there are no or small benefits of doing so.
     * For instance, if no nodes are behind a firewall or a NAT, seeds don't
     * need to make outgoing connections.
     *
     * @param value
     */
    public SettingsPack seedingOutgoingConnections(boolean value) {
        sp.set_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public boolean anonymousMode() {
        return sp.get_bool(settings_pack.bool_types.anonymous_mode.swigValue());
    }

    /**
     * defaults to false. When set to true, the client tries to hide its
     * identity to a certain degree. The peer-ID will no longer include the
     * client's fingerprint. The user-agent will be reset to an empty string.
     * It will also try to not leak other identifying information, such as
     * your local listen port, your IP etc.
     * <p>
     * If you're using I2P, a VPN or a proxy, it might make sense to enable
     * anonymous mode.
     *
     * @param value
     */
    public SettingsPack anonymousMode(boolean value) {
        sp.set_bool(settings_pack.bool_types.anonymous_mode.swigValue(), value);
        return this;
    }

    /**
     * Starts the dht node and makes the trackerless service available to
     * torrents.
     *
     * @return true if enable
     */
    public boolean isEnableDht() {
        return sp.get_bool(settings_pack.bool_types.enable_dht.swigValue());
    }

    /**
     * Starts the dht node and makes the trackerless service available to
     * torrents.
     *
     * @param value true if enable
     */
    public void setEnableDht(boolean value) {
        sp.set_bool(settings_pack.bool_types.enable_dht.swigValue(), value);
    }

    /**
     * Starts and stops Local Service Discovery. This service will
     * broadcast the info-hashes of all the non-private torrents on the
     * local network to look for peers on the same swarm within multicast
     * reach.
     *
     * @return true if enable
     */
    public boolean isEnableLsd() {
        return sp.get_bool(settings_pack.bool_types.enable_lsd.swigValue());
    }

    /**
     * Starts and stops Local Service Discovery. This service will
     * broadcast the info-hashes of all the non-private torrents on the
     * local network to look for peers on the same swarm within multicast
     * reach.
     *
     * @param value true if enable
     */
    public void setEnableLsd(boolean value) {
        sp.set_bool(settings_pack.bool_types.enable_lsd.swigValue(), value);
    }

    /**
     * The maximum allowed size (in bytes) to be
     * received by the metadata extension, i.e. magnet links.
     */
    public int getMaxMetadataSize() {
        return sp.get_int(settings_pack.int_types.max_metadata_size.swigValue());
    }

    /**
     * The maximum allowed size (in bytes) to be
     * received by the metadata extension, i.e. magnet links.
     *
     * @param value true if enable
     */
    public void setMaxMetadataSize(int value) {
        sp.set_int(settings_pack.int_types.max_metadata_size.swigValue(), value);
    }

    /**
     * This is a comma-separated list of IP port-pairs. They will be added
     * to the DHT node (if it's enabled) as back-up nodes in case we don't
     * know of any.
     * <p>
     * Changing these after the DHT has been started may not have any
     * effect until the DHT is restarted.
     */
    public String getDhtBootstrapNodes() {
        return  sp.get_str(string_types.dht_bootstrap_nodes.swigValue());
    }

    /**
     * This is a comma-separated list of IP port-pairs. They will be added
     * to the DHT node (if it's enabled) as back-up nodes in case we don't
     * know of any.
     * <p>
     * Changing these after the DHT has been started may not have any
     * effect until the DHT is restarted.
     *
     * @param value the IP port-pairs list
     */
    public void setDhtBootstrapNodes(String value) {
        sp.set_str(string_types.dht_bootstrap_nodes.swigValue(), value);
    }

    /**
     * This is the STUN server used by WebTorrent to enable ICE NAT
     * traversal for WebRTC. It must have the format ``hostname:port``.
     *
     * TODO: Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0
     * See https://github.com/arvidn/libtorrent/blob/master/include/libtorrent/settings_pack.hpp#L370
     */
    public String getWebtorrentStunServer() {
        new RuntimeException("Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0");
        return "sp.get_str(string_types.webtorrent_stun_server.swigValue());";
    }

    /**
     * This is the STUN server used by WebTorrent to enable ICE NAT
     * traversal for WebRTC. It must have the format ``hostname:port``.
     *
     * TODO: Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0
     * See https://github.com/arvidn/libtorrent/blob/master/include/libtorrent/settings_pack.hpp#L370
     *
     * @param value the STUN server endpoint
     */
    public void setWebtorrentStunServer(String value) {
        new RuntimeException("Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0");
        //sp.set_str(string_types.webtorrent_stun_server.swigValue(), value);
    }

    /**
     * @return
     */
    public String listenInterfaces() {
        return sp.get_str(settings_pack.string_types.listen_interfaces.swigValue());
    }

    /**
     * @param value
     * @return this
     */
    public SettingsPack listenInterfaces(String value) {
        sp.set_str(settings_pack.string_types.listen_interfaces.swigValue(), value);
        return this;
    }

    /**
     * @return the current value
     * @see #stopTrackerTimeout(int)
     */
    public int stopTrackerTimeout() {
        return sp.get_int(settings_pack.int_types.stop_tracker_timeout.swigValue());
    }

    /**
     * {@code stop_tracker_timeout} is the number of seconds to wait when
     * sending a stopped message before considering a tracker to have
     * timed out. This is usually shorter, to make the client quit faster.
     * If the value is set to 0, the connections to trackers with the
     * stopped event are suppressed.
     *
     * @param value the new value
     * @return this
     */
    public SettingsPack stopTrackerTimeout(int value) {
        sp.set_int(settings_pack.int_types.stop_tracker_timeout.swigValue(), value);
        return this;
    }

    /**
     * @return the current value
     * @see #alertQueueSize(int)
     */
    public int alertQueueSize() {
        return sp.get_int(settings_pack.int_types.alert_queue_size.swigValue());
    }

    /**
     * {@code alert_queue_size} is the maximum number of alerts queued up
     * internally. If alerts are not popped, the queue will eventually
     * fill up to this level.
     *
     * @param value the new value
     * @return this
     */
    public SettingsPack alertQueueSize(int value) {
        sp.set_int(settings_pack.int_types.alert_queue_size.swigValue(), value);
        return this;
    }


    /**
     * {@code validate_https_trackers} when set to true, the certificate of HTTPS trackers
     * and HTTPS web seeds will be validated against the system's certificate store (as defined by OpenSSL).
     * If the system does not have a certificate store, this option may have to be disabled
     * in order to get trackers and web seeds to work).
     */
    public SettingsPack validateHttpsTrackers(boolean value) {
        sp.set_bool(settings_pack.bool_types.validate_https_trackers.swigValue(), value);
        return this;
    }

    public boolean validateHttpsTrackers() {
        return sp.get_bool(settings_pack.bool_types.validate_https_trackers.swigValue());
    }

    /**
     * Returns whether the disk copy-on-write optimization is disabled.
     * <p>
     * When disabled, libtorrent will not use the operating system's copy-on-write
     * mechanism for memory-mapped files. Disabling copy-on-write can improve
     * disk I/O performance on certain filesystems (e.g., Btrfs, APFS, ZFS)
     * where copy-on-write introduces additional overhead for torrent workloads.
     * <p>
     * By default this is {@code false} (copy-on-write is enabled/used by the OS
     * if available).
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Check current setting
     * boolean cowDisabled = pack.diskDisableCopyOnWrite();
     *
     * // Disable copy-on-write for better performance on CoW filesystems
     * pack.diskDisableCopyOnWrite(true);
     *
     * // Apply settings to session
     * sessionManager.applySettings(pack);
     *
     * // Or use fluent API during session setup
     * SettingsPack settings = new SettingsPack()
     *     .diskDisableCopyOnWrite(true)
     *     .downloadRateLimit(0)
     *     .uploadRateLimit(0);
     * }</pre>
     *
     * @return {@code true} if copy-on-write is disabled, {@code false} otherwise
     * @see #diskDisableCopyOnWrite(boolean)
     */
    public boolean diskDisableCopyOnWrite() {
        return sp.get_bool(settings_pack.bool_types.disk_disable_copy_on_write.swigValue());
    }

    /**
     * Sets whether to disable the disk copy-on-write optimization.
     * <p>
     * When set to {@code true}, libtorrent will not use the operating system's
     * copy-on-write mechanism for memory-mapped files. This can improve disk
     * I/O performance on filesystems that use copy-on-write by default (e.g.,
     * Btrfs, APFS, ZFS), where the CoW behavior introduces extra overhead
     * for random-write workloads typical of BitTorrent downloads.
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>On Btrfs/ZFS/APFS filesystems where CoW causes fragmentation</li>
     *   <li>When downloading to SSDs where CoW write amplification is a concern</li>
     *   <li>When experiencing slower-than-expected disk I/O throughput</li>
     * </ul>
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack()
     *     .diskDisableCopyOnWrite(true)
     *     .downloadRateLimit(0);
     *
     * sessionManager.applySettings(pack);
     * }</pre>
     *
     * @param value {@code true} to disable copy-on-write, {@code false} to leave it enabled
     * @return this SettingsPack for fluent chaining
     * @see #diskDisableCopyOnWrite()
     */
    public SettingsPack diskDisableCopyOnWrite(boolean value) {
        sp.set_bool(settings_pack.bool_types.disk_disable_copy_on_write.swigValue(), value);
        return this;
    }
}
