package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.session_settings;

/**
 * This holds most of the session-wide settings in libtorrent. Pass this
 * to {@link Session#setSettings(SessionSettings)} to change the settings,
 * initialize it from {@link Session#getSettings()} to get the current
 * settings.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SessionSettings {

    private final session_settings s;

    public SessionSettings(session_settings s) {
        this.s = s;
    }

    public session_settings getSwig() {
        return s;
    }

    /**
     * automatically set to the libtorrent version you're using in order to
     * be forward binary compatible. This field should not be changed.
     *
     * @return
     */
    public int getVersion() {
        return s.getVersion();
    }

    /**
     * the client identification to the tracker. The recommended format of
     * this string is: "ClientName/ClientVersion
     * libtorrent/libtorrentVersion". This name will not only be used when
     * making HTTP requests, but also when sending extended headers to peers
     * that support that extension.
     *
     * @return
     */
    public String getUserAgent() {
        return s.getUser_agent();
    }

    /**
     * the client identification to the tracker. The recommended format of
     * this string is: "ClientName/ClientVersion
     * libtorrent/libtorrentVersion". This name will not only be used when
     * making HTTP requests, but also when sending extended headers to peers
     * that support that extension.
     */
    public void setUserAgent(String value) {
        s.setUser_agent(value);
    }

    // the number of seconds the tracker connection will wait from when it
    // sent the request until it considers the tracker to have timed-out.
    // Default value is 60 seconds.
    //int tracker_completion_timeout;

    // the number of seconds to wait to receive any data from the tracker. If
    // no data is received for this number of seconds, the tracker will be
    // considered as having timed out. If a tracker is down, this is the kind
    // of timeout that will occur. The default value is 20 seconds.
    //int tracker_receive_timeout;

    // the time to wait when sending a stopped message before considering a
    // tracker to have timed out. this is usually shorter, to make the client
    // quit faster
    //
    // This is given in seconds. Default is 10 seconds.
    //int stop_tracker_timeout;

    // the maximum number of bytes in a tracker response. If a response size
    // passes this number it will be rejected and the connection will be
    // closed. On gzipped responses this size is measured on the uncompressed
    // data. So, if you get 20 bytes of gzip response that'll expand to 2
    // megs, it will be interrupted before the entire response has been
    // uncompressed (given your limit is lower than 2 megs). Default limit is
    // 1 megabyte.
    //int tracker_maximum_response_length;

    // controls the number of seconds from a request is sent until it times
    // out if no piece response is returned.
    //int piece_timeout;

    // the number of seconds one block (16kB) is expected to be received
    // within. If it's not, the block is requested from a different peer
    //int request_timeout;

    // the length of the request queue given in the number of seconds it
    // should take for the other end to send all the pieces. i.e. the actual
    // number of requests depends on the download rate and this number.
    //int request_queue_time;

    // the number of outstanding block requests a peer is allowed to queue up
    // in the client. If a peer sends more requests than this (before the
    // first one has been sent) the last request will be dropped. the higher
    // this is, the faster upload speeds the client can get to a single peer.
    //int max_allowed_in_request_queue;

    // the maximum number of outstanding requests to send to a peer. This
    // limit takes precedence over request_queue_time. i.e. no matter the
    // download speed, the number of outstanding requests will never exceed
    // this limit.
    //int max_out_request_queue;

    // if a whole piece can be downloaded in this number of seconds, or less,
    // the peer_connection will prefer to request whole pieces at a time from
    // this peer. The benefit of this is to better utilize disk caches by
    // doing localized accesses and also to make it easier to identify bad
    // peers if a piece fails the hash check.
    //int whole_pieces_threshold;

    // the number of seconds to wait for any activity on the peer wire before
    // closing the connectiong due to time out. This defaults to 120 seconds,
    // since that's what's specified in the protocol specification. After
    // half the time out, a keep alive message is sent.
    //int peer_timeout;

    /**
     * same as peer_timeout, but only applies to url-seeds. this is usually
     * set lower, because web servers are expected to be more reliable. This
     * value defaults to 20 seconds.
     *
     * @return
     */
    public int getUrlSeedTimeout() {
        return s.getUrlseed_timeout();
    }

    /**
     * same as peer_timeout, but only applies to url-seeds. this is usually
     * set lower, because web servers are expected to be more reliable. This
     * value defaults to 20 seconds.
     *
     * @return
     */
    public void setUrlSeedTimeout(int value) {
        s.setUrlseed_timeout(value);
    }

    // controls the pipelining with the web server. When using persistent
    // connections to HTTP 1.1 servers, the client is allowed to send more
    // requests before the first response is received. This number controls
    // the number of outstanding requests to use with url-seeds. Default is
    // 5.
    //int urlseed_pipeline_size;

    // time to wait until a new retry takes place
    //int urlseed_wait_retry;

    // sets the upper limit on the total number of files this session will
    // keep open. The reason why files are left open at all is that some anti
    // virus software hooks on every file close, and scans the file for
    // viruses. deferring the closing of the files will be the difference
    // between a usable system and a completely hogged down system. Most
    // operating systems also has a limit on the total number of file
    // descriptors a process may have open. It is usually a good idea to find
    // this limit and set the number of connections and the number of files
    // limits so their sum is slightly below it.
    //int file_pool_size;

    /**
     * determines if connections from the same IP address as existing
     * connections should be rejected or not. Multiple connections from the
     * same IP address is not allowed by default, to prevent abusive behavior
     * by peers. It may be useful to allow such connections in cases where
     * simulations are run on the same machie, and all peers in a swarm has
     * the same IP address.
     *
     * @return
     */
    public boolean allowMultipleConnectionsPerIp() {
        return s.getAllow_multiple_connections_per_ip();
    }

    /**
     * determines if connections from the same IP address as existing
     * connections should be rejected or not. Multiple connections from the
     * same IP address is not allowed by default, to prevent abusive behavior
     * by peers. It may be useful to allow such connections in cases where
     * simulations are run on the same machie, and all peers in a swarm has
     * the same IP address.
     */
    public void allowMultipleConnectionsPerIp(boolean value) {
        s.setAllow_multiple_connections_per_ip(value);
    }

    // the maximum times we try to connect to a peer before stop connecting
    // again. If a peer succeeds, its failcounter is reset. If a peer is
    // retrieved from a peer source (other than DHT) the failcount is
    // decremented by one, allowing another try.
    //int max_failcount;

    // the number of seconds to wait to reconnect to a peer. this time is
    // multiplied with the failcount.
    //int min_reconnect_time;

    // the number of seconds to wait after a connection attempt is initiated
    // to a peer until it is considered as having timed out. The default is
    // 10 seconds. This setting is especially important in case the number of
    // half-open connections are limited, since stale half-open connection
    // may delay the connection of other peers considerably.
    //int peer_connect_timeout;

    // if set to true, upload, download and unchoke limits are ignored for
    // peers on the local network.
    //bool ignore_limits_on_local_network;

    /**
     * the number of connection attempts that are made per second. If a
     * number < 0 is specified, it will default to 200 connections per
     * second. If 0 is specified, it means don't make outgoing connections at
     * all.
     *
     * @return
     */
    public int getConnectionSpeed() {
        return s.getConnection_speed();
    }

    /**
     * the number of connection attempts that are made per second. If a
     * number < 0 is specified, it will default to 200 connections per
     * second. If 0 is specified, it means don't make outgoing connections at
     * all.
     *
     * @param value
     */
    public void setConnectionSpeed(int value) {
        s.setConnection_speed(value);
    }

    // if this is set to true, have messages will be sent to peers that
    // already have the piece. This is typically not necessary, but it might
    // be necessary for collecting statistics in some cases. Default is
    // false.
    //bool send_redundant_have;

    // prevents outgoing bitfields from being full. If the client is seed, a
    // few bits will be set to 0, and later filled in with have-messages.
    // This is an old attempt to prevent certain ISPs from stopping people
    // from seeding.
    //bool lazy_bitfields;

    // if a peer is uninteresting and uninterested for longer than this
    // number of seconds, it will be disconnected. default is 10 minutes
    //int inactivity_timeout;

    // the number of seconds between chokes/unchokes. On this interval, peers
    // are re-evaluated for being choked/unchoked. This is defined as 30
    // seconds in the protocol, and it should be significantly longer than
    // what it takes for TCP to ramp up to it's max rate.
    //int unchoke_interval;

    // the number of seconds between each *optimistic* unchoke. On this
    // timer, the currently optimistically unchoked peer will change.
    //int optimistic_unchoke_interval;

    /**
     * the ip address passed along to trackers as the ``&ip=`` parameter. If
     * left as the default (an empty string), that parameter is omitted. Most
     * trackers ignore this argument. This is here for completeness for
     * edge-cases where it may be useful.
     *
     * @return
     */
    public String getAnnounceIp() {
        return s.getAnnounce_ip();
    }

    /**
     * the ip address passed along to trackers as the ``&ip=`` parameter. If
     * left as the default (an empty string), that parameter is omitted. Most
     * trackers ignore this argument. This is here for completeness for
     * edge-cases where it may be useful.
     *
     * @param value
     */
    public void setAnnounceIp(String value) {
        s.setAnnounce_ip(value);
    }

    // the number of peers we want from each tracker request. It defines what
    // is sent as the ``&num_want=`` parameter to the tracker. Stopped
    // messages always send num_want=0. This setting control what to say in
    // the case where we actually want peers.
    //int num_want;

    // specifies the number of pieces we need before we switch to rarest
    // first picking. This defaults to 4, which means the 4 first pieces in
    // any torrent are picked at random, the following pieces are picked in
    // rarest first order.
    //int initial_picker_threshold;

    // the number of allowed pieces to send to choked peers that supports the
    // fast extensions
    //int allowed_fast_set_size;

    /**
     * this determines which pieces will be suggested to peers suggest read
     * cache will make libtorrent suggest pieces that are fresh in the disk
     * read cache, to potentially lower disk access and increase the cache
     * hit ratio
     *
     * @return
     */
    public SuggestMode getSuggestMode() {
        return SuggestMode.fromSwig(s.getSuggest_mode());
    }

    /**
     * this determines which pieces will be suggested to peers suggest read
     * cache will make libtorrent suggest pieces that are fresh in the disk
     * read cache, to potentially lower disk access and increase the cache
     * hit ratio
     *
     * @param value
     */
    public void setSuggestMode(SuggestMode value) {
        s.setSuggest_mode(value.getSwig());
    }

    // the maximum number of bytes a connection may have pending in the disk
    // write queue before its download rate is being throttled. This prevents
    // fast downloads to slow medias to allocate more memory indefinitely.
    // This should be set to at least 16 kB to not completely disrupt normal
    // downloads. If it's set to 0, you will be starving the disk thread and
    // nothing will be written to disk. this is a per session setting.
    //
    // When this limit is reached, the peer connections will stop reading
    // data from their sockets, until the disk thread catches up. Setting
    // this too low will severly limit your download rate.
    //int max_queued_disk_bytes;

    // this is the low watermark for the disk buffer queue. whenever the
    // number of queued bytes exceed the max_queued_disk_bytes, libtorrent
    // will wait for it to drop below this value before issuing more reads
    // from the sockets. If set to 0, the low watermark will be half of the
    // max queued disk bytes
    //int max_queued_disk_bytes_low_watermark;

    // the number of seconds to wait for a handshake response from a peer. If
    // no response is received within this time, the peer is disconnected.
    //int handshake_timeout;

    /**
     * determines how the DHT is used. If this is true, the DHT will only be
     * used for torrents where all trackers in its tracker list has failed.
     * Either by an explicit error message or a time out. This is false by
     * default, which means the DHT is used by default regardless of if the
     * trackers fail or not.
     *
     * @return
     */
    public boolean useDhtAsFallback() {
        return s.getUse_dht_as_fallback();
    }

    /**
     * determines how the DHT is used. If this is true, the DHT will only be
     * used for torrents where all trackers in its tracker list has failed.
     * Either by an explicit error message or a time out. This is false by
     * default, which means the DHT is used by default regardless of if the
     * trackers fail or not.
     *
     * @param value
     */
    public void useDhtAsFallback(boolean value) {
        s.setUse_dht_as_fallback(value);
    }

    // determines whether or not the torrent's piece hashes are kept in
    // memory after the torrent becomes a seed or not. If it is set to
    // ``true`` the hashes are freed once the torrent is a seed (they're not
    // needed anymore since the torrent won't download anything more). If
    // it's set to false they are not freed. If they are freed, the
    // torrent_info returned by get_torrent_info() will return an object that
    // may be incomplete, that cannot be passed back to async_add_torrent()
    // and add_torrent() for instance.
    //bool free_torrent_hashes;

    // indicates whether or not the UPnP implementation should ignore any
    // broadcast response from a device whose address is not the configured
    // router for this machine. i.e. it's a way to not talk to other people's
    // routers by mistake.
    //bool upnp_ignore_nonrouters;

    // This is the minimum send buffer target size (send buffer includes
    // bytes pending being read from disk). For good and snappy seeding
    // performance, set this fairly high, to at least fit a few blocks. This
    // is essentially the initial window size which will determine how fast
    // we can ramp up the send rate
    //int send_buffer_low_watermark;

    // the upper limit of the send buffer low-watermark.
    //
    // if the send buffer has fewer bytes than this, we'll read another 16kB
    // block onto it. If set too small, upload rate capacity will suffer. If
    // set too high, memory will be wasted. The actual watermark may be lower
    // than this in case the upload rate is low, this is the upper limit.
    //int send_buffer_watermark;

    // the current upload rate to a peer is multiplied by this factor to get
    // the send buffer watermark. The factor is specified as a percentage.
    // i.e. 50 indicates a factor of 0.5.
    //
    // This product is clamped to the send_buffer_watermark setting to not
    // exceed the max. For high speed upload, this should be set to a greater
    // value than 100. The default is 50.
    //
    // For high capacity connections, setting this higher can improve upload
    // performance and disk throughput. Setting it too high may waste RAM and
    // create a bias towards read jobs over write jobs.
    //int send_buffer_watermark_factor;

    /**
     * specifies which algorithm to use to determine which peers to unchoke.
     * This setting replaces the deprecated settings ``auto_upload_slots``
     * and ``auto_upload_slots_rate_based``. For options, see
     * choking_algorithm_t.
     *
     * @return
     */
    public ChokingAlgorithm getChokingAlgorithm() {
        return ChokingAlgorithm.fromSwig(s.getChoking_algorithm());
    }

    /**
     * specifies which algorithm to use to determine which peers to unchoke.
     * This setting replaces the deprecated settings ``auto_upload_slots``
     * and ``auto_upload_slots_rate_based``. For options, see
     * choking_algorithm_t.
     *
     * @param value
     */
    public void setChokingAlgorithm(ChokingAlgorithm value) {
        s.setChoking_algorithm(value.getSwig());
    }

    /**
     * controls the seeding unchoke behavior. For options, see
     * seed_choking_algorithm_t.
     *
     * @return
     */
    public SeedChokingAlgorithm getSeedChokingAlgorithm() {
        return SeedChokingAlgorithm.fromSwig(s.getSeed_choking_algorithm());
    }

    /**
     * controls the seeding unchoke behavior. For options, see
     * seed_choking_algorithm_t.
     *
     * @param value
     */
    public void setSeedChokingAlgorithm(SeedChokingAlgorithm value) {
        s.setSeed_choking_algorithm(value.getSwig());
    }

    // specifies if parole mode should be used. Parole mode means that peers
    // that participate in pieces that fail the hash check are put in a mode
    // where they are only allowed to download whole pieces. If the whole
    // piece a peer in parole mode fails the hash check, it is banned. If a
    // peer participates in a piece that passes the hash check, it is taken
    // out of parole mode.
    //bool use_parole_mode;

    // the disk write and read  cache. It is specified in units of 16 KiB
    // blocks. Buffers that are part of a peer's send or receive buffer also
    // count against this limit. Send and receive buffers will never be
    // denied to be allocated, but they will cause the actual cached blocks
    // to be flushed or evicted. If this is set to -1, the cache size is
    // automatically set to the amount of physical RAM available in the
    // machine divided by 8. If the amount of physical RAM cannot be
    // determined, it's set to 1024 (= 16 MiB).
    //
    // Disk buffers are allocated using a pool allocator, the number of
    // blocks that are allocated at a time when the pool needs to grow can be
    // specified in ``cache_buffer_chunk_size``. This defaults to 16 blocks.
    // Lower numbers saves memory at the expense of more heap allocations. It
    // must be at least 1.
    //int cache_size;

    // this is the number of disk buffer blocks (16 kiB) that should be
    // allocated at a time. It must be at least 1. Lower number saves memory
    // at the expense of more heap allocations
    //int cache_buffer_chunk_size;

    // the number of seconds a write cache entry sits idle in the cache
    // before it's forcefully flushed to disk.
    //int cache_expiry;

    // when set to true (default), the disk cache is also used to cache
    // pieces read from disk. Blocks for writing pieces takes presedence.
    //bool use_read_cache;

    // defaults to 0. If set to something greater than 0, the disk read cache
    // will not be evicted by cache misses and will explicitly be controlled
    // based on the rarity of pieces. Rare pieces are more likely to be
    // cached. This would typically be used together with ``suggest_mode``
    // set to ``suggest_read_cache``. The value is the number of pieces to
    // keep in the read cache. If the actual read cache can't fit as many, it
    // will essentially be clamped.
    //bool explicit_read_cache;

    // the number of seconds in between each refresh of a part of the
    // explicit read cache. Torrents take turns in refreshing and this is the
    // time in between each torrent refresh. Refreshing a torrent's explicit
    // read cache means scanning all pieces and picking a random set of the
    // rarest ones. There is an affinity to pick pieces that are already in
    // the cache, so that subsequent refreshes only swaps in pieces that are
    // rarer than whatever is in the cache at the time.
    //int explicit_cache_interval;

    /**
     * Gets the session-global upload rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public int getUploadRateLimit() {
        return s.getUpload_rate_limit();
    }

    /**
     * Sets the session-global upload rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public void setUploadRateLimit(int value) {
        s.setUpload_rate_limit(value);
    }

    /**
     * Gets the session-global download rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public int getDownloadRateLimit() {
        return s.getDownload_rate_limit();
    }

    /**
     * Sets the session-global download rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public void setDownloadRateLimit(int value) {
        s.setDownload_rate_limit(value);
    }

    /**
     * Gets the session-global local upload rate limit, in
     * bytes per second. The local rates refer to peers on the local network.
     * By default peers on the local network are not rate limited.
     * <p/>
     * These rate limits are only used for local peers (peers within the same
     * subnet as the client itself) and it is only used when
     * ``session_settings::ignore_limits_on_local_network`` is set to true
     * (which it is by default). These rate limits default to unthrottled,
     * but can be useful in case you want to treat local peers
     * preferentially, but not quite unthrottled.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public int getLocalUploadRateLimit() {
        return s.getLocal_upload_rate_limit();
    }

    /**
     * Sets the session-global local upload rate limit, in
     * bytes per second. The local rates refer to peers on the local network.
     * By default peers on the local network are not rate limited.
     * <p/>
     * These rate limits are only used for local peers (peers within the same
     * subnet as the client itself) and it is only used when
     * ``session_settings::ignore_limits_on_local_network`` is set to true
     * (which it is by default). These rate limits default to unthrottled,
     * but can be useful in case you want to treat local peers
     * preferentially, but not quite unthrottled.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public void setLocalUploadRateLimit(int value) {
        s.setLocal_upload_rate_limit(value);
    }

    /**
     * Gets the session-global local download rate limit, in
     * bytes per second. The local rates refer to peers on the local network.
     * By default peers on the local network are not rate limited.
     * <p/>
     * These rate limits are only used for local peers (peers within the same
     * subnet as the client itself) and it is only used when
     * ``session_settings::ignore_limits_on_local_network`` is set to true
     * (which it is by default). These rate limits default to unthrottled,
     * but can be useful in case you want to treat local peers
     * preferentially, but not quite unthrottled.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public int getLocalDownloadRateLimit() {
        return s.getLocal_download_rate_limit();
    }

    /**
     * Sets the session-global local download rate limit, in
     * bytes per second. The local rates refer to peers on the local network.
     * By default peers on the local network are not rate limited.
     * <p/>
     * These rate limits are only used for local peers (peers within the same
     * subnet as the client itself) and it is only used when
     * ``session_settings::ignore_limits_on_local_network`` is set to true
     * (which it is by default). These rate limits default to unthrottled,
     * but can be useful in case you want to treat local peers
     * preferentially, but not quite unthrottled.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @return
     */
    public void setLocalDownloadRateLimit(int value) {
        s.setLocal_download_rate_limit(value);
    }

    /**
     * The default values of the session settings are set for a regular
     * bittorrent client running on a desktop system. There are functions that
     * can set the session settings to pre set settings for other environments.
     * These can be used for the basis, and should be tweaked to fit your needs
     * better.
     *
     * @return
     */
    public static SessionSettings newDefaults() {
        return new SessionSettings(new session_settings());
    }

    /**
     * Returns settings that will use the minimal amount of
     * RAM, at the potential expense of upload and download performance. It
     * adjusts the socket buffer sizes, disables the disk cache, lowers the send
     * buffer watermarks so that each connection only has at most one block in
     * use at any one time. It lowers the outstanding blocks send to the disk
     * I/O thread so that connections only have one block waiting to be flushed
     * to disk at any given time. It lowers the max number of peers in the peer
     * list for torrents. It performs multiple smaller reads when it hashes
     * pieces, instead of reading it all into memory before hashing.
     * <p/>
     * This configuration is inteded to be the starting point for embedded
     * devices. It will significantly reduce memory usage.
     *
     * @return
     */
    public static SessionSettings newMinMemoryUsage() {
        return new SessionSettings(libtorrent.min_memory_usage());
    }

    public static SessionSettings newHighPerformanceSeed() {
        return new SessionSettings(libtorrent.high_performance_seed());
    }

    /**
     * options for session_settings::suggest_mode.
     */
    public static enum SuggestMode {

        /**
         * the default. will not send out suggest messages.
         */
        NO_PIECE_SUGGESTIONS(session_settings.suggest_mode_t.no_piece_suggestions.swigValue()),

        /**
         * send out suggest messages for the most recent pieces that are in
         * the read cache.
         */
        SUGGEST_READ_CACHE(session_settings.suggest_mode_t.suggest_read_cache.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private SuggestMode(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static SuggestMode fromSwig(int swigValue) {
            SuggestMode[] enumValues = SuggestMode.class.getEnumConstants();
            for (SuggestMode ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }

    /**
     * the different choking algorithms available. Set
     * session_settings::choking_algorithm to one of these
     */
    public static enum ChokingAlgorithm {

        /**
         * the traditional choker with a fixed number of unchoke slots, as
         * specified by session::set_max_uploads().
         */
        FIXED_SLOTS_CHOKER(session_settings.choking_algorithm_t.fixed_slots_choker.swigValue()),

        /**
         * opens at least the number of slots as specified by
         * session::set_max_uploads() but opens up more slots if the upload
         * capacity is not saturated. This unchoker will work just like the
         * ``fixed_slot_choker`` if there's no global upload rate limit set.
         */
        AUTO_EXPAND_CHOKER(session_settings.choking_algorithm_t.auto_expand_choker.swigValue()),

        /**
         * opens up unchoke slots based on the upload rate achieved to peers.
         * The more slots that are opened, the marginal upload rate required
         * to open up another slot increases.
         */
        RATE_BASED_CHOKER(session_settings.choking_algorithm_t.rate_based_choker.swigValue()),

        /**
         * attempts to optimize download rate by finding the reciprocation
         * rate of each peer individually and prefers peers that gives the
         * highest *return on investment*. It still allocates all upload
         * capacity, but shuffles it around to the best peers first. For this
         * choker to be efficient, you need to set a global upload rate limit
         * session_settings::upload_rate_limit. For more information about
         * this choker, see the paper_.
         * <p/>
         * .. _paper: http://bittyrant.cs.washington.edu/#papers
         */
        BITTYRANT_CHOKER(session_settings.choking_algorithm_t.bittyrant_choker.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private ChokingAlgorithm(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static ChokingAlgorithm fromSwig(int swigValue) {
            ChokingAlgorithm[] enumValues = ChokingAlgorithm.class.getEnumConstants();
            for (ChokingAlgorithm ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }

    /**
     * the different choking algorithms available when seeding. Set
     * session_settings::seed_choking_algorithm to one of these
     */
    public static enum SeedChokingAlgorithm {

        /**
         * round-robins the peers that are unchoked when seeding. This
         * distributes the upload bandwidht uniformly and fairly. It minimizes
         * the ability for a peer to download everything without
         * redistributing it.
         */
        ROUND_ROBIN(session_settings.seed_choking_algorithm_t.round_robin.swigValue()),

        /**
         * unchokes the peers we can send to the fastest. This might be a bit
         * more reliable in utilizing all available capacity.
         */
        FASTEST_UPLOAD(session_settings.seed_choking_algorithm_t.fastest_upload.swigValue()),

        /**
         * prioritizes peers who have just started or are just about to finish
         * the download. The intention is to force peers in the middle of the
         * download to trade with each other.
         */
        ANTI_LEECH(session_settings.seed_choking_algorithm_t.anti_leech.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private SeedChokingAlgorithm(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static SeedChokingAlgorithm fromSwig(int swigValue) {
            SeedChokingAlgorithm[] enumValues = SeedChokingAlgorithm.class.getEnumConstants();
            for (SeedChokingAlgorithm ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }
}
