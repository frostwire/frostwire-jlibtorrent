#!/bin/bash

#sudo add-apt-repository ppa:deluge-team/develop
#sudo apt-get update
#sudo apt-get install libtorrent-rasterbar8
#
#OR from downloaded sources
#
#LIBTORRENT_ROOT=./libtorrent-rasterbar-1.0.2
#BOOST_ROOT=./boost_1_56_0
#
#Compile libtorrent-rasterbar.so.8 and boost libs, copy to
#LIBTORRENT_LIBS=./binlib

JDK_INCLUDE_1=/usr/lib/jvm/java-7-openjdk-amd64/include
JDK_INCLUDE_2=/usr/lib/jvm/java-7-openjdk-amd64/include/linux

CXX=g++
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_DYN_LINK=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-l:libtorrent-rasterbar.so.8"
CXXFLAGS="-fPIC -fno-strict-aliasing -O3"
LDFLAGS="-fPIC -Wl,-Bsymbolic -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

rm libtorrent_jni.o
