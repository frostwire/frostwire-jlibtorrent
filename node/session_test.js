var jlibtorrent = require('./jlibtorrent.js');

console.log("Using libtorrent version: " + jlibtorrent.LibTorrent.fullVersion());

var sp = new jlibtorrent.SettingsPack();
var s = new jlibtorrent.Session(sp, false);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
