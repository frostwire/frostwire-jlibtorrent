#!/usr/bin/env bash
# This script downloads the latest travis build from https://s3.amazonaws.com/gubatron-jlibtorrent/release/<os_build>/<os_arch>/<library.ext>
# for each available architecture, places them where they'd be if we had built them locally
# and then invokes the gradle tasks to create all the jars for each os_build/os_arch

function downloadLibrary {
  fileName=$1
  dest=$2
  url=$3

  echo URL: ${url}

  if [[ ! -d ${dest} ]]; then
    mkdir -p ${dest}
  fi

  wget -O ${dest}/${fileName} $url
}

# Android-ARM
downloadLibrary 'libjlibtorrent.so' 'bin/release/android/armeabi-v7a' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/armeabi-v7a/libjlibtorrent.so'

# Android-X86
downloadLibrary 'libjlibtorrent.so' 'bin/release/android/x86' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/android/x86/libjlibtorrent.so'

# Windows (dual-architecture jar)
downloadLibrary 'jlibtorrent.dll' 'bin/release/windows/x86' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/windows/x86/jlibtorrent.dll'
downloadLibrary 'jlibtorrent.dll' 'bin/release/windows/x86_64' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/windows/x86_64/jlibtorrent.dll'

# Mac
downloadLibrary 'libjlibtorrent.dylib' 'bin/release/macosx/x86_64' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/macosx/x86_64/libjlibtorrent.dylib'

# Linux (dual architecture .jar)
downloadLibrary 'libjlibtorrent.so 'bin/release/linux/x86' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86/libjlibtorrent.so'
downloadLibrary 'libjlibtorrent.so 'bin/release/linux/x86_64' 'https://s3.amazonaws.com/gubatron-jlibtorrent/release/linux/x86_64/libjlibtorrent.so'

pushd ..
gradle clean
gradle build
popd