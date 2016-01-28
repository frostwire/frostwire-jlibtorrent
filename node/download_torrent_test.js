var lt = require('./jlibtorrent');

var logging = false;
var alert_mask = lt.alert.all_categories;
if (!logging) {
    var log_mask = lt.alert.session_log_notification |
        lt.alert.torrent_log_notification |
        lt.alert.peer_log_notification |
        lt.alert.dht_log_notification |
        lt.alert.port_mapping_log_notification;
    alert_mask = alert_mask & ~log_mask;
}

var sp = new lt.settings_pack();

sp.set_int(lt.settings_pack.alert_mask, alert_mask);
var s = new lt.session(sp);

function session_alerts_loop() {
    var max_wait = lt.to_milliseconds(100);
    var alert = s.wait_for_alert(max_wait);

    if (alert != null) {
        var vector = new lt.alert_ptr_vector();
        s.pop_alerts(vector);
        var size = vector.size();
        for (var i = 0; i < size; i++) {
            var a = vector.get(i);
            console.log(a.type() + " - " + a.what() + " - " + a.message());
        }
        vector.clear();
    }
}

setInterval(session_alerts_loop, 500);

var torrent = "/Users/aldenml/Downloads/LukHash_The_Other_Side_FrostClick_FrostWire_MP3_January_17_2016.torrent";

var p = lt.add_torrent_params.create_instance();
var ec = new lt.error_code();
var ti = new lt.torrent_info(torrent, ec);
p.set_ti(ti);
s.async_add_torrent(p);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
