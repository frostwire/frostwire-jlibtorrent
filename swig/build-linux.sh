#!/bin/bash

$BOOST_ROOT/bjam toolset=gcc cxxflags=-fPIC cflags=-fPIC location=bin/linux
TARGET=../binaries/linux/x86_64/libjlibtorrent.so
cp bin/linux/libjlibtorrent.so.1.1.0 $TARGET
strip --strip-unneeded -x $TARGET
cp $TARGET ../
