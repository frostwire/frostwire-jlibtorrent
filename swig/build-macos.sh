#!/usr/bin/env bash

export DEVELOPMENT_ROOT=~/Development
export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0
export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent
export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-macos

$BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
cp bin/macosx/x86_64/libjlibtorrent.dylib ../

node-gyp configure build
cp build/Release/jlibtorrent.node ../node
