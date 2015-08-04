#!/bin/bash

$BOOST_ROOT/bjam toolset=darwin location=bin/macosx
mv bin/macosx/libjlibtorrent.dylib.1.1.0 bin/macosx/libjlibtorrent.dylib
TARGET=bin/macosx/libjlibtorrent.dylib
strip -S -x $TARGET
cp $TARGET ../
