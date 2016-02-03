const EventEmitter = require('events');
const util = require('util');

const swig = require('./jlibtorrent.node');

// swig
(function () {

    exports.swig = swig;

}());

// LibTorrent
(function () {

    function version() {
        return swig.LIBTORRENT_VERSION;
    }

    function revision() {
        return swig.LIBTORRENT_REVISION_SHA1;
    }

    function jrevision() {
        return swig.JLIBTORRENT_REVISION_SHA1;
    }

    function boostVersion() {
        return swig.boost_version();
    }

    function boostLibVersion() {
        return swig.boost_lib_version();
    }

    function fullVersion() {
        return version() + '-rev-' + revision() + '-jrev-' + jrevision() + '-boost-' + boostVersion();
    }

    exports.LibTorrent = {
        version: version,
        revision: revision,
        jrevision: jrevision,
        boostVersion: boostVersion,
        boostLibVersion: boostLibVersion,
        fullVersion: fullVersion
    };

}());

// SettingsPack
(function () {

    function SettingsPack(sp) {
        this.sp = sp || new swig.settings_pack();
    }

    SettingsPack.prototype.swig = function () {
        return this.sp;
    }

    SettingsPack.prototype.boolean = function (name, value) {
        if (value) {
            this.sp.set_bool(name, value);
        } else {
            value = this.sp.get_bool(name);
        }

        return value;
    }

    SettingsPack.prototype.integer = function (name, value) {
        if (value) {
            this.sp.set_int(name, value);
        } else {
            value = this.sp.get_int(name);
        }

        return value;
    }

    SettingsPack.prototype.string = function (name, value) {
        if (value) {
            this.sp.set_str(name, value);
        } else {
            value = this.sp.get_str(name);
        }

        return value;
    }

    exports.SettingsPack = SettingsPack;

}());

// Session
(function () {

    function createSession(settings, logging) {
        var sp = settings.swig(),
            alert_mask = swig.alert.all_categories;
        if (!logging) {
            var log_mask = swig.alert.session_log_notification |
                swig.alert.torrent_log_notification |
                swig.alert.peer_log_notification |
                swig.alert.dht_log_notification |
                swig.alert.port_mapping_log_notification;
            alert_mask = alert_mask & ~log_mask;
        }

        // we always override alert_mask since we use it for our internal operations
        sp.set_int(swig.settings_pack.alert_mask, alert_mask);

        return new swig.session(sp);
    }

    function alertsLoop(emitter, s) {
        var session_alerts_loop = function () {
            var max_wait = swig.to_milliseconds(100);
            var alert = s.wait_for_alert(max_wait);

            if (alert != null) {
                var vector = new swig.alert_ptr_vector();
                s.pop_alerts(vector);
                var size = vector.size();
                for (var i = 0; i < size; i++) {
                    var a = vector.get(i);
                    emitter.emit('alert', a);
                }
                vector.clear();
            }
        }

        setInterval(session_alerts_loop, 1000);
    }

    function defaultRouters() {
        var list = [];

        list.push(new swig.string_int_pair("router.bittorrent.com", 6881));
        list.push(new swig.string_int_pair("dht.transmissionbt.com", 6881));

        return list;
    }

    function Session(settings, logging, listener) {
        EventEmitter.call(this);

        settings = settings || new exports.SettingsPack();
        logging = logging || false;

        this.s = createSession(settings, logging);

        if (listener) {
            this.on('alert', listener);
        }

        alertsLoop(this, this.s);

        var arr = defaultRouters();
        var size = arr.length;
        for (var i = 0; i < size; i++) {
            this.s.add_dht_router(arr[i]);
        }
    }

    util.inherits(Session, EventEmitter);

    Session.prototype.swig = function () {
        return this.sp;
    }

    Session.prototype.postDHTStats = function () {
        this.s.post_dht_stats();
    }

    Session.prototype.fetchMagnet = function (uri) {
        var p = swig.add_torrent_params.create_instance_disabled_storage();
        var ec = new swig.error_code();
        swig.parse_magnet_uri(uri, p, ec);

        if (ec.value() != 0) {
            throw ec.message();
        }

        var info_hash = p.info_hash;

        var th = this.s.find_torrent(info_hash);
        if (th && th.is_valid()) {
            // we have a download with the same info-hash, improve this
            return;
        }

        p.name = "fetch_magnet:" + uri;
        p.save_path = "fetch_magnet/" + uri;

        var flags = p.flags;
        flags &= ~swig.add_torrent_params.flag_auto_managed;
        p.flags = flags;

        ec.clear();
        th = this.s.add_torrent(p, ec);
        th.resume();
    }

    exports.Session = Session;

}());
