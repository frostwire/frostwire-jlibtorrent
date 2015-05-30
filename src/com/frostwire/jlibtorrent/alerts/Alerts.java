package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Alerts {

    private static final Logger LOG = Logger.getLogger(Alerts.class);

    private static final Map<Integer, CastAlertFunction> CAST_TABLE = buildCastAlertTable();

    private Alerts() {
    }

    public static Alert<?> cast(alert a) {
        CastAlertFunction function = CAST_TABLE.get(a.type());

        Alert<?> r;

        if (function != null) {
            r = function.cast(a);
        } else {
            r = new GenericAlert(a);
        }

        return r;
    }

    private static Map<Integer, CastAlertFunction> buildCastAlertTable() {
        Map<Integer, CastAlertFunction> map = new HashMap<Integer, CastAlertFunction>();

        CAST_ALERT_METHOD(torrent_alert.class, map);
        CAST_ALERT_METHOD(peer_alert.class, map);
        CAST_ALERT_METHOD(tracker_alert.class, map);
        CAST_ALERT_METHOD(torrent_added_alert.class, map);
        CAST_ALERT_METHOD(torrent_removed_alert.class, map);
        CAST_ALERT_METHOD(read_piece_alert.class, map);
        CAST_ALERT_METHOD(file_completed_alert.class, map);
        CAST_ALERT_METHOD(file_renamed_alert.class, map);
        CAST_ALERT_METHOD(file_rename_failed_alert.class, map);
        CAST_ALERT_METHOD(performance_alert.class, map);
        CAST_ALERT_METHOD(state_changed_alert.class, map);
        CAST_ALERT_METHOD(tracker_error_alert.class, map);
        CAST_ALERT_METHOD(tracker_warning_alert.class, map);
        CAST_ALERT_METHOD(scrape_reply_alert.class, map);
        CAST_ALERT_METHOD(scrape_failed_alert.class, map);
        CAST_ALERT_METHOD(tracker_reply_alert.class, map);
        CAST_ALERT_METHOD(dht_reply_alert.class, map);
        CAST_ALERT_METHOD(tracker_announce_alert.class, map);
        CAST_ALERT_METHOD(hash_failed_alert.class, map);
        CAST_ALERT_METHOD(peer_ban_alert.class, map);
        CAST_ALERT_METHOD(peer_unsnubbed_alert.class, map);
        CAST_ALERT_METHOD(peer_snubbed_alert.class, map);
        CAST_ALERT_METHOD(peer_error_alert.class, map);
        CAST_ALERT_METHOD(peer_connect_alert.class, map);
        CAST_ALERT_METHOD(peer_disconnected_alert.class, map);
        CAST_ALERT_METHOD(invalid_request_alert.class, map);
        CAST_ALERT_METHOD(torrent_finished_alert.class, map);
        CAST_ALERT_METHOD(piece_finished_alert.class, map);
        CAST_ALERT_METHOD(request_dropped_alert.class, map);
        CAST_ALERT_METHOD(block_timeout_alert.class, map);
        CAST_ALERT_METHOD(block_finished_alert.class, map);
        CAST_ALERT_METHOD(block_downloading_alert.class, map);
        CAST_ALERT_METHOD(unwanted_block_alert.class, map);
        CAST_ALERT_METHOD(storage_moved_alert.class, map);
        CAST_ALERT_METHOD(storage_moved_failed_alert.class, map);
        CAST_ALERT_METHOD(torrent_deleted_alert.class, map);
        CAST_ALERT_METHOD(torrent_delete_failed_alert.class, map);
        CAST_ALERT_METHOD(save_resume_data_alert.class, map);
        CAST_ALERT_METHOD(save_resume_data_failed_alert.class, map);
        CAST_ALERT_METHOD(torrent_paused_alert.class, map);
        CAST_ALERT_METHOD(torrent_resumed_alert.class, map);
        CAST_ALERT_METHOD(torrent_checked_alert.class, map);
        CAST_ALERT_METHOD(url_seed_alert.class, map);
        CAST_ALERT_METHOD(file_error_alert.class, map);
        CAST_ALERT_METHOD(metadata_failed_alert.class, map);
        CAST_ALERT_METHOD(metadata_received_alert.class, map);
        CAST_ALERT_METHOD(udp_error_alert.class, map);
        CAST_ALERT_METHOD(external_ip_alert.class, map);
        CAST_ALERT_METHOD(listen_failed_alert.class, map);
        CAST_ALERT_METHOD(listen_succeeded_alert.class, map);
        CAST_ALERT_METHOD(portmap_error_alert.class, map);
        CAST_ALERT_METHOD(portmap_alert.class, map);
        CAST_ALERT_METHOD(portmap_log_alert.class, map);
        CAST_ALERT_METHOD(fastresume_rejected_alert.class, map);
        CAST_ALERT_METHOD(peer_blocked_alert.class, map);
        CAST_ALERT_METHOD(dht_announce_alert.class, map);
        CAST_ALERT_METHOD(dht_get_peers_alert.class, map);
        CAST_ALERT_METHOD(stats_alert.class, map);
        CAST_ALERT_METHOD(cache_flushed_alert.class, map);
        CAST_ALERT_METHOD(anonymous_mode_alert.class, map);
        CAST_ALERT_METHOD(lsd_peer_alert.class, map);
        CAST_ALERT_METHOD(trackerid_alert.class, map);
        CAST_ALERT_METHOD(dht_bootstrap_alert.class, map);
        CAST_ALERT_METHOD(torrent_error_alert.class, map);
        CAST_ALERT_METHOD(torrent_need_cert_alert.class, map);
        CAST_ALERT_METHOD(incoming_connection_alert.class, map);
        CAST_ALERT_METHOD(add_torrent_alert.class, map);
        CAST_ALERT_METHOD(state_update_alert.class, map);
        CAST_ALERT_METHOD(torrent_update_alert.class, map);
        CAST_ALERT_METHOD(dht_error_alert.class, map);
        CAST_ALERT_METHOD(dht_immutable_item_alert.class, map);
        CAST_ALERT_METHOD(dht_mutable_item_alert.class, map);
        CAST_ALERT_METHOD(dht_put_alert.class, map);
        CAST_ALERT_METHOD(i2p_alert.class, map);

        CAST_ALERT_METHOD(dht_get_peers_reply_alert.class, map);
        CAST_ALERT_METHOD(set_piece_hashes_alert.class, map);

        return Collections.unmodifiableMap(map);
    }

    private static void CAST_ALERT_METHOD(Class<? extends alert> clazz, Map<Integer, CastAlertFunction> map) {
        try {
            Field f = clazz.getDeclaredField("alert_type");
            int type = f.getInt(null);
            CastAlertFunction function = new CastAlertFunction(clazz);

            map.put(type, function);
        } catch (Throwable e) {
            LOG.warn(e.toString());
        }
    }

    private static final class CastAlertFunction {

        private final Method method;
        private final Constructor<? extends Alert<?>> constructor;

        public CastAlertFunction(Class<? extends alert> swigClazz) throws NoSuchMethodException, ClassNotFoundException {

            String swigClazzName = swigClazz.getName().replace("com.frostwire.jlibtorrent.swig.", "");
            String libClazzName = "com.frostwire.jlibtorrent.alerts." + capitalizeAlertTypeName(swigClazzName);

            @SuppressWarnings("unchecked")
            Class<? extends Alert<?>> libClazz = (Class<? extends Alert<?>>) Class.forName(libClazzName);

            this.method = alert.class.getDeclaredMethod("cast_to_" + swigClazzName, alert.class);
            this.constructor = libClazz.getDeclaredConstructor(swigClazz);
        }

        public Alert<?> cast(alert a) {
            Alert<?> r;

            try {
                Object obj = method.invoke(null, a);
                r = constructor.newInstance(obj);
            } catch (Throwable e) {
                LOG.warn(e.toString());
                r = new GenericAlert(a);
            }

            return r;
        }

        private static String capitalizeAlertTypeName(String s) {
            StringBuilder sb = new StringBuilder(s.length());

            boolean capitalize = true;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (capitalize) {
                    sb.append(Character.toUpperCase(ch));
                    capitalize = false;
                } else if (ch == '_') {
                    capitalize = true;
                } else {
                    sb.append(ch);
                }
            }

            return sb.toString();
        }
    }
}
