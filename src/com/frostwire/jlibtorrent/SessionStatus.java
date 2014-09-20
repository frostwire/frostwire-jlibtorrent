/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_status;

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

    // false as long as no incoming connections have been
    // established on the listening socket. Every time you change the listen port, this will
    // be reset to false.
    public boolean hasIncomingConnections() {
        return s.getHas_incoming_connections();
    }

    // the total download and upload rates accumulated
    // from all torrents. This includes bittorrent protocol, DHT and an estimated TCP/IP
    // protocol overhead.
    public int getUploadRate() {
        return s.getUpload_rate();
    }

    // the total download and upload rates accumulated
    // from all torrents. This includes bittorrent protocol, DHT and an estimated TCP/IP
    // protocol overhead.
    public int getDownloadRate() {
        return s.getDownload_rate();
    }

    // the total number of bytes downloaded and
    // uploaded to and from all torrents. This also includes all the protocol overhead.
    public long getTotalDownload() {
        return s.getTotal_download();
    }

    // the total number of bytes downloaded and
    // uploaded to and from all torrents. This also includes all the protocol overhead.
    public long getTotalUpload() {
        return s.getTotal_upload();
    }

    // the rate of the payload
    // down- and upload only.
    public int getPayloadUploadRate() {
        return s.getPayload_upload_rate();
    }

    // the rate of the payload
    // down- and upload only.
    public int getPayloadDownloadRate() {
        return s.getPayload_download_rate();
    }

    // the total transfers of payload
    // only. The payload does not include the bittorrent protocol overhead, but only parts of the
    // actual files to be downloaded.
    public long getTotalPayloadDownload() {
        return s.getTotal_payload_download();
    }

    // the total transfers of payload
    // only. The payload does not include the bittorrent protocol overhead, but only parts of the
    // actual files to be downloaded.
    public long getTotalPayloadUpload() {
        return s.getTotal_payload_upload();
    }
/*
        // the estimated TCP/IP overhead in each direction.
		int ip_overhead_upload_rate;
		int ip_overhead_download_rate;
		size_type total_ip_overhead_download;
		size_type total_ip_overhead_upload;

		// the upload and download rate used by DHT traffic. Also the total number
		// of bytes sent and received to and from the DHT.
		int dht_upload_rate;
		int dht_download_rate;
		size_type total_dht_download;
		size_type total_dht_upload;

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

		// only available when
		// built with DHT support. They are all set to 0 if the DHT isn't running. When
		// the DHT is running, ``dht_nodes`` is set to the number of nodes in the routing
		// table. This number only includes *active* nodes, not cache nodes. The
		// ``dht_node_cache`` is set to the number of nodes in the node cache. These nodes
		// are used to replace the regular nodes in the routing table in case any of them
		// becomes unresponsive.
		int dht_nodes;
		int dht_node_cache;

		// the number of torrents tracked by the DHT at the moment.
		int dht_torrents;

		// an estimation of the total number of nodes in the DHT
		// network.
		size_type dht_global_nodes;

		// a vector of the currently running DHT lookups.
		std::vector<dht_lookup> active_requests;

		// contains information about every bucket in the DHT routing
		// table.
		std::vector<dht_routing_bucket> dht_routing_table;

		// the number of nodes allocated dynamically for a
		// particular DHT lookup. This represents roughly the amount of memory used
		// by the DHT.
		int dht_total_allocations;

		// statistics on the uTP sockets.
		utp_status utp_stats;

		// the number of known peers across all torrents. These are not necessarily
		// connected peers, just peers we know of.
		int peerlist_size;
     */
}
