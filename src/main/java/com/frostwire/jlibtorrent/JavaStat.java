package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
final class JavaStat {

    // these are the channels we keep stats for
    public final static int UPLOAD_PAYLOAD = Stat.UPLOAD_PAYLOAD;
    public final static int UPLOAD_PROTOCOL = Stat.UPLOAD_PROTOCOL;
    public final static int DOWNLOAD_PAYLOAD = Stat.DOWNLOAD_PAYLOAD;
    public final static int DOWNLOAD_PROTOCOL = Stat.DOWNLOAD_PROTOCOL;
    public final static int UPLOAD_IP_PROTOCOL = Stat.UPLOAD_IP_PROTOCOL;
    public final static int DOWNLOAD_IP_PROTOCOL = Stat.DOWNLOAD_IP_PROTOCOL;
    public final static int NUM_CHANNELS = Stat.NUM_CHANNELS;

    private final JavaStatChannel[] stat;

    public JavaStat() {
        this.stat = new JavaStatChannel[NUM_CHANNELS];
        for (int i = 0; i < stat.length; i++) {
            stat[i] = new JavaStatChannel();
        }
    }

    public void sent(long payload, long protocol, long ip) {
        stat[UPLOAD_PAYLOAD].add(payload);
        stat[UPLOAD_PROTOCOL].add(protocol);
        stat[UPLOAD_IP_PROTOCOL].add(ip);
    }

    public void received(long payload, long protocol, long ip) {
        stat[DOWNLOAD_PAYLOAD].add(payload);
        stat[DOWNLOAD_PROTOCOL].add(protocol);
        stat[DOWNLOAD_IP_PROTOCOL].add(ip);
    }

    // should be called once every second
    public void secondTick(long tickIntervalMs) {
        for (int i = 0; i < NUM_CHANNELS; ++i)
            stat[i].secondTick(tickIntervalMs);
    }

    public long uploadRate() {
        return stat[UPLOAD_PAYLOAD].rate()
                + stat[UPLOAD_PROTOCOL].rate()
                + stat[UPLOAD_IP_PROTOCOL].rate();
    }

    public long downloadRate() {
        return stat[DOWNLOAD_PAYLOAD].rate()
                + stat[DOWNLOAD_PROTOCOL].rate()
                + stat[DOWNLOAD_IP_PROTOCOL].rate();
    }

    public long upload() {
        return stat[UPLOAD_PAYLOAD].total()
                + stat[UPLOAD_PROTOCOL].total()
                + stat[UPLOAD_IP_PROTOCOL].total();
    }

    public long download() {
        return stat[DOWNLOAD_PAYLOAD].total()
                + stat[DOWNLOAD_PROTOCOL].total()
                + stat[DOWNLOAD_IP_PROTOCOL].total();
    }

    public long uploadPayloadRate() {
        return stat[UPLOAD_PAYLOAD].rate();
    }

    public long downloadPayloadRate() {
        return stat[DOWNLOAD_PAYLOAD].rate();
    }

    public long uploadPayload() {
        return stat[UPLOAD_PAYLOAD].total();
    }

    public long downloadPayload() {
        return stat[DOWNLOAD_PAYLOAD].total();
    }

    public long uploadProtocol() {
        return stat[UPLOAD_PROTOCOL].total();
    }

    public long downloadProtocol() {
        return stat[DOWNLOAD_PROTOCOL].total();
    }

    public long uploadIPProtocol() {
        return stat[UPLOAD_IP_PROTOCOL].total();
    }

    public long downloadIPProtocol() {
        return stat[DOWNLOAD_IP_PROTOCOL].total();
    }

    public void clear() {
        for (int i = 0; i < NUM_CHANNELS; i++) {
            stat[i].clear();
        }
    }
}
