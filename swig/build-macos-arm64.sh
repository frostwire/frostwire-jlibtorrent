#!/usr/bin/env bash
# This script is meant to run here inside the swig folder
# It's supposed to be a one step build for the java jar and macosx (.dylib enclosing) jar
# Output .jar files will be at:
# ../build/libs/${LIBRARY_NAME}-<version>.jar
# ../build/libs/${LIBRARY_NAME}-macosx-<version>.jar
source build-utils.shinc

# Default behavior is to run both swig and build
run_prep=true
run_swig_only=false
run_build_only=false
# Call the function to parse flags
parse_flags "$@"

macosx_env
common_env
check_min_req_vars

export os_arch=arm64
export os_build=macosx
export SHARED_LIB=lib${LIBRARY_NAME}.dylib
export RELEASE_SHARED_LIB=lib${LIBRARY_NAME}.${os_arch}.dylib
export CXX=g++
export CC=gcc
export CORES=$(sysctl -n hw.ncpu)
export run_openssl_configure="./Configure darwin64-${os_arch}-cc ${OPENSSL_NO_OPTS} --prefix=${OPENSSL_ROOT}"
export run_readelf="otool -L bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_bjam="${BOOST_ROOT}/b2 -j${CORES} -d2 --user-config=config/${os_build}-${os_arch}-config.jam variant=release toolset=darwin-${os_arch} target-os=darwin location=bin/release/${os_build}/${os_arch}"
export run_objcopy="echo dummy run_objcopy for ${os_build} ${os_arch}"
export run_strip="strip -S -x bin/release/${os_build}/${os_arch}/${SHARED_LIB}"
export run_native_jar="./gradlew nativeMacOSArm64Jar"
export run_clean_native_jar="./gradlew cleanNativeMacOSArm64Jar"
create_folder_if_it_doesnt_exist ${SRC}
prompt_msg "$0:About to prepare BOOST ${BOOST_VERSION}"

if [ "${run_prep}" = true ]; then
    press_any_to_continue
    prepare_boost
    prepare_openssl
    prepare_libtorrent
    build_openssl
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
