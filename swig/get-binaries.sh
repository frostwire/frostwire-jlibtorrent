#!/usr/bin/env bash

rm -rf bin
mkdir bin

# android arm
wget -P bin/release/android/armeabi-v7a https://s3.amazonaws.com/jlibtorrent1/release/android/armeabi-v7a/libjlibtorrent.so
wget -P bin/debug/android/armeabi-v7a https://s3.amazonaws.com/jlibtorrent1/debug/android/armeabi-v7a/libjlibtorrent.so
wget -P bin/debug/android/armeabi-v7a https://s3.amazonaws.com/jlibtorrent1/debug/android/armeabi-v7a/libjlibtorrent.so.debug
# android
wget -P bin/release/android/arm64-v8a https://s3.amazonaws.com/jlibtorrent1/release/android/arm64-v8a/libjlibtorrent.so
wget -P bin/release/android/x86 https://s3.amazonaws.com/jlibtorrent1/release/android/x86/libjlibtorrent.so
wget -P bin/release/android/x86_64 https://s3.amazonaws.com/jlibtorrent1/release/android/x86_64/libjlibtorrent.so

# linux x86
wget -P bin/release/linux/x86 https://s3.amazonaws.com/jlibtorrent1/release/linux/x86/libjlibtorrent.so
# linux x86_64
wget -P bin/release/linux/x86_64 https://s3.amazonaws.com/jlibtorrent1/release/linux/x86_64/libjlibtorrent.so
wget -P bin/debug/linux/x86_64 https://s3.amazonaws.com/jlibtorrent1/debug/linux/x86_64/libjlibtorrent.so
wget -P bin/debug/linux/x86_64 https://s3.amazonaws.com/jlibtorrent1/debug/linux/x86_64/libjlibtorrent.so.debug

# linux
wget -P bin/release/linux/arm https://s3.amazonaws.com/jlibtorrent1/release/linux/arm/libjlibtorrent.so
wget -P bin/release/linux/arm64 https://s3.amazonaws.com/jlibtorrent1/release/linux/arm64/libjlibtorrent.so

# windows
wget -P bin/release/windows/x86 https://s3.amazonaws.com/jlibtorrent1/release/windows/x86/jlibtorrent.dll
wget -P bin/release/windows/x86_64 https://s3.amazonaws.com/jlibtorrent1/release/windows/x86_64/jlibtorrent.dll

# macos
wget -P bin/release/macosx/x86_64 https://s3.amazonaws.com/jlibtorrent1/release/macosx/x86_64/libjlibtorrent.dylib
wget -P bin/debug/macosx/x86_64 https://s3.amazonaws.com/jlibtorrent1/debug/macosx/x86_64/libjlibtorrent.dylib
wget -P bin/debug/macosx/x86_64 https://s3.amazonaws.com/jlibtorrent1/debug/macosx/x86_64/libjlibtorrent.dylib.debug
