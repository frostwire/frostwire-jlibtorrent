#!/bin/bash

export DEVELOPMENT_ROOT=~/Development
export TOOLCHAINS_ROOT=$DEVELOPMENT_ROOT/toolchains

export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_59_0
export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent

export OSXCROSS_NO_INCLUDE_PATH_WARNINGS=1

function buildMacOSX()
{
    $BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
    $TOOLCHAINS_ROOT/macosx-x86_64/bin/x86_64-apple-darwin15-strip -S -x bin/macosx/x86_64/libjlibtorrent.dylib
}

function buildAndroidArm()
{
    $BOOST_ROOT/b2 --user-config=config/android-arm-config.jam toolset=gcc-arm target-os=linux location=bin/android/armeabi-v7a
    $TOOLCHAINS_ROOT/android-arm/bin/arm-linux-androideabi-strip --strip-unneeded -x bin/android/armeabi-v7a/libjlibtorrent.so
}

function buildAndroidX86()
{
    $BOOST_ROOT/b2 --user-config=config/android-x86-config.jam toolset=gcc-x86 target-os=linux location=bin/android/x86
    $TOOLCHAINS_ROOT/android-x86/bin/i686-linux-android-strip --strip-unneeded -x bin/android/x86/libjlibtorrent.so
}

function buildAndroidArm64()
{
    $BOOST_ROOT/b2 --user-config=config/android-arm64-config.jam toolset=gcc-arm64 target-os=linux location=bin/android/arm64-v8a
    $TOOLCHAINS_ROOT/android-arm/bin/aarch64-linux-android-strip --strip-unneeded -x bin/android/arm64-v8a/libjlibtorrent.so
}

function buildWindowsX86()
{
    $BOOST_ROOT/b2 --user-config=config/windows-x86-config.jam toolset=gcc-x86 target-os=windows location=bin/windows/x86
    $TOOLCHAINS_ROOT/windows-x86/bin/i686-w64-mingw32-strip --strip-unneeded -x bin/windows/x86/libjlibtorrent.dll
    mv bin/windows/x86/libjlibtorrent.dll bin/windows/x86/jlibtorrent.dll
}

function buildWindowsX86_64()
{
    $BOOST_ROOT/b2 --user-config=config/windows-x86_64-config.jam toolset=gcc-x86_64 target-os=windows location=bin/windows/x86_64
    $TOOLCHAINS_ROOT/windows-x86_64/bin/x86_64-w64-mingw32-strip --strip-unneeded -x bin/windows/x86_64/libjlibtorrent.dll
    mv bin/windows/x86_64/libjlibtorrent.dll bin/windows/x86_64/jlibtorrent.dll
}

#buildMacOSX

#buildAndroidArm
buildAndroidX86

#buildAndroidArm64

#fixes for windows
ORIGINAL_BOOST_ROOT=$BOOST_ROOT
export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_55_0

sed -i 's/ JNICALL Java_com_frostwire/ JNICALL _Java_com_frostwire/g' libtorrent_jni.cpp
#buildWindowsX86
sed -i 's/ JNICALL _Java_com_frostwire/ JNICALL Java_com_frostwire/g' libtorrent_jni.cpp

#buildWindowsX86_64

export BOOST_ROOT=$ORIGINAL_BOOST_ROOT
