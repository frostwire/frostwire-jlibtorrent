package com.frostwire.jlibtorrent;

/**
 * This class provides a lens only functionality.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DHT {

    private final Session s;

    public DHT(Session s) {
        this.s = s;
    }

    public void start() {
        s.startDHT();
    }

    public void stop() {
        s.stopDHT();
    }

    public boolean isRunning() {
        return s.isDHTRunning();
    }

    public void get(String sha1) {
        s.dhtGetItem(new Sha1Hash(sha1));
    }

    public String put(Entry entry) {
        return s.dhtPutItem(entry).toString();
    }
}
