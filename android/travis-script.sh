#!/usr/bin/env bash

echo no | android create avd --force --name test --target android-23 --abi $1
emulator -avd test -no-skin -no-window &

gradle check -PdisablePreDex --continue --stacktrace
android-wait-for-emulator
adb devices
adb shell input keyevent 82 &
gradle connectedAndroidTest -PdisablePreDex --continue --stacktrace
