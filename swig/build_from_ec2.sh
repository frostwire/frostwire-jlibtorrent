#!/usr/bin/env bash

# We build everything but windows 32, that we build on the vmware instance
export DONT_PRESS_ANY_KEY=1

./build-android-arm.sh
./build-android-arm64.sh
./build-android-x86.sh
./build-android-x86_64.sh

./build-linux-x86_64.sh

./build-windows-x86_64.sh
