package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_lookup_vector;
import com.frostwire.jlibtorrent.swig.dht_routing_bucket_vector;
import com.frostwire.jlibtorrent.swig.session_status;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains session wide state and counters.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SessionStatus {

    private final session_status s;

    public SessionStatus(session_status s) {
        this.s = s;
    }

    public session_status getSwig() {
        return s;
    }

    /**
     * false as long as no incoming connections have been
     * established on the listening socket. Every time you change the listen port, this will
     * be reset to false.
     *
     * @return
     */
    public boolean hasIncomingConnections() {
        return s.getHas_incoming_connections();
    }

    /**
     * the total download and upload rates accumulated
     * from all torrents. This includes bittorrent protocol, DHT and an estimated TCP/IP
     * protocol overhead.
     *
     * @return
     */
    public int getUploadRate() {
        return s.getUpload_rate();
    }

    /**
     * the total download and upload rates accumulated
     * from all torrents. This includes bittorrent protocol, DHT and an estimated TCP/IP
     * protocol overhead.
     *
     * @return
     */
    public int getDownloadRate() {
        return s.getDownload_rate();
    }

    /**
     * the total number of bytes downloaded and
     * uploaded to and from all torrents. This also includes all the protocol overhead.
     *
     * @return
     */
    public long getTotalDownload() {
        return s.getTotal_download();
    }

    /**
     * the total number of bytes downloaded and
     * uploaded to and from all torrents. This also includes all the protocol overhead.
     *
     * @return
     */
    public long getTotalUpload() {
        return s.getTotal_upload();
    }

    /**
     * the rate of the payload
     * down- and upload only.
     *
     * @return
     */
    public int getPayloadUploadRate() {
        return s.getPayload_upload_rate();
    }

    /**
     * the rate of the payload down- and upload only.
     *
     * @return
     */
    public int getPayloadDownloadRate() {
        return s.getPayload_download_rate();
    }

    /**
     * the total transfers of payload
     * only. The payload does not include the bittorrent protocol overhead, but only parts of the
     * actual files to be downloaded.
     *
     * @return
     */
    public long getTotalPayloadDownload() {
        return s.getTotal_payload_download();
    }

    /**
     * the total transfers of payload
     * only. The payload does not include the bittorrent protocol overhead, but only parts of the
     * actual files to be downloaded.
     *
     * @return
     */
    public long getTotalPayloadUpload() {
        return s.getTotal_payload_upload();
    }

    /**
     * The estimated TCP/IP overhead.
     *
     * @return
     */
    public int getIPOverheadUploadRate() {
        return s.getIp_overhead_upload_rate();
    }

    /**
     * The estimated TCP/IP overhead.
     *
     * @return
     */
    public int getIPOverheadDownloadRate() {
        return s.getIp_overhead_download_rate();
    }

    /**
     * The estimated TCP/IP overhead.
     *
     * @return
     */
    public long getTotalIPOverheadDownload() {
        return s.getTotal_ip_overhead_download();
    }

    /**
     * The estimated TCP/IP overhead.
     *
     * @return
     */
    public long getTotalIPOverheadUpload() {
        return s.getTotal_ip_overhead_upload();
    }

    /**
     * The upload rate used by DHT traffic.
     *
     * @return
     */
    public int getDHTUploadRate() {
        return s.getDht_upload_rate();
    }

    /**
     * The download rate used by DHT traffic.
     *
     * @return
     */
    public int getDHTDownloadRate() {
        return s.getDht_download_rate();
    }

    /**
     * The total number of bytes received from the DHT.
     *
     * @return
     */
    public long getTotalDHTDownload() {
        return s.getTotal_dht_download();
    }

    /**
     * The total number of bytes sent to the DHT.
     *
     * @return
     */
    public long getTotalDHTUpload() {
        return s.getTotal_dht_upload();
    }

/*
            // the upload and download rate used by tracker traffic. Also the total number
            // of bytes sent and received to and from trackers.
            int tracker_upload_rate;
            int tracker_download_rate;
            size_type total_tracker_download;
            size_type total_tracker_upload;

            // the number of bytes that has been received more than once.
            // This can happen if a request from a peer times out and is requested from a different
            // peer, and then received again from the first one. To make this lower, increase the
            // ``request_timeout`` and the ``piece_timeout`` in the session settings.
            size_type total_redundant_bytes;

            // the number of bytes that was downloaded which later failed
            // the hash-check.
            size_type total_failed_bytes;

            // the total number of peer connections this session has. This includes
            // incoming connections that still hasn't sent their handshake or outgoing connections
            // that still hasn't completed the TCP connection. This number may be slightly higher
            // than the sum of all peers of all torrents because the incoming connections may not
            // be assigned a torrent yet.
            int num_peers;

            // the current number of unchoked peers.
            int num_unchoked;

            // the current allowed number of unchoked peers.
            int allowed_upload_slots;

            // the number of peers that are
            // waiting for more bandwidth quota from the torrent rate limiter.
            int up_bandwidth_queue;
            int down_bandwidth_queue;

            // count the number of
            // bytes the connections are waiting for to be able to send and receive.
            int up_bandwidth_bytes_queue;
            int down_bandwidth_bytes_queue;

            // tells the number of
            // seconds until the next optimistic unchoke change and the start of the next
            // unchoke interval. These numbers may be reset prematurely if a peer that is
            // unchoked disconnects or becomes notinterested.
            int optimistic_unchoke_counter;
            int unchoke_counter;

            // the number of peers currently
            // waiting on a disk write or disk read to complete before it receives or sends
            // any more data on the socket. It'a a metric of how disk bound you are.
            int disk_write_queue;
            int disk_read_queue;
    */

    /**
     * Only available when built with DHT support. It is set to 0 if the DHT isn't running.
     * <p/>
     * When the DHT is running, ``dht_nodes`` is set to the number of nodes in the routing
     * table. This number only includes *active* nodes, not cache nodes.
     * <p/>
     * These nodes are used to replace the regular nodes in the routing table in case any of them
     * becomes unresponsive.
     *
     * @return
     */
    public int getDHTNodes() {
        return s.getDht_nodes();
    }

    /**
     * Only available when built with DHT support. It is set to 0 if the DHT isn't running.
     * <p/>
     * When the DHT is running, ``dht_node_cache`` is set to the number of nodes in the node cache.
     * <p/>
     * These nodes are used to replace the regular nodes in the routing table in case any of them
     * becomes unresponsive.
     *
     * @return
     */
    public int getDHTNodeCache() {
        return s.getDht_node_cache();
    }

    /**
     * the number of torrents tracked by the DHT at the moment.
     *
     * @return
     */
    public int getDHTTorrents() {
        return s.getDht_torrents();
    }

    /**
     * An estimation of the total number of nodes in the DHT network.
     *
     * @return
     */
    public long getDHTGlobalNodes() {
        return s.getDht_global_nodes();
    }

    /**
     * a vector of the currently running DHT lookups.
     *
     * @return
     */
    public List<DHTLookup> getActiveRequests() {
        dht_lookup_vector v = s.getActive_requests();
        int size = (int) v.size();

        List<DHTLookup> l = new ArrayList<DHTLookup>(size);
        for (int i = 0; i < size; i++) {
            l.add(new DHTLookup(v.get(i)));
        }

        return l;
    }

    /**
     * contains information about every bucket in the DHT routing table.
     *
     * @return
     */
    public List<DHTRoutingBucket> getDHTRoutingTable() {
        dht_routing_bucket_vector v = s.getDht_routing_table();
        int size = (int) v.size();

        List<DHTRoutingBucket> l = new ArrayList<DHTRoutingBucket>(size);
        for (int i = 0; i < size; i++) {
            l.add(new DHTRoutingBucket(v.get(i)));
        }

        return l;
    }

    /**
     * the number of nodes allocated dynamically for a
     * particular DHT lookup. This represents roughly the amount of memory used
     * by the DHT.
     *
     * @return
     */
    public int getDHTTotalAllocations() {
        return s.getDht_total_allocations();
    }

    /**
     * statistics on the uTP sockets.
     *
     * @return
     */
    public UTPStatus getUTPStats() {
        return new UTPStatus(s.getUtp_stats());
    }

    /**
     * the number of known peers across all torrents. These are not necessarily
     * connected peers, just peers we know of.
     *
     * @return
     */
    public int getPeerlistSize() {
        return s.getPeerlist_size();
    }
}
