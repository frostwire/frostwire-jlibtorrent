#!/bin/bash

make-standalone-toolchain.sh --platform=android-15 --arch=arm  --toolchain=arm-linux-androideabi-4.8 --install-dir=toolchain-platform-15-arm-gcc-4.8
make-standalone-toolchain.sh --platform=android-15 --arch=mips --toolchain=mipsel-linux-android-4.8  --install-dir=toolchain-platform-15-mips-gcc-4.8
make-standalone-toolchain.sh --platform=android-15 --arch=x86  --toolchain=x86-4.8                   --install-dir=toolchain-platform-15-x86-gcc-4.8