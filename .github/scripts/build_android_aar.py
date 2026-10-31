#!/usr/bin/env python3
"""Packs jlibtorrent's Android jars into one Android library (AAR).

usage: build_android_aar.py <dist_dir> <version>

Reads from <dist_dir> the Java classes jar (jlibtorrent-<version>.jar) and the per-ABI native jars
(jlibtorrent-android-{arm,arm64,x86,x86_64}-<version>.jar, each holding lib/<abi>/libjlibtorrent.so)
and writes <dist_dir>/jlibtorrent-android-<version>.aar:

  AndroidManifest.xml   package com.frostwire.jlibtorrent, minSdk 26 (the natives' ANDROID_API)
  classes.jar           the Java classes jar, unchanged
  jni/<abi>/libjlibtorrent.so   one per ABI, byte-identical to the jars' copies
  proguard.txt          consumer keep rules: R8 applies them to every app that uses the AAR
  R.txt                 empty (no resources)

The jars stay published: the AAR is built from them, and apps that don't use AARs keep using them.
Apps pick ABIs with ndk.abiFilters instead of picking jars. Python standard library only.
"""
import sys
import zipfile
from pathlib import Path

ABIS = {  # jar suffix -> Android ABI directory
    "arm": "armeabi-v7a",
    "arm64": "arm64-v8a",
    "x86": "x86",
    "x86_64": "x86_64",
}

MIN_SDK = 26  # swig/Dockerfile ANDROID_API: the natives can't load on older Android

MANIFEST = f"""<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.frostwire.jlibtorrent">
    <uses-sdk android:minSdkVersion="{MIN_SDK}" />
</manifest>
"""

# Names the native library looks up at run time: R8 must not rename or remove them.
CONSUMER_RULES = """# jlibtorrent (from the AAR; R8 applies these to the app that uses it)
# The native methods, and the SwigDirector_* methods C++ calls back into, are looked up by name.
-keep class com.frostwire.jlibtorrent.swig.libtorrent_jni { *; }
-keepclasseswithmembernames,includedescriptorclasses class com.frostwire.jlibtorrent.** {
    native <methods>;
}
# Directors: C++ finds these classes by name and calls their methods by name, including the
# overrides in an app's subclasses.
-keep class com.frostwire.jlibtorrent.swig.add_files_listener { *; }
-keep class com.frostwire.jlibtorrent.swig.alert_notify_callback { *; }
-keep class com.frostwire.jlibtorrent.swig.posix_wrapper { *; }
-keep class com.frostwire.jlibtorrent.swig.set_piece_hashes_listener { *; }
-keep class * extends com.frostwire.jlibtorrent.swig.add_files_listener { *; }
-keep class * extends com.frostwire.jlibtorrent.swig.alert_notify_callback { *; }
-keep class * extends com.frostwire.jlibtorrent.swig.posix_wrapper { *; }
-keep class * extends com.frostwire.jlibtorrent.swig.set_piece_hashes_listener { *; }
"""


def build(dist: Path, version: str) -> Path:
    classes = dist / f"jlibtorrent-{version}.jar"
    if not classes.is_file():
        sys.exit(f"build_android_aar: missing {classes}")
    out = dist / f"jlibtorrent-android-{version}.aar"
    tmp = out.with_suffix(".aar.part")
    with zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as aar:
        aar.writestr("AndroidManifest.xml", MANIFEST)
        aar.write(classes, "classes.jar")
        aar.writestr("proguard.txt", CONSUMER_RULES)
        aar.writestr("R.txt", "")
        for suffix, abi in ABIS.items():
            jar = dist / f"jlibtorrent-android-{suffix}-{version}.jar"
            if not jar.is_file():
                sys.exit(f"build_android_aar: missing {jar}")
            with zipfile.ZipFile(jar) as z:
                name = f"lib/{abi}/libjlibtorrent.so"
                try:
                    data = z.read(name)
                except KeyError:
                    sys.exit(f"build_android_aar: {jar.name} has no {name}")
            # stored, not deflated: the bytes stay exactly as built (and 16 KB aligned inside)
            aar.writestr(zipfile.ZipInfo(f"jni/{abi}/libjlibtorrent.so", date_time=(1980, 1, 1, 0, 0, 0)),
                         data, compress_type=zipfile.ZIP_STORED)
    tmp.replace(out)
    return out


if __name__ == "__main__":
    if len(sys.argv) != 3:
        sys.exit(__doc__)
    print(build(Path(sys.argv[1]), sys.argv[2]))
