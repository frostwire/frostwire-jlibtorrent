#!/bin/bash

#$BOOST_ROOT
#$LIBTORRENT_ROOT
#$JDK_INCLUDE_1
#$JDK_INCLUDE_2
#$LIBTORRENT_LIBS

#copy user-config.jam to home
#make-standalone-toolchain.sh --platform=android-15 --arch=arm --toolchain=arm-linux-androideabi-4.9 --install-dir=toolchain-platform-15-arm-gcc-4.9
#$BOOST_ROOT/b2 toolset=gcc-arm variant=release link=static target-os=linux --stagedir="android-arm" stage --without-context --without-coroutine --without-python --without-mpi --without-wave --without-test --without-graph --without-graph_parallel --without-iostreams --without-math
#$BOOST_ROOT/bjam toolset=gcc-arm variant=release link=static target-os=linux deprecated-functions=off logging=none boost=source

CXX=arm-linux-androideabi-g++
CXXFLAGS="-mthumb -fno-strict-aliasing -lstdc++ -std=c++11 -O3 -march=armv7-a -DNDEBUG -D__GLIBC__ -D_GLIBCXX__PTHREADS -D__arm__ -D_REENTRANT"
DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_atomic -lboost_random"
LDFLAGS="-Wl,-Bsymbolic -march=armv7-a -L$LIBTORRENT_LIBS"
TARGET="libjlibtorrent.so"

#$CXX $CXXFLAGS $DEFINES $INCLUDES -c swig/libtorrent_jni.cpp
#$CXX -shared -o $TARGET libtorrent_jni.o $LDFLAGS $LIBS

#arm-linux-androideabi-strip --strip-unneeded -x $TARGET
#rm -rf libtorrent_jni.o

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android_arm target-os=linux
