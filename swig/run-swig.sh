#!/bin/bash

# Remove this when the issue in swig is fixed
# https://github.com/swig/swig/issues/672
function fixAlertFinal() {
    sed -i '' 's/alert final : /alert  : /g' ${LIBTORRENT_ROOT}/include/libtorrent/alert_types.hpp
}
function refixAlertFinal() {
    sed -i '' 's/alert  : /alert final : /g' ${LIBTORRENT_ROOT}/include/libtorrent/alert_types.hpp
}

function runJni()
{
    JAVA_SRC_OUTPUT=../src/main/java/com/frostwire/jlibtorrent/swig

    rm -rf ${JAVA_SRC_OUTPUT}
    mkdir -p ${JAVA_SRC_OUTPUT}

    swig -c++ -java -o libtorrent_jni.cpp \
        -outdir ${JAVA_SRC_OUTPUT} \
        -package com.frostwire.jlibtorrent.swig \
        -I${BOOST_ROOT} \
        -I${LIBTORRENT_ROOT}/include \
        -DBOOST_ASIO_DECL="" \
        -DBOOST_NO_TYPEID=1 \
        -DBOOST_NO_EXCEPTIONS \
        -DBOOST_POSIX_API=1 \
        -DBOOST_SYSTEM_NOEXCEPT="" \
        -DBOOST_SYSTEM_DECL="" \
        -DBOOST_SYSTEM_NO_DEPRECATED=1 \
        -DBOOST_NO_IOSTREAM \
        -DTORRENT_CFG="TORRENT_CFG" \
        -DTORRENT_NO_DEPRECATE \
        -DTORRENT_DEPRECATED_EXPORT="" \
        -DTORRENT_DEPRECATED \
        -DTORRENT_EXPORT="" \
        -DTORRENT_EXTRA_EXPORT="" \
        -DTORRENT_USE_IPV6=1 \
        -DTORRENT_DISABLE_GEO_IP=1 \
        -DTORRENT_USE_BOOST_DATE_TIME=1 \
        -DTORRENT_EXCEPTION_THROW_SPECIFIER=noexcept \
        -DTORRENT_NO_RETURN="" \
        -DTORRENT_DEBUG_REFCOUNTS=1 \
        -DTORRENT_FORMAT\(x,y\)="" \
        -DTORRENT_DISK_STATS=1 \
        -DTORRENT_UTP_LOG_ENABLE=1 \
        -DNDEBUG=1 \
        libtorrent.i

    # at first sigh, this could look like a very dangerous thing to
    # do, but in practice, these director types are controlled by us
    # and we know we can do it. The main reason is to be able to
    # compile with -fno-rtti.
    sed -i '' 's/dynamic_cast<SwigDirector_/static_cast<SwigDirector_/g' libtorrent_jni.cpp
}

function runNode()
{
    swig -c++ -javascript -node -o libtorrent_node.cpp \
        -DV8_VERSION=0x040685 \
        -I${BOOST_ROOT} \
        -I${LIBTORRENT_ROOT}/include \
        -DBOOST_ASIO_DECL="" \
        -DBOOST_NO_TYPEID=1 \
        -DBOOST_NO_EXCEPTIONS \
        -DBOOST_POSIX_API=1 \
        -DBOOST_SYSTEM_NOEXCEPT="" \
        -DBOOST_SYSTEM_DECL="" \
        -DBOOST_SYSTEM_NO_DEPRECATED=1 \
        -DBOOST_NO_IOSTREAM \
        -DTORRENT_CFG="TORRENT_CFG" \
        -DTORRENT_NO_DEPRECATE \
        -DTORRENT_DEPRECATED_EXPORT="" \
        -DTORRENT_DEPRECATED \
        -DTORRENT_EXPORT="" \
        -DTORRENT_EXTRA_EXPORT="" \
        -DTORRENT_USE_IPV6=1 \
        -DTORRENT_DISABLE_GEO_IP=1 \
        -DTORRENT_USE_BOOST_DATE_TIME=1 \
        -DTORRENT_EXCEPTION_THROW_SPECIFIER=noexcept \
        -DTORRENT_NO_RETURN="" \
        -DTORRENT_DEBUG_REFCOUNTS=1 \
        -DTORRENT_FORMAT\(x,y\)="" \
        -DTORRENT_DISK_STATS=1 \
        -DTORRENT_UTP_LOG_ENABLE=1 \
        -DNDEBUG=1 \
        libtorrent.i
}

fixAlertFinal
runJni
runNode
refixAlertFinal
