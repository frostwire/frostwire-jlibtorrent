#!/usr/bin/env bash

# AWS EC2 remote jlibtorrent machine builds everything except windows 32-bit

# Android
if [ ! -d bin/release/android/arm64-v8a ]; then
  mkdir -p bin/release/android/arm64-v8a
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/android/arm64-v8a/libjlibtorrent.so bin/release/android/arm64-v8a/

if [ ! -d bin/release/android/armeabi-v7a ]; then
  mkdir -p bin/release/android/armeabi-v7a
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/android/armeabi-v7a/libjlibtorrent.so bin/release/android/armeabi-v7a/

if [ ! -d bin/release/android/x86 ]; then
  mkdir -p bin/release/android/x86
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/android/x86/libjlibtorrent.so bin/release/android/x86/

if [ ! -d bin/release/android/x86_64 ]; then
  mkdir -p bin/release/android/x86_64
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/android/x86_64/libjlibtorrent.so bin/release/android/x86_64/


# Linux
if [ ! -d bin/release/linux/x86_64 ]; then
  mkdir -p bin/release/linux/x86_64
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/linux/x86_64/libjlibtorrent.so bin/release/linux/x86_64/

# Windows (Only 64bit), can't build 32-bit on the remote machine
#
if [ ! -d bin/release/windows/x86_64 ]; then
    mkdir -p bin/release/windows/x86_64
fi
scp ubuntu@jlibtorrent:~/frostwire-jlibtorrent/swig/bin/release/windows/x86_64/jlibtorrent.dll bin/release/windows/x86_64/
