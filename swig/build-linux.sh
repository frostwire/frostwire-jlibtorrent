#!/bin/bash
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