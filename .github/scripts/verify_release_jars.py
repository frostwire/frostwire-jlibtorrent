#!/usr/bin/env python3
"""Checks a directory of jlibtorrent release jars before they are published.

Usage: verify_release_jars.py <dir> <version>

For each of the 12 jars a release ships it checks that the jar exists and, for the native jars,
that it holds exactly the native library NativeLibraryLoader looks for, at that path, built for
the right CPU, carrying <version> (the Java side refuses a library of another version), and, on
64-bit Android, with 16 KB aligned LOAD segments (required by Android 15+ devices with 16 KB
pages). Exits non-zero listing every problem.
"""

import struct
import sys
import zipfile

# ELF e_machine, PE Machine and Mach-O cputype values.
EM_386, EM_ARM, EM_X86_64, EM_AARCH64 = 3, 40, 62, 183
PE_AMD64 = 0x8664
MACHO_X86_64, MACHO_ARM64 = 0x01000007, 0x0100000C

# jar name prefix -> (path inside the jar, format, expected machine, needs 16 KB alignment)
NATIVE_JARS = {
    "jlibtorrent-macosx-arm64": ("lib/arm64/libjlibtorrent.arm64.dylib", "macho", MACHO_ARM64, False),
    "jlibtorrent-macosx-x86_64": ("lib/x86_64/libjlibtorrent.x86_64.dylib", "macho", MACHO_X86_64, False),
    "jlibtorrent-linux-x86_64": ("lib/x86_64/libjlibtorrent.so", "elf", EM_X86_64, False),
    "jlibtorrent-linux-arm64": ("lib/arm64/libjlibtorrent.so", "elf", EM_AARCH64, False),
    "jlibtorrent-windows": ("lib/x86_64/jlibtorrent.dll", "pe", PE_AMD64, False),
    "jlibtorrent-android-arm": ("lib/armeabi-v7a/libjlibtorrent.so", "elf", EM_ARM, False),
    "jlibtorrent-android-arm64": ("lib/arm64-v8a/libjlibtorrent.so", "elf", EM_AARCH64, True),
    "jlibtorrent-android-x86": ("lib/x86/libjlibtorrent.so", "elf", EM_386, False),
    "jlibtorrent-android-x86_64": ("lib/x86_64/libjlibtorrent.so", "elf", EM_X86_64, True),
}
NATIVE_SUFFIXES = (".so", ".dll", ".dylib")


def elf_info(data):
    """(e_machine, [p_align of each PT_LOAD segment]) of an ELF image."""
    is64 = data[4] == 2
    endian = "<" if data[5] == 1 else ">"
    machine = struct.unpack_from(endian + "H", data, 18)[0]
    if is64:
        phoff = struct.unpack_from(endian + "Q", data, 0x20)[0]
        phentsize, phnum = struct.unpack_from(endian + "HH", data, 0x36)
    else:
        phoff = struct.unpack_from(endian + "I", data, 0x1C)[0]
        phentsize, phnum = struct.unpack_from(endian + "HH", data, 0x2A)
    aligns = []
    for i in range(phnum):
        offset = phoff + i * phentsize
        if struct.unpack_from(endian + "I", data, offset)[0] != 1:  # PT_LOAD
            continue
        if is64:
            aligns.append(struct.unpack_from(endian + "Q", data, offset + 48)[0])
        else:
            aligns.append(struct.unpack_from(endian + "I", data, offset + 28)[0])
    return machine, aligns


def machine_of(data, fmt):
    if fmt == "elf":
        if data[:4] != b"\x7fELF":
            return None
        return elf_info(data)[0]
    if fmt == "pe":
        if data[:2] != b"MZ":
            return None
        pe = struct.unpack_from("<I", data, 0x3C)[0]
        if data[pe:pe + 4] != b"PE\0\0":
            return None
        return struct.unpack_from("<H", data, pe + 4)[0]
    if fmt == "macho":
        if data[:4] != b"\xcf\xfa\xed\xfe":  # 64-bit little-endian Mach-O
            return None
        return struct.unpack_from("<I", data, 4)[0]
    return None


def check_native(directory, version, prefix, spec, problems):
    path, fmt, machine, needs_16k = spec
    name = f"{prefix}-{version}.jar"
    try:
        jar = zipfile.ZipFile(f"{directory}/{name}")
    except FileNotFoundError:
        problems.append(f"{name}: missing")
        return
    with jar:
        natives = [n for n in jar.namelist() if n.endswith(NATIVE_SUFFIXES)]
        if natives != [path]:
            problems.append(f"{name}: expected only {path}, found {natives}")
            if path not in natives:
                return
        data = jar.read(path)
    found = machine_of(data, fmt)
    if found != machine:
        problems.append(f"{name}: {path} is not a {fmt} binary for machine {machine:#x} (found {found})")
        return
    if version.encode() not in data:
        problems.append(f"{name}: {path} does not carry version {version}")
    if needs_16k:
        aligns = elf_info(data)[1]
        if not aligns or min(aligns) < 16384:
            problems.append(f"{name}: LOAD segments aligned to {aligns}, Android needs 16384")
    print(f"ok  {name}  {path}  {len(data)} bytes")


def check_java(directory, version, problems):
    for suffix in ("", "-sources", "-javadoc"):
        name = f"jlibtorrent-{version}{suffix}.jar"
        try:
            with zipfile.ZipFile(f"{directory}/{name}") as jar:
                names = set(jar.namelist())
        except FileNotFoundError:
            problems.append(f"{name}: missing")
            continue
        wanted = {
            "": "com/frostwire/jlibtorrent/SessionManager.class",
            "-sources": "com/frostwire/jlibtorrent/SessionManager.java",
            "-javadoc": "com/frostwire/jlibtorrent/SessionManager.html",
        }[suffix]
        if wanted not in names:
            problems.append(f"{name}: has no {wanted}")
        else:
            print(f"ok  {name}")


def main():
    if len(sys.argv) != 3:
        sys.exit(__doc__)
    directory, version = sys.argv[1], sys.argv[2]
    problems = []
    check_java(directory, version, problems)
    for prefix, spec in NATIVE_JARS.items():
        check_native(directory, version, prefix, spec, problems)
    if problems:
        print("\n".join(["", "PROBLEMS:"] + problems))
        sys.exit(1)
    print(f"\nAll 12 jlibtorrent {version} jars check out.")


if __name__ == "__main__":
    main()
