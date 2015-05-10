#!/bin/bash

#$BOOST_ROOT
#$LIBTORRENT_ROOT
#$JDK_INCLUDE_1
#$JDK_INCLUDE_2
#$LIBTORRENT_LIBS

#$BOOST_ROOT/b2 cxxflags="-Os" variant=release link=static --stagedir=macosx stage --without-context --without-coroutine --without-python --without-mpi --without-wave --without-test --without-graph --without-graph_parallel --without-iostreams --without-math
#$BOOST_ROOT/bjam toolset=darwin cxxflags="-Os" variant=release link=static deprecated-functions=off logging=on boost=source

CXX=g++
CXXFLAGS="-std=c++11 -Os"
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_ABSOLUTE_TIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_random"
LDFLAGS="-L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.dylib"

$CXX $CXXFLAGS $DEFINES $INCLUDES -c swig/libtorrent_jni.cpp
$CXX -dynamiclib -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

strip -S -x $TARGET
rm -rf libtorrent_jni.o
