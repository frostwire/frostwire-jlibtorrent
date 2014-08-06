package com.frostwire.libtorrent;

public class Session {

    private final long handle;

    public Session() {
        this.handle = create();
    }

    @Override
    protected void finalize() throws Throwable {
        if (handle != 0) {
            release(handle);
        }
        super.finalize();
    }

    private native long create();

    private native void release(long handle);
}
