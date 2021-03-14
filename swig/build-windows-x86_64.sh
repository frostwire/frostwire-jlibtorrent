#!/usr/bin/env bash
# This script is meant to run here inside the swig folder in the Docker container
# It's supposed to be a one step build for the java jar and windows x86 (.dll enclosing) jar
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-windows-x86-<version>.jar
source build-utils.shinc
windows_env
common_env
check_min_req_vars
os_arch=x86_64
os_build=windows
SHARED_LIB=lib${LIBRARY_NAME}.dll
CXX=g++
export CXX=x86_64-w64-mingw32-g++-posix
export CC=x86_64-w64-mingw32-gcc-posix
run_bjam="${BOOST_ROOT}/b2 -j2 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
run_readelf="eval objdump -p bin/release/${os_build}/${os_arch}/jlibtorrent.dll | grep DLL"
run_strip="x86_64-w64-mingw32-strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/libjlibtorrent.dll";
run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"
prepare_libtorrent
./run-swig.sh
build_libraries
