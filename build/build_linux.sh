#!/bin/bash
# NOTE: run ./build/configure_linux.sh script to download/update all the necessary dependencies.
source ./build/environment_linux.sh
sanityCheck

CXX=g++
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1 -DTORRENT_USE_OPENSSL=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include/ -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_random -lssl -lcrypto"
CXXFLAGS="-fPIC -fno-strict-aliasing -O2"

LDFLAGS="-Wl,-Bsymbolic -pthread -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

strip --strip-unneeded -x $TARGET
rm -rf libtorrent_jni.o
