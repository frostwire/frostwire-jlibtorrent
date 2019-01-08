#!/usr/bin/env bash
# This script is meant to run here inside frostwire-jlibtorrent/swig folder
# It's supposed to be a one step build for the java jar and macosx (.dylib enclosing) jar
# Output .jar files will be at:
# frostwire-jlibtorrent/build/libs/jlibtorrent-<version>.jar
# frostwire-jlibtorrent/build/libs/jlibtorrent-macosx-<version>.jar

export LIBTORRENT_REVISION=6724c1eec03caa27f42c48c89367b35d718b8c02
export OPENSSL_VERSION="1.1.1a"
export BOOST_VERSION="69"
export BOOST_MAJOR="1"
export BOOST_MINOR="0"

export os_arch=x86_64
export os_build=macosx
export SHARED_LIB=libjlibtorrent.dylib
export SHARED_LIB_FINAL=${SHARED_LIB} # dummy for macosx

export SWIG=`pwd`
export SRC="${HOME}/src"
export BOOST_ROOT="${SRC}/boost_1_${BOOST_VERSION}_0"
export LIBTORRENT_ROOT="${SRC}/libtorrent"
export CXX=g++
export CC=gcc

export OPENSSL_SOURCE="$SRC/openssl-${OPENSSL_VERSION}"
export OPENSSL_ROOT="$SRC/openssl"
export OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw no-makedepend no-multiblock no-nextprotoneg no-posix-io no-psk no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine no-stdio no-threads no-ui-console no-zlib no-zlib-dynamic -fno-strict-aliasing -fvisibility=hidden -Os"

export run_openssl_configure="./Configure darwin64-${os_arch}-cc ${OPENSSL_NO_OPTS} --prefix=${OPENSSL_ROOT}";
export run_readelf="otool -L bin/release/${os_build}/${os_arch}/${SHARED_LIB}";

export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=darwin-${os_arch} target-os=darwin location=bin/release/${os_build}/${os_arch}"
export run_strip="strip -S -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"
source build-utils.shinc

create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "About to prepare BOOST ${BOOST_VERSION}"
press_any_to_continue
prepare_boost
prepare_openssl
build_openssl
prepare_libtorrent
build_libraries
cleanup_objects

