#!/usr/bin/env bash

wget -O android-ndk.zip http://dl.google.com/android/repository/android-ndk-r11c-linux-x86_64.zip
echo "Extracting NDK...wait"
unzip -qq android-ndk.zip
export NDK_ROOT=$PWD/android-ndk

echo no | android create avd --force --name test --target $android_target --abi $android_abi
emulator -avd test -no-skin -no-audio -no-window &

gradle check -PdisablePreDex --continue --stacktrace
android-wait-for-emulator
adb devices
adb shell input keyevent 82 &
gradle connectedAndroidTest -PdisablePreDex --continue --stacktrace
