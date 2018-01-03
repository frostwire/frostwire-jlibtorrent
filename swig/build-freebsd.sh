#!/usr/bin/env bash

# boost: download and bootstrap
rm -rf boost_1_66_0
wget -nv --show-progress -O boost.zip https://dl.bintray.com/boostorg/release/1.66.0/source/boost_1_66_0.zip
unzip -qq boost.zip
cd boost_1_66_0
./bootstrap.sh
cd ..
export BOOST_ROOT=$PWD/boost_1_66_0

# openssl: download
rm -rf openssl-1.1.0g
wget -nv --show-progress -O openssl.tar.gz https://www.openssl.org/source/openssl-1.1.0g.tar.gz
tar xzf openssl.tar.gz
export OPENSSL_SOURCE=$PWD/openssl-1.1.0g

# libtorrent: download and checkout revision
rm -rf libtorrent
git clone https://github.com/arvidn/libtorrent
cd libtorrent
git checkout ef5d44ea9d3993bee3d3caa3ade0feb4c8021656
cd ..
export LIBTORRENT_ROOT=$PWD/libtorrent

# compile openssl
rm -rf openssl
cd $OPENSSL_SOURCE
export OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit
    no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls
    no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw
    no-makedepend no-multiblock no-nextprotoneg no-pic no-posix-io no-psk
    no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine
    no-stdio no-threads no-ui no-zlib no-zlib-dynamic
    -fno-strict-aliasing -fvisibility=hidden -Os"
./Configure BSD-x86_64 ${OPENSSL_NO_OPTS} -fPIC --prefix=$OPENSSL_SOURCE/../openssl
echo "Compiling openssl...(remove &> /dev/null to see output)"
make &> /dev/null
make install &> /dev/null
cd ..
export OPENSSL_ROOT=$PWD/openssl

# compile jlibtorrent
rm -rf bin
export B2_PATH=${BOOST_ROOT}/tools/build/src/engine/bin.freebsdx86_64
${B2_PATH}/b2 --user-config=config/freebsd-x86_64-config.jam iconv=on variant=release toolset=clang-x86_64 target-os=freebsd location=bin/release/freebsd/x86_64
strip --strip-unneeded -x bin/release/freebsd/x86_64/libjlibtorrent.so
readelf -d bin/release/freebsd/x86_64/libjlibtorrent.so
