package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_settings;

/**
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
}
