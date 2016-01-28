var swig = require('./jlibtorrent.node');

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
