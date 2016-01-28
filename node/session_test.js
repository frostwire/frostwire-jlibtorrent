var lt = require('./jlibtorrent');

var alert_mask = lt.alert.all_categories;
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

setTimeout(session_alerts_loop, 500);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
