#!/usr/bin/env bash
# This script is meant to run here inside the swig folder
# It's supposed to be a one step build for the java jar and windows x86 (.dll enclosing) jar
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-windows-x86-<version>.jar
./run-swig.sh
source build-utils.shinc
check_min_req_vars
os_arch=x86
os_build=windows
SHARED_LIB=lib${LIBRARY_NAME}.dll
CXX=g++
CC=i686-w64-mingw32-gcc-posix
run_openssl_configure="./Configure mingw ${OPENSSL_NO_OPTS} --prefix=$OPENSSL_SOURCE/../openssl"
run_readelf="eval objdump -p bin/release/${os_build}/${os_arch}/jlibtorrent.dll | grep DLL"
run_bjam="${BOOST_ROOT}/b2 -j8 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
run_strip="i686-w64-mingw32-strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/libjlibtorrent.dll"
run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"
prepare_windows_x86_toolchain
create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "About to prepare BOOST ${BOOST_VERSION}"
press_any_to_continue
prepare_boost
prepare_openssl
build_openssl
prepare_libtorrent
build_libraries
