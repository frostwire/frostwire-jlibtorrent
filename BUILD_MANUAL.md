# jlibtorrent Binary Build Manual

This guide explains how to build jlibtorrent native binaries for all supported platforms and architectures.

## Table of Contents
1. [Overview](#overview)
2. [Build Outputs](#build-outputs)
3. [macOS Builds](#macos-builds)
4. [Docker-Based Builds (Linux, Windows, Android)](#docker-based-builds)
5. [Platform-Specific Notes](#platform-specific-notes)
6. [Troubleshooting](#troubleshooting)

---

## Overview

jlibtorrent consists of:
- **Java wrapper classes** (`jlibtorrent.jar`) - Platform-independent Java bindings
- **Native binaries** - Platform/architecture-specific JNI libraries (.dll, .so, .dylib)

The build process uses **SWIG** to generate JNI bindings from C++ interfaces and compile them into native libraries.

### Supported Platforms and Architectures

| Platform | Architectures | Build Method |
|----------|---------------|--------------|
| macOS    | x86_64 (Intel), arm64 (Apple Silicon) | Native build on Mac |
| Linux    | x86_64, arm64 | Docker container |
| Windows  | x86_64 | Docker container |
| Android  | armeabi-v7a (arm), arm64-v8a (arm64), x86, x86_64 | Docker container |

---

## Build Outputs

After a successful build, binary JAR files are located in:
```
./build/libs/
```

### JAR Files Generated

**Core wrapper library (required for all platforms):**
- `jlibtorrent-w.x.y.z.jar` - Java class files and interfaces

**Platform-specific binaries:**
- `jlibtorrent-macosx-x86_64-w.x.y.z.jar` - macOS Intel (x86_64)
- `jlibtorrent-macosx-arm64-w.x.y.z.jar` - macOS Apple Silicon (arm64)
- `jlibtorrent-windows-w.x.y.z.jar` - Windows x86_64
- `jlibtorrent-linux-x86_64-w.x.y.z.jar` - Linux x86_64
- `jlibtorrent-linux-arm64-w.x.y.z.jar` - Linux arm64
- `jlibtorrent-android-arm-w.x.y.z.jar` - Android armeabi-v7a (32-bit ARM)
- `jlibtorrent-android-arm64-w.x.y.z.jar` - Android arm64-v8a (64-bit ARM)
- `jlibtorrent-android-x86-w.x.y.z.jar` - Android x86
- `jlibtorrent-android-x86_64-w.x.y.z.jar` - Android x86_64

### Native Library Files

Raw native libraries are available in:
```
./swig/bin/release/
```

Structure:
```
swig/bin/release/
├── windows/x86_64/
│   ├── jlibtorrent.dll
│   └── jlibtorrent.dll.debug
├── macosx/
│   ├── x86_64/
│   │   ├── libjlibtorrent.dylib
│   │   └── libjlibtorrent.dylib.debug
│   └── arm64/
│       ├── libjlibtorrent.dylib
│       └── libjlibtorrent.dylib.debug
├── linux/
│   ├── x86_64/
│   │   ├── libjlibtorrent.so
│   │   └── libjlibtorrent.so.debug
│   └── arm64/
│       ├── libjlibtorrent.so
│       └── libjlibtorrent.so.debug
└── android/
    ├── armeabi-v7a/
    │   ├── libjlibtorrent.so
    │   └── libjlibtorrent.so.debug
    ├── arm64-v8a/
    │   ├── libjlibtorrent.so
    │   └── libjlibtorrent.so.debug
    ├── x86/
    │   ├── libjlibtorrent.so
    │   └── libjlibtorrent.so.debug
    └── x86_64/
        ├── libjlibtorrent.so
        └── libjlibtorrent.so.debug
```

---

## macOS Builds

Building on macOS requires a physical Mac computer because cross-compilation from Docker x86_64 containers to macOS is not currently supported by Apple's toolchain. Each architecture (Intel x86_64 and Apple Silicon arm64) must be built on a Mac with that architecture.

### Prerequisites

Install build dependencies via Homebrew:

```bash
brew install wget pcre2 automake autoconf bison
```

**Important:** macOS Xcode's built-in `bison` has compatibility issues. The script ensures the Homebrew `bison` is used.

### Prepare macOS Build Environment

From the repository root, navigate to the swig directory and run the preparation script:

```bash
cd swig
./prepare-macos.sh
```

This script:
- Validates your Xcode Command Line Tools installation
- Checks for the required SWIG version (4.3.1)
- Downloads and builds SWIG if needed
- Installs SWIG globally to `/usr/local/bin`

**Typical output:**
```
SWIG version 4.3.1 is already installed.
No need to reinstall SWIG.
```

Or if installing:
```
Proceeding to download and install SWIG version 4.3.1...
[... compilation output ...]
SWIG version 4.3.1 has been successfully installed.
```

### Build macOS Binaries

Once the environment is prepared, build the native library:

```bash
cd swig
./build-macos.sh
```

The `build-macos.sh` script automatically detects your Mac's architecture and calls the appropriate build script:
- On Intel Mac: executes `./build-macos-x86_64.sh`
- On Apple Silicon Mac: executes `./build-macos-arm64.sh`

**Build time:** Typically 10-30 minutes depending on your Mac's performance and build cache.

**Successful completion example:**
```
Building libtorrent for macOS arm64...
[... compilation output ...]
Generated jlibtorrent-macosx-arm64-2.0.12.7.jar
```

### Building Both macOS Architectures

To support both Intel and Apple Silicon, you have two options:

**Option 1: Build on each Mac type (Recommended)**
- Build on Intel Mac: produces `jlibtorrent-macosx-x86_64-w.x.y.z.jar`
- Build on Apple Silicon Mac: produces `jlibtorrent-macosx-arm64-w.x.y.z.jar`

**Option 2: Cross-compilation investigation (Advanced)**
- Currently, Apple's toolchain doesn't support cross-compilation from Docker x86_64 to macOS
- Future enhancement: May be possible with Apple's cross-compiler setup
- You would still need Mac hardware for architecture-native builds to verify correctness

**Recommendation:** Use Option 1 for production builds.

### macOS Build Environment Variables

The build scripts use these environment variables (optional to override):

```bash
# Maximum number of parallel build jobs
export CORES=8

# Custom build cache location
export BUILD_CACHE=/path/to/cache

# macOS deployment target
export MACOSX_DEPLOYMENT_TARGET=10.13
```

---

## Docker-Based Builds

Docker-based builds produce Windows, Linux, and Android binaries all in one containerized environment. This approach ensures consistency and eliminates platform-specific build issues.

### Prerequisites

**Required:**
- Docker installed and running on your system
- At least 8 GB of free RAM (16+ GB recommended)
- 20-30 GB of free disk space for build cache and artifacts

**Supported host platforms:**
- macOS (any Intel or Apple Silicon Mac with Docker Desktop)
- Linux x86_64 (not arm64 - Android NDK tools are x86_64 only)
- Windows with WSL2 or Docker Desktop

### One-Command Build Process

From the repository root:

```bash
cd swig
./docker_build_binaries.sh
```

This single command:
1. Builds the Docker image (cached if unchanged)
2. Starts a Docker container with optimized CPU and memory allocation
3. Compiles Windows x86_64 binaries
4. Compiles Linux x86_64 and arm64 binaries
5. Compiles all Android variants

**Total build time:** 45-90 minutes depending on your system and build cache state.

### Docker Build Process Details

#### Step 1: Docker Image Build

```bash
./docker_build_image.sh
```

This command:
- Checks host OS and CPU count
- Validates host isn't arm64 (Android NDK requires x86_64)
- Builds `jlibtorrent-android` Docker image using the `Dockerfile`
- Tags the image for later reuse

The build is cached, so subsequent runs are instant unless dependencies change.

**Example output:**
```
docker_build_image.sh: We tried doing this on linux arm64 but...
docker_build_image.sh: Number of CPUs: 8
Step 1/50 : FROM ubuntu:20.04
...
Successfully built jlibtorrent-android:latest
```

#### Step 2: Docker Container Execution

```bash
./docker_build_binaries.sh
```

Automatically executes after image is built. The script:
- Calculates 80% of available system RAM and assigns to container
- Counts available CPU cores and assigns to container
- Mounts the repository as `/frostwire-jlibtorrent` inside container
- Runs two build scripts inside container:
  - `/build_desktop.sh` - Windows and Linux binaries
  - `/build_android_all.sh` - All Android variants

**Memory and CPU allocation:**
```bash
--cpus=$NUM_CPUS          # All available CPUs
--memory=$TOTAL_MEMORY    # 80% of available RAM
```

### Docker Build Configuration

The `Dockerfile` includes:
- Ubuntu 20.04 base image
- Build tools: gcc, clang, make, cmake
- Cross-compilation toolchains: NDK, MSVC, Linux GCC
- Dependencies: OpenSSL, Boost, pkg-config

Environment variables in Docker:
```bash
CORES=<host_cpu_count>  # Parallel build jobs
NDK_VERSION=r21e        # Android NDK version
```

### Docker Build Outputs

After `docker_build_binaries.sh` completes:

1. **JAR files:** `./build/libs/` (main deliverables)
2. **Raw binaries:** `./swig/bin/release/` (for debugging/distribution)
3. **Build logs:** Printed to console during execution

### Troubleshooting Docker Builds

**Container exits unexpectedly:**
```bash
# Run interactively to see output
cd swig
docker_run_interactive.sh
```

**Out of memory errors:**
```bash
# Reduce cores used (limits parallelism but uses less RAM)
export NUM_CPUS=4
./docker_build_binaries.sh
```

**Docker image cache is stale:**
```bash
# Force rebuild the Docker image
docker rmi jlibtorrent-android
./docker_build_image.sh
```

---

## Platform-Specific Notes

### Android Builds

**All Android native libraries are built with 16 KB page size compatibility** for Google Play Store requirements (mandatory as of November 2025).

**Android SDK/NDK Information:**
- Minimum SDK: API 24 (Android 7.0)
- NDK Version: r21e (specified in Dockerfile)
- Architectures: armeabi-v7a, arm64-v8a, x86, x86_64

**Android JAR dependencies:**
Most Android projects need all four architectures:
```gradle
implementation 'com.frostwire:jlibtorrent-android-arm:2.0.12.7'
implementation 'com.frostwire:jlibtorrent-android-arm64:2.0.12.7'
implementation 'com.frostwire:jlibtorrent-android-x86:2.0.12.7'
implementation 'com.frostwire:jlibtorrent-android-x86_64:2.0.12.7'
```

### Linux Builds

**Linux x86_64 and arm64** are both built inside the Docker container.

**Build environment:**
- GCC cross-compiler for arm64 when building on x86_64
- Native build for x86_64

**Linux SDK/toolchain:**
- GCC 9.x for both architectures
- glibc 2.29+ compatibility

### Windows Builds

**Windows x86_64 only** (arm64 Windows not currently supported).

**Build tools:**
- MinGW-w64 cross-compiler running on Linux
- Builds .dll for Windows x86_64

**Windows SDK compatibility:**
- Windows 7 SP1 and later
- x86_64 architecture only

### macOS Builds

See [macOS Builds](#macos-builds) section above.

---

## Full Build Workflow Example

Complete example: building for all platforms on a Mac with Docker installed.

### Step 1: Prepare macOS Environment (one-time)

```bash
cd swig
./prepare-macos.sh
```

### Step 2: Build macOS Binaries

```bash
cd swig
./build-macos.sh
# Produces: jlibtorrent-macosx-arm64-w.x.y.z.jar (on Apple Silicon Mac)
# or: jlibtorrent-macosx-x86_64-w.x.y.z.jar (on Intel Mac)
```

### Step 3: Build All Other Platforms (Docker)

```bash
cd swig
./docker_build_binaries.sh
# Produces:
# - jlibtorrent-windows-w.x.y.z.jar
# - jlibtorrent-linux-x86_64-w.x.y.z.jar
# - jlibtorrent-linux-arm64-w.x.y.z.jar
# - jlibtorrent-android-arm-w.x.y.z.jar
# - jlibtorrent-android-arm64-w.x.y.z.jar
# - jlibtorrent-android-x86-w.x.y.z.jar
# - jlibtorrent-android-x86_64-w.x.y.z.jar
```

### Step 4: Verify Build Outputs

```bash
cd build/libs/
ls -lah jlibtorrent-*.jar
```

All JAR files should be present and contain JNI libraries for their respective platforms.

---

## Performance Optimization

### Reducing Build Time

1. **Use a fast disk for Docker volumes:**
   ```bash
   # NVMe/SSD significantly faster than spinning drives
   ```

2. **Cache preservation:**
   ```bash
   # Don't delete build/libs between builds to reuse cached artifacts
   ```

3. **Parallel jobs (if low memory):**
   ```bash
   export CORES=4  # Reduce from available CPUs
   ./docker_build_binaries.sh
   ```

### Recommended System Specs for Full Builds

- **CPU:** 8+ cores (16+ cores recommended)
- **RAM:** 16 GB minimum (32 GB recommended)
- **Disk:** 50 GB SSD minimum (100 GB for comfortable cache)
- **Network:** 100 Mbps+ (for downloading dependencies)

---

## Troubleshooting

### macOS: "SWIG not found" or "Wrong version"

```bash
cd swig
./prepare-macos.sh  # Reinstalls SWIG
```

### Docker: "Cannot connect to Docker daemon"

```bash
# Ensure Docker Desktop is running (macOS/Windows)
# Or Docker daemon is running (Linux)
docker ps  # Test connection
```

### Docker: "arm64 architecture not supported"

```
docker_build_image.sh: We tried doing this on linux arm64 but the Android NDK
tools are meant to run on x86_64. Exiting.
```

**Solution:** Android NDK cross-compilation tools run only on x86_64. Use an x86_64 machine or a Docker buildx setup targeting x86_64.

### Build fails with "Out of memory"

```bash
# Reduce memory pressure:
export CORES=4       # Fewer parallel jobs
./docker_build_binaries.sh

# Or kill other applications and retry
```

### Build completes but JAR files empty

```bash
# Check Docker volume mount permissions
ls -lah ./build/libs/

# Try running with verbose output
docker_run_interactive.sh
```

### Rebuilding after source changes

When you modify C++ source, rebuild via:

```bash
cd swig
make clean  # Clear build artifacts
./build-macos.sh  # or docker_build_binaries.sh
```

---

## Building Specific Platforms Only

To build individual platforms (not recommended - use full build for consistency):

```bash
# macOS only
cd swig && ./build-macos-arm64.sh    # Or build-macos-x86_64.sh

# Linux only (from Docker container)
cd swig && ./build-linux.sh

# Android specific build (from Docker container)
cd swig && ./build-android-arm64.sh  # Or other variants
```

---

## Advanced: Custom Docker Container

For custom builds or debugging:

```bash
cd swig
./docker_run_interactive.sh
# Now you're inside the container with access to build scripts
bash /build_desktop.sh
bash /build_android_all.sh
```

---

## Version Information

Current build configuration:
- **SWIG:** 4.3.1 (macOS builds)
- **Android NDK:** r21e (Docker builds)
- **libtorrent:** RC_2_0 branch (latest)
- **Boost:** Latest stable (managed by build system)

---

## Summary

| Platform | Build Method | Command | Time |
|----------|--------------|---------|------|
| macOS Intel (x86_64) | Native on Mac | `cd swig && ./build-macos-x86_64.sh` | 15-30 min |
| macOS Apple Silicon (arm64) | Native on Mac | `cd swig && ./build-macos-arm64.sh` | 15-30 min |
| Windows x86_64 | Docker | `cd swig && ./docker_build_binaries.sh` | 45-90 min |
| Linux x86_64 + arm64 | Docker | `cd swig && ./docker_build_binaries.sh` | 45-90 min |
| Android (all variants) | Docker | `cd swig && ./docker_build_binaries.sh` | 45-90 min |
| **ALL platforms** | Docker + Mac | Both commands above | ~3-4 hours total |

---

## Next Steps

After building binaries:
1. JAR files in `./build/libs/` are ready to use
2. Push to Maven repository: `./gradlew publish`
3. Create GitHub release with JAR files
4. Update documentation with new version

For further information, see [README.md](README.md).
