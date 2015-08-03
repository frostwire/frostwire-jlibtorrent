#!/bin/bash

$BOOST_ROOT/bjam toolset=darwin location=bin/macosx
TARGET=../binaries/macosx/libjlibtorrent.dylib
cp bin/macosx/libjlibtorrent.dylib.1.1.0 $TARGET
strip -S -x $TARGET
cp $TARGET ../
