#!/bin/bash

export DEVELOPMENT_ROOT=~/Development
export TOOLCHAINS_ROOT=$DEVELOPMENT_ROOT/toolchains

if [ -z ${BOOST_ROOT+x} ]; then
    export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0
fi

if [ -z ${LIBTORRENT_ROOT+x} ]; then
    export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent
fi

export OSXCROSS_NO_INCLUDE_PATH_WARNINGS=1

function buildMacOSX()
{
    $BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
    $TOOLCHAINS_ROOT/macosx-x86_64/bin/x86_64-apple-darwin15-strip -S -x bin/macosx/x86_64/libjlibtorrent.dylib
}

function buildMacOSXNative() {
    $BOOST_ROOT/b2 --user-config=config/macosx-native-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
    TARGET=bin/macosx/x86_64/libjlibtorrent.dylib
    strip -S -x $TARGET
    cp $TARGET ../

    node-gyp configure build
    cp build/Release/jlibtorrent.node ../node
}

function buildAndroidArm()
{
    $BOOST_ROOT/b2 --user-config=config/android-arm-config.jam toolset=gcc-arm target-os=linux location=bin/android/armeabi-v7a wrap-posix=on
    $TOOLCHAINS_ROOT/android-arm/bin/arm-linux-androideabi-strip --strip-unneeded -x bin/android/armeabi-v7a/libjlibtorrent.so
}

function buildAndroidX86()
{
    $BOOST_ROOT/b2 --user-config=config/android-x86-config.jam toolset=gcc-x86 target-os=linux location=bin/android/x86 wrap-posix=on
    $TOOLCHAINS_ROOT/android-x86/bin/i686-linux-android-strip --strip-unneeded -x bin/android/x86/libjlibtorrent.so
}

function buildAndroidArm64()
{
    $BOOST_ROOT/b2 --user-config=config/android-arm64-config.jam toolset=gcc-arm64 target-os=linux location=bin/android/arm64-v8a wrap-posix=on
    $TOOLCHAINS_ROOT/android-arm64/bin/aarch64-linux-android-strip --strip-unneeded -x bin/android/arm64-v8a/libjlibtorrent.so
}

function buildAndroidX86_64()
{
    $BOOST_ROOT/b2 --user-config=config/android-x86_64-config.jam toolset=gcc-x86_64 target-os=linux location=bin/android/x86_64 wrap-posix=on
    $TOOLCHAINS_ROOT/android-x86_64/bin/x86_64-linux-android-strip --strip-unneeded -x bin/android/x86_64/libjlibtorrent.so
}

function buildLinuxX86()
{
    $BOOST_ROOT/b2 --user-config=config/linux-x86-config.jam toolset=gcc-x86 target-os=linux location=bin/linux/x86
    strip --strip-unneeded -x bin/linux/x86/libjlibtorrent.so
}

