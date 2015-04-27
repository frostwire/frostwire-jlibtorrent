#!/bin/bash

# Make sure these environment variables set and exported.
# $BOOST_ROOT
# $LIBTORRENT_ROOT

# LIBTORRENT_LIBS is Ideally a folder where all the libtorrent .a files will be.
# $LIBTORRENT_LIBS

# BUILDING BOOST

# Download boost from boost.org (1.58 is the version we use)
# run the ./bootstrap.sh script so that ./b2 will work. Then execute:

# Release for Desktop
# $BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags="-O3 -fPIC" --with-date_time --with-chrono --with-system --with-random --with-thread

# BUILDING libtorrent
# After you've built boost, you should build libtorrent like this from your $LIBTORRENT_ROOT folder.
# $BOOST_ROOT/bjam toolset=gcc cxxflags="-O3 -fPIC" cflags="-fPIC" variant=release link=static deprecated-functions=off logging=none boost=source

# Once you've build boost and libtorrent, copy all the .a files to $LIBTORRENT_LIBS wherever that may be.

JDK_INCLUDE_1=/usr/lib/jvm/java-8-openjdk-amd64/include
JDK_INCLUDE_2=/usr/lib/jvm/java-8-openjdk-amd64/include/linux

CXX=g++
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include/ -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_random"
CXXFLAGS="-fPIC -fno-strict-aliasing -O3"

LDFLAGS="-Wl,-Bsymbolic -pthread -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

strip --strip-unneeded -x $TARGET
rm -rf libtorrent_jni.o
