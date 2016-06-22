#!/usr/bin/env bash

export DEVELOPMENT_ROOT=~/Development
export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0
export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent

# native OSX
export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-osx

$BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
cp bin/macosx/x86_64/libjlibtorrent.dylib ../

# android arm
#export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-android-arm
#export ANDROID_TOOLCHAIN=$DEVELOPMENT_ROOT/ndk-arm-toolchain

#$BOOST_ROOT/b2 --user-config=config/android-arm-config.jam toolset=clang-arm target-os=android location=bin/android/armeabi-v7a wrap-posix=on;
#cp bin/android/armeabi-v7a/libjlibtorrent.so ../android/src/main/jniLibs/armeabi-v7a

# android x86
#export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-android-x86
#export ANDROID_TOOLCHAIN=$DEVELOPMENT_ROOT/ndk-x86-toolchain

#$BOOST_ROOT/b2 --user-config=config/android-x86-config.jam toolset=gcc-x86 target-os=linux location=bin/android/x86 wrap-posix=on;
#cp bin/android/x86/libjlibtorrent.so ../android/src/main/jniLibs/x86

node-gyp configure build
cp build/Release/jlibtorrent.node ../node
