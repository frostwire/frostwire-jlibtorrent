package com.frostwire.libtorrent;

public class Session {

    private final long session;

    private long upnp;
    private long natpmp;

    public Session() {
        this.session = create();
    }

    public void startNetworking() {
        this.upnp = startUPnP(session);
        this.natpmp = startNATPMP(session);

        this.startLSD(session);
        this.startDHT(session);
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
}
