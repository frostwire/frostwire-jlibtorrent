#!/bin/bash

$BOOST_ROOT/bjam toolset=gcc cxxflags=-fPIC cflags=-fPIC location=bin/linux/x86_64
mv bin/linux/x86_64/libjlibtorrent.so.1.1.0 bin/linux/x86_64/libjlibtorrent.so
TARGET=bin/linux/x86_64/libjlibtorrent.so
strip --strip-unneeded -x $TARGET
cp $TARGET ../
