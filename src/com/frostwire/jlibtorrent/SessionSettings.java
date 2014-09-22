/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

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
}