function buildLinuxX86_64()
{
    $BOOST_ROOT/b2 --user-config=config/linux-x86_64-config.jam toolset=gcc-x86_64 target-os=linux location=bin/linux/x86_64
    strip --strip-unneeded -x bin/linux/x86_64/libjlibtorrent.so
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

function usage() {
  echo "jlibtorrent's build script exclusive options. (can only use one at the time)"
  echo ""
  echo "--h,-h           Shows this help."
  echo ""
  echo "--osx            Build MacOSX x86_64 library only. (must be used on Linux)"
  echo "--osx-native     Build MaxOSX x86_64 library only. (must be used on Mac)"
  echo ""
  echo "--android        Build Android library for arm32, arm64, x86, x86_64."
  echo "--androidarm     Build Android library for arm32, arm64 only."
  echo "--androidarm32   Build Android library for arm32 only."
  echo "--androidarm64   Build Android library for arm64 only."
  echo "--androidintel   Build Android library for x86, x86_64 only."
  echo "--androidintel32 Build Android library for x86 only." 
  echo "--androidintel64 Build Android library for x86_64 only." 
  echo ""
  echo "--linux          Build Linux library for x86, x86_64."
  echo "--linux32        Build Linux library for x86 only."
  echo "--linux64        Build Linux library for x86_64 only."
  echo ""
  echo "--win            Build Windows library for x86, x86_64."
  echo "--win32          Build Windows library for x86 only."
  echo "--win64          Build Windows library for x86_64 only."
  echo ""
  echo "If no options are passed, you will be prompted to make sure you want to build all architectures in one go."
  echo ""
  echo ""
  echo "AUTHORS"
  echo "jlibtorrent was written by Alden Torres and Angel Leon."
  echo "libtorrent was written by Arvid Norberg and multiple contributors."
  echo ""
  echo "Submit issues, feedback and contributions to"
  echo "https://github.com/frostwire/frostwire-jlibtorrent"
  echo ""
}


if [ "$1" == "--help" -o "$1" == "-h" ]; then
  usage;
  exit 1;
elif [ "$1" == "--osx" ]; then
  buildMacOSX
  exit 1;
elif [ "$1" == "--osx-native" ]; then
  buildMacOSXNative
  exit 1;
elif [ "$1" == "--android" ]; then
  buildAndroidArm
  buildAndroidX86
  buildAndroidArm64
  buildAndroidX86_64
  exit 1;
elif [ "$1" == "--androidarm" ]; then
  buildAndroidArm
  buildAndroidArm64
  exit 1;
elif [ "$1" == "--androidarm32" ]; then
  buildAndroidArm
  exit 1;
elif [ "$1" == "--androidarm64" ]; then
  buildAndroidArm64
  exit 1;
elif [ "$1" == "--androidintel" ]; then
  buildAndroidX86
  buildAndroidX86_64
  exit 1;
elif [ "$1" == "--androidintel32" ]; then
  buildAndroidX86
  exit 1;
elif [ "$1" == "--androidintel64" ]; then
  buildAndroidX86_64
  exit 1;
elif [ "$1" == "--linux" ]; then
  buildLinuxX86
  buildLinuxX86_64
  exit 1;
elif [ "$1" == "--linux32" ]; then
  buildLinuxX86
  exit 1;
elif [ "$1" == "--linux64" ]; then
  buildLinuxX86_64
  exit 1;
elif [ "$1" == "--win" -o "$1" == "--win32" -o "$1" == "--win64" ]; then 
  #fixes for windows
  ORIGINAL_BOOST_ROOT=$BOOST_ROOT
  export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0

  if [ "$1" == "--win" ]; then
    sed -i 's/ JNICALL Java_com_frostwire/ JNICALL _Java_com_frostwire/g' libtorrent_jni.cpp
    buildWindowsX86
    sed -i 's/ JNICALL _Java_com_frostwire/ JNICALL Java_com_frostwire/g' libtorrent_jni.cpp
    buildWindowsX86_64
  elif [ "$1" == "--win32" ]; then
    sed -i 's/ JNICALL Java_com_frostwire/ JNICALL _Java_com_frostwire/g' libtorrent_jni.cpp
    buildWindowsX86
    sed -i 's/ JNICALL _Java_com_frostwire/ JNICALL Java_com_frostwire/g' libtorrent_jni.cpp
  elif [ "$1" == "--win64" ]; then
    buildWindowsX86_64
  fi
  export BOOST_ROOT=$ORIGINAL_BOOST_ROOT
  exit 1;
else

   echo "Are you sure you want to build jlibtorrent for"
   echo "    OSX"
   echo "    Android (arm32)"
   echo "    Android (arm64)"
   echo "    Android (x86)"
   echo "    Android (x64_64)"
   echo "    Linux (x86)"
   echo "    Linux (x86_64)"
   echo "    Windows (x86)"
   echo "    Windows (x86_64)?"
   echo ""
   echo "(Press [y] to continue, or any other key to abort. Press [^C] and './build.sh -h' for help)" 

  read buildAll

  if echo $buildAll | grep -q "^[Yy]" ; then
    buildMacOSX

    buildAndroidArm
    buildAndroidX86

    buildAndroidArm64
    buildAndroidX86_64

    buildLinuxX86
    buildLinuxX86_64

    #fixes for windows
    ORIGINAL_BOOST_ROOT=$BOOST_ROOT
    export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0

    sed -i 's/ JNICALL Java_com_frostwire/ JNICALL _Java_com_frostwire/g' libtorrent_jni.cpp
    buildWindowsX86
    sed -i 's/ JNICALL _Java_com_frostwire/ JNICALL Java_com_frostwire/g' libtorrent_jni.cpp
    buildWindowsX86_64

    export BOOST_ROOT=$ORIGINAL_BOOST_ROOT
    exit 1;
  else 
    echo "Aborted."
    exit 0;
  fi
fi
