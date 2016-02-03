const jlibtorrent = require('./jlibtorrent.js');

console.log("Using libtorrent version: " + jlibtorrent.LibTorrent.fullVersion());

const l = function (a) {
    console.log(a.type() + " - " + a.what() + " - " + a.message());
}

const sp = new jlibtorrent.SettingsPack();
const s = new jlibtorrent.Session(sp, false, l);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
