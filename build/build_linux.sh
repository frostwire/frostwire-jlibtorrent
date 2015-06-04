#!/bin/bash

# First run ./build/configure_linux.sh script to download all the necessary dependencies.

# Boost and libtorrent versions:
LIBTORRENT_VERSION="RC_1_0"
BOOST_VERSION="1_58_0"


# Set the exports
export LIBTORRENT_LIBS="$HOME/LIBTORRENT_LIBS"
export LIBTORRENT_ROOT="$LIBTORRENT_LIBS/libtorrent-$LIBTORRENT_VERSION"
export BOOST_ROOT="$LIBTORRENT_LIBS/boost_$BOOST_VERSION"

# Set the java home directories
JDK_INCLUDE_1=$JAVA_HOME/include
JDK_INCLUDE_2=$JAVA_HOME/include/linux

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
