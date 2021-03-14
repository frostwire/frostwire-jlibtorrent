#!/usr/bin/env bash
# This script is meant to run here inside the swig folder in the Docker container
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv8 and x86)
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-android-arm64-<version>.jar
# remote android-arm build with travis is available at https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/arm64-v8a/libjlibtorrent.so
source build-utils.shinc
export os_arch=arm64
export os_build=android
export android_api=21
export SHARED_LIB=lib${LIBRARY_NAME}.so
export ANDROID_TOOLCHAIN=/src/android-toolchain-arm64
export PATH=${ANDROID_TOOLCHAIN}/aarch64-linux-android/bin:${PATH}
export CXX=${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android-clang++
export CC=${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android-clang
android_env
common_env
check_min_req_vars
export run_readelf="${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android-readelf -d bin/release/${os_build}/${os_arch}-v8a/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j4 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}-v8a"
export run_strip="${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}-v8a/${SHARED_LIB}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}-v8a/${SHARED_LIB} bin/release/${os_build}/${os_arch}-v8a/${SHARED_LIB}.debug"
export BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION} && ./run-swig.sh
build_libraries
