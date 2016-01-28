#!/bin/bash

$BOOST_ROOT/b2 --user-config=config/macosx-native-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
TARGET=bin/macosx/x86_64/libjlibtorrent.dylib
strip -S -x $TARGET
cp $TARGET ../

node-gyp configure build
cp build/Release/jlibtorrent.node ../node
