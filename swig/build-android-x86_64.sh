#!/usr/bin/env bash
# This script is meant to run here inside the swig folder in the Docker container
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/jlibtorrent-<version>.jar
# ../build/libs/${LIBRARY_NAME}-android-x86_64-<version>.jar
# remote android-x86 build with travis is available at https://s3.amazonaws.com/gubatron-${LIBRARY_NAME}/release/android/x86_64/lib${LIBRARY_NAME}.so
source build-utils.shinc
export os_arch=x86_64
export os_build=android
export android_api=24
export SHARED_LIB=lib${LIBRARY_NAME}.so
export NDK_VERSION=r23
export ANDROID_TOOLCHAIN=/src/android-ndk/toolchains/llvm/prebuilt/linux-x86_64
export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH};
export CXX=${ANDROID_TOOLCHAIN}/bin/x86_64-linux${android_api}-android-clang++
export CC=${ANDROID_TOOLCHAIN}/bin/x86_64-linux-android${android_api}-clang
android_env
common_env
check_min_req_vars
export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/${os_arch}/${SHARED_LIB}.debug"
export run_strip="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_readelf="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_native_jar="./gradlew nativeAndroidX64Jar"
BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION} && ./run-swig.sh
build_libraries
