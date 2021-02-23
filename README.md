frostwire-jlibtorrent
=====================
![JLibtorrent Logo](logo/jlibtorrent_logo_color.png)

A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

[Discord Developer Chatroom](https://discord.com/channels/461752211802947585/461766946669461515)

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
 - `jlibtorrent-android-x86-w.x.y.z.jar`

If you use ProGuard to obfuscate/minify make sure to add the following statement

`-keep class com.frostwire.jlibtorrent.swig.libtorrent_jni {*;}`


Note that there are multiple version of jlibtorrent for different platforms: `jlibtorrent`, `jlibtorrent-windows`, `jlibtorrent-linux`, `jlibtorrent-macosx` and `jlibtorrent-android-<arch>`. These are all different artifacts.

For examples look at https://github.com/frostwire/frostwire-jlibtorrent/tree/master/src/test/java/com/frostwire/jlibtorrent/demo

Architectures supported:

- Android (armeabi-v7a, arm64-v8a, x86, x86_64)
- Linux (x86, x86_64)
- Windows (x86, x86_64)
- Mac OS X (x86_64)

Building Locally (Mac and Linux)
================================

A build script exists for each operating system, for example if you're on macos you can use the `build-macos.sh`, running it without setting things up should tell you about certain environment variables you'll need to set up. To understand the build process we recommend you read your corresponding build script and [`build-utils.shinc`](https://github.com/frostwire/frostwire-jlibtorrent/blob/master/swig/build-utils.shinc)

The hacking and building process might require you to run the `run-swig.sh` script, we usually need to run this script if there are C++ api changes in libtorrent that require adjustments in `libtorrent.i` or `libtorrent.h`, this script will create automatic JNI wrappers in the outer source java folders. You should not run this script unless you know what you're doing.

Then you run the build script for your OS, get to the parent folder and invoke
`gradle build`

The gradle build will look in the swig folder for the JNI shared libraries and it will automatically package the native holding `.jar` files, the final jars will be in the `build/libs` folder.

The windows build script is not finished, for now the windows build is done with travis builds.

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
