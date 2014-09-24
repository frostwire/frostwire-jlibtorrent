package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.session_settings;

/**
 * @author gubatron
 * @author aldenml
 */
// This holds most of the session-wide settings in libtorrent. Pass this
// to session::set_settings() to change the settings, initialize it from
// session::get_settings() to get the current settings.
public final class SessionSettings {

    private final session_settings s;

    public SessionSettings(session_settings s) {
        this.s = s;
    }

    public session_settings getSwig() {
        return s;
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public int getUploadRateLimit() {
        return s.getUpload_rate_limit();
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public void setUploadRateLimit(int value) {
        s.setUpload_rate_limit(value);
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public int getDownloadRateLimit() {
        return s.getDownload_rate_limit();
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public void setDownloadRateLimit(int value) {
        s.setDownload_rate_limit(value);
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public int getLocalUploadRateLimit() {
        return s.getLocal_upload_rate_limit();
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public void setLocalUploadRateLimit(int value) {
        s.setLocal_upload_rate_limit(value);
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public int getLocalDownloadRateLimit() {
        return s.getLocal_download_rate_limit();
    }

    // sets the session-global limits of upload and download rate limits, in
    // bytes per second. The local rates refer to peers on the local network.
    // By default peers on the local network are not rate limited.
    //
    // These rate limits are only used for local peers (peers within the same
    // subnet as the client itself) and it is only used when
    // ``session_settings::ignore_limits_on_local_network`` is set to true
    // (which it is by default). These rate limits default to unthrottled,
    // but can be useful in case you want to treat local peers
    // preferentially, but not quite unthrottled.
    //
    // A value of 0 means unlimited.
    public void setLocalDownloadRateLimit(int value) {
        s.setLocal_download_rate_limit(value);
    }

    // The default values of the session settings are set for a regular
    // bittorrent client running on a desktop system. There are functions that
    // can set the session settings to pre set settings for other environments.
    // These can be used for the basis, and should be tweaked to fit your needs
    // better.
    public static SessionSettings newDefaults() {
        return new SessionSettings(new session_settings());
    }

    // ``min_memory_usage`` returns settings that will use the minimal amount of
    // RAM, at the potential expense of upload and download performance. It
    // adjusts the socket buffer sizes, disables the disk cache, lowers the send
    // buffer watermarks so that each connection only has at most one block in
    // use at any one time. It lowers the outstanding blocks send to the disk
    // I/O thread so that connections only have one block waiting to be flushed
    // to disk at any given time. It lowers the max number of peers in the peer
    // list for torrents. It performs multiple smaller reads when it hashes
    // pieces, instead of reading it all into memory before hashing.
    //
    // This configuration is inteded to be the starting point for embedded
    // devices. It will significantly reduce memory usage.
    public static SessionSettings newMinMemoryUsage() {
        return new SessionSettings(libtorrent.min_memory_usage());
    }

    public static SessionSettings newHighPerformanceSeed() {
        return new SessionSettings(libtorrent.high_performance_seed());
    }
}
