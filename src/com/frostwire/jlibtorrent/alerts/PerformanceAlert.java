package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.performance_alert;

/**
 * This alert is generated when a limit is reached that might have a negative impact on
 * upload or download rate performance.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PerformanceAlert extends TorrentAlert<performance_alert> {

    public PerformanceAlert(performance_alert alert) {
        super(alert);
    }

    public PerformanceWarning getWarningCode() {
        return PerformanceWarning.fromSwig(alert.getWarning_code().swigValue());
    }

    public enum PerformanceWarning {

        // This warning means that the number of bytes queued to be written to disk
        // exceeds the max disk byte queue setting (``session_settings::max_queued_disk_bytes``).
        // This might restrict the download rate, by not queuing up enough write jobs
        // to the disk I/O thread. When this alert is posted, peer connections are
        // temporarily stopped from downloading, until the queued disk bytes have fallen
        // below the limit again. Unless your ``max_queued_disk_bytes`` setting is already
        // high, you might want to increase it to get better performance.
        OUTSTANDING_DISK_BUFFER_LIMIT_REACHED(performance_alert.performance_warning_t.outstanding_disk_buffer_limit_reached.swigValue()),

        // This is posted when libtorrent would like to send more requests to a peer,
        // but it's limited by ``session_settings::max_out_request_queue``. The queue length
        // libtorrent is trying to achieve is determined by the download rate and the
        // assumed round-trip-time (``session_settings::request_queue_time``). The assumed
        // rount-trip-time is not limited to just the network RTT, but also the remote disk
        // access time and message handling time. It defaults to 3 seconds. The target number
        // of outstanding requests is set to fill the bandwidth-delay product (assumed RTT
        // times download rate divided by number of bytes per request). When this alert
        // is posted, there is a risk that the number of outstanding requests is too low
        // and limits the download rate. You might want to increase the ``max_out_request_queue``
        // setting.
        OUTSTANDING_REQUEST_LIMIT_REACHED(performance_alert.performance_warning_t.outstanding_request_limit_reached.swigValue()),

        // This warning is posted when the amount of TCP/IP overhead is greater than the
        // upload rate limit. When this happens, the TCP/IP overhead is caused by a much
        // faster download rate, triggering TCP ACK packets. These packets eat into the
        // rate limit specified to libtorrent. When the overhead traffic is greater than
        // the rate limit, libtorrent will not be able to send any actual payload, such
        // as piece requests. This means the download rate will suffer, and new requests
        // can be sent again. There will be an equilibrium where the download rate, on
        // average, is about 20 times the upload rate limit. If you want to maximize the
        // download rate, increase the upload rate limit above 5% of your download capacity.
        UPLOAD_LIMIT_TOO_LOW(performance_alert.performance_warning_t.upload_limit_too_low.swigValue()),

        // This is the same warning as ``upload_limit_too_low`` but referring to the download
        // limit instead of upload. This suggests that your download rate limit is mcuh lower
        // than your upload capacity. Your upload rate will suffer. To maximize upload rate,
        // make sure your download rate limit is above 5% of your upload capacity.
        DOWNLOAD_LIMIT_TOO_LOW(performance_alert.performance_warning_t.download_limit_too_low.swigValue()),

        // We're stalled on the disk. We want to write to the socket, and we can write
        // but our send buffer is empty, waiting to be refilled from the disk.
        // This either means the disk is slower than the network connection
        // or that our send buffer watermark is too small, because we can
        // send it all before the disk gets back to us.
        // The number of bytes that we keep outstanding, requested from the disk, is calculated
        // as follows::
        //
        //   min(512, max(upload_rate * send_buffer_watermark_factor / 100, send_buffer_watermark))
        //
        // If you receive this alert, you migth want to either increase your ``send_buffer_watermark``
        // or ``send_buffer_watermark_factor``.
        SEND_BUFFER_WATERMARK_TOO_LOW(performance_alert.performance_warning_t.send_buffer_watermark_too_low.swigValue()),

        // If the half (or more) of all upload slots are set as optimistic unchoke slots, this
        // warning is issued. You probably want more regular (rate based) unchoke slots.
        TOO_MANY_OPTIMISTIC_UNCHOKE_SLOTS(performance_alert.performance_warning_t.too_many_optimistic_unchoke_slots.swigValue()),

        // If the disk write queue ever grows larger than half of the cache size, this warning
        // is posted. The disk write queue eats into the total disk cache and leaves very little
        // left for the actual cache. This causes the disk cache to oscillate in evicting large
        // portions of the cache before allowing peers to download any more, onto the disk write
        // queue. Either lower ``max_queued_disk_bytes`` or increase ``cache_size``.
        TOO_HIGH_DISK_QUEUE_LIMIT(performance_alert.performance_warning_t.too_high_disk_queue_limit.swigValue()),

        BITTYRANT_WITH_NO_UPLIMIT(performance_alert.performance_warning_t.bittyrant_with_no_uplimit.swigValue()),

        // This is generated if outgoing peer connections are failing because of *address in use*
        // errors, indicating that ``session_settings::outgoing_ports`` is set and is too small of
        // a range. Consider not using the ``outgoing_ports`` setting at all, or widen the range to
        // include more ports.
        TOO_FEW_OUTGOING_PORTS(performance_alert.performance_warning_t.too_few_outgoing_ports.swigValue()),

        TOO_FEW_FILE_DESCRIPTORS(performance_alert.performance_warning_t.too_few_file_descriptors.swigValue()),

        NUM_WARNINGS(performance_alert.performance_warning_t.num_warnings.swigValue()),

        UNKNOWN(-1);

        private PerformanceWarning(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static PerformanceWarning fromSwig(int swigValue) {
            PerformanceWarning[] enumValues = PerformanceWarning.class.getEnumConstants();
            for (PerformanceWarning ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
