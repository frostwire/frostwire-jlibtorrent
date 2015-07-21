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

    public void add(JavaStat s) {
        for (int i = 0; i < NUM_CHANNELS; i++) {
            stat[i].add(s.stat[i]);
        }
    }

    public void sentSyn(boolean ipv6) {
        stat[UPLOAD_IP_PROTOCOL].add(ipv6 ? 60 : 40);
    }

    public void receivedSynack(boolean ipv6) {
        // we received SYN-ACK and also sent ACK back
        stat[DOWNLOAD_IP_PROTOCOL].add(ipv6 ? 60 : 40);
        stat[UPLOAD_IP_PROTOCOL].add(ipv6 ? 60 : 40);
    }

    public void receivedBytes(int bytesPayload, int bytesProtocol) {
        stat[DOWNLOAD_PAYLOAD].add(bytesPayload);
        stat[DOWNLOAD_PROTOCOL].add(bytesProtocol);
    }

    public void sentBytes(int bytesPayload, int bytesProtocol) {
        stat[UPLOAD_PAYLOAD].add(bytesPayload);
        stat[UPLOAD_PROTOCOL].add(bytesProtocol);
    }

    // and IP packet was received or sent
    // account for the overhead caused by it
    public void trancieveIPPacket(int bytesTransferred, boolean ipv6) {
        // one TCP/IP packet header for the packet
        // sent or received, and one for the ACK
        // The IPv4 header is 20 bytes
        // and IPv6 header is 40 bytes
        int header = (ipv6 ? 40 : 20) + 20;
        int mtu = 1500;
        int packetSize = mtu - header;
        int overhead = Math.max(1, (bytesTransferred + packetSize - 1) / packetSize) * header;
        stat[DOWNLOAD_IP_PROTOCOL].add(overhead);
        stat[UPLOAD_IP_PROTOCOL].add(overhead);
    }

    public long uploadIPOverhead() {
        return stat[UPLOAD_IP_PROTOCOL].counter();
    }

    public long downloadIPOverhead() {
        return stat[DOWNLOAD_IP_PROTOCOL].counter();
    }

    // should be called once every second
    public void secondTick(int tickIntervalMs) {
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

    public long total_protocol_download() {
        return stat[DOWNLOAD_PROTOCOL].total();
    }

    public long totalTransfer(int channel) {
        return stat[channel].total();
    }

    public long transferRate(int channel) {
        return stat[channel].rate();
    }

    // this is used to offset the statistics when a
    // peer_connection is opened and have some previous
    // transfers from earlier connections.
    public void addStat(long downloaded, long uploaded) {
        stat[DOWNLOAD_PAYLOAD].offset(downloaded);
        stat[UPLOAD_PAYLOAD].offset(uploaded);
    }

    public long lastPayloadDownloaded() {
        return stat[DOWNLOAD_PAYLOAD].counter();
    }

    public long lastPayloadUploaded() {
        return stat[UPLOAD_PAYLOAD].counter();
    }

    public long lastProtocolDownloaded() {
        return stat[DOWNLOAD_PROTOCOL].counter();
    }

    public long lastProtocolUploaded() {
        return stat[UPLOAD_PROTOCOL].counter();
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
