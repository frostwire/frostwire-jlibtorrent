#!/usr/bin/env bash
# This script is meant to run here inside the swig folder
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/jlibtorrent-<version>.jar
# ../build/libs/${LIBRARY_NAME}-android-x86-<version>.jar
# remote android-x86 build with travis is available at https://s3.amazonaws.com/gubatron-${LIBRARY_NAME}/release/android/x86/lib${LIBRARY_NAME}.so
source build-utils.shinc
export os_arch=x86
export os_build=android
export android_api=21
export SHARED_LIB=lib${LIBRARY_NAME}.so
export ANDROID_TOOLCHAIN="/src/android-toolchain-x86"
export CXX=${ANDROID_TOOLCHAIN}/bin/i686-linux-android-clang++
export CC=${ANDROID_TOOLCHAIN}/bin/i686-linux-android-clang
export NDK_VERSION=r21d

android_env
common_env
check_min_req_vars

export run_openssl_configure="./Configure linux-elf ${OPENSSL_NO_OPTS} -fPIC -mstackrealign --prefix=${OPENSSL_ROOT}"

export run_readelf="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j4 -q --debug-building --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_strip="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/${os_arch}/${SHARED_LIB}.debug"
export PATH=${ANDROID_TOOLCHAIN}/i686-linux-android/bin:$PATH;
#sed -i 's/RANLIB = ranlib/RANLIB = "${ANDROID_TOOLCHAIN}\/bin\/i686-linux-android-ranlib"/g' ${BOOST_ROOT}/tools/build/src/tools/gcc.jam;
export CXXFLAGS="-fPIC -std=c++14 -DANDROID -D__STDC_FORMAT_MACROS -DWITH_IPP=OFF -D__USE_FILE_OFFSET64 -D_FILE_OFFSET_BITS=64 -fno-strict-aliasing -fvisibility=hidden -mstackrealign"
export LDFLAGS="-static-libstdc++"
export BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION} && ./run-swig.sh
build_libraries
