#!/usr/bin/env bash
# This script is meant to run here inside the swig folder
# It's supposed to be a one step build for the java jar and android (.so enclosing) jars (armv7 and x86)
# Output .jar files will be at:
# ../build/libs/jlibtorrent-<version>.jar
# ../build/libs/${LIBRARY_NAME}-android-x86-<version>.jar

# remote android-x86 build with travis is available at https://s3.amazonaws.com/gubatron-${LIBRARY_NAME}/release/android/x86/lib${LIBRARY_NAME}.so

source build-utils.shinc
check_min_req_vars

export os_arch=x86
export os_build=android
export android_api=19
export SHARED_LIB=lib${LIBRARY_NAME}.so
export SHARED_LIB_FINAL=${SHARED_LIB} # dummy for macosx

export CXX=g++
export NDK_VERSION=r21d

prepare_android_toolchain
abort_if_var_unset "ANDROID_TOOLCHAIN" ${ANDROID_TOOLCHAIN}
export CC=$ANDROID_TOOLCHAIN/bin/i686-linux-android-clang

export run_openssl_configure="./Configure linux-elf ${OPENSSL_NO_OPTS} -fPIC -mstackrealign --prefix=${OPENSSL_ROOT}";
export run_readelf="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-readelf -d bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=clang--${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_strip="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-strip --strip-unneeded -x -g bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_objcopy="${ANDROID_TOOLCHAIN}/bin/i686-linux-android-objcopy --only-keep-debug bin/release/${os_build}/${os_arch}/${SHARED_LIB} bin/release/${os_build}/{$os_arch}/${SHARED_LIB}.debug"
export PATH=$ANDROID_TOOLCHAIN/i686-linux-android/bin:$PATH;
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
