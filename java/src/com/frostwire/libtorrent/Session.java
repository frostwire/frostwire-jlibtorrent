package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;

import java.util.Arrays;
import java.util.List;

public class Session {

    private final long h;

    public Session() {
        this.h = create();
    }

    public void startNetworking() {
        startUPnP(h);
        startNATPMP(h);
        startLSD(h);
        startDHT(h);
    }

    public void stopNetworking() {
        stopUPnP(h);
        stopNATPMP(h);
        stopLSD(h);
        stopDHT(h);
    }

    public List<Alert> waitForAlerts(int millis) {
        return Arrays.asList(waitForAlert(h, millis));
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.h != 0) {
            release(this.h);
        }
        super.finalize();
    }

    private native long create();

    private native void release(long h);

    private native void startUPnP(long h);

    private native void startNATPMP(long h);

    private native void startLSD(long h);

    private native void startDHT(long h);

    private native void stopUPnP(long h);

    private native void stopNATPMP(long h);

    private native void stopLSD(long h);

    private native void stopDHT(long h);

    private native Alert[] waitForAlert(long h, int millis);
}
