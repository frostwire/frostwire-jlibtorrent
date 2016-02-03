const jlibtorrent = require('./jlibtorrent.js');

console.log("Using libtorrent version: " + jlibtorrent.LibTorrent.fullVersion());

const sp = new jlibtorrent.SettingsPack();
const s = new jlibtorrent.Session(sp, false);

s.on('alert', function (a) {
    console.log(a.type() + " - " + a.what() + " - " + a.message());
});

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
