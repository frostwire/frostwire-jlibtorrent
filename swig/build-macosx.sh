#!/bin/bash

# to compile under Xcode 7 and openssl being deprecated
CXXFLAGS="-std=c++11 -stdlib=libc++ -I/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift-migrator/sdk/MacOSX.sdk/usr/include -Wno-deprecated-declarations -Wno-reserved-id-macro -mmacosx-version-min=10.7"

$BOOST_ROOT/bjam toolset=darwin location=bin/macosx cxxflags="$CXXFLAGS"
TARGET=bin/macosx/libjlibtorrent.dylib
strip -S -x $TARGET
cp $TARGET ../
