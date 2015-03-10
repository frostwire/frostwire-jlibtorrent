#!/bin/bash

#$BOOST_ROOT
#$LIBTORRENT_ROOT
#$JDK_INCLUDE_1
#$JDK_INCLUDE_2
#$LIBTORRENT_LIBS

#copy user-config.jam to ~/ change it for intel if necessary
#make-standalone-toolchain.sh --platform=android-15 --arch=arm --toolchain=arm-linux-androideabi-4.8 --install-dir=toolchain-platform-15-arm-gcc-4.8
#ARM: export CXXFLAGS="-mthumb -fno-strict-aliasing -lstdc++ -O3 -D__GLIBC__ -D_GLIBCXX__PTHREADS -D__arm__ -D_REENTRANT -I$BOOST_ROOT"
#X86: export CXXFLAGS="-fno-strict-aliasing -lstdc++ -O3 -D__GLIBC__ -D_GLIBCXX__PTHREADS -D_REENTRANT -I$BOOST_ROOT"
#$BOOST_ROOT/b2 toolset=gcc-arm variant=release link=static target-os=linux --stagedir="android-arm" stage
#$BOOST_ROOT/bjam toolset=gcc-arm variant=release link=static target-os=linux deprecated-functions=off

CXX=arm-linux-androideabi-g++
CXXFLAGS="-mthumb -D__arm__ -fno-strict-aliasing -lstdc++ -O3 -D__GLIBC__ -D_GLIBCXX__PTHREADS -D_REENTRANT"
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent_trunk -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_atomic -lboost_random"
LDFLAGS="-Wl,-Bsymbolic -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $DEFINES $INCLUDES -std=c++11 -c swig/libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS $LDFLAGS

rm -rf libtorrent_jni.o
