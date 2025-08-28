# Android 16 KB Page Size Compatibility

This document explains the changes made to support Android's 16 KB page size requirement.

## Background

Starting November 1st, 2025, all new apps and updates to existing apps submitted to Google Play and targeting Android 15+ devices must support 16 KB page sizes. For more information, visit [Android's official documentation](https://developer.android.com/guide/practices/page-sizes).

## Changes Made

Added the linker flag `-Wl,--max-page-size=16384` to all Android architecture configuration files:

- `swig/config/android-arm-config.jam`
- `swig/config/android-arm64-config.jam` 
- `swig/config/android-x86-config.jam`
- `swig/config/android-x86_64-config.jam`

## How to Verify

### During Build
When building Android native libraries, the linker flag will be applied automatically. You can verify it's being used by checking the build logs for the presence of `--max-page-size=16384`.

### On Device Testing
To verify page size compatibility on an Android device:

```bash
# Check the page size on a device
adb shell getconf PAGE_SIZE
```

The output should be `16384` on devices with 16 KB page sizes.

### APK Analysis
You can analyze the APK in Android Studio to check for any warnings related to page size compatibility.

## Technical Details

- **What it does**: Ensures native shared libraries (`.so` files) are aligned to 16 KB page boundaries
- **Impact**: Minimal - only affects Android native library builds
- **Compatibility**: Backward compatible - works on devices with both 4 KB and 16 KB page sizes

## Build Process

The flag is automatically applied during the native library compilation process when building Android architectures using the Docker-based build system:

```bash
cd swig && ./docker_build_binaries.sh
```

The resulting `.so` files will be compatible with 16 KB page size devices.