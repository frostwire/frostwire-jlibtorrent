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
    maven {
        url "https://dl.frostwire.com/maven"
        content {
            includeGroup "com.frostwire"
        }
    }
}

dependencies {
    def jlibtorrent_version = '2.0.12.7' // change version for latest

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
    implementation 'com.frostwire:jlibtorrent-linux-x86_64:' + jlibtorrent_version
    // LINUX arm64
    implementation 'com.frostwire:jlibtorrent-linux-arm64:' + jlibtorrent_version
}
```

build.gradle.kts (Kotlin) example
```kotlin
repositories {
    maven {
        setUrl("https://dl.frostwire.com/maven")
        content {
            includeGroup("com.frostwire")
        }
    }
}

dependencies {
    val jlibtorrentVersion = "2.0.13.6" // change version for latest

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
    implementation("com.frostwire:jlibtorrent-linux-x86_64:$jlibtorrentVersion")
    // LINUX arm64
    implementation("com.frostwire:jlibtorrent-linux-arm64:$jlibtorrentVersion")
}
```

build.sbt SBT example
```scala
// Add the custom FrostWire repository
resolvers += "FrostWire Maven" at "https://dl.frostwire.com/maven"

// Define the library version as a variable
val jlibtorrentVersion = "2.0.13.6" // change version for latest

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
  "com.frostwire" % "jlibtorrent-linux-x86_64" % jlibtorrentVersion,
  // LINUX arm64
  "com.frostwire" % "jlibtorrent-linux-arm64" % jlibtorrentVersion
)
```

Maven example
```xml
<project>
    <properties>
        <jlibtorrent.version>2.0.13.6</jlibtorrent.version>
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
            <artifactId>jlibtorrent-linux-x86_64</artifactId>
            <version>${jlibtorrent.version}</version>
        </dependency>

        <!-- LINUX ARM64 -->
        <dependency>
            <groupId>com.frostwire</groupId>
            <artifactId>jlibtorrent-linux-arm64</artifactId>
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
 - Windows: `jlibtorrent-windows-w.x.y.z.jar` (x86_64 .dll)
 - Mac: `jlibtorrent-macosx-x86_64-w.x.y.z.jar` (x86_64 .dylib), `jlibtorrent-macosx-arm64-w.x.y.z.jar` (arm64 .dylib)
 - Linux: `jlibtorrent-linux-x86_64-w.x.y.z.jar` (x86_64 .so), `jlibtorrent-linux-arm64-w.x.y.z.jar` (arm64 .so)

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
  - ✅ **Android 16 KB page size compatible** (required for Google Play as of Nov 2025)
- Linux (x86_64, arm64)
- Windows (x86_64)
- Mac OS X (x86_64, arm64)

Building
================================

All Android native libraries are built with 16 KB page size compatibility for Google Play requirements (mandatory as of Nov 2025).

**For detailed build instructions, see [BUILD_MANUAL.md](BUILD_MANUAL.md)**

### Quick Start

**Build macOS binaries** (requires macOS computer):
```bash
cd swig
./prepare-macos.sh   # One-time setup
./build-macos.sh     # Build for current architecture
```

**Build all other platforms** (requires Docker):
```bash
cd swig
./docker_build_binaries.sh   # Builds Windows, Linux, Android
```

### Build Output

The resulting JAR files will be in `./build/libs/`:
 - `jlibtorrent-w.x.y.z.jar` - Java wrapper classes (all platforms)
 - `jlibtorrent-macosx-x86_64-w.x.y.z.jar` - macOS Intel (x86_64)
 - `jlibtorrent-macosx-arm64-w.x.y.z.jar` - macOS Apple Silicon (arm64)
 - `jlibtorrent-windows-w.x.y.z.jar` - Windows x86_64
 - `jlibtorrent-linux-x86_64-w.x.y.z.jar` - Linux x86_64
 - `jlibtorrent-linux-arm64-w.x.y.z.jar` - Linux arm64
 - `jlibtorrent-android-arm-w.x.y.z.jar` - Android armeabi-v7a
 - `jlibtorrent-android-arm64-w.x.y.z.jar` - Android arm64-v8a
 - `jlibtorrent-android-x86-w.x.y.z.jar` - Android x86
 - `jlibtorrent-android-x86_64-w.x.y.z.jar` - Android x86_64

### Native Libraries

Raw native library files (.so, .dylib, .dll) are available in:
```
./swig/bin/release/
├── windows/x86_64/jlibtorrent.dll
├── macosx/x86_64/libjlibtorrent.dylib
├── macosx/arm64/libjlibtorrent.dylib
├── linux/x86_64/libjlibtorrent.so
├── linux/arm64/libjlibtorrent.so
└── android/
    ├── armeabi-v7a/libjlibtorrent.so
    ├── arm64-v8a/libjlibtorrent.so
    ├── x86/libjlibtorrent.so
    └── x86_64/libjlibtorrent.so
```

### Prerequisites

**macOS (native build):**
- Xcode Command Line Tools
- Homebrew with: wget, pcre2, automake, autoconf, bison

**All other platforms (Docker):**
- Docker installed and running
- 8+ GB RAM (16+ GB recommended)
- 20-30 GB free disk space

For detailed prerequisites and troubleshooting, see [BUILD_MANUAL.md](BUILD_MANUAL.md).

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
