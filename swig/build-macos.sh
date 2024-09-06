#!/bin/bash
if [ $(arch) == 'i386' ]; then
  ./build-macos-x86_64.sh "$@"
else
  ./build-macos-arm64.sh "$@"
fi
