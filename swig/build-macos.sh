#!/usr/bin/env bash

export DEVELOPMENT_ROOT=~/Development
export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_62_0
export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent
export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-macos

$BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam variant=release toolset=clang-x86_64 target-os=darwin location=bin/release/macosx/x86_64
cp bin/release/macosx/x86_64/libjlibtorrent.dylib ../

export ANDROID_TOOLCHAIN=$DEVELOPMENT_ROOT/android-toolchain-arm
export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-arm
export PATH=$ANDROID_TOOLCHAIN/arm-linux-androideabi/bin:$PATH
$BOOST_ROOT/b2 --user-config=config/android-arm-config.jam variant=release toolset=clang-linux-arm target-os=android location=bin/release/android/armeabi-v7a
#${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-objcopy --only-keep-debug libjlibtorrent.so libjlibtorrent.so.debug;
#${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-objcopy --strip-debug libjlibtorrent.so;
#${ANDROID_TOOLCHAIN}/bin/arm-linux-androideabi-objcopy --add-gnu-debuglink=libjlibtorrent.so.debug libjlibtorrent.so;
