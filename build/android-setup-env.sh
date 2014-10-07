#!/bin/bash

export AR="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-ar"
export AS="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-as"
export CXX="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-g++"
export CC="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-gcc"
export LD="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-ld"
export NM="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-nm"
export RANLIB="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-ranlib"
export STRIP="$ANDROID_ARM_TOOLCHAIN_ROOT/bin/arm-linux-androideabi-strip"

alias ar=$AR
alias as=$AS
alias g++=$CXX
alias gcc=$CC
alias ld=$LD
alias nm=$NM
alias ranlib=$RANLIB
alias strip=$STRIP
