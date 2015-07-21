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

    public long totalUpload() {
        return stat[UPLOAD_PAYLOAD].total()
                + stat[UPLOAD_PROTOCOL].total()
                + stat[UPLOAD_IP_PROTOCOL].total();
    }

    public long totalDownload() {
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

    public long totalPayloadUpload() {
        return stat[UPLOAD_PAYLOAD].total();
    }

    public long totalPayloadDownload() {
        return stat[DOWNLOAD_PAYLOAD].total();
    }

    public long totalProtocolUpload() {
        return stat[UPLOAD_PROTOCOL].total();
    }

    public long totalProtocolDownload() {
        return stat[DOWNLOAD_PROTOCOL].total();
    }

    public long totalIPProtocolUpload() {
        return stat[UPLOAD_IP_PROTOCOL].total();
    }

    public long totalIPProtocolDownload() {
        return stat[DOWNLOAD_IP_PROTOCOL].total();
    }

    public long totalTransfer(int channel) {
        return stat[channel].total();
    }

    public long transferRate(int channel) {
        return stat[channel].rate();
    }

    public void clear() {
        for (int i = 0; i < NUM_CHANNELS; i++) {
            stat[i].clear();
        }
    }

    public JavaStatChannel channel(int i) {
        return stat[i];
    }
}
