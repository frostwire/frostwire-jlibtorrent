frostwire-jlibtorrent
=====================
![JLibtorrent Logo](logo/jlibtorrent_logo_color.png)

A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

[Discord Developer Chatroom Invite](https://discord.com/invite/8nWfSGhNdX)

Using
========

## Maven packages

We've finally created a Maven Repository for our [releases](https://github.com/frostwire/frostwire-jlibtorrent/releases)

build.gradle (Groovy) example
```groovy
repositories {
    maven { url "https://dl.frostwire.com/maven" }
}

dependencies {
    def jlibtorrent_version = '2.0.12.2' // change version for latest

    // ALL ARCHITECTURES need the java .class wrappers jlibtorrent.jar
    implementation 'com.frostwire:jlibtorrent:' + jlibtorrent_version

    // ANDROID (needs all of them for most projects if you want to maximize compatibility)
    implementation 'com.frostwire:jlibtorrent-android-arm:' + jlibtorrent_version
    implementation 'com.frostwire:jlibtorrent-android-arm64:' + jlibtorrent_version
    implementation 'com.frostwire:jlibtorrent-android-x86:' + jlibtorrent_version
    implementation 'com.frostwire:jlibtorrent-android-x86_64:' + jlibtorrent_version

    // WINDOWS x86_64
    implementation 'com.frostwire:jlibtorrent-windows:' + jlibtorrent_version

    // MAC OS x86_64 (Intel)
    implementation 'com.frostwire:jlibtorrent-macosx-x86_64:' + jlibtorrent_version
    // MAC OS arm64 (Apple Silicon)
    implementation 'com.frostwire:jlibtorrent-macosx-arm64:' + jlibtorrent_version

    // LINUX x86_64
    implementation 'com.frostwire:jlibtorrent-linux:' + jlibtorrent_version    
}
```

build.gradle.kts (Kotlin) example
```kotlin
repositories {
    maven { url = uri("https://dl.frostwire.com/maven") }
}

dependencies {
    val jlibtorrentVersion = "2.0.12.0" // change version for latest

    // ALL ARCHITECTURES need the java .class wrappers jlibtorrent.jar
    implementation("com.frostwire:jlibtorrent:$jlibtorrentVersion")

    // ANDROID (needs all of them for most projects if you want to maximize compatibility)
    implementation("com.frostwire:jlibtorrent-android-arm:$jlibtorrentVersion")
    implementation("com.frostwire:jlibtorrent-android-arm64:$jlibtorrentVersion")
    implementation("com.frostwire:jlibtorrent-android-x86:$jlibtorrentVersion")
    implementation("com.frostwire:jlibtorrent-android-x86_64:$jlibtorrentVersion")

    // WINDOWS x86_64
    implementation("com.frostwire:jlibtorrent-windows:$jlibtorrentVersion")

    // MAC OS x86_64 (Intel)
    implementation("com.frostwire:jlibtorrent-macosx-x86_64:$jlibtorrentVersion")
    // MAC OS arm64 (Apple Silicon)
    implementation("com.frostwire:jlibtorrent-macosx-arm64:$jlibtorrentVersion")

    // LINUX x86_64
    implementation("com.frostwire:jlibtorrent-linux:$jlibtorrentVersion")
}
```

build.sbt SBT example
```scala
// Add the custom FrostWire repository
resolvers += "FrostWire Maven" at "https://dl.frostwire.com/maven"

// Define the library version as a variable
val jlibtorrentVersion = "2.0.12.0" // change version for latest

// Add all the necessary library dependencies
libraryDependencies ++= Seq(
  // ALL ARCHITECTURES need the java .class wrappers jlibtorrent.jar
  "com.frostwire" % "jlibtorrent" % jlibtorrentVersion,

  // ANDROID (needs all of them for most projects if you want to maximize compatibility)
  "com.frostwire" % "jlibtorrent-android-arm" % jlibtorrentVersion,
  "com.frostwire" % "jlibtorrent-android-arm64" % jlibtorrentVersion,
  "com.frostwire" % "jlibtorrent-android-x86" % jlibtorrentVersion,
  "com.frostwire" % "jlibtorrent-android-x86_64" % jlibtorrentVersion,

  // WINDOWS x86_64
  "com.frostwire" % "jlibtorrent-windows" % jlibtorrentVersion,

  // MAC OS x86_64 (Intel)
  "com.frostwire" % "jlibtorrent-macosx-x86_64" % jlibtorrentVersion,
  // MAC OS arm64 (Apple Silicon)
  "com.frostwire" % "jlibtorrent-macosx-arm64" % jlibtorrentVersion,

  // LINUX x86_64
  "com.frostwire" % "jlibtorrent-linux" % jlibtorrentVersion
)
```

Maven example
```xml
<project>
    <properties>
        <jlibtorrent.version>2.0.12.0</jlibtorrent.version>
    </properties>

    <repositories>
        <repository>
            <id>frostwire-maven</id>
            <url>https://dl.frostwire.com/maven</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- ALL ARCHITECTURES need the java .class wrappers jlibtorrent.jar -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- ANDROID projects need all the following artifacts to maximize chip compatibility -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-android-arm</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-android-arm64</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-android-x86</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-android-x86_64</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- WINDOWS x86_64 -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-windows</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- MAC OS X86_64 (Intel) -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-macosx-x86_64</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- MAC OS ARM64 (Apple Silicon) -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-macosx-arm64</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- LINUX X86_64 -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-linux</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>
    </dependencies>
</project>
```

## Manual use of .jar files

[Download the latest release .jars](https://github.com/frostwire/frostwire-jlibtorrent/releases)

All platforms will need you to use at least 2 `.jar` files.

The `.jar` with the java classes -> `jlibtorrent-w.x.y.z.jar` and a secondary `.jar`s containing the JNI binary library for the particular OS and CPU architecture.

In the case of desktop operating systems, you might want to extract the shared library inside the jar (.dll, .so, .dylib) and place it in a folder specified by the `java.library.path`

The secondary jars are:
 - Windows: `jlibtorrent-windows-w.x.y.z.jar` (x86 and x86_64 .dlls)
 - Mac: `jlibtorrent-macosx-w.x.y.z.jar` (x86_64 .dylib)
 - Linux: `jlibtorrent-linux-w.x.y.z.jar` (x86 and x86_64 .so)

In the case of Android, make sure to put the following 5 jars in your project's `libs` folder (see [FrostWire for Android's](https://github.com/frostwire/frostwire/tree/master/android/libs) as an example):
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
