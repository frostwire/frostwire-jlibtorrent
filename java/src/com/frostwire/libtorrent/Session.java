package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;

import java.util.Arrays;
import java.util.List;

public class Session {

    private final long session;

    public Session() {
        this.session = create();
    }

    public void startNetworking() {
        startUPnP(session);
        startNATPMP(session);
        startLSD(session);
        startDHT(session);
    }

    public void stopNetworking() {
        stopUPnP(session);
        stopNATPMP(session);
        stopLSD(session);
        stopDHT(session);
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

    private native void startUPnP(long handle);

    private native void startNATPMP(long handle);

    private native void startLSD(long handle);

    private native void startDHT(long handle);

    private native void stopUPnP(long handle);

    private native void stopNATPMP(long handle);

    private native void stopLSD(long handle);

    private native void stopDHT(long handle);

    private native Alert[] waitForAlerts(long handle, int millis);
}
