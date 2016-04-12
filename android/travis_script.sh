#!/usr/bin/env bash

echo no | android create avd --force --name test --target $android_target --abi $android_abi
emulator -avd test -no-skin -no-audio -no-window &
gradle check -PdisablePreDex --continue --stacktrace
android-wait-for-emulator
adb devices
adb shell input keyevent 82 &
gradle connectedAndroidTest -PdisablePreDex --continue --stacktrace
