#!/bin/bash

#$BOOST_ROOT
#$LIBTORRENT_ROOT
#$JDK_INCLUDE_1
#$JDK_INCLUDE_2
#$LIBTORRENT_LIBS

#export CXXFLAGS="-stdlib=libc++ -std=c++11 -O3 -flto -I$BOOST_ROOT"
#$BOOST_ROOT/b2 variant=release link=static --stagedir=macosx stage --without-context --without-coroutine --without-python --without-mpi --without-wave --without-test --without-graph --without-graph_parallel --without-iostreams
#$BOOST_ROOT/bjam toolset=darwin variant=release link=static deprecated-functions=off logging=off

CXX=clang++
CXXFLAGS="-stdlib=libc++ -std=c++11 -O3 -flto"
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_ABSOLUTE_TIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$LIBTORRENT_ROOT/include/libtorrent -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_random"
LDFLAGS="-flto -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.dylib"

$CXX $CXXFLAGS $DEFINES $INCLUDES -c swig/libtorrent_jni.cpp
$CXX -dynamiclib -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

strip -S -x $TARGET
rm -rf libtorrent_jni.o
