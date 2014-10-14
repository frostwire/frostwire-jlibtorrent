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

    public int getNumIdle() {
        return s.getNum_idle();
    }

    public int getNumSynSent() {
        return s.getNum_syn_sent();
    }

    public int getNumConnected() {
        return s.getNum_connected();
    }

    public int getNumFinSent() {
        return s.getNum_fin_sent();
    }

    public int getNumCloseWait() {
        return s.getNum_close_wait();
    }

    // counters. These are monotonically increasing
    // and cumulative counters for their respective event.

    public BigInteger getPacketLoss() {
        return s.getPacket_loss();
    }

    public BigInteger getTimeout() {
        return s.getTimeout();
    }

    public BigInteger getPacketsIn() {
        return s.getPackets_in();
    }

    public BigInteger getPacketsOut() {
        return s.getPackets_out();
    }

    public BigInteger getFastRetransmit() {
        return s.getFast_retransmit();
    }

    public BigInteger getPacketResend() {
        return s.getPacket_resend();
    }

    public BigInteger getSamplesAboveTarget() {
        return s.getSamples_above_target();
    }

    public BigInteger getSamplesBelowTarget() {
        return s.getSamples_below_target();
    }

    public BigInteger getPayloadPktsIn() {
        return s.getPayload_pkts_in();
    }

    public BigInteger getPayloadPktsOut() {
        return s.getPayload_pkts_out();
    }

    public BigInteger getInvalidPktsIn() {
        return s.getInvalid_pkts_in();
    }

    public BigInteger getRedundantPktsIn() {
        return s.getRedundant_pkts_in();
    }
}
