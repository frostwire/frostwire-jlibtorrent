var swig = require('./jlibtorrent.node');

// swig
(function () {

    exports.swig = {
        swig: swig
    }

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
