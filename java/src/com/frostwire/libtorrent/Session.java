package com.frostwire.libtorrent;

import com.frostwire.libtorrent.Alerts.Alert;

import java.util.Arrays;
import java.util.List;

public class Session {

    private final long session;

    private long upnp;
    private long natpmp;

    public Session() {
        this.session = create();
    }

    public void startNetworking() {
        upnp = startUPnP(session);
        natpmp = startNATPMP(session);

        startLSD(session);
        startDHT(session);
    }

    public List<Alert> waitForAlerts(int millis) {
        return Arrays.asList(waitForAlerts(session, millis));
    }

    @Override
    protected void finalize() throws Throwable {
        if (session != 0) {
            release(session);
        }
        super.finalize();
    }

    private native long create();

    private native void release(long handle);

    private native long startUPnP(long handle);

    private native long startNATPMP(long handle);

    private native void startLSD(long handle);

    private native void startDHT(long handle);

    private native Alert[] waitForAlerts(long handle, int millis);
}
