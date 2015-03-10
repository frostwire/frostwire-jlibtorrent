#!/bin/bash

# Make sure these environment variables set.
#$BOOST_ROOT
#$LIBTORRENT_ROOT

# LIBTORRENT_LIBS is Ideally a folder where all the libtorrent .a files will be.
#$LIBTORRENT_LIBS

# The first time you will need to build boost using the following 2 lines.
#export CXXFLAGS="-std=c++11 -O3 -fPIC -I$BOOST_ROOT"
# Download boost from boost.org (1.56 is the version we use, we've had high CPU usage issues with 1.57 and Windows Networking)
# run the ./bootstrap.sh script so that ./b2 will work. Then execute:
#$BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags=-fPIC cflags=-fPIC --with-chrono

# After you've built boost, you should build libtorrent like this from your $LIBTORRENT_ROOT folder.
#$BOOST_ROOT/bjam toolset=gcc variant=release link=static deprecated-functions=off boost=source cxxflags=-fPIC cflags=-fPIC

# All the lines above we leave commented as they're only needed as requirements to build frostwire-jlibtorrent.
# All further compilations will just need the script below.

JDK_INCLUDE_1=/usr/lib/jvm/java-7-openjdk-amd64/include
JDK_INCLUDE_2=/usr/lib/jvm/java-7-openjdk-amd64/include/linux

CXX=g++
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent_trunk -lboost_system -lboost_chrono -lboost_date_time -lboost_thread"
CXXFLAGS="-fPIC -fno-strict-aliasing -O3"
LDFLAGS="-fPIC -Wl,-Bsymbolic -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

rm -rf libtorrent_jni.o
