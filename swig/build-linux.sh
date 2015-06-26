#!/bin/bash

$BOOST_ROOT/bjam toolset=gcc cxxflags=-fPIC cflags=-fPIC
TARGET=../binaries/linux/x86_64/libjlibtorrent.so
cp bin/gcc-4.8/release/boost-source/crypto-openssl/deprecated-functions-off/libjlibtorrent.so.1.1.0 $TARGET
strip --strip-unneeded -x $TARGET
cp $TARGET ../
