#!/usr/bin/env bash

# compile openssl 1.1.0 with the following arguments
#no-afalgeng no-asan no-async no-autoalginit no-autoerrinit no-bf no-blake2 no-camellia no-capieng no-cast no-chacha no-cmac no-cms no-comp no-crypto-mdebug no-crypto-mdebug-backtrace no-ct no-deprecated no-des no-dgram no-dh no-dsa no-dso no-dtls no-dynamic-engine no-ec no-ec2m no-ecdh no-ecdsa no-ec_nistp_64_gcc_128 no-egd no-engine no-err no-filenames no-fuzz-libfuzzer no-fuzz-afl no-gost no-heartbeats no-hw no-idea no-makedepend no-md2 no-md4 no-mdc2 no-multiblock no-nextprotoneg no-ocb no-ocsp no-pic no-poly1305 no-posix-io no-psk no-rc2 no-rc4 no-rc5 no-rdrand no-rfc3779 no-rmd160 no-scrypt no-sctp no-seed no-shared no-sock no-srp no-srtp no-ssl no-ssl-trace no-static-engine no-stdio no-threads no-tls no-ts no-ubsan no-ui no-unit-test no-whirlpool no-weak-ssl-ciphers no-zlib no-zlib-dynamic

export DEVELOPMENT_ROOT=~/Development
export BOOST_ROOT=$DEVELOPMENT_ROOT/boost_1_60_0
export LIBTORRENT_ROOT=$DEVELOPMENT_ROOT/libtorrent

# native OSX
export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-osx

$BOOST_ROOT/b2 --user-config=config/macosx-x86_64-config.jam toolset=darwin-x86_64 target-os=darwin location=bin/macosx/x86_64
cp bin/macosx/x86_64/libjlibtorrent.dylib ../

# android arm
#export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-android-arm
#export ANDROID_TOOLCHAIN=$DEVELOPMENT_ROOT/ndk-arm-toolchain

#$BOOST_ROOT/b2 --user-config=config/android-arm-config.jam toolset=clang-arm target-os=android location=bin/android/armeabi-v7a wrap-posix=on;
#cp bin/android/armeabi-v7a/libjlibtorrent.so ../android/src/main/jniLibs/armeabi-v7a

# android x86
#export OPENSSL_ROOT=$DEVELOPMENT_ROOT/openssl-android-x86
#export ANDROID_TOOLCHAIN=$DEVELOPMENT_ROOT/ndk-x86-toolchain

#$BOOST_ROOT/b2 --user-config=config/android-x86-config.jam toolset=gcc-x86 target-os=linux location=bin/android/x86 wrap-posix=on;
#cp bin/android/x86/libjlibtorrent.so ../android/src/main/jniLibs/x86

node-gyp configure build
cp build/Release/jlibtorrent.node ../node
