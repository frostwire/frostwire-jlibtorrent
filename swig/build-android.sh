#!/bin/bash

#CXXFLAGS="-mthumb -fno-strict-aliasing -lstdc++ -std=c++11 -O3 -march=armv7-a -DNDEBUG -D__GLIBC__ -D_GLIBCXX__PTHREADS -D__arm__ -D_REENTRANT"
#DEFINES="-DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1 -DBOOST_ASIO_DISABLE_STD_CHRONO=1 -DBOOST_ASIO_HAS_BOOST_CHRONO=1"
#INCLUDES="-I$BOOST_ROOT -I$LIBTORRENT_ROOT/include -I$JDK_INCLUDE_1 -I$JDK_INCLUDE_2"
#LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -lboost_atomic -lboost_random"
#LDFLAGS="-Wl,-Bsymbolic -march=armv7-a -L$LIBTORRENT_LIBS"
#TARGET="libjlibtorrent.so"

#$CXX $CXXFLAGS $DEFINES $INCLUDES -c swig/libtorrent_jni.cpp
#$CXX -shared -o $TARGET libtorrent_jni.o $LDFLAGS $LIBS

#
#rm -rf libtorrent_jni.o

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android.arm target-os=android
TARGET=../binaries/android/armeabi-v7a/libjlibtorrent.so
cp bin/gcc-android.arm/release/boost-source/crypto-openssl/deprecated-functions-off/target-os-android/libjlibtorrent.so.1.1.0 $TARGET
$NDK_ROOT/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-strip --strip-unneeded -x $TARGET
