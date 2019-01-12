#!/usr/bin/env bash
# This script is meant to run here inside frostwire-jlibtorrent/swig folder
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# frostwire-jlibtorrent/build/libs/jlibtorrent-<version>.jar
# frostwire-jlibtorrent/build/libs/jlibtorrent-macosx-<version>.jar

export LIBTORRENT_REVISION=6724c1eec03caa27f42c48c89367b35d718b8c02
export OPENSSL_VERSION="1.1.1a"
export BOOST_VERSION="69"
export BOOST_MAJOR="1"
export BOOST_MINOR="0"

export os_arch=arm
export os_build=android
export android_api=19
export SHARED_LIB=libjlibtorrent.so
export SHARED_LIB_FINAL=${SHARED_LIB} # dummy for macosx

export SWIG=`pwd`
export SRC="${HOME}/src"
export BOOST_ROOT="${SRC}/boost_1_${BOOST_VERSION}_0"
export LIBTORRENT_ROOT="${SRC}/libtorrent"
export CXX=g++
export NDK_VERSION=r18b
source build-utils.shinc
prepare_android_toolchain
abort_if_var_unset "ANDROID_TOOLCHAIN" ${ANDROID_TOOLCHAIN}
export CC=$ANDROID_TOOLCHAIN/bin/arm-linux-androideabi-clang

export OPENSSL_SOURCE="$SRC/openssl-${OPENSSL_VERSION}"
export OPENSSL_ROOT="$SRC/openssl"
export OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw no-makedepend no-multiblock no-nextprotoneg no-posix-io no-psk no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine no-stdio no-threads no-ui-console no-zlib no-zlib-dynamic -fno-strict-aliasing -fvisibility=hidden -Os"

export run_openssl_configure="./Configure linux-${os_arch}v4 ${OPENSSL_NO_OPTS} -march=armv7-a -mfpu=neon -fPIC --prefix=${OPENSSL_ROOT}";
export run_readelf="${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-readelf -d bin/release/android/armeabi-v7a/${SHARED_LIB}"


export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang-linux-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}eabi-v7a"
export run_strip="${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}eabi-v7a/${SHARED_LIB}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}eabi-v7a/${SHARED_LIB} bin/release/${os_build}/{$os_arch}eabi-v7a/${SHARED_LIB}.debug"
export PATH=$ANDROID_TOOLCHAIN/arm-linux-androideabi/bin:$PATH;
sed -i 's/RANLIB = ranlib/RANLIB = "${ANDROID_TOOLCHAIN}\/bin\/arm-linux-androideabi-ranlib"/g' ${BOOST_ROOT}/tools/build/src/tools/gcc.jam;

create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "About to prepare BOOST ${BOOST_VERSION}"
press_any_to_continue
prepare_boost
prepare_openssl
build_openssl
prepare_libtorrent
build_libraries
cleanup_objects
