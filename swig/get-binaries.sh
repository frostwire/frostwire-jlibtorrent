#!/usr/bin/env bash

rm -rf bin
mkdir bin

wget -P bin/android/armeabi-v7a https://s3.amazonaws.com/jlibtorrent1/android/armeabi-v7a/libjlibtorrent.so
wget -P bin/android/arm64-v8a https://s3.amazonaws.com/jlibtorrent1/android/arm64-v8a/libjlibtorrent.so
wget -P bin/android/x86 https://s3.amazonaws.com/jlibtorrent1/android/x86/libjlibtorrent.so
wget -P bin/android/x86_64 https://s3.amazonaws.com/jlibtorrent1/android/x86_64/libjlibtorrent.so
wget -P bin/linux/x86 https://s3.amazonaws.com/jlibtorrent1/linux/x86/libjlibtorrent.so
wget -P bin/linux/x86_64 https://s3.amazonaws.com/jlibtorrent1/linux/x86_64/libjlibtorrent.so
wget -P bin/linux/arm https://s3.amazonaws.com/jlibtorrent1/linux/arm/libjlibtorrent.so
wget -P bin/linux/arm64 https://s3.amazonaws.com/jlibtorrent1/linux/arm64/libjlibtorrent.so
wget -P bin/windows/x86 https://s3.amazonaws.com/jlibtorrent1/windows/x86/jlibtorrent.dll
wget -P bin/windows/x86_64 https://s3.amazonaws.com/jlibtorrent1/windows/x86_64/jlibtorrent.dll
wget -P bin/macosx/x86_64 https://s3.amazonaws.com/jlibtorrent1/macosx/x86_64/libjlibtorrent.dylib
