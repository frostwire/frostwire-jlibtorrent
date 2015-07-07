#!/bin/bash

$BOOST_ROOT/bjam toolset=darwin
TARGET=../binaries/macosx/libjlibtorrent.dylib
cp bin/darwin-4.2.1/release/boost-source/crypto-openssl/deprecated-functions-off/libjlibtorrent.dylib.1.1.0 $TARGET
strip -S -x $TARGET
cp $TARGET ../
