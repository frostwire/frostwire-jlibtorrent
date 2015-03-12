#!/bin/bash

# Make sure these environment variables set and exported.
# $BOOST_ROOT
# $LIBTORRENT_ROOT

# LIBTORRENT_LIBS is Ideally a folder where all the libtorrent .a files will be.
# $LIBTORRENT_LIBS

# BUILDING BOOST
# The first time you will need to build boost using the following 2 lines.
# export CXXFLAGS="-std=c++11 -O3 -fPIC -I$BOOST_ROOT"

# Download boost from boost.org (1.56 is the version we use, we've had high CPU usage issues with 1.57 and Windows Networking)
# run the ./bootstrap.sh script so that ./b2 will work. Then execute:
# $BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags=-fPIC cflags=-fPIC --with-chrono --with-system --with-random --with-thread --with_date_time --with-filesystem; cp $BOOST_ROOT/linux/lib/*.a $LIBTORRENT_LIBS/

# BUILDING libtorrent
# After you've built boost, you should build libtorrent like this from your $LIBTORRENT_ROOT folder.
# $BOOST_ROOT/bjam toolset=gcc variant=release link=static deprecated-functions=off boost=source cxxflags=-fPIC cflags=-fPIC; cp $LIBTORRENT_ROOT/bin/gcc-4.9.1/release/boost-source/deprecated-functions-off/link-static/threading-multi/libtorrent.a $LIBTORRENT_LIBS/libtorrent_trunk.a; cp $LIBTORRENT_LIBS/libtorrent_trunk.a $LIBTORRENT_LIBS/libtorrent.a

# Once you've build boost and libtorrent, copy all the .a files to $LIBTORRENT_LIBS wherever that may be.
# You should end up with a list like follows:
# 
# ls -l $LIBTORRENT_LIBS
# total 10128
# -rw-rw-r-- 1 gubatron gubatron  115194 Mar 10 00:03 libboost_chrono.a
# -rw-rw-r-- 1 gubatron gubatron   39954 Mar 10 00:03 libboost_random.a
# -rw-rw-r-- 1 gubatron gubatron   22362 Mar 10 00:03 libboost_system.a
# -rw-rw-r-- 1 gubatron gubatron  275180 Mar 10 00:03 libboost_thread.a
# -rw-rw-r-- 1 gubatron gubatron 9904828 Mar 10 00:03 libtorrent.a

# OTHER REQUIREMENTS
# If you are in Ubuntu:
# sudo apt-get install build-essentials libpcre3-dev
#
# Download swig 3 from swig.org (./configure; ./make; sudo make install)

JDK_INCLUDE_1=/usr/lib/jvm/java-7-openjdk-amd64/include
JDK_INCLUDE_2=/usr/lib/jvm/java-7-openjdk-amd64/include/linux

CXX=g++
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_BOOST_CHRONO=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent_trunk -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_random"
CXXFLAGS="-fPIC -fno-strict-aliasing -O3"
LDFLAGS="-fPIC -Wl,-Bsymbolic -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

rm -rf libtorrent_jni.o
