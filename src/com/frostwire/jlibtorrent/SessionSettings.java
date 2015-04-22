package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.int_int_pair;
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

    /**
     * The number of seconds the tracker connection will wait from when it
     * sent the request until it considers the tracker to have timed-out.
     * <p/>
     * Default value is 60 seconds.
     *
     * @return
     */
    public int getTrackerCompletionTimeout() {
        return s.getTracker_completion_timeout();
    }

    /**
     * The number of seconds the tracker connection will wait from when it
     * sent the request until it considers the tracker to have timed-out.
     * <p/>
     * Default value is 60 seconds.
     *
     * @param value
     */
    public void setTrackerCompletionTimeout(int value) {
        s.setTracker_completion_timeout(value);
    }

    /**
     * The number of seconds to wait to receive any data from the tracker. If
     * no data is received for this number of seconds, the tracker will be
     * considered as having timed out. If a tracker is down, this is the kind
     * of timeout that will occur.
     * <p/>
     * The default value is 20 seconds.
     *
     * @return
     */
    public int getTrackerReceiveTimeout() {
        return s.getTracker_receive_timeout();
    }

    /**
     * The number of seconds to wait to receive any data from the tracker. If
     * no data is received for this number of seconds, the tracker will be
     * considered as having timed out. If a tracker is down, this is the kind
     * of timeout that will occur.
     * <p/>
     * The default value is 20 seconds.
     *
     * @param value
     */
    public void setTrackerReceiveTimeout(int value) {
        s.setTracker_receive_timeout(value);
    }

    /**
     * The time to wait when sending a stopped message before considering a
     * tracker to have timed out. this is usually shorter, to make the client
     * quit faster.
     * <p/>
     * This is given in seconds. Default is 10 seconds.
     *
     * @return
     */
    public int getStopTrackerTimeout() {
        return s.getStop_tracker_timeout();
    }

    /**
     * The time to wait when sending a stopped message before considering a
     * tracker to have timed out. this is usually shorter, to make the client
     * quit faster.
     * <p/>
     * This is given in seconds. Default is 10 seconds.
     *
     * @param value
     */
    public void setStopTrackerTimeout(int value) {
        s.setStop_tracker_timeout(value);
    }

    // the maximum number of bytes in a tracker response. If a response size
    // passes this number it will be rejected and the connection will be
    // closed. On gzipped responses this size is measured on the uncompressed
    // data. So, if you get 20 bytes of gzip response that'll expand to 2
    // megs, it will be interrupted before the entire response has been
    // uncompressed (given your limit is lower than 2 megs). Default limit is
    // 1 megabyte.
    //int tracker_maximum_response_length;

    /**
     * Controls the number of seconds from a request is sent until it times
     * out if no piece response is returned.
     *
     * @return
     */
    public int getPieceTimeout() {
        return s.getPiece_timeout();
    }

    /**
     * Controls the number of seconds from a request is sent until it times
     * out if no piece response is returned.
     *
     * @param value
     */
    public void setPieceTimeout(int value) {
        s.setPiece_timeout(value);
    }

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

    /**
     * The number of seconds to wait for any activity on the peer wire before
     * closing the connectiong due to time out. This defaults to 120 seconds,
     * since that's what's specified in the protocol specification. After
     * half the time out, a keep alive message is sent.
     *
     * @return
     */
    public int getPeerTimeout() {
        return s.getPeer_timeout();
    }

    /**
     * The number of seconds to wait for any activity on the peer wire before
     * closing the connectiong due to time out. This defaults to 120 seconds,
     * since that's what's specified in the protocol specification. After
     * half the time out, a keep alive message is sent.
     *
     * @param value
     */
    public void setPeerTimeout(int value) {
        s.setPeer_timeout(value);
    }

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

    /**
     * The maximum times we try to connect to a peer before stop connecting
     * again. If a peer succeeds, its failcounter is reset. If a peer is
     * retrieved from a peer source (other than DHT) the failcount is
     * decremented by one, allowing another try.
     *
     * @return
     */
    public int getMaxFailcount() {
        return s.getMax_failcount();
    }

    /**
     * The maximum times we try to connect to a peer before stop connecting
     * again. If a peer succeeds, its failcounter is reset. If a peer is
     * retrieved from a peer source (other than DHT) the failcount is
     * decremented by one, allowing another try.
     *
     * @param value
     */
    public void setMaxFailcount(int value) {
        s.setMax_failcount(value);
    }

    /**
     * The number of seconds to wait to reconnect to a peer. this time is
     * multiplied with the failcount.
     *
     * @return
     */
    public int getMinReconnectTime() {
        return s.getMin_reconnect_time();
    }

    /**
     * The number of seconds to wait to reconnect to a peer. this time is
     * multiplied with the failcount.
     *
     * @param value
     */
    public void setMinReconnectTime(int value) {
        s.setMin_reconnect_time(value);
    }

    /**
     * The number of seconds to wait after a connection attempt is initiated
     * to a peer until it is considered as having timed out. The default is
     * 10 seconds. This setting is especially important in case the number of
     * half-open connections are limited, since stale half-open connection
     * may delay the connection of other peers considerably.
     *
     * @return
     */
    public int getPeerConnectTimeout() {
        return s.getPeer_connect_timeout();
    }

    /**
     * The number of seconds to wait after a connection attempt is initiated
     * to a peer until it is considered as having timed out. The default is
     * 10 seconds. This setting is especially important in case the number of
     * half-open connections are limited, since stale half-open connection
     * may delay the connection of other peers considerably.
     *
     * @param value
     */
    public void setPeerConnectTimeout(int value) {
        s.setPeer_connect_timeout(value);
    }

    /**
     * If set to true, upload, download and unchoke limits are ignored for
     * peers on the local network.
     *
     * @return
     */
    public boolean ignoreLimitsOnLocalNetwork() {
        return s.getIgnore_limits_on_local_network();
    }

    /**
     * If set to true, upload, download and unchoke limits are ignored for
     * peers on the local network.
     *
     * @param value
     */
    public void ignoreLimitsOnLocalNetwork(boolean value) {
        s.setIgnore_limits_on_local_network(value);
    }

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

    /**
     * if this is set to true, have messages will be sent to peers that
     * already have the piece. This is typically not necessary, but it might
     * be necessary for collecting statistics in some cases. Default is
     * false.
     *
     * @return
     */
    public boolean isSendRedundantHave() {
        return s.getSend_redundant_have();
    }

    /**
     * if this is set to true, have messages will be sent to peers that
     * already have the piece. This is typically not necessary, but it might
     * be necessary for collecting statistics in some cases. Default is
     * false.
     *
     * @param value
     */
    public void setSendRedundantHave(boolean value) {
        s.setSend_redundant_have(value);
    }

    /**
     * prevents outgoing bitfields from being full. If the client is seed, a
     * few bits will be set to 0, and later filled in with have-messages.
     * This is an old attempt to prevent certain ISPs from stopping people
     * from seeding.
     *
     * @return
     */
    public boolean isLazyBitfields() {
        return s.getLazy_bitfields();
    }

    /**
     * prevents outgoing bitfields from being full. If the client is seed, a
     * few bits will be set to 0, and later filled in with have-messages.
     * This is an old attempt to prevent certain ISPs from stopping people
     * from seeding.
     *
     * @param value
     */
    public void setLazyBitfields(boolean value) {
        s.setLazy_bitfields(value);
    }

    /**
     * if a peer is uninteresting and uninterested for longer than this
     * number of seconds, it will be disconnected. default is 10 minutes
     *
     * @return
     */
    public int getInactivityTimeout() {
        return s.getInactivity_timeout();
    }

    /**
     * if a peer is uninteresting and uninterested for longer than this
     * number of seconds, it will be disconnected. default is 10 minutes
     *
     * @param value
     */
    public void setInactivityTimeout(int value) {
        s.setInactivity_timeout(value);
    }

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

    /**
     * the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled. This prevents
     * fast downloads to slow medias to allocate more memory indefinitely.
     * This should be set to at least 16 kB to not completely disrupt normal
     * downloads. If it's set to 0, you will be starving the disk thread and
     * nothing will be written to disk. this is a per session setting.
     * <p/>
     * When this limit is reached, the peer connections will stop reading
     * data from their sockets, until the disk thread catches up. Setting
     * this too low will severly limit your download rate.
     *
     * @return
     */
    public int getMaxQueuedDiskBytes() {
        return s.getMax_queued_disk_bytes();
    }

    /**
     * the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled. This prevents
     * fast downloads to slow medias to allocate more memory indefinitely.
     * This should be set to at least 16 kB to not completely disrupt normal
     * downloads. If it's set to 0, you will be starving the disk thread and
     * nothing will be written to disk. this is a per session setting.
     * <p/>
     * When this limit is reached, the peer connections will stop reading
     * data from their sockets, until the disk thread catches up. Setting
     * this too low will severly limit your download rate.
     *
     * @param value
     */
    public void setMaxQueuedDiskBytes(int value) {
        s.setMax_queued_disk_bytes(value);
    }

    /**
     * this is the low watermark for the disk buffer queue. whenever the
     * number of queued bytes exceed the max_queued_disk_bytes, libtorrent
     * will wait for it to drop below this value before issuing more reads
     * from the sockets. If set to 0, the low watermark will be half of the
     * max queued disk bytes.
     *
     * @return
     */
    public int getMaxQueuedDiskBytesLowWatermark() {
        return s.getMax_queued_disk_bytes_low_watermark();
    }

    /**
     * this is the low watermark for the disk buffer queue. whenever the
     * number of queued bytes exceed the max_queued_disk_bytes, libtorrent
     * will wait for it to drop below this value before issuing more reads
     * from the sockets. If set to 0, the low watermark will be half of the
     * max queued disk bytes.
     *
     * @param value
     */
    public void setMaxQueuedDiskBytesLowWatermark(int value) {
        s.setMax_queued_disk_bytes_low_watermark(value);
    }

    /**
     * The number of seconds to wait for a handshake response from a peer. If
     * no response is received within this time, the peer is disconnected.
     *
     * @return
     */
    public int getHandshakeTimeout() {
        return s.getHandshake_timeout();
    }

    /**
     * The number of seconds to wait for a handshake response from a peer. If
     * no response is received within this time, the peer is disconnected.
     *
     * @param value
     */
    public void setHandshakeTimeout(int value) {
        s.setHandshake_timeout(value);
    }

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

    /**
     * indicates whether or not the UPnP implementation should ignore any
     * broadcast response from a device whose address is not the configured
     * router for this machine. i.e. it's a way to not talk to other people's
     * routers by mistake.
     *
     * @return
     */
    public boolean isUPnPIgnoreNonRouters() {
        return s.getUpnp_ignore_nonrouters();
    }

    /**
     * indicates whether or not the UPnP implementation should ignore any
     * broadcast response from a device whose address is not the configured
     * router for this machine. i.e. it's a way to not talk to other people's
     * routers by mistake.
     *
     * @param value
     */
    public void setUPnPIgnoreNonRouters(boolean value) {
        s.setUpnp_ignore_nonrouters(value);
    }

    /**
     * This is the minimum send buffer target size (send buffer includes
     * bytes pending being read from disk). For good and snappy seeding
     * performance, set this fairly high, to at least fit a few blocks. This
     * is essentially the initial window size which will determine how fast
     * we can ramp up the send rate.
     *
     * @return
     */
    public int getSendBufferLowWatermark() {
        return s.getSend_buffer_low_watermark();
    }

    /**
     * This is the minimum send buffer target size (send buffer includes
     * bytes pending being read from disk). For good and snappy seeding
     * performance, set this fairly high, to at least fit a few blocks. This
     * is essentially the initial window size which will determine how fast
     * we can ramp up the send rate.
     *
     * @param value
     */
    public void setSendBufferLowWatermark(int value) {
        s.setSend_buffer_low_watermark(value);
    }

    /**
     * the upper limit of the send buffer low-watermark.
     * <p/>
     * if the send buffer has fewer bytes than this, we'll read another 16kB
     * block onto it. If set too small, upload rate capacity will suffer. If
     * set too high, memory will be wasted. The actual watermark may be lower
     * than this in case the upload rate is low, this is the upper limit.
     *
     * @return
     */
    public int getSendBufferWatermark() {
        return s.getSend_buffer_watermark();
    }

    /**
     * the upper limit of the send buffer low-watermark.
     * <p/>
     * if the send buffer has fewer bytes than this, we'll read another 16kB
     * block onto it. If set too small, upload rate capacity will suffer. If
     * set too high, memory will be wasted. The actual watermark may be lower
     * than this in case the upload rate is low, this is the upper limit.
     *
     * @param value
     */
    public void setSendBufferWatermark(int value) {
        s.setSend_buffer_watermark(value);
    }

    /**
     * the current upload rate to a peer is multiplied by this factor to get
     * the send buffer watermark. The factor is specified as a percentage.
     * i.e. 50 indicates a factor of 0.5.
     * <p/>
     * This product is clamped to the send_buffer_watermark setting to not
     * exceed the max. For high speed upload, this should be set to a greater
     * value than 100. The default is 50.
     * <p/>
     * For high capacity connections, setting this higher can improve upload
     * performance and disk throughput. Setting it too high may waste RAM and
     * create a bias towards read jobs over write jobs.
     *
     * @return
     */
    public int getSendBufferWatermarkFactor() {
        return s.getSend_buffer_watermark_factor();
    }

    /**
     * the current upload rate to a peer is multiplied by this factor to get
     * the send buffer watermark. The factor is specified as a percentage.
     * i.e. 50 indicates a factor of 0.5.
     * <p/>
     * This product is clamped to the send_buffer_watermark setting to not
     * exceed the max. For high speed upload, this should be set to a greater
     * value than 100. The default is 50.
     * <p/>
     * For high capacity connections, setting this higher can improve upload
     * performance and disk throughput. Setting it too high may waste RAM and
     * create a bias towards read jobs over write jobs.
     *
     * @param value
     */
    public void setSendBufferWatermarkFactor(int value) {
        s.setSend_buffer_watermark_factor(value);
    }

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

    /**
     * Specifies if parole mode should be used. Parole mode means that peers
     * that participate in pieces that fail the hash check are put in a mode
     * where they are only allowed to download whole pieces. If the whole
     * piece a peer in parole mode fails the hash check, it is banned. If a
     * peer participates in a piece that passes the hash check, it is taken
     * out of parole mode.
     *
     * @return
     */
    public boolean useParoleMode() {
        return s.getUse_parole_mode();
    }

    /**
     * Specifies if parole mode should be used. Parole mode means that peers
     * that participate in pieces that fail the hash check are put in a mode
     * where they are only allowed to download whole pieces. If the whole
     * piece a peer in parole mode fails the hash check, it is banned. If a
     * peer participates in a piece that passes the hash check, it is taken
     * out of parole mode.
     *
     * @param value
     */
    public void useParoleMode(boolean value) {
        s.setUse_parole_mode(value);
    }

    /**
     * the disk write and read  cache. It is specified in units of 16 KiB
     * blocks. Buffers that are part of a peer's send or receive buffer also
     * count against this limit. Send and receive buffers will never be
     * denied to be allocated, but they will cause the actual cached blocks
     * to be flushed or evicted. If this is set to -1, the cache size is
     * automatically set to the amount of physical RAM available in the
     * machine divided by 8. If the amount of physical RAM cannot be
     * determined, it's set to 1024 (= 16 MiB).
     * <p/>
     * Disk buffers are allocated using a pool allocator, the number of
     * blocks that are allocated at a time when the pool needs to grow can be
     * specified in ``cache_buffer_chunk_size``. This defaults to 16 blocks.
     * Lower numbers saves memory at the expense of more heap allocations. It
     * must be at least 1.
     *
     * @return
     */
    public int getCacheSize() {
        return s.getCache_size();
    }

    /**
     * the disk write and read  cache. It is specified in units of 16 KiB
     * blocks. Buffers that are part of a peer's send or receive buffer also
     * count against this limit. Send and receive buffers will never be
     * denied to be allocated, but they will cause the actual cached blocks
     * to be flushed or evicted. If this is set to -1, the cache size is
     * automatically set to the amount of physical RAM available in the
     * machine divided by 8. If the amount of physical RAM cannot be
     * determined, it's set to 1024 (= 16 MiB).
     * <p/>
     * Disk buffers are allocated using a pool allocator, the number of
     * blocks that are allocated at a time when the pool needs to grow can be
     * specified in ``cache_buffer_chunk_size``. This defaults to 16 blocks.
     * Lower numbers saves memory at the expense of more heap allocations. It
     * must be at least 1.
     *
     * @param value
     */
    public void setCacheSize(int value) {
        s.setCache_size(value);
    }

    // this is the number of disk buffer blocks (16 kiB) that should be
    // allocated at a time. It must be at least 1. Lower number saves memory
    // at the expense of more heap allocations
    //int cache_buffer_chunk_size;

    // the number of seconds a write cache entry sits idle in the cache
    // before it's forcefully flushed to disk.
    //int cache_expiry;

    /**
     * when set to true (default), the disk cache is also used to cache
     * pieces read from disk. Blocks for writing pieces takes presedence.
     *
     * @return
     */
    public boolean useReadCache() {
        return s.getUse_read_cache();
    }

    /**
     * when set to true (default), the disk cache is also used to cache
     * pieces read from disk. Blocks for writing pieces takes presedence.
     *
     * @param value
     */
    public void useReadCache(boolean value) {
        s.setUse_read_cache(value);
    }

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
     * determines how files are opened when they're in read only mode versus
     * read and write mode. For options, see io_buffer_mode_t.
     * <p/>
     * One reason to disable caching is that it may help the operating system
     * from growing its file cache indefinitely. Since some OSes only allow
     * aligned files to be opened in unbuffered mode, It is recommended to
     * make the largest file in a torrent the first file (with offset 0) or
     * use pad files to align all files to piece boundries.
     *
     * @return
     */
    public IoBufferMode getDiskIoWriteMode() {
        return IoBufferMode.fromSwig(s.getDisk_io_write_mode());
    }

    /**
     * determines how files are opened when they're in read only mode versus
     * read and write mode. For options, see io_buffer_mode_t.
     * <p/>
     * One reason to disable caching is that it may help the operating system
     * from growing its file cache indefinitely. Since some OSes only allow
     * aligned files to be opened in unbuffered mode, It is recommended to
     * make the largest file in a torrent the first file (with offset 0) or
     * use pad files to align all files to piece boundries.
     *
     * @param value
     */
    public void setDiskIoWriteMode(IoBufferMode value) {
        s.setDisk_io_write_mode(value.getSwig());
    }

    /**
     * determines how files are opened when they're in read only mode versus
     * read and write mode. For options, see io_buffer_mode_t.
     * <p/>
     * One reason to disable caching is that it may help the operating system
     * from growing its file cache indefinitely. Since some OSes only allow
     * aligned files to be opened in unbuffered mode, It is recommended to
     * make the largest file in a torrent the first file (with offset 0) or
     * use pad files to align all files to piece boundries.
     *
     * @return
     */
    public IoBufferMode getDiskIoReadMode() {
        return IoBufferMode.fromSwig(s.getDisk_io_read_mode());
    }

    /**
     * determines how files are opened when they're in read only mode versus
     * read and write mode. For options, see io_buffer_mode_t.
     * <p/>
     * One reason to disable caching is that it may help the operating system
     * from growing its file cache indefinitely. Since some OSes only allow
     * aligned files to be opened in unbuffered mode, It is recommended to
     * make the largest file in a torrent the first file (with offset 0) or
     * use pad files to align all files to piece boundries.
     *
     * @param value
     */
    public void setDiskIoReadMode(IoBufferMode value) {
        s.setDisk_io_read_mode(value.getSwig());
    }

    /**
     * when set to true, instead of issuing multiple adjacent reads or writes
     * to the disk, allocate a larger buffer, copy all writes into it and
     * issue a single write. For reads, read into a larger buffer and copy
     * the buffer into the smaller individual read buffers afterwards. This
     * may save system calls, but will cost in additional memory allocation
     * and copying.
     *
     * @return
     */
    public boolean isCoalesceReads() {
        return s.getCoalesce_reads();
    }

    /**
     * when set to true, instead of issuing multiple adjacent reads or writes
     * to the disk, allocate a larger buffer, copy all writes into it and
     * issue a single write. For reads, read into a larger buffer and copy
     * the buffer into the smaller individual read buffers afterwards. This
     * may save system calls, but will cost in additional memory allocation
     * and copying.
     *
     * @param value
     */
    public void setCoalesceReads(boolean value) {
        s.setCoalesce_reads(value);
    }

    /**
     * when set to true, instead of issuing multiple adjacent reads or writes
     * to the disk, allocate a larger buffer, copy all writes into it and
     * issue a single write. For reads, read into a larger buffer and copy
     * the buffer into the smaller individual read buffers afterwards. This
     * may save system calls, but will cost in additional memory allocation
     * and copying.
     *
     * @return
     */
    public boolean isCoalesceWrites() {
        return s.getCoalesce_writes();
    }

    /**
     * when set to true, instead of issuing multiple adjacent reads or writes
     * to the disk, allocate a larger buffer, copy all writes into it and
     * issue a single write. For reads, read into a larger buffer and copy
     * the buffer into the smaller individual read buffers afterwards. This
     * may save system calls, but will cost in additional memory allocation
     * and copying.
     *
     * @param value
     */
    public void setCoalesceWrites(boolean value) {
        s.setCoalesce_writes(value);
    }

    /**
     * if set to something other than (0, 0) is a range of ports used to bind
     * outgoing sockets to. This may be useful for users whose router allows
     * them to assign QoS classes to traffic based on its local port. It is a
     * range instead of a single port because of the problems with failing to
     * reconnect to peers if a previous socket to that peer and port is in
     * ``TIME_WAIT`` state.
     * <p/>
     * .. warning::
     * setting outgoing ports will limit the ability to keep multiple
     * connections to the same client, even for different torrents. It is not
     * recommended to change this setting. Its main purpose is to use as an
     * escape hatch for cheap routers with QoS capability but can only
     * classify flows based on port numbers.
     *
     * @return
     */
    public Pair<Integer, Integer> getOutgoingPorts() {
        int_int_pair p = s.getOutgoing_ports();
        return new Pair<Integer, Integer>(p.getFirst(), p.getSecond());
    }

    /**
     * if set to something other than (0, 0) is a range of ports used to bind
     * outgoing sockets to. This may be useful for users whose router allows
     * them to assign QoS classes to traffic based on its local port. It is a
     * range instead of a single port because of the problems with failing to
     * reconnect to peers if a previous socket to that peer and port is in
     * ``TIME_WAIT`` state.
     * <p/>
     * .. warning::
     * setting outgoing ports will limit the ability to keep multiple
     * connections to the same client, even for different torrents. It is not
     * recommended to change this setting. Its main purpose is to use as an
     * escape hatch for cheap routers with QoS capability but can only
     * classify flows based on port numbers.
     *
     * @param value
     */
    public void setOutgoingPorts(Pair<Integer, Integer> value) {
        s.setOutgoing_ports(value.to_int_int_pair());
    }


    /**
     * `active_downloads`` controls how many active
     * downloading torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveDownloads() {
        return s.getActive_downloads();
    }

    /**
     * `active_downloads`` controls how many active
     * downloading torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveDownloads(int value) {
        s.setActive_downloads(value);
    }

    /**
     * ``active_seeds`` controls how many active seeding
     * torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveSeeds() {
        return s.getActive_seeds();
    }

    /**
     * ``active_seeds`` controls how many active seeding
     * torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveSeeds(int value) {
        s.setActive_seeds(value);
    }

    /**
     * `active_dht_limit`` limits the number of
     * torrents that will be active on the DHT. If the
     * active limit is set higher than these numbers, some torrents will be
     * "active" in the sense that they will accept incoming connections, but
     * not announce on the DHT or their trackers.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveDHTLimit() {
        return s.getActive_dht_limit();
    }

    /**
     * `active_dht_limit`` limits the number of
     * torrents that will be active on the DHT. If the
     * active limit is set higher than these numbers, some torrents will be
     * "active" in the sense that they will accept incoming connections, but
     * not announce on the DHT or their trackers.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveDHTLimit(int value) {
        s.setActive_dht_limit(value);
    }

    /**
     * `active_tracker_limit`` limits the number of
     * torrents that will be active on the tracker. If the
     * active limit is set higher than these numbers, some torrents will be
     * "active" in the sense that they will accept incoming connections, but
     * not announce on the DHT or their trackers.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveTrackerLimit() {
        return s.getActive_tracker_limit();
    }

    /**
     * `active_tracker_limit`` limits the number of
     * torrents that will be active on the tracker. If the
     * active limit is set higher than these numbers, some torrents will be
     * "active" in the sense that they will accept incoming connections, but
     * not announce on the DHT or their trackers.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveTrackerLimit(int value) {
        s.setActive_tracker_limit(value);
    }

    /**
     * ``active_lsd_limit`` is the max number of torrents to announce to the
     * local network over the local service discovery protocol. By default
     * this is 80, which is no more than one announce every 5 seconds
     * (assuming the default announce interval of 5 minutes).
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveLsdLimit() {
        return s.getActive_lsd_limit();
    }

    /**
     * ``active_lsd_limit`` is the max number of torrents to announce to the
     * local network over the local service discovery protocol. By default
     * this is 80, which is no more than one announce every 5 seconds
     * (assuming the default announce interval of 5 minutes).
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveLsdLimit(int value) {
        s.setActive_lsd_limit(value);
    }

    /**
     * ``active_limit`` is a hard limit on the number of active torrents.
     * This applies even to slow torrents.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
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
    public int getActiveLimit() {
        return s.getActive_limit();
    }

    /**
     * ``active_limit`` is a hard limit on the number of active torrents.
     * This applies even to slow torrents.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveLimit(int value) {
        s.setActive_limit(value);
    }

    /**
     * prefer seeding torrents when determining which torrents to give active
     * slots to, the default is false which gives preference to downloading
     * torrents
     *
     * @return
     */
    public boolean getAutoManagePreferSeeds() {
        return s.getAuto_manage_prefer_seeds();
    }

    /**
     * prefer seeding torrents when determining which torrents to give active
     * slots to, the default is false which gives preference to downloading
     * torrents
     *
     * @param value
     */
    public void setAutoManagePreferSeeds(boolean value) {
        s.setAuto_manage_prefer_seeds(value);
    }

    // if true, torrents without any payload transfers are not subject to the
    // ``active_seeds`` and ``active_downloads`` limits. This is intended to
    // make it more likely to utilize all available bandwidth, and avoid
    // having torrents that don't transfer anything block the active slots.
    //bool dont_count_slow_torrents;

    // the number of seconds in between recalculating which torrents to
    // activate and which ones to queue
    //int auto_manage_interval;

    // when a seeding torrent reaches either the share ratio (bytes up /
    // bytes down) or the seed time ratio (seconds as seed / seconds as
    // downloader) or the seed time limit (seconds as seed) it is considered
    // done, and it will leave room for other torrents the default value for
    // share ratio is 2 the default seed time ratio is 7, because that's a
    // common asymmetry ratio on connections
    //
    //.. note::
    //	This is an out-dated option that doesn't make much sense. It will be
    //	removed in future versions of libtorrent
    //float share_ratio_limit;

    // the seeding time / downloading time ratio limit for considering a
    // seeding torrent to have met the seed limit criteria. See queuing_.
    //float seed_time_ratio_limit;

    // the limit on the time a torrent has been an active seed (specified in
    // seconds) before it is considered having met the seed limit criteria.
    // See queuing_.
    //int seed_time_limit;

    /**
     * controls a feature where libtorrent periodically can disconnect the
     * least useful peers in the hope of connecting to better ones.
     * ``peer_turnover_interval`` controls the interval of this optimistic
     * disconnect. It defaults to every 5 minutes, and is specified in
     * seconds.
     *
     * @return
     */
    public int getPeerTurnoverInterval() {
        return s.getPeer_turnover_interval();
    }

    /**
     * controls a feature where libtorrent periodically can disconnect the
     * least useful peers in the hope of connecting to better ones.
     * ``peer_turnover_interval`` controls the interval of this optimistic
     * disconnect. It defaults to every 5 minutes, and is specified in
     * seconds.
     *
     * @param value
     */
    public void setPeerTurnoverInterval(int value) {
        s.setPeer_turnover_interval(value);
    }

    // controls a feature where libtorrent periodically can disconnect the
    // least useful peers in the hope of connecting to better ones.
    // ``peer_turnover_interval`` controls the interval of this optimistic
    // disconnect. It defaults to every 5 minutes, and is specified in
    // seconds.
    //
    // ``peer_turnover`` Is the fraction of the peers that are disconnected.
    // This is a float where 1.f represents all peers an 0 represents no
    // peers. It defaults to 4% (i.e. 0.04f)
    //
    // ``peer_turnover_cutoff`` is the cut off trigger for optimistic
    // unchokes. If a torrent has more than this fraction of its connection
    // limit, the optimistic unchoke is triggered. This defaults to 90% (i.e.
    // 0.9f).
    //float peer_turnover;
    //float peer_turnover_cutoff;

    // specifies whether libtorrent should close connections where both ends
    // have no utility in keeping the connection open. For instance if both
    // ends have completed their downloads, there's no point in keeping it
    // open. This defaults to ``true``.
    //bool close_redundant_connections;

    // the number of seconds between scrapes of queued torrents (auto managed
    // and paused torrents). Auto managed torrents that are paused, are
    // scraped regularly in order to keep track of their downloader/seed
    // ratio. This ratio is used to determine which torrents to seed and
    // which to pause.
    //int auto_scrape_interval;

    // the minimum number of seconds between any automatic scrape (regardless
    // of torrent). In case there are a large number of paused auto managed
    // torrents, this puts a limit on how often a scrape request is sent.
    //int auto_scrape_min_interval;

    /**
     * the maximum number of peers in the list of known peers. These peers
     * are not necessarily connected, so this number should be much greater
     * than the maximum number of connected peers. Peers are evicted from the
     * cache when the list grows passed 90% of this limit, and once the size
     * hits the limit, peers are no longer added to the list. If this limit
     * is set to 0, there is no limit on how many peers we'll keep in the
     * peer list.
     *
     * @return
     */
    public int getMaxPeerlistSize() {
        return s.getMax_peerlist_size();
    }

    /**
     * the maximum number of peers in the list of known peers. These peers
     * are not necessarily connected, so this number should be much greater
     * than the maximum number of connected peers. Peers are evicted from the
     * cache when the list grows passed 90% of this limit, and once the size
     * hits the limit, peers are no longer added to the list. If this limit
     * is set to 0, there is no limit on how many peers we'll keep in the
     * peer list.
     *
     * @param value
     */
    public void setMaxPeerlistSize(int value) {
        s.setMax_peerlist_size(value);
    }

    /**
     * the max peer list size used for torrents that are paused. This default
     * to the same as ``max_peerlist_size``, but can be used to save memory
     * for paused torrents, since it's not as important for them to keep a
     * large peer list.
     *
     * @return
     */
    public int getMaxPausedPeerlistSize() {
        return s.getMax_paused_peerlist_size();
    }

    /**
     * the max peer list size used for torrents that are paused. This default
     * to the same as ``max_peerlist_size``, but can be used to save memory
     * for paused torrents, since it's not as important for them to keep a
     * large peer list.
     *
     * @param value
     */
    public void setMaxPausedPeerlistSize(int value) {
        s.setMax_paused_peerlist_size(value);
    }

    // the minimum allowed announce interval for a tracker. This is specified
    // in seconds, defaults to 5 minutes and is used as a sanity check on
    // what is returned from a tracker. It mitigates hammering misconfigured
    // trackers.
    //int min_announce_interval;

    // If true, partial pieces are picked before pieces that are more rare.
    // If false, rare pieces are always prioritized, unless the number of
    // partial pieces is growing out of proportion.
    //bool prioritize_partial_pieces;

    // the number of seconds a torrent is considered active after it was
    // started, regardless of upload and download speed. This is so that
    // newly started torrents are not considered inactive until they have a
    // fair chance to start downloading.
    //int auto_manage_startup;

    // if set to true, the estimated TCP/IP overhead is drained from the rate
    // limiters, to avoid exceeding the limits with the total traffic
    //bool rate_limit_ip_overhead;

    // controls how multi tracker torrents are treated. If this is set to
    // true, all trackers in the same tier are announced to in parallel. If
    // all trackers in tier 0 fails, all trackers in tier 1 are announced as
    // well. If it's set to false, the behavior is as defined by the multi
    // tracker specification. It defaults to false, which is the same
    // behavior previous versions of libtorrent has had as well.
    //bool announce_to_all_trackers;

    // controls how multi tracker torrents are treated. When this is set to
    // true, one tracker from each tier is announced to. This is the uTorrent
    // behavior. This is false by default in order to comply with the
    // multi-tracker specification.
    //bool announce_to_all_tiers;

    // true by default. It means that trackers may be rearranged in a way
    // that udp trackers are always tried before http trackers for the same
    // hostname. Setting this to fails means that the trackers' tier is
    // respected and there's no preference of one protocol over another.
    //bool prefer_udp_trackers;

    // when this is set to true, a piece has to have been forwarded to a
    // third peer before another one is handed out. This is the traditional
    // definition of super seeding.
    //bool strict_super_seeding;

    // the number of pieces to send to a peer, when seeding, before rotating
    // in another peer to the unchoke set. It defaults to 3 pieces, which
    // means that when seeding, any peer we've sent more than this number of
    // pieces to will be unchoked in favour of a choked peer.
    //int seeding_piece_quota;

    // is a limit of the number of *sparse regions* in a torrent. A sparse
    // region is defined as a hole of pieces we have not yet downloaded, in
    // between pieces that have been downloaded. This is used as a hack for
    // windows vista which has a bug where you cannot write files with more
    // than a certain number of sparse regions. This limit is not hard, it
    // will be exceeded. Once it's exceeded, pieces that will maintain or
    // decrease the number of sparse regions are prioritized. To disable this
    // functionality, set this to 0. It defaults to 0 on all platforms except
    // windows.
    //int max_sparse_regions;

    // if lock disk cache is set to true the disk cache that's in use, will
    // be locked in physical memory, preventing it from being swapped out.
    //bool lock_disk_cache;

    // the number of piece requests we will reject in a row while a peer is
    // choked before the peer is considered abusive and is disconnected.
    //int max_rejects;

    // specifies the buffer sizes set on peer sockets. 0 (which is the
    // default) means the OS default (i.e. don't change the buffer sizes).
    // The socket buffer sizes are changed using setsockopt() with
    // SOL_SOCKET/SO_RCVBUF and SO_SNDBUFFER.
    //int recv_socket_buffer_size;
    //int send_socket_buffer_size;

    /**
     * chooses between two ways of reading back piece data from disk when its
     * complete and needs to be verified against the piece hash. This happens
     * if some blocks were flushed to the disk out of order. Everything that
     * is flushed in order is hashed as it goes along. Optimizing for speed
     * will allocate space to fit all the remaining, unhashed, part of
     * the piece, reads the data into it in a single call and hashes it. This
     * is the default. If ``optimizing_hashing_for_speed`` is false, a single
     * block will be allocated (16 kB), and the unhashed parts of the piece
     * are read, one at a time, and hashed in this single block. This is
     * appropriate on systems that are memory constrained.
     *
     * @return
     */
    public boolean optimizeHashingForSpeed() {
        return s.getOptimize_hashing_for_speed();
    }

    /**
     * chooses between two ways of reading back piece data from disk when its
     * complete and needs to be verified against the piece hash. This happens
     * if some blocks were flushed to the disk out of order. Everything that
     * is flushed in order is hashed as it goes along. Optimizing for speed
     * will allocate space to fit all the remaining, unhashed, part of
     * the piece, reads the data into it in a single call and hashes it. This
     * is the default. If ``optimizing_hashing_for_speed`` is false, a single
     * block will be allocated (16 kB), and the unhashed parts of the piece
     * are read, one at a time, and hashed in this single block. This is
     * appropriate on systems that are memory constrained.
     *
     * @param value
     */
    public void optimizeHashingForSpeed(boolean value) {
        s.setOptimize_hashing_for_speed(value);
    }

    // the number of milliseconds to sleep
    // in between disk read operations when checking torrents. This defaults
    // to 0, but can be set to higher numbers to slow down the rate at which
    // data is read from the disk while checking. This may be useful for
    // background tasks that doesn't matter if they take a bit longer, as long
    // as they leave disk I/O time for other processes.
    //int file_checks_delay_per_block;

    /**
     * tells the disk I/O thread which cache flush algorithm to use.
     * This is specified by the disk_cache_algo_t enum.
     *
     * @return
     */
    public DiskCacheAlgo getDiskCacheAlgorithm() {
        return DiskCacheAlgo.fromSwig(s.getDisk_cache_algorithm().swigValue());
    }

    /**
     * tells the disk I/O thread which cache flush algorithm to use.
     * This is specified by the disk_cache_algo_t enum.
     *
     * @param value
     */
    public void setDiskCacheAlgorithm(DiskCacheAlgo value) {
        session_settings.disk_cache_algo_t v = session_settings.disk_cache_algo_t.swigToEnum(value.getSwig());
        s.setDisk_cache_algorithm(v);
    }

    // the number of blocks to read into the read cache when a read cache
    // miss occurs. Setting this to 0 is essentially the same thing as
    // disabling read cache. The number of blocks read into the read cache is
    // always capped by the piece boundry.
    //
    // When a piece in the write cache has ``write_cache_line_size``
    // contiguous blocks in it, they will be flushed. Setting this to 1
    // effectively disables the write cache.
    //int read_cache_line_size;

    // whenever a contiguous range of this many blocks is found in the write
    // cache, it is flushed immediately
    //int write_cache_line_size;

    // the number of seconds from a disk write errors occur on a torrent
    // until libtorrent will take it out of the upload mode, to test if the
    // error condition has been fixed.
    //
    // libtorrent will only do this automatically for auto managed torrents.
    //
    // You can explicitly take a torrent out of upload only mode using
    // set_upload_mode().
    //int optimistic_disk_retry;

    // controls if downloaded pieces are verified against the piece hashes in
    // the torrent file or not. The default is false, i.e. to verify all
    // downloaded data. It may be useful to turn this off for performance
    // profiling and simulation scenarios. Do not disable the hash check for
    // regular bittorrent clients.
    //bool disable_hash_checks;

    // if this is true, disk read operations may be re-ordered based on their
    // physical disk read offset. This greatly improves throughput when
    // uploading to many peers. This assumes a traditional hard drive with a
    // read head and spinning platters. If your storage medium is a solid
    // state drive, this optimization doesn't give you an benefits
    //bool allow_reordered_disk_operations;

    // if this is true, i2p torrents are allowed to also get peers from other
    // sources than the tracker, and connect to regular IPs, not providing
    // any anonymization. This may be useful if the user is not interested in
    // the anonymization of i2p, but still wants to be able to connect to i2p
    // peers.
    //bool allow_i2p_mixed;

    // the max number of suggested piece indices received from a peer that's
    // remembered. If a peer floods suggest messages, this limit prevents
    // libtorrent from using too much RAM. It defaults to 10.
    //int max_suggest_pieces;

    // If set to true (it defaults to false), piece requests that have been
    // skipped enough times when piece messages are received, will be
    // considered lost. Requests are considered skipped when the returned
    // piece messages are re-ordered compared to the order of the requests.
    // This was an attempt to get out of dead-locks caused by BitComet peers
    // silently ignoring some requests. It may cause problems at high rates,
    // and high level of reordering in the uploading peer, that's why it's
    // disabled by default.
    //bool drop_skipped_requests;

    // determines if the disk I/O should use a normal
    // or low priority policy. This defaults to true, which means that
    // it's low priority by default. Other processes doing disk I/O will
    // normally take priority in this mode. This is meant to improve the
    // overall responsiveness of the system while downloading in the
    // background. For high-performance server setups, this might not
    // be desirable.
    //bool low_prio_disk;

    // the time between local
    // network announces for a torrent. By default, when local service
    // discovery is enabled a torrent announces itself every 5 minutes.
    // This interval is specified in seconds.
    //int local_service_announce_interval;

    // the number of seconds between announcing
    // torrents to the distributed hash table (DHT). This is specified to
    // be 15 minutes which is its default.
    //int dht_announce_interval;

    // the number of seconds libtorrent
    // will keep UDP tracker connection tokens around for. This is specified
    // to be 60 seconds, and defaults to that. The higher this value is, the
    // fewer packets have to be sent to the UDP tracker. In order for higher
    // values to work, the tracker needs to be configured to match the
    // expiration time for tokens.
    //int udp_tracker_token_expiry;

    // if this is set to true, read cache blocks
    // that are hit by peer read requests are removed from the disk cache
    // to free up more space. This is useful if you don't expect the disk
    // cache to create any cache hits from other peers than the one who
    // triggered the cache line to be read into the cache in the first place.
    //bool volatile_read_cache;

    /**
     * enables the disk cache to adjust the size
     * of a cache line generated by peers to depend on the upload rate
     * you are sending to that peer. The intention is to optimize the RAM
     * usage of the cache, to read ahead further for peers that you're
     * sending faster to.
     *
     * @return
     */
    public boolean isGuidedReadCache() {
        return s.getGuided_read_cache();
    }

    /**
     * enables the disk cache to adjust the size
     * of a cache line generated by peers to depend on the upload rate
     * you are sending to that peer. The intention is to optimize the RAM
     * usage of the cache, to read ahead further for peers that you're
     * sending faster to.
     *
     * @param value
     */
    public void setGuidedReadCache(boolean value) {
        s.setGuided_read_cache(value);
    }

    // the minimum number of seconds any read cache line is kept in the
    // cache. This defaults to one second but may be greater if
    // ``guided_read_cache`` is enabled. Having a lower bound on the time a
    // cache line stays in the cache is an attempt to avoid swapping the same
    // pieces in and out of the cache in case there is a shortage of spare
    // cache space.
    //int default_cache_min_age;

    // the number of optimistic unchoke slots to use. It defaults to 0, which
    // means automatic. Having a higher number of optimistic unchoke slots
    // mean you will find the good peers faster but with the trade-off to use
    // up more bandwidth. When this is set to 0, libtorrent opens up 20% of
    // your allowed upload slots as optimistic unchoke slots.
    //int num_optimistic_unchoke_slots;

    // this is a linux-only option and passes in the ``O_NOATIME`` to
    // ``open()`` when opening files. This may lead to some disk performance
    // improvements.
    //bool no_atime_storage;

    // the assumed reciprocation rate from peers when using the BitTyrant
    // choker. This defaults to 14 kiB/s. If set too high, you will
    // over-estimate your peers and be more altruistic while finding the true
    // reciprocation rate, if it's set too low, you'll be too stingy and
    // waste finding the true reciprocation rate.
    //int default_est_reciprocation_rate;

    // specifies how many percent the extimated reciprocation rate should be
    // increased by each unchoke interval a peer is still choking us back.
    // This defaults to 20%. This only applies to the BitTyrant choker.
    //int increase_est_reciprocation_rate;

    // specifies how many percent the estimated reciprocation rate should be
    // decreased by each unchoke interval a peer unchokes us. This default to
    // 3%. This only applies to the BitTyrant choker.
    //int decrease_est_reciprocation_rate;

    // defaults to false. If a torrent has been paused by the auto managed
    // feature in libtorrent, i.e. the torrent is paused and auto managed,
    // this feature affects whether or not it is automatically started on an
    // incoming connection. The main reason to queue torrents, is not to make
    // them unavailable, but to save on the overhead of announcing to the
    // trackers, the DHT and to avoid spreading one's unchoke slots too thin.
    // If a peer managed to find us, even though we're no in the torrent
    // anymore, this setting can make us start the torrent and serve it.
    //bool incoming_starts_queued_torrents;

    // when set to true, the downloaded counter sent to trackers will include
    // the actual number of payload bytes donwnloaded including redundant
    // bytes. If set to false, it will not include any redundany bytes
    //bool report_true_downloaded;

    // defaults to true, and controls when a block may be requested twice. If
    // this is ``true``, a block may only be requested twice when there's ay
    // least one request to every piece that's left to download in the
    // torrent. This may slow down progress on some pieces sometimes, but it
    // may also avoid downloading a lot of redundant bytes. If this is
    // ``false``, libtorrent attempts to use each peer connection to its max,
    // by always requesting something, even if it means requesting something
    // that has been requested from another peer already.
    //bool strict_end_game_mode;

    /**
     * if set to true, the local peer discovery (or Local Service Discovery)
     * will not only use IP multicast, but also broadcast its messages. This
     * can be useful when running on networks that don't support multicast.
     * Since broadcast messages might be expensive and disruptive on
     * networks, only every 8th announce uses broadcast.
     *
     * @return
     */
    public boolean broadcastLSD() {
        return s.getBroadcast_lsd();
    }

    /**
     * if set to true, the local peer discovery (or Local Service Discovery)
     * will not only use IP multicast, but also broadcast its messages. This
     * can be useful when running on networks that don't support multicast.
     * Since broadcast messages might be expensive and disruptive on
     * networks, only every 8th announce uses broadcast.
     *
     * @param value
     */
    public void broadcastLSD(boolean value) {
        s.setBroadcast_lsd(value);
    }

    // these all determines if libtorrent should attempt to make outgoing
    // connections of the specific type, or allow incoming connection. By
    // default all of them are enabled.
    //bool enable_outgoing_utp;
    //bool enable_incoming_utp;
    //bool enable_outgoing_tcp;
    //bool enable_incoming_tcp;

    // the max number of peers we accept from pex messages from a single peer.
    // this limits the number of concurrent peers any of our peers claims to
    // be connected to. If they clain to be connected to more than this, we'll
    // ignore any peer that exceeds this limit
    //int max_pex_peers;

    // determines if the storage, when loading resume data files, should
    // verify that the file modification time with the timestamps in the
    // resume data. This defaults to false, which means timestamps are taken
    // into account, and resume data is less likely to accepted (torrents are
    // more likely to be fully checked when loaded). It might be useful to
    // set this to true if your network is faster than your disk, and it
    // would be faster to redownload potentially missed pieces than to go
    // through the whole storage to look for them.
    //bool ignore_resume_timestamps;

    // determines if the storage should check the whole files when resume
    // data is incomplete or missing or whether it should simply assume we
    // don't have any of the data. By default, this is determined by the
    // existance of any of the files. By setting this setting to true, the
    // files won't be checked, but will go straight to download mode.
    //bool no_recheck_incomplete_resume;

    /**
     * defaults to false. When set to true, the client tries to hide its
     * identity to a certain degree. The peer-ID will no longer include the
     * client's fingerprint. The user-agent will be reset to an empty string.
     * It will also try to not leak other identifying information, such as
     * your local listen port, your IP etc.
     * <p/>
     * If you're using I2P, a VPN or a proxy, it might make sense to enable
     * anonymous mode.
     *
     * @return
     */
    public boolean isAnonymousMode() {
        return s.getAnonymous_mode();
    }

    /**
     * defaults to false. When set to true, the client tries to hide its
     * identity to a certain degree. The peer-ID will no longer include the
     * client's fingerprint. The user-agent will be reset to an empty string.
     * It will also try to not leak other identifying information, such as
     * your local listen port, your IP etc.
     * <p/>
     * If you're using I2P, a VPN or a proxy, it might make sense to enable
     * anonymous mode.
     *
     * @param value
     */
    public void setAnonymousMode(boolean value) {
        s.setAnonymous_mode(value);
    }

    // disables any communication that's not going over a proxy. Enabling
    // this requires a proxy to be configured as well, see
    // ``set_proxy_settings``. The listen sockets are closed, and incoming
    // connections will only be accepted through a SOCKS5 or I2P proxy (if a
    // peer proxy is set up and is run on the same machine as the tracker
    // proxy). This setting also disabled peer country lookups, since those
    // are done via DNS lookups that aren't supported by proxies.
    //bool force_proxy;

    /**
     * specifies the number of milliseconds between internal ticks. This is
     * the frequency with which bandwidth quota is distributed to peers. It
     * should not be more than one second (i.e. 1000 ms). Setting this to a
     * low value (around 100) means higher resolution bandwidth quota
     * distribution, setting it to a higher value saves CPU cycles.
     *
     * @return
     */
    public int getTickInterval() {
        return s.getTick_interval();
    }

    /**
     * specifies the number of milliseconds between internal ticks. This is
     * the frequency with which bandwidth quota is distributed to peers. It
     * should not be more than one second (i.e. 1000 ms). Setting this to a
     * low value (around 100) means higher resolution bandwidth quota
     * distribution, setting it to a higher value saves CPU cycles.
     *
     * @param value
     */
    public void setTickInterval(int value) {
        s.setTick_interval(value);
    }

    // specifies whether downloads from web seeds is reported to the
    // tracker or not. Defaults to on
    //bool report_web_seed_downloads;

    // specifies the target share ratio for share mode torrents. This
    // defaults to 3, meaning we'll try to upload 3 times as much as we
    // download. Setting this very high, will make it very conservative and
    // you might end up not downloading anything ever (and not affecting your
    // share ratio). It does not make any sense to set this any lower than 2.
    // For instance, if only 3 peers need to download the rarest piece, it's
    // impossible to download a single piece and upload it more than 3 times.
    // If the share_mode_target is set to more than 3, nothing is downloaded.
    //int share_mode_target;

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
     * sets the rate limit on the DHT. This is specified in bytes per second
     * and defaults to 4000. For busy boxes with lots of torrents that
     * requires more DHT traffic, this should be raised.
     *
     * @return
     */
    public int getDHTUploadRateLimit() {
        return s.getDht_upload_rate_limit();
    }

    /**
     * sets the rate limit on the DHT. This is specified in bytes per second
     * and defaults to 4000. For busy boxes with lots of torrents that
     * requires more DHT traffic, this should be raised.
     *
     * @param value
     */
    public void getDHTUploadRateLimit(int value) {
        s.setDht_upload_rate_limit(value);
    }

    /**
     * the max number of unchoked peers in the session. The number of unchoke
     * slots may be ignored depending on what ``choking_algorithm`` is set
     * to. A value of -1 means infinite.
     *
     * @return
     */
    public int getUnchokeSlotsLimit() {
        return s.getUnchoke_slots_limit();
    }

    /**
     * the max number of unchoked peers in the session. The number of unchoke
     * slots may be ignored depending on what ``choking_algorithm`` is set
     * to. A value of -1 means infinite.
     *
     * @param value
     */
    public void setUnchokeSlotsLimit(int value) {
        s.setUnchoke_slots_limit(value);
    }

    /**
     * sets the maximum number of half-open connections libtorrent will have
     * when connecting to peers. A half-open connection is one where
     * connect() has been called, but the connection still hasn't been
     * established (nor failed). Windows XP Service Pack 2 sets a default,
     * system wide, limit of the number of half-open connections to 10. So,
     * this limit can be used to work nicer together with other network
     * applications on that system. The default is to have no limit, and
     * passing -1 as the limit, means to have no limit. When limiting the
     * number of simultaneous connection attempts, peers will be put in a
     * queue waiting for their turn to get connected.
     *
     * @return
     */
    public int getHalfOpenLimit() {
        return s.getHalf_open_limit();
    }

    /**
     * sets the maximum number of half-open connections libtorrent will have
     * when connecting to peers. A half-open connection is one where
     * connect() has been called, but the connection still hasn't been
     * established (nor failed). Windows XP Service Pack 2 sets a default,
     * system wide, limit of the number of half-open connections to 10. So,
     * this limit can be used to work nicer together with other network
     * applications on that system. The default is to have no limit, and
     * passing -1 as the limit, means to have no limit. When limiting the
     * number of simultaneous connection attempts, peers will be put in a
     * queue waiting for their turn to get connected.
     *
     * @param value
     */
    public void setHalgOpenLimit(int value) {
        s.setHalf_open_limit(value);
    }

    /**
     * sets a global limit on the number of connections opened. The number of
     * connections is set to a hard minimum of at least two per torrent, so
     * if you set a too low connections limit, and open too many torrents,
     * the limit will not be met.
     *
     * @return
     */
    public int getConnectionsLimit() {
        return s.getConnections_limit();
    }

    /**
     * sets a global limit on the number of connections opened. The number of
     * connections is set to a hard minimum of at least two per torrent, so
     * if you set a too low connections limit, and open too many torrents,
     * the limit will not be met.
     *
     * @param value
     */
    public void setConnectionsLimit(int value) {
        s.setConnections_limit(value);
    }

    /**
     * the number of extra incoming connections allowed temporarily, in order
     * to support replacing peers.
     *
     * @return
     */
    public int getConnectionsSlack() {
        return s.getConnections_slack();
    }

    /**
     * the number of extra incoming connections allowed temporarily, in order
     * to support replacing peers.
     *
     * @param value
     */
    public void setConnectionsSlack(int value) {
        s.setConnections_slack(value);
    }

    // the target delay for uTP sockets in milliseconds. A high value will
    // make uTP connections more aggressive and cause longer queues in the
    // upload bottleneck. It cannot be too low, since the noise in the
    // measurements would cause it to send too slow. The default is 50
    // milliseconds.
    //int utp_target_delay;

    // the number of bytes the uTP congestion window can increase at the most
    // in one RTT. This defaults to 300 bytes. If this is set too high, the
    // congestion controller reacts too hard to noise and will not be stable,
    // if it's set too low, it will react slow to congestion and not back off
    // as fast.
    //int utp_gain_factor;

    // the shortest allowed uTP socket timeout, specified in milliseconds.
    // This defaults to 500 milliseconds. The timeout depends on the RTT of
    // the connection, but is never smaller than this value. A connection
    // times out when every packet in a window is lost, or when a packet is
    // lost twice in a row (i.e. the resent packet is lost as well).
    //
    // The shorter the timeout is, the faster the connection will recover
    // from this situation, assuming the RTT is low enough.
    //int utp_min_timeout;

    // the number of SYN packets that are sent (and timed out) before
    // giving up and closing the socket.
    //int utp_syn_resends;

    // the number of resent packets sent on a closed socket before giving up
    //int utp_fin_resends;

    // the number of times a packet is sent (and lossed or timed out)
    // before giving up and closing the connection.
    //int utp_num_resends;

    // the number of milliseconds of timeout for the initial SYN packet for
    // uTP connections. For each timed out packet (in a row), the timeout is
    // doubled.
    //int utp_connect_timeout;

    /**
     * controls if the uTP socket manager is allowed to increase the socket
     * buffer if a network interface with a large MTU is used (such as
     * loopback or ethernet jumbo frames). This defaults to true and might
     * improve uTP throughput. For RAM constrained systems, disabling this
     * typically saves around 30kB in user space and probably around 400kB in
     * kernel socket buffers (it adjusts the send and receive buffer size on
     * the kernel socket, both for IPv4 and IPv6).
     *
     * @return
     */
    public boolean isUtpDynamicSockBuf() {
        return s.getUtp_dynamic_sock_buf();
    }

    /**
     * controls if the uTP socket manager is allowed to increase the socket
     * buffer if a network interface with a large MTU is used (such as
     * loopback or ethernet jumbo frames). This defaults to true and might
     * improve uTP throughput. For RAM constrained systems, disabling this
     * typically saves around 30kB in user space and probably around 400kB in
     * kernel socket buffers (it adjusts the send and receive buffer size on
     * the kernel socket, both for IPv4 and IPv6).
     *
     * @param value
     */
    public void setUtpDynamicSockBuf(boolean value) {
        s.setUtp_dynamic_sock_buf(value);
    }

    // controls how the congestion window is changed when a packet loss is
    // experienced. It's specified as a percentage multiplier for ``cwnd``.
    // By default it's set to 50 (i.e. cut in half). Do not change this value
    // unless you know what you're doing. Never set it higher than 100.
    //int utp_loss_multiplier;

    /**
     * determines how to treat TCP connections when there are uTP
     * connections. Since uTP is designed to yield to TCP, there's an
     * inherent problem when using swarms that have both TCP and uTP
     * connections. If nothing is done, uTP connections would often be
     * starved out for bandwidth by the TCP connections. This mode is
     * ``prefer_tcp``. The ``peer_proportional`` mode simply looks at the
     * current throughput and rate limits all TCP connections to their
     * proportional share based on how many of the connections are TCP. This
     * works best if uTP connections are not rate limited by the global rate
     * limiter, see rate_limit_utp.
     * <p/>
     * see bandwidth_mixed_algo_t for options.
     *
     * @return
     */
    public BandwidthMixedAlgo getMixedModeAlgorithm() {
        return BandwidthMixedAlgo.fromSwig(s.getMixed_mode_algorithm());
    }

    /**
     * determines how to treat TCP connections when there are uTP
     * connections. Since uTP is designed to yield to TCP, there's an
     * inherent problem when using swarms that have both TCP and uTP
     * connections. If nothing is done, uTP connections would often be
     * starved out for bandwidth by the TCP connections. This mode is
     * ``prefer_tcp``. The ``peer_proportional`` mode simply looks at the
     * current throughput and rate limits all TCP connections to their
     * proportional share based on how many of the connections are TCP. This
     * works best if uTP connections are not rate limited by the global rate
     * limiter, see rate_limit_utp.
     * <p/>
     * see bandwidth_mixed_algo_t for options.
     *
     * @param value
     */
    public void setMixedModeAlgorithm(BandwidthMixedAlgo value) {
        s.setMixed_mode_algorithm(value.getSwig());
    }

    // determines if uTP connections should be throttled by the global rate
    // limiter or not. By default they are.
    //bool rate_limit_utp;

    // the value passed in to listen() for the listen socket. It is the
    // number of outstanding incoming connections to queue up while we're not
    // actively waiting for a connection to be accepted. The default is 5
    // which should be sufficient for any normal client. If this is a high
    // performance server which expects to receive a lot of connections, or
    // used in a simulator or test, it might make sense to raise this number.
    // It will not take affect until listen_on() is called again (or for the
    // first time).
    //int listen_queue_size;

    /**
     * if true, the ``&ip=`` argument in tracker requests (unless otherwise
     * specified) will be set to the intermediate IP address, if the user is
     * double NATed. If ther user is not double NATed, this option has no
     * affect.
     *
     * @return
     */
    public boolean announceDoubleNAT() {
        return s.getAnnounce_double_nat();
    }

    /**
     * if true, the ``&ip=`` argument in tracker requests (unless otherwise
     * specified) will be set to the intermediate IP address, if the user is
     * double NATed. If ther user is not double NATed, this option has no
     * affect.
     *
     * @param value
     */
    public void announceDoubleNAT(boolean value) {
        s.setAnnounce_double_nat(value);
    }

    /**
     * the number of peers to try to connect to immediately when the first
     * tracker response is received for a torrent. This is a boost to given
     * to new torrents to accelerate them starting up. The normal connect
     * scheduler is run once every second, this allows peers to be connected
     * immediately instead of waiting for the session tick to trigger
     * connections.
     *
     * @return
     */
    public int getTorrentConnectBoost() {
        return s.getTorrent_connect_boost();
    }

    /**
     * the number of peers to try to connect to immediately when the first
     * tracker response is received for a torrent. This is a boost to given
     * to new torrents to accelerate them starting up. The normal connect
     * scheduler is run once every second, this allows peers to be connected
     * immediately instead of waiting for the session tick to trigger
     * connections.
     *
     * @param value
     */
    public void setTorrentConnectBoost(int value) {
        s.setTorrent_connect_boost(value);
    }

    /**
     * determines if seeding (and finished) torrents should attempt to make
     * outgoing connections or not. By default this is true. It may be set to
     * false in very specific applications where the cost of making outgoing
     * connections is high, and there are no or small benefits of doing so.
     * For instance, if no nodes are behind a firewall or a NAT, seeds don't
     * need to make outgoing connections.
     *
     * @return
     */
    public boolean isSeedingOutgoingConnections() {
        return s.getSeeding_outgoing_connections();
    }

    /**
     * determines if seeding (and finished) torrents should attempt to make
     * outgoing connections or not. By default this is true. It may be set to
     * false in very specific applications where the cost of making outgoing
     * connections is high, and there are no or small benefits of doing so.
     * For instance, if no nodes are behind a firewall or a NAT, seeds don't
     * need to make outgoing connections.
     *
     * @param value
     */
    public void setSeedingOutgoingConnections(boolean value) {
        s.setSeeding_outgoing_connections(value);
    }

    // if true (which is the default), libtorrent will not connect to any
    // peers on priviliged ports (<= 1023). This can mitigate using
    // bittorrent swarms for certain DDoS attacks.
    //bool no_connect_privileged_ports;

    // the maximum number of alerts queued up internally. If alerts are not
    // popped, the queue will eventually fill up to this level. This defaults
    // to 1000.
    //int alert_queue_size;

    /**
     * the maximum allowed size (in bytes) to be received
     * by the metadata extension, i.e. magnet links. It defaults to 1 MiB.
     *
     * @return
     */
    public int getMaxMetadataSize() {
        return s.getMax_metadata_size();
    }

    /**
     * the maximum allowed size (in bytes) to be received
     * by the metadata extension, i.e. magnet links. It defaults to 1 MiB.
     *
     * @param value
     */
    public void setMaxMetadataSize(int value) {
        s.setMax_metadata_size(value);
    }

    // true by default, which means the number of connection attempts per
    // second may be limited to below the ``connection_speed``, in case we're
    // close to bump up against the limit of number of connections. The
    // intention of this setting is to more evenly distribute our connection
    // attempts over time, instead of attempting to connect in batches, and
    // timing them out in batches.
    //bool smooth_connects;

    // defaults to false. When set to true, web connections will include a
    // user-agent with every request, as opposed to just the first request in
    // a connection.
    //bool always_send_user_agent;

    // defaults to true. It determines whether the IP filter applies to
    // trackers as well as peers. If this is set to false, trackers are
    // exempt from the IP filter (if there is one). If no IP filter is set,
    // this setting is irrelevant.
    //bool apply_ip_filter_to_trackers;

    // used to avoid starvation of read jobs in the disk I/O thread. By
    // default, read jobs are deferred, sorted by physical disk location and
    // serviced once all write jobs have been issued. In scenarios where the
    // download rate is enough to saturate the disk, there's a risk the read
    // jobs will never be serviced. With this setting, every *x* write job,
    // issued in a row, will instead pick one read job off of the sorted
    // queue, where *x* is ``read_job_every``.
    //int read_job_every;

    // defaults to true and will attempt to optimize disk reads by giving the
    // operating system heads up of disk read requests as they are queued in
    // the disk job queue. This gives a significant performance boost for
    // seeding.
    //bool use_disk_read_ahead;

    // determines whether or not to lock files which libtorrent is
    // downloading to or seeding from. This is implemented using
    // ``fcntl(F_SETLK)`` on unix systems and by not passing in
    // ``SHARE_READ`` and ``SHARE_WRITE`` on windows. This might prevent 3rd
    // party processes from corrupting the files under libtorrent's feet.
    //bool lock_files;

    /**
     * Sets the listen port for SSL connections. If this is set to 0, no SSL
     * listen port is opened. Otherwise a socket is opened on this port. This
     * setting is only taken into account when opening the regular listen
     * port, and won't re-open the listen socket simply by changing this
     * setting.
     * <p/>
     * if this is 0, outgoing SSL connections are disabled.
     * <p/>
     * It defaults to port 4433.
     *
     * @return
     */
    public int getSslListen() {
        return s.getSsl_listen();
    }

    /**
     * Sets the listen port for SSL connections. If this is set to 0, no SSL
     * listen port is opened. Otherwise a socket is opened on this port. This
     * setting is only taken into account when opening the regular listen
     * port, and won't re-open the listen socket simply by changing this
     * setting.
     * <p/>
     * if this is 0, outgoing SSL connections are disabled.
     * <p/>
     * It defaults to port 4433.
     *
     * @param value
     */
    public void setSslListen(int value) {
        s.setSsl_listen(value);
    }

    // ``tracker_backoff`` determines how aggressively to back off from
    // retrying failing trackers. This value determines *x* in the following
    // formula, determining the number of seconds to wait until the next
    // retry:
    //
    // 	delay = 5 + 5 * x / 100 * fails^2
    //
    // It defaults to 250.
    //
    // This setting may be useful to make libtorrent more or less aggressive
    // in hitting trackers.
    //
    //int tracker_backoff;

    // enables banning web seeds. By default, web seeds that send corrupt
    // data are banned.
    //bool ban_web_seeds;

    // specifies the max number of bytes to receive into RAM buffers when
    // downloading stuff over HTTP. Specifically when specifying a URL to a
    // .torrent file when adding a torrent or when announcing to an HTTP
    // tracker. The default is 2 MiB.
    //int max_http_recv_buffer_size;

    // enables or disables the share mode extension. This is enabled by
    // default.
    //bool support_share_mode;

    // enables or disables the merkle tree torrent support. This is enabled
    // by default.
    //bool support_merkle_torrents;

    // enables or disables reporting redundant bytes to the tracker. This is
    // enabled by default.
    //bool report_redundant_bytes;

    // the version string to advertise for this client in the peer protocol
    // handshake. If this is empty the user_agent is used
    //std::string handshake_client_version;

    // if this is true, the disk cache uses a pool allocator for disk cache
    // blocks. Enabling this improves performance of the disk cache with the
    // side effect that the disk cache is less likely and slower at returning
    // memory to the kernel when cache pressure is low.
    //bool use_disk_cache_pool;

    /**
     * the download rate limit for a torrent to be considered
     * active by the queuing mechanism. A torrent whose download rate is less
     * than ``inactive_down_rate`` and whose upload rate is less than
     * ``inactive_up_rate`` for ``auto_manage_startup`` seconds, is
     * considered inactive, and another queued torrent may be startert.
     * This logic is disabled if ``dont_count_slow_torrents`` is false.
     *
     * @return
     */
    public int getInactiveDownRate() {
        return s.getInactive_down_rate();
    }

    /**
     * the download rate limit for a torrent to be considered
     * active by the queuing mechanism. A torrent whose download rate is less
     * than ``inactive_down_rate`` and whose upload rate is less than
     * ``inactive_up_rate`` for ``auto_manage_startup`` seconds, is
     * considered inactive, and another queued torrent may be startert.
     * This logic is disabled if ``dont_count_slow_torrents`` is false.
     *
     * @param value
     */
    public void setInactiveDownRate(int value) {
        s.setInactive_down_rate(value);
    }

    /**
     * the upload rate limit for a torrent to be considered
     * active by the queuing mechanism. A torrent whose download rate is less
     * than ``inactive_down_rate`` and whose upload rate is less than
     * ``inactive_up_rate`` for ``auto_manage_startup`` seconds, is
     * considered inactive, and another queued torrent may be startert.
     * This logic is disabled if ``dont_count_slow_torrents`` is false.
     *
     * @return
     */
    public int getInactiveUpRate() {
        return s.getInactive_up_rate();
    }

    /**
     * the upload rate limit for a torrent to be considered
     * active by the queuing mechanism. A torrent whose download rate is less
     * than ``inactive_down_rate`` and whose upload rate is less than
     * ``inactive_up_rate`` for ``auto_manage_startup`` seconds, is
     * considered inactive, and another queued torrent may be startert.
     * This logic is disabled if ``dont_count_slow_torrents`` is false.
     *
     * @param value
     */
    public void setInactiveUpRate(int value) {
        s.setInactive_up_rate(value);
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
     * Options for {@link #setSuggestMode}.
     */
    public enum SuggestMode {

        /**
         * The default. will not send out suggest messages.
         */
        NO_PIECE_SUGGESTIONS(session_settings.suggest_mode_t.no_piece_suggestions.swigValue()),

        /**
         * Send out suggest messages for the most recent pieces that are in
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
    public enum ChokingAlgorithm {

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
    public enum SeedChokingAlgorithm {

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

    /**
     * the buffer modes to use for reading and writing. Set
     * session_settings::disk_io_read_mode and disk_io_write_mode to one of
     * these.
     */
    public enum IoBufferMode {

        /**
         * This is the default and files are opened normally, with the OS
         * caching reads and writes.
         */
        ENABLE_OS_CACHE(session_settings.io_buffer_mode_t.enable_os_cache.swigValue()),

        /**
         * This will open files in unbuffered mode for files where every read
         * and write would be sector aligned. Using aligned disk offsets is a
         * requirement on some operating systems.
         */
        DISABLE_OS_CACHE_FOR_ALIGNED_FILES(session_settings.io_buffer_mode_t.disable_os_cache_for_aligned_files.swigValue()),

        /**
         * This opens all files in unbuffered mode (if allowed by the
         * operating system). Linux and Windows, for instance, require disk
         * offsets to be sector aligned, and in those cases, this option is
         * the same as ``disable_os_caches_for_aligned_files``.
         */
        DISABLE_OS_CACHE(session_settings.io_buffer_mode_t.disable_os_cache.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private IoBufferMode(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static IoBufferMode fromSwig(int swigValue) {
            IoBufferMode[] enumValues = IoBufferMode.class.getEnumConstants();
            for (IoBufferMode ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }

    /**
     * the disk cache algorithms available. Set
     * session_settings::disk_cache_algorithm to one of these.
     */
    public enum DiskCacheAlgo {

        /**
         * This flushes the entire piece, in the write cache, that was least
         * recently written to.
         */
        LRU(session_settings.disk_cache_algo_t.lru.swigValue()),

        /**
         * will flush the largest sequences of contiguous blocks from the
         * write cache, regarless of the piece's last use time.
         */
        LARGEST_CONTIGUOUS(session_settings.disk_cache_algo_t.largest_contiguous.swigValue()),

        /**
         * will prioritize flushing blocks that will avoid having to read them
         * back in to verify the hash of the piece once it's done. This is
         * especially useful for high throughput setups, where reading from
         * the disk is especially expensive.
         */
        AVOID_READBACK(session_settings.disk_cache_algo_t.avoid_readback.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private DiskCacheAlgo(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static DiskCacheAlgo fromSwig(int swigValue) {
            DiskCacheAlgo[] enumValues = DiskCacheAlgo.class.getEnumConstants();
            for (DiskCacheAlgo ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            throw new IllegalArgumentException("No enum " + DiskCacheAlgo.class + " with swig value " + swigValue);
        }
    }

    /**
     * the options for session_settings::mixed_mode_algorithm.
     */
    public enum BandwidthMixedAlgo {

        /**
         * disables the mixed mode bandwidth balancing
         */
        PREFER_TCP(session_settings.bandwidth_mixed_algo_t.prefer_tcp.swigValue()),

        /**
         * does not throttle uTP, throttles TCP to the same proportion
         * of throughput as there are TCP connections
         */
        PEER_PROPORTIONAL(session_settings.bandwidth_mixed_algo_t.peer_proportional.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private BandwidthMixedAlgo(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static BandwidthMixedAlgo fromSwig(int swigValue) {
            BandwidthMixedAlgo[] enumValues = BandwidthMixedAlgo.class.getEnumConstants();
            for (BandwidthMixedAlgo ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }
}
