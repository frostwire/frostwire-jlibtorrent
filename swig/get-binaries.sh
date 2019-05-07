#!/usr/bin/env bash

rm -rf bin
mkdir bin

# android
wget --no-check-certificate -P bin/release/android/armeabi-v7a https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/armeabi-v7a/libjlibtorrent.so
wget --no-check-certificate -P bin/release/android/arm64-v8a https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/arm64-v8a/libjlibtorrent.so
wget --no-check-certificate -P bin/release/android/x86 https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/x86/libjlibtorrent.so
wget --no-check-certificate -P bin/release/android/x86_64 https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/x86_64/libjlibtorrent.so

# linux
wget --no-check-certificate -P bin/release/linux/x86 https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86/libjlibtorrent.so
wget --no-check-certificate -P bin/release/linux/x86_64 https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86_64/libjlibtorrent.so

# windows
wget --no-check-certificate -P bin/release/windows/x86 https://s3.amazonaws.com/gubatron-jlibtorrent/release/windows/x86/jlibtorrent.dll
wget --no-check-certificate -P bin/release/windows/x86_64 https://s3.amazonaws.com/gubatron-jlibtorrent/release/windows/x86_64/jlibtorrent.dll

# macos
wget --no-check-certificate -P bin/release/macosx/x86_64 https://s3.amazonaws.com/gubatron-jlibtorrent/release/macosx/x86_64/libjlibtorrent.dylib
