#!/usr/bin/env bash
# This script is meant to run here inside the swig folder in the Docker container
# It's supposed to be a one step build for the java jar and windows x86 (.dll enclosing) jar
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-windows-x86-<version>.jar
source build-utils.shinc
# Default behavior is to run both swig and build
run_prep=true
run_swig_only=false
run_build_only=false
# Call the function to parse flags
parse_flags "$@"

windows_env
common_env
check_min_req_vars
export os_arch=x86_64
export os_build=windows
export SHARED_LIB=lib${LIBRARY_NAME}.dll
export CXX=x86_64-w64-mingw32-g++-posix
export CC=x86_64-w64-mingw32-gcc-posix
export CORES=$(( $(nproc) / 2 ))
export run_bjam="${BOOST_ROOT}/b2 -j${CORES} --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=gcc-${os_arch} target-os=${os_build} location=bin/release/${os_build}/${os_arch}"
export run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"
export run_strip="x86_64-w64-mingw32-strip --strip-unneeded -x bin/release/${os_build}/${os_arch}/libjlibtorrent.dll";
export run_readelf="eval objdump -p bin/release/${os_build}/${os_arch}/jlibtorrent.dll | grep DLL"
export run_native_jar="./gradlew nativeWindowsX86_64Jar"
export run_clean_native_jar="./gradlew cleanNativeWindowsX86_64Jar"

if [ "${run_prep}" = true ]; then
    prepare_libtorrent
fi

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
