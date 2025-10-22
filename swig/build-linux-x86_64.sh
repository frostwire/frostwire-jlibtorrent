#!/usr/bin/env bash
# This script is meant to run here inside the swig/ folder from the Docker container
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-linux-<version>.jar
# remote linux-x86 build with travis is available at https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86_64/libjlibtorrent.so
source build-utils.shinc
linux_env
common_env
check_min_req_vars
export os_arch=x86_64
export os_build=linux
export SHARED_LIB=lib${LIBRARY_NAME}.so

#this would work if we were on x86_64
#export CXX=g++
#export CC=gcc-7
# x86_64 → Linux (glibc)
export CC=x86_64-linux-gnu-gcc
export CXX=x86_64-linux-gnu-g++
export AR=x86_64-linux-gnu-ar
export RANLIB=x86_64-linux-gnu-ranlib
export STRIP=x86_64-linux-gnu-strip
export LD=x86_64-linux-gnu-ld
# (optional, helps when cross pkg-config files are installed)
export PKG_CONFIG=x86_64-linux-gnu-pkg-config
# or: export PKG_CONFIG_LIBDIR=/usr/x86_64-linux-gnu/pkgconfig:/usr/lib/x86_64-linux-gnu/pkgconfig


export run_openssl_configure="./Configure linux-x86_64 ${OPENSSL_NO_OPTS} -fPIC --prefix=${OPENSSL_ROOT}"
export run_readelf="readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j${CORES} --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_objcopy="objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/${os_arch}/${SHARED_LIB}.debug"
export run_strip="strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_readelf="readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_native_jar="./gradlew nativeLinuxX86_64Jar"
export run_clean_native_jar="./gradlew cleanNativeLinuxX86_64Jar"
press_any_to_continue
prepare_libtorrent
export BOOST_ROOT=/src/boost_${BOOST_UNDERSCORE_VERSION}
./run-swig.sh
build_libraries
