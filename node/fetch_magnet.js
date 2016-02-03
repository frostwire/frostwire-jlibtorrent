const jlibtorrent = require('./jlibtorrent.js');

console.log("Using libtorrent version: " + jlibtorrent.LibTorrent.fullVersion());

const sp = new jlibtorrent.SettingsPack();
const s = new jlibtorrent.Session(sp, false);

s.on('alert', function (a) {
    if (a.type() === jlibtorrent.swig.metadata_received_alert.alert_type) {
        a = jlibtorrent.swig.alert.cast_to_metadata_received_alert(a);
        console.log(a)
        var th = a.handle;
        var ti = th.get_torrent_copy();
        var entry = new jlibtorrent.swig.create_torrent(ti).generate();
        console.log(entry.to_string());
        process.exit(0);
    }

    if (a.type() === jlibtorrent.swig.metadata_failed_alert.alert_type) {
        console.log("Failed to receive the magnet metadata");
        process.exit(0);
    }
});

function wait_for_nodes() {
    s.fetchMagnet("magnet:?xt=urn:btih:a83cc13bf4a07e85b938dcf06aa707955687ca7c");
}

console.log("Waiting 3 seconds to request...patience...");
setTimeout(wait_for_nodes, 3000);

process.stdout.write('Press ENTER to exit...');
process.stdin.once('data', function (data) {
    process.exit(0);
});
