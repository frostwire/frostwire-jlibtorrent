var lt = require('build/Release/jlibtorrent');

var alert_mask = lt.alert.all_categories;
var sp = new lt.settings_pack();

sp.set_int(lt.settings_pack.alert_mask, alert_mask);

var s = new lt.session(sp);

lt.session_alerts_loop(s);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
