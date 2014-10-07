#!/bin/sh

export CXXFLAGS="-mthumb -fno-strict-aliasing -lstdc++ -D__GLIBC__ -D_GLIBCXX__PTHREADS -D__arm__ -D_REENTRANT -O3 -DTORRENT_USE_IPV6=1 -DNDEBUG=1 -DBOOST_ASIO_SEPARATE_COMPILATION=1 -DTORRENT_USE_CLOCK_GETTIME=1 -DTORRENT_DISABLE_GEO_IP=1 -DTORRENT_NO_DEPRECATE=1"

INCLUDES="-I$BOOST_ROOT -I/Users/aldenml/Development/android-libtorrent/libtorrent-rasterbar-1.0.2/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/include/ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/include/darwin"
LIBS="-ltorrent -lboost_system -lboost_chrono -lboost_date_time -lboost_thread -Wl,-Bsymbolic"
TARGET="libjlibtorrent.so"

$CXX $CXXFLAGS $INCLUDES -c libtorrent_jni.cpp
$CXX -shared -o $TARGET libtorrent_jni.o $LIBS

rm -rf libtorrent_jni.o
