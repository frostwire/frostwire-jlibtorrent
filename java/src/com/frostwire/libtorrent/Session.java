package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;

import java.util.Arrays;
import java.util.List;

public class Session {

    private final long s;

    public Session() {
        this.s = create();
    }

    public void startNetworking() {
        startUPnP(s);
        startNATPMP(s);
        startLSD(s);
        startDHT(s);
    }

    public void stopNetworking() {
        stopUPnP(s);
        stopNATPMP(s);
        stopLSD(s);
        stopDHT(s);
    }

    public List<Alert> waitForAlerts(int millis) {
        return Arrays.asList(waitForAlert(s, millis));
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.s != 0) {
            release(this.s);
        }
        super.finalize();
    }

    private native long create();

    private native void release(long s);

    private native void startUPnP(long s);

    private native void startNATPMP(long s);

    private native void startLSD(long s);

    private native void startDHT(long s);

    private native void stopUPnP(long s);

    private native void stopNATPMP(long s);

    private native void stopLSD(long s);

    private native void stopDHT(long s);

    private native Alert[] waitForAlert(long s, int millis);
}
