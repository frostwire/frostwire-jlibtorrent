frostwire-jlibtorrent
=====================
![JLibtorrent Logo](logo/jlibtorrent_logo_color.png)

A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

[Discord Developer Chatroom Invite](https://discord.com/invite/8nWfSGhNdX)

Using
========

[Download the latest release .jars](https://github.com/frostwire/frostwire-jlibtorrent/releases)

All platforms will need you to use at least 2 `.jar` files.

The `.jar` with the java classes -> `jlibtorrent-w.x.y.z.jar` and a secondary `.jar`s containing the JNI binary library for the particular OS and CPU architecture.

In the case of desktop operating systems, you might want to extract the shared library inside the jar (.dll, .so, .dylib) and place it in a folder specified by the `java.library.path`

The secondary jars are:
 - Windows: `jlibtorrent-windows-w.x.y.z.jar` (x86 and x86_64 .dlls)
 - Mac: `jlibtorrent-macosx-w.x.y.z.jar` (x86_64 .dylib)
 - Linux: `jlibtorrent-linux-w.x.y.z.jar` (x86 and x86_64 .so)

In the case of Android, make sure to put the following 3 jars in your project's `libs` folder (see [FrostWire for Android's](https://github.com/frostwire/frostwire/tree/master/android/libs) as an example):
 - `jlibtorrent-w.x.y.z.jar`
 - `jlibtorrent-android-arm-w.x.y.z.jar`
 - `jlibtorrent-android-arm64-w.x.y.z.jar`
 - `jlibtorrent-android-x86-w.x.y.z.jar`
 - `jlibtorrent-android-x86_64-w.x.y.z.jar`

If you use ProGuard to obfuscate/minify make sure to add the following statement

`-keep class com.frostwire.jlibtorrent.swig.libtorrent_jni {*;}`


Note that there are multiple version of jlibtorrent for different platforms: `jlibtorrent`, `jlibtorrent-windows`, `jlibtorrent-linux`, `jlibtorrent-macosx` and `jlibtorrent-android-<arch>`. These are all different artifacts.

For examples look at https://github.com/frostwire/frostwire-jlibtorrent/tree/master/src/test/java/com/frostwire/jlibtorrent/demo

Architectures supported:

- Android (armeabi-v7a, arm64-v8a, x86, x86_64) sdk 24 (Android 7.0) and up
- Linux (x86_64)
- Windows (x86_64)
- Mac OS X (x86_64)

Building
================================

To build the macos (x86_64) binaries you will need a mac computer, to build the mac library issue the following command:

`cd swig && ./build-macos.sh`

To build the windows (x86_64), linux (x86_64) and android binaries (arm, arm64, x86, x86_64) ***you will need a working version of Docker***, then just issue the following command

`cd swig && ./docker_build_binaries.sh`

The resulting jars will be at:
 - `./build/libs/jlibtorrent-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-windows-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-macos-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-linux-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-android-arm-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-android-arm64-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-android-x86-w.x.y.z.jar`
 - `./build/libs/jlibtorrent-android-x86_64-w.x.y.z.jar`

 If you need the singled out `.so`,`.dylib` and `.dll` files and their `.debug` versions they will be at:
 - `./swig/bin/release/windows/x86_64/jlibtorrent.dll`
 - `./swig/bin/release/macosx/x86_64/libjlibtorrent.dylib`
 - `./swig/bin/release/linux/x86_64/libjlibtorrent.so`
 - `./swig/bin/release/android/armeabi-v7a/libjlibtorrent.so`
 - `./swig/bin/release/android/arm64-v8a/libjlibtorrent.so`
 - `./swig/bin/release/android/x86/libjlibtorrent.so`
 - `./swig/bin/release/android/x86_64/libjlibtorrent.so`

Projects using jLibtorrent
==========================
- [FrostWire](http://www.frostwire.com) (both desktop and android editions)
- [TorrentStream-Android](https://github.com/mianharisali/TorrentStream-Android)
- [Simple-Torrent-Android](https://github.com/masterwok/simple-torrent-android)
- [TorrentTunes-Client](https://github.com/dessalines/torrenttunes-client)
- [LibreTorrent](https://github.com/proninyaroslav/libretorrent)

License
========

This software is offered under the MIT License, available [here](License.md).
