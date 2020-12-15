package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_params;
import com.frostwire.jlibtorrent.swig.settings_pack;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartPausedSessionTest {
    @Test
    public void startSessionPausedTest() {
        SessionManager sessionManager = new SessionManager();
        sessionManager.start(new SessionParams(new session_params(new settings_pack())),
                SessionHandle.PAUSED);
        assertEquals(sessionManager.isPaused(), true);
        sessionManager.stop();
    }

    @Test
    public void startSessionNonPausedTest() {
        SessionManager sessionManager = new SessionManager();
        sessionManager.start(new SessionParams(new session_params(new settings_pack())));
        assertEquals(sessionManager.isPaused(), false);
        sessionManager.stop();
    }
}
