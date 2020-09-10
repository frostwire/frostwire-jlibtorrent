#!/usr/bin/env bash
# This script is meant to run here inside the swig/ folder
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-linux-<version>.jar

# remote linux-x86 build with travis is available at https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86_64/libjlibtorrent.so

source build-utils.shinc
check_min_req_vars

export os_arch=x86_64
export os_build=linux
export SHARED_LIB=lib${LIBRARY_NAME}.so
export SHARED_LIB_FINAL=${SHARED_LIB} # dummy for macosx
export CXX=g++
prepare_linux_toolchain
export CC=gcc-7
export run_openssl_configure="./Configure linux-x86_64 ${OPENSSL_NO_OPTS} -fPIC --prefix=${OPENSSL_ROOT}";
export run_readelf="readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_strip="strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_objcopy="objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/{$os_arch}/${SHARED_LIB}.debug"
sed -i 's/RANLIB = ranlib/RANLIB = "${ANDROID_TOOLCHAIN}\/bin\/i686-linux-android-ranlib"/g' ${BOOST_ROOT}/tools/build/src/tools/gcc.jam;

create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "About to prepare BOOST ${BOOST_VERSION}"
press_any_to_continue
prepare_boost
prepare_openssl
build_openssl
prepare_libtorrent
build_libraries
cleanup_objects
