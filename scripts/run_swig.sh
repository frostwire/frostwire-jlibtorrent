#!/bin/bash

JAVA_SRC_OUTPUT=src/com/frostwire/libtorrent/swig

rm -rf ${JAVA_SRC_OUTPUT}
mkdir -p ${JAVA_SRC_OUTPUT}

swig -c++ -java -o libtorrent_jni.cpp \
    -outdir ${JAVA_SRC_OUTPUT} \
    -package com.frostwire.libtorrent.swig \
    -Ilibtorrent-rasterbar-1.0.1/include \
    -I/usr/local/include \
    -DBOOST_ASIO_DECL="" \
    -DBOOST_NO_TYPEID=1 \
    -DBOOST_VERSION=105000 \
    -DBOOST_POSIX_API=1 \
    -DBOOST_SYSTEM_NOEXCEPT="" \
    -DBOOST_SYSTEM_NO_DEPRECATED=1 \
    -DTORRENT_NO_DEPRECATE=1 \
    -DTORRENT_EXPORT="" \
    -DTORRENT_EXTRA_EXPORT="" \
    -DTORRENT_USE_IPV6=1 \
    -DTORRENT_DISABLE_GEO_IP=1 \
    -DTORRENT_USE_BOOST_DATE_TIME=1 \
    swig/libtorrent.i

CC="clang++"
INCLUDES="-I/usr/local/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/include/ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/include/darwin"
LIBS="-ltorrent-rasterbar -lboost_system"
CFLAGS="-stdlib=libc++ -std=c++11 -O3 -DBOOST_ASIO_DYN_LINK=1 -DTORRENT_NO_DEPRECATE=1 -DTORRENT_DISABLE_GEO_IP=1"
LDFLAGS="-L/usr/local/lib"
TARGET="jlibtorrent.dylib"

$CC $CFLAGS $INCLUDES -c libtorrent_jni.cpp
$CC $LDFLAGS $LIBS -dynamiclib -o $TARGET libtorrent_jni.o

rm -rf libtorrent_jni.o
