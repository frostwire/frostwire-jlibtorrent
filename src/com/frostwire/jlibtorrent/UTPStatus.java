package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.utp_status;

import java.math.BigInteger;

/**
 * holds counters and gauges for the uTP sockets.
 *
 * @author gubatron
 * @author aldenml
 */
public final class UTPStatus {

    private final utp_status s;

    public UTPStatus(utp_status s) {
        this.s = s;
    }

    public utp_status getSwig() {
        return s;
    }

    // gauges. These are snapshots of the number of
    // uTP sockets in each respective state
//    int num_idle;
//    int num_syn_sent;
//    int num_connected;
//    int num_fin_sent;
//    int num_close_wait;

    // counters. These are monotonically increasing
    // and cumulative counters for their respective event.

    public BigInteger getPacketLoss() {
        return s.getPacket_loss();
    }

//    boost::uint64_t timeout;
//    boost::uint64_t packets_in;
//    boost::uint64_t packets_out;
//    boost::uint64_t fast_retransmit;
//    boost::uint64_t packet_resend;
//    boost::uint64_t samples_above_target;
//    boost::uint64_t samples_below_target;
//    boost::uint64_t payload_pkts_in;
//    boost::uint64_t payload_pkts_out;
//    boost::uint64_t invalid_pkts_in;
//    boost::uint64_t redundant_pkts_in;
}
