#!/usr/bin/env bash
# This script is meant to run here inside the swig/ folder from the Docker container
# It's supposed to be a one step build for the java jar and Linux ARM64 (.so enclosing) jar
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-linux-arm64-<version>.jar
# remote linux-arm64 build with docker is available at the container built by the Dockerfile
source build-utils.shinc
linux_arm64_env
common_env
check_min_req_vars
export os_arch=arm64
export os_build=linux
export SHARED_LIB=lib${LIBRARY_NAME}.so
export CXX=aarch64-linux-gnu-g++
export CC=aarch64-linux-gnu-gcc
export run_openssl_configure="./Configure linux-aarch64 ${OPENSSL_NO_OPTS} -fPIC --prefix=${OPENSSL_ROOT}"
export run_readelf="aarch64-linux-gnu-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j${CORES} --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_objcopy="aarch64-linux-gnu-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/${os_arch}/${SHARED_LIB}.debug"
export run_strip="aarch64-linux-gnu-strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_readelf="aarch64-linux-gnu-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_native_jar="./gradlew nativeLinuxArm64Jar"
export run_clean_native_jar="./gradlew cleanNativeLinuxArm64Jar"
press_any_to_continue
prepare_boost
ensure_swig
prepare_libtorrent
export BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION}
./run-swig.sh
build_libraries
