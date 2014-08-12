package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;

import java.util.Arrays;
import java.util.List;

public class Session {

    public Session() {
        create();
    }

    public void startNetworking() {
        startUPnP();
        startNATPMP();
        startLSD();
        startDHT();
    }

    public void stopNetworking() {
        stopUPnP();
        stopNATPMP();
        stopLSD();
        stopDHT();
    }

    public List<Alert> waitForAlerts(int millis) {
        return Arrays.asList(waitForAlert(millis));
    }

    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    private native long create();

    private native void release();

    private native void startUPnP();

    private native void startNATPMP();

    private native void startLSD();

    private native void startDHT();

    private native void stopUPnP();

    private native void stopNATPMP();

    private native void stopLSD();

    private native void stopDHT();

    private native Alert[] waitForAlert(int millis);
}
