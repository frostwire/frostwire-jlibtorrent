#!/bin/bash

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android.arm target-os=android location=bin/android/armeabi-v7a
mv bin/android/armeabi-v7a/libjlibtorrent.so.1.1.0 bin/android/armeabi-v7a/libjlibtorrent.so
TARGET=bin/android/armeabi-v7a/libjlibtorrent.so
$NDK_ROOT/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-strip --strip-unneeded -x $TARGET

$BOOST_ROOT/bjam --user-config=android-config.jam toolset=gcc-android.x86 target-os=android location=bin/android/x86
mv bin/android/x86/libjlibtorrent.so.1.1.0 bin/android/x86/libjlibtorrent.so
TARGET=bin/android/x86/libjlibtorrent.so
$NDK_ROOT/toolchains/x86-4.9/prebuilt/darwin-x86_64/bin/i686-linux-android-strip --strip-unneeded -x $TARGET
