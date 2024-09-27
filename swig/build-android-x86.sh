#!/usr/bin/env bash
# This script is meant to run here inside the swig folder in the Docker container
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/jlibtorrent-<version>.jar
# ../build/libs/${LIBRARY_NAME}-android-x86-<version>.jar
# remote android-x86 build with travis is available at https://s3.amazonaws.com/gubatron-${LIBRARY_NAME}/release/android/x86/lib${LIBRARY_NAME}.so
source build-utils.shinc

# Default behavior is to run both swig and build
run_swig_only=false
run_build_only=false
# Call the function to parse flags
parse_flags "$@"

export os_arch=x86
export ANDROID_TOOLCHAIN=/src/android-ndk/toolchains/llvm/prebuilt/linux-x86_64

android_env
common_env
check_min_req_vars

export os_build=android
export android_api=24
export SHARED_LIB=lib${LIBRARY_NAME}.so
export NDK_VERSION=r26d
export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH};
export CXX=${ANDROID_TOOLCHAIN}/bin/i686-linux-android${android_api}-clang++
export CC=${ANDROID_TOOLCHAIN}/bin/i686-linux-android${android_api}-clang
export CORES=$(nproc)
export run_bjam="${BOOST_ROOT}/b2 -j${CORES} -q --debug-building --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/llvm-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/${os_arch}/${SHARED_LIB}.debug"
export run_strip="${ANDROID_TOOLCHAIN}/bin/llvm-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_readelf="${ANDROID_TOOLCHAIN}/bin/llvm-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_native_jar="./gradlew nativeAndroidX86Jar"
export run_clean_native_jar="./gradlew cleanNativeAndroidX86Jar"
export BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION}

if [ "${run_swig_only}" = true ]; then
    echo "--swig-only mode on"
    ./run-swig.sh
    exit 0
fi

if [ "${run_build_only}" = true ]; then
    echo "--build-only mode on"
    build_libraries
    exit 0
fi

./run-swig.sh
build_libraries
