#!/bin/bash

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android.arm target-os=android
TARGET=../binaries/android/armeabi-v7a/libjlibtorrent.so
cp bin/gcc-android.arm/release/boost-source/crypto-openssl/deprecated-functions-off/target-os-android/libjlibtorrent.so.1.1.0 $TARGET
$NDK_ROOT/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-strip --strip-unneeded -x $TARGET

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android.x86 target-os=android
TARGET=../binaries/android/x86/libjlibtorrent.so
cp bin/gcc-android.x86/release/boost-source/crypto-openssl/deprecated-functions-off/target-os-android/libjlibtorrent.so.1.1.0 $TARGET
$NDK_ROOT/toolchains/x86-4.9/prebuilt/darwin-x86_64/bin/i686-linux-android-strip --strip-unneeded -x $TARGET
