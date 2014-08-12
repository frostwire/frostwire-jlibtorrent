package com.frostwire.libtorrent;

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

    public void waitForAlerts(int millis) {
        waitForAlerts(session, millis);
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

    private native void waitForAlerts(long handle, int millis);
}
