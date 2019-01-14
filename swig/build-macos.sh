#!/usr/bin/env bash
# This script is meant to run here inside frostwire-jlibtorrent/swig folder
# It's supposed to be a one step build for the java jar and macosx (.dylib enclosing) jar
# Output .jar files will be at:
# frostwire-jlibtorrent/build/libs/jlibtorrent-<version>.jar
# frostwire-jlibtorrent/build/libs/jlibtorrent-macosx-<version>.jar

source build-utils.shinc
check_min_req_vars

export os_arch=x86_64
export os_build=macosx
export SHARED_LIB=libjlibtorrent.dylib
export SHARED_LIB_FINAL=${SHARED_LIB} # dummy for macosx
export CXX=g++
export CC=gcc
export run_openssl_configure="./Configure darwin64-${os_arch}-cc ${OPENSSL_NO_OPTS} --prefix=${OPENSSL_ROOT}";
export run_readelf="otool -L bin/release/${os_build}/${os_arch}/${SHARED_LIB}";
export run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=darwin-${os_arch} target-os=darwin location=bin/release/${os_build}/${os_arch}"
export run_strip="strip -S -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"

create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "About to prepare BOOST ${BOOST_VERSION}"
press_any_to_continue
prepare_boost
prepare_openssl
build_openssl
prepare_libtorrent
build_libraries
cleanup_objects

