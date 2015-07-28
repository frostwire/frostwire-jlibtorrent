#!/bin/bash

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
    -DBOOST_VERSION=105000 \
    -DBOOST_POSIX_API=1 \
    -DBOOST_SYSTEM_NOEXCEPT="" \
    -DBOOST_SYSTEM_DECL="" \
    -DBOOST_SYSTEM_NO_DEPRECATED=1 \
    -DTORRENT_CFG="TORRENT_CFG" \
    -DTORRENT_NO_DEPRECATE=1 \
    -DTORRENT_DEPRECATED_EXPORT="" \
    -DTORRENT_EXPORT="" \
    -DTORRENT_EXTRA_EXPORT="" \
    -DTORRENT_USE_IPV6=1 \
    -DTORRENT_DISABLE_GEO_IP=1 \
    -DTORRENT_USE_BOOST_DATE_TIME=1 \
    -DTORRENT_USE_OPENSSL=1 \
    -DTORRENT_EXCEPTION_THROW_SPECIFIER=noexcept \
    -DTORRENT_NO_RETURN="" \
    -DTORRENT_OVERRIDE="" \
    -DTORRENT_FINAL="" \
    -DTORRENT_DISABLE_LOGGING=1 \
    -DTORRENT_DEBUG_REFCOUNTS=1 \
    -DTORRENT_FORMAT\(x,y\)="" \
    -DNDEBUG=1 \
    libtorrent.i
