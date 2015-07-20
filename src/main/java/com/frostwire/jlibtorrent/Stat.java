package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.stat;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Stat {

    public final static int UPLOAD_PAYLOAD = stat.upload_payload;
    public final static int UPLOAD_PROTOCOL = stat.upload_protocol;
    public final static int DOWNLOAD_PAYLOAD = stat.download_payload;
    public final static int DOWNLOAD_PROTOCOL = stat.download_protocol;
    public final static int UPLOAD_IP_PROTOCOL = stat.upload_ip_protocol;
    public final static int DOWNLOAD_IP_PROTOCOL = stat.download_ip_protocol;
    public final static int NUM_CHANNELS = stat.num_channels;

    private final stat s;

    public Stat(stat s) {
        this.s = s;
    }

    public stat getSwig() {
        return s;
    }
}
