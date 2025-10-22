#!/bin/bash

# ubuntu needs to have these cross building tools
# sudo apt-get install g++-x86-64-linux-gnu gcc-x86-64-linux-gnu binutils-x86-64-linux-gnu

# Auto-select Linux build script based on architecture
arch=$(uname -m)
if [ "$arch" == "x86_64" ]; then
  ./build-linux-x86_64.sh "$@"
elif [ "$arch" == "aarch64" ] || [ "$arch" == "arm64" ]; then
  ./build-linux-arm64.sh "$@"
else
  echo "Error: Unsupported architecture $arch for Linux build"
  exit 1
fi
