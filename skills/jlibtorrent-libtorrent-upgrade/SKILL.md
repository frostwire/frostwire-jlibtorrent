# Skill: jlibtorrent-libtorrent-upgrade

## Description

End-to-end workflow for upgrading the underlying libtorrent C++ library in the frostwire-jlibtorrent Java/JNI bindings project. This skill covers the full lifecycle from version bump through SWIG regeneration, Java wrapper additions, native builds across all platforms, testing, and release.

Use this skill whenever a new libtorrent `RC_2_0` commit needs to be integrated.

**Branch policy (2026-10-29):** jlibtorrent 2.0.x tracks `RC_2_0`. Upstream's default branch is now `RC_2_1` (v2.1.x), but that migration has ~20 binding breaks and a new default disk engine; it is planned for the FrostWire 7.2.x series in frostwire/frostwire-jlibtorrent#329, which lists the measured breaks. Don't drift onto `RC_2_1` during a 2.0.x upgrade.

---

## Prerequisites

### Dev Machine (Apple Silicon Mac)
- macOS with Xcode / command-line tools
- SWIG 4.5.1 on the `PATH` (`swig -version`). It isn't packaged anywhere useful; build it from the tarball on `dl.frostwire.com/other/swig/` (see "Upgrading SWIG"). `run-swig.sh` uses whichever `swig` comes first on the `PATH`.
- JDK 17 for Gradle: run `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` first. Gradle 9.5.1 can't run on a newer default JDK (JDK 27 fails with `Unsupported class file major version 71`).
- A consistent Apple toolchain: clang, ld and the SDK from the same install (see Build Config Caveats, "Toolchain/SDK mismatch")
- Gradle or `./gradlew`
- Local libtorrent checkout at `~/src/libtorrent` (for SWIG regeneration)
- Local boost at `~/src/boost_1_88_0`
- Local openssl at `~/src/openssl`

### Intel Mac (separate machine)
- macOS with Xcode / command-line tools
- Same local dependencies as dev machine
- Used ONLY for macOS x86_64 builds

### AWS EC2 x86_64 Server
- Docker installed and running
- Repo checked out at the correct commit
- Used for ALL non-macOS platforms: Linux x86_64, Linux arm64, Windows x86_64, Android (arm/arm64/x86/x86_64)

---

## Step 1: Identify Target Commit and Version

1. Determine the target `libtorrent` commit hash on `RC_2_0`.
2. Check `include/libtorrent/version.hpp` in that commit to confirm MAJOR.MINOR.TINY.
3. jlibtorrent version format: `{libtorrent MAJOR}.{libtorrent MINOR}.{libtorrent TINY}.{jlibtorrent patch}`
   - Example: libtorrent `2.0.12` + jlibtorrent patch `8` → version `2.0.12.9`
   - A new libtorrent version starts the patch at 0: libtorrent `2.0.15` → `2.0.15.0`
   - Since 2.0.15.0 the native file names carry no version and the native library reports its version through `libtorrent_jni.jlibtorrentNativeVersion()`. A Java-only fix can ship as `X.Y.Z.(W+1)` reusing the previous natives, as long as the libtorrent part (`X.Y.Z`) matches.

**Lesson Learned (cb6fe6b9c upgrade):** Even 86 commits on the same RC_2_0 branch can still be libtorrent `2.0.12` with only the jlibtorrent patch incrementing.

**Lesson Learned (eb397173d upgrade):** `LIBTORRENT_REVISION` inside libtorrent's `version.hpp` is not updated on every commit (eb397173d still says `485467b33`); trust the `MAJOR.MINOR.TINY` defines, not that string.

---

## Step 2: Update Mechanical Version References

**CRITICAL:** Do this in a single commit before any build/SWIG work. These files have hardcoded version strings that are easy to miss:

| File | What to update |
|------|---------------|
| `swig/Dockerfile` | `ENV LIBTORRENT_REVISION="<commit>"` |
| `swig/build-utils.shinc` | `export LIBTORRENT_REVISION=<commit>` (update inline comment too) |
| `build.gradle` | `version = ... 'X.Y.Z.W'` |
| `README.md` | Groovy/Kotlin/SBT dependency examples |
| `BUILD_MANUAL.md` | Any hardcoded versions in build examples |
| `.github/workflows/publish.yml` | `default: 'release/X.Y.Z.W'` |
| `src/main/java/com/frostwire/jlibtorrent/LibTorrent.java` | Javadoc example output comments |
| `changelog.txt` | Add new version header at the TOP (newest first) |

**Lesson Learned (cb6fe6b9c upgrade):** We missed `publish.yml`, `LibTorrent.java`, and `BUILD_MANUAL.md` on the first pass. Always grep for the old version string across the entire repo before committing.

```bash
grep -rn "X.Y.Z.W" --include="*.md" --include="*.gradle" --include="*.sh" --include="*.java" --include="*.yml" --include="*.txt"
```

Commit: `Update libtorrent revision to <hash>`

---

## Step 3: Regenerate SWIG Bindings

### macOS path (always use this for SWIG)
```bash
cd swig
export SRC="${HOME}/src"
export BOOST_MAJOR="1"
export BOOST_VERSION="88"
export BOOST_MINOR="0"
export LIBTORRENT_ROOT="${SRC}/libtorrent"
export OPENSSL_VERSION="3.5.2"
./run-swig.sh
```

`run-swig.sh` never modifies the libtorrent checkout (since #339). SWIG parses a patched copy of `torrent_handle.hpp` from a temporary overlay directory searched first: SWIG 4.5.1 can't parse its braced default argument `client_data_t{}`. The other six in-place `sed` edits older versions needed (`alert.hpp`, `file_storage.hpp`, `announce_entry.hpp`, `ip_filter.hpp`) are gone; without them the output is byte-identical. Old checkouts may still carry the `alert.hpp` edit from before b8581836 (`//deleted temporarily because it is defined twice`): `git -C ~/src/libtorrent status` must be clean. When a SWIG or libtorrent upgrade breaks parsing, add the fix to `prepareHeaderOverlay`, never to the checkout.

Ensure the local `libtorrent` repo is at the target commit before running SWIG. `~/src/libtorrent`'s remote may be a fork that lacks the commit, so fetch it from upstream by URL:
```bash
cd ~/src/libtorrent
git checkout -- include/libtorrent/alert.hpp   # only if the stale SWIG edit is there
git fetch https://github.com/arvidn/libtorrent <commit>
git checkout RC_2_0
git reset --hard <commit>
git submodule update --init
```

Review the diff of auto-generated files:
- `swig/libtorrent_jni.cpp`
- `swig/libtorrent_jni.h`
- `src/main/java/com/frostwire/jlibtorrent/swig/*.java`

Look for:
- New enum values in `settings_pack` → add to `SettingsPack.java`
- New alert types → add to `Alerts.java` alert type table
- New `torrent_handle` methods/flags → add to `TorrentHandle.java`
- New `stats_metric` indices → add to `StatsMetric.java`
- New `torrent_info` methods → add to `TorrentInfo.java`

**Lesson Learned (cb6fe6b9c upgrade):** The 86-commit delta only exposed 3 new settings and 1 new `torrent_info` method. Don't assume scale — always diff the generated files.

**Lesson Learned (eb397173d upgrade):** 123 commits exposed only `torrent_info::resolve_duplicate_filenames()`, which upstream marks `// hidden` (internal). A header diff can also be comment-only (`settings_pack.hpp` +26 lines, no new settings). Check for `// hidden` before wrapping something SWIG exposed.

Commit: `SWIG regeneration: <list new features>`

---

## Step 3b (optional, before committing to a target): Measure binding breaks cheaply

To judge a target, especially a new upstream branch, without building natives:
1. `git clone --filter=blob:none https://github.com/arvidn/libtorrent` into a scratch dir, then `git worktree add` the candidate commit and run `git submodule update --init --depth 1` there.
2. Copy `swig/` (minus `bin`, `build`, `swig-*`) and `build.gradle` into a scratch dir, then run the real `swig/run-swig.sh` with `LIBTORRENT_ROOT=<worktree>`. A hand-rolled `swig` command without `fixCode`'s header patches fails at `torrent_handle.hpp` (`client_data_t{}` default argument).
3. Syntax-check the generated C++ with the build's defines:
   ```bash
   clang++ -std=c++20 -fsyntax-only -fno-rtti -ferror-limit=0 -w \
     -DTORRENT_ABI_VERSION=100 -DTORRENT_USE_OPENSSL -DTORRENT_USE_LIBCRYPTO -DTORRENT_SSL_PEERS \
     -DBOOST_ASIO_ENABLE_CANCELIO -DBOOST_ASIO_NO_DEPRECATED \
     -I. -I<worktree>/include -I$HOME/src/boost_1_88_0 -I/opt/homebrew/opt/openssl@3/include libtorrent_jni.cpp
   ```
4. Each `fatal error: 'libtorrent/X.hpp' file not found` hides everything after it. If `include/libtorrent/aux_/X.hpp` exists, add a shim `shim/libtorrent/X.hpp` containing `#include <libtorrent/aux_/X.hpp>`, add `-Ishim`, and repeat. Then group the errors by symbol.

2026-10-29 result: RC_2_0 head had 0 errors; RC_2_1 at e5590c42a had 165 errors from about 20 causes (see #329).

## Step 4: Diff Review & Triage

Before writing any Java wrappers, study the libtorrent delta to understand what needs wrapping:

```bash
cd ~/src/libtorrent
git diff --stat <old_commit>..<new_commit> -- include/libtorrent/*.hpp
git log --oneline --no-merges <old_commit>..<new_commit>
```

Key files to check:
- `include/libtorrent/settings_pack.hpp` — new settings constants
- `include/libtorrent/alert_types.hpp` — new alert types
- `include/libtorrent/torrent_handle.hpp` — new methods/flags
- `include/libtorrent/torrent_info.hpp` — new metadata queries
- `include/libtorrent/create_torrent.hpp` — BEP changes (e.g., symlinks)
- `include/libtorrent/stats_metric.hpp` — new performance counters

For the changelog's `lt:` lines, list only upstream entries that are new to us. Release tags don't tell you that: entries pile up on the branch before a tag, so some may already be in the old pin. Diff the ChangeLog itself:
```bash
diff <(git show <old>:ChangeLog) <(git show <new>:ChangeLog) | grep '^>' | grep '\*'
```

Partition work among parallel agents based on the diff. Write a MentisDB Checkpoint with specific task assignments.

---

## Step 5: Add Java Wrappers and Tests

### Writing Wrappers
For each new symbol exposed by SWIG:
1. Add type-safe Java wrapper methods/constants in the relevant hand-written class.
2. Add comprehensive Javadoc (3+ lines, real behavior, not boilerplate).
3. Follow existing naming conventions (`camelCase` in Java, matching the C++ snake_case concept).

### Writing Tests (NOT LAME)
**Do NOT write round-trip getter/setter tests that only prove JNI works.** Every test must validate actual libtorrent behavior:

| Good Test | Bad Test |
|-----------|----------|
| Verify DEFAULT value from actual SWIG layer | `setFoo(true); assertTrue(getFoo())` |
| Test boundary conditions (0, -1, MAX_VALUE) | Round-trip with a random value |
| Compare v1 vs v2 semantics for new methods | Assert `a == b` without understanding why |
| Test that invalid indices don't crash the JVM | Assert a specific return value for undefined behavior |
| Verify SWIG enum ordinal matches expected value | Just check the Java constant exists |
| Test integration: set via wrapper, read via raw `swig()` object | Only test through the Java wrapper layer |

**Lesson Learned (cb6fe6b9c upgrade):**
- `diskDisableCopyOnWrite` default changed from `true` to `false` upstream (commit `5720b90a1`). Tests that hardcoded the old default failed.
- `pieceSizeForReq()` for v2 torrents does NOT monotonically decrease and sum does NOT equal `totalSize` (pad blocks are excluded). Naive structural assumptions caused test failures.
- `pieceSizeForReq(-1)` returns a garbage value (65536) rather than 0 because the C++ function doesn't validate input. Tests should verify JVM survival, not assert a return value for undefined behavior.

Commit: `Add <feature> to <Class>`, `Add tests for <feature>`

---

## Step 6: Smoke Test — macOS arm64 ONLY (Dev Machine)

**ALWAYS run a smoke test on macOS arm64 before handing off to other build machines.** This catches compile failures, linker errors, and runtime crashes early. You only need to smoke test ONE platform — if it fails here, it will fail everywhere with the same root cause.

### macOS arm64 smoke test (dev machine)
```bash
cd swig
./build-macos-arm64.sh --build-only
```

If the build fails, diagnose before proceeding. Common causes:
- **Compiler flags incompatible with upstream changes** — e.g., `-fno-rtti` breaks `dynamic_cast` in new code (see Build Config Caveats).
- **Missing cross-compiler or toolchain**
- **SWIG generation produced uncompilable output**

Once the `.dylib` builds:
```bash
cd ..
./gradlew test
```

If tests fail:
- Check `forkEvery = 1` is set in `build.gradle` (prevents JNI memory corruption).
- Update stale torrent hashes if test torrents changed upstream.
- Disable or fix network-dependent tests if they flap.
- Review whether upstream changed defaults that your tests assumed.

**DO NOT attempt to build Linux, Windows, or Android locally** — these require the AWS EC2 x86_64 Docker environment.

**Lesson Learned (cb6fe6b9c upgrade):** The smoke test caught a `dynamic_cast` compilation failure in `smart_ban.cpp` introduced by upstream commit `d0e59960a`. Fixing this (`-frtti`) on macOS arm64 prevented the same failure on ALL other platforms (Linux x86_64, Linux arm64, Windows, and all Android arches), saving hours of wasted parallel Docker builds on EC2.

---

## Step 7: Build Native Binaries

### Build Topology

| Platform | Architecture | Where to Build | Method |
|----------|-------------|----------------|--------|
| macOS | arm64 | **Apple Silicon dev machine** (local) | `./build-macos-arm64.sh` |
| macOS | x86_64 | **Separate Intel Mac** (different machine) | `./build-macos-x86_64.sh` |
| Linux | x86_64 | **AWS EC2 x86_64** | Docker |
| Linux | arm64 | **AWS EC2 x86_64** | Docker (cross-compile) |
| Windows | x86_64 | **AWS EC2 x86_64** | Docker (cross-compile) |
| Android | arm | **AWS EC2 x86_64** | Docker (cross-compile) |
| Android | arm64 | **AWS EC2 x86_64** | Docker (cross-compile) |
| Android | x86 | **AWS EC2 x86_64** | Docker (cross-compile) |
| Android | x86_64 | **AWS EC2 x86_64** | Docker (cross-compile) |

**CRITICAL:** The `Dockerfile` and all Docker-based builds are **ONLY to be run on the AWS EC2 x86_64 server.** Do NOT run Docker builds on Apple Silicon — the Dockerfile contains x86_64-centric cross-compilation steps (especially Android ARM OpenSSL) that crash under QEMU emulation with SIGTRAP/Error 133.

### macOS arm64 (Apple Silicon dev machine)
```bash
cd swig
./build-macos-arm64.sh --build-only
```

### macOS x86_64 (Intel Mac — separate machine)
```bash
cd swig
./build-macos-x86_64.sh --build-only
```

### Linux / Windows / Android (AWS EC2 x86_64 ONLY)

**On the EC2 x86_64 instance:**
```bash
# Build Docker image (x86_64 host)
docker build -t jlibtorrent-build swig/

# Desktop (Linux x86_64, Linux arm64, Windows x86_64)
docker run --rm -v $(pwd):/frostwire-jlibtorrent jlibtorrent-build /build_desktop.sh

# Android (all 4 architectures)
docker run --rm -v $(pwd):/frostwire-jlibtorrent jlibtorrent-build /build_android_all.sh
```

**Artifact retrieval from EC2:**
After all EC2 builds complete:
1. Verify all binaries exist and are non-empty
2. `rsync` or `scp` artifacts back to local machine:
   ```bash
   rsync -av ec2-user@<ec2-host>:~/frostwire-jlibtorrent/swig/bin/release/linux/ ./swig/bin/release/linux/
   rsync -av ec2-user@<ec2-host>:~/frostwire-jlibtorrent/swig/bin/release/windows/ ./swig/bin/release/windows/
   rsync -av ec2-user@<ec2-host>:~/frostwire-jlibtorrent/swig/bin/release/android/ ./swig/bin/release/android/
   ```

### Validate Android 16KB page size
```bash
./validate_android_16kb.sh
```

---

## Step 8: Assemble JARs

```bash
./gradlew jar                          # Platform-independent classes + sources + javadoc
./gradlew nativeMacOSArm64Jar
./gradlew nativeMacOSX86_64Jar         # if Intel Mac build available
./gradlew nativeLinuxX86_64Jar
./gradlew nativeLinuxArm64Jar
./gradlew nativeWindowsX86_64Jar
./gradlew nativeAndroidArmJar
./gradlew nativeAndroidX86Jar
./gradlew nativeAndroidArm64Jar
./gradlew nativeAndroidX64Jar
```

Verify all JARs exist in `build/libs/`.

---

## Step 9: Finalize Changelog

In `changelog.txt`, document incrementally as you work — don't leave it to the end:
- New jlibtorrent Java API additions (with method signatures)
- libtorrent upstream changes (summarize from `git log` delta, use `lt:` prefix)
- Build / test infrastructure fixes
- Breaking changes or deprecations
- Platform-specific notes (e.g., "macOS x86_64 no longer built — Apple Silicon only")

**Format:**
```
X.Y.Z.W YYYY/MMM/DD
 * libtorrent upgraded to <hash> (RC_2_0, N commits ahead of <old_hash>)
 * new SettingsPack.fooBar() getter/setter for ...
 * new TorrentInfo.bazQux() for v2-aware ...
 * build: add -frtti to macOS arm64 config (required by smart_ban.cpp dynamic_cast)
 * fix: someUpstreamSetting default changed from true to false per upstream commit <hash>
 * lt: <upstream change summary>
```

Commit: `Update changelog for version X.Y.Z.W`

---

## Step 10: Release

**ONLY release after ALL platforms have been built and verified.**

1. Create and push git tag:
```bash
git tag release/X.Y.Z.W
git push origin release/X.Y.Z.W
```

2. Gather all artifacts from their build locations:
   - **Dev machine:** `build/libs/jlibtorrent-X.Y.Z.W.jar`, `jlibtorrent-X.Y.Z.W-sources.jar`, `jlibtorrent-X.Y.Z.W-javadoc.jar`, `jlibtorrent-macosx-arm64-X.Y.Z.W.jar`
   - **Intel Mac:** `build/libs/jlibtorrent-macosx-x86_64-X.Y.Z.W.jar` (scp/rsync back)
   - **EC2:** `build/libs/jlibtorrent-linux-x86_64-X.Y.Z.W.jar`, `jlibtorrent-linux-arm64-X.Y.Z.W.jar`, `jlibtorrent-windows-X.Y.Z.W.jar`, `jlibtorrent-android-*-X.Y.Z.W.jar` (scp/rsync back)

3. Create GitHub Release, upload all JAR artifacts:
   - `jlibtorrent-X.Y.Z.W.jar`
   - `jlibtorrent-X.Y.Z.W-sources.jar`
   - `jlibtorrent-X.Y.Z.W-javadoc.jar`
   - `jlibtorrent-macosx-arm64-X.Y.Z.W.jar`
   - `jlibtorrent-macosx-x86_64-X.Y.Z.W.jar`
   - `jlibtorrent-windows-X.Y.Z.W.jar`
   - `jlibtorrent-linux-x86_64-X.Y.Z.W.jar`
   - `jlibtorrent-linux-arm64-X.Y.Z.W.jar`
   - `jlibtorrent-android-arm-X.Y.Z.W.jar`
   - `jlibtorrent-android-x86-X.Y.Z.W.jar`
   - `jlibtorrent-android-arm64-X.Y.Z.W.jar`
   - `jlibtorrent-android-x86_64-X.Y.Z.W.jar`

3. Trigger Maven publish:
   - Use `.github/workflows/publish.yml` (`workflow_dispatch` with `release/X.Y.Z.W`)
   - Or run `gradle publish` locally with `GITHUB_TOKEN` / `USERNAME`

4. Verify packages at `https://maven.pkg.github.com/frostwire/frostwire-jlibtorrent/`

---

## Parallel Execution Strategy (MentisDB-Coordinated)

When using multiple agents, follow this dependency graph:

```
Phase 1: Mechanical Bumps          [1 agent, single commit]
Phase 2: SWIG Regeneration         [1 agent, single commit]
         └─→ MentisDB Checkpoint A
Phase 3: Diff Review & Triage     [1 agent, reads Checkpoint A]
         └─→ MentisDB Checkpoint B (task assignments)
Phase 4: Java Wrappers + Tests    [N agents in parallel, reads Checkpoint B]
         └─→ Each agent commits independently (different files)
         └─→ MentisDB Checkpoint C (wrappers done)
Phase 5: Smoke Test              [1 agent, reads Checkpoint C]
         └─→ Build + test ONE platform first
         └─→ If green → Checkpoint D (all-platform-builds approved)
         └─→ If red → back to Phase 4 fixes, rewrite Checkpoint C
Phase 6: Native Builds            [Parallel agents per platform]
         ├─ macOS arm64 (local)
         ├─ macOS x86_64 (local, if available)
         ├─ Linux x86_64 (Docker/EC2)
         ├─ Linux arm64 (Docker/EC2)
         ├─ Windows x86_64 (Docker/EC2)
         └─ Android 4 arches (Docker/EC2)
         └─→ MentisDB Checkpoint E (all builds done)
Phase 7: JAR Assembly              [1 agent]
Phase 8: Changelog + Release      [1 agent]
```

**Coordination rules:**
- Every agent operates as `agent_id=gubatron` on `chain_key=frostwire`
- Each phase ends with a **MentisDB Checkpoint** thought
- Agents read the latest Checkpoint before starting via `mentisdb_recent_context` or `mentisdb_search`
- Agents that fail write a `LessonLearned` or `Mistake` thought immediately

---

## Build Config Caveats

### RTTI (`-frtti` / `-fno-rtti`)
Since #339 RTTI is on everywhere: the Jamfile sets `<rtti>on`, every config has `-frtti`, and the generated director code keeps SWIG's `dynamic_cast` (run-swig.sh no longer rewrites it to `static_cast`). The history below explains how it got here.
jlibtorrent historically builds with `-fno-rtti` to reduce binary size. However, upstream libtorrent may introduce `dynamic_cast` in new commits.

**Symptom:** Build fails with `error: use of dynamic_cast requires -frtti`

**Fix:** Add `<cxxflags>-frtti` to the relevant `config/<os>-<arch>-config.jam`:
```jam
using darwin : arm64 : clang++ :
    <cxxflags>-mmacosx-version-min=11.3
    <cxxflags>-frtti
    ...
    ;
```

**Lesson Learned (cb6fe6b9c upgrade):** Commit `d0e59960a` added `dynamic_cast<smart_ban_plugin*>` in `src/smart_ban.cpp`. We added `-frtti` to both `macosx-arm64-config.jam` and `linux-arm64-config.jam`. Check if this is needed for other platforms too.

### Toolchain/SDK mismatch (macOS)
**Symptom:** everything compiles, then linking `libjlibtorrent.dylib` fails with `tapi error: malformed file ... unknown architecture arm64e.x1-macos` in `libSystem.tbd` / `libc++.tbd`. Even a hello-world fails to link.

**Cause:** `xcode-select` points at an Xcode whose `ld` (e.g. ld-1230, Xcode 26) is older than the SDK `xcrun` hands clang. Here that was the CommandLineTools `MacOSX.sdk` → `MacOSX27.0.sdk`, after a CommandLineTools update without an Xcode update.

**Fix (per build, no system change):** use the CommandLineTools toolchain as a whole, then clean-rebuild:
```bash
export DEVELOPER_DIR=/Library/Developer/CommandLineTools   # its ld (27037+) matches its SDK
rm -rf "${S:?}"/*   # with S=bin/release/macosx/arm64
./build-macos-arm64.sh --build-only
```
Check first with `echo 'int main(){}' > t.cc && clang++ t.cc -o t`. Updating Xcode, or `sudo xcode-select -s`, is the maintainer's call.

### Upgrading SWIG
SWIG generates all of `swig/libtorrent_jni.cpp` and `src/main/java/com/frostwire/jlibtorrent/swig/`, so a SWIG upgrade is a binding change and is checked like one. Done for 4.4.1 → 4.5.1 on 2026-10-29:
1. **Check the release notes for Java and C++ changes.** Read `RELEASENOTES` and the `[Java]` and `*** POTENTIAL INCOMPATIBILITY ***` entries in `CHANGES` for the new version. 4.5.0's nine incompatibilities were all Python, Ruby, Lua and other languages, plus one C++ parsing fix.
2. **Get the official tarball and prove it matches the tag.** It's on SourceForge (`https://sourceforge.net/projects/swig/files/swig/swig-X.Y.Z/swig-X.Y.Z.tar.gz/download`); GitHub releases carry no files. Clone the `vX.Y.Z` tag, and `cmp` every `git ls-files` path against the extracted tarball. Expect 0 differences; only repo housekeeping files are missing from the tarball.
3. **Mirror it.** Upload it to `dl.frostwire.com/other/swig/` on virginia1 (maintainer's OK: production server), then check a fresh download's SHA-256. Put the hash in `SWIG_SHA256` in `swig/Dockerfile` and `swig/build-utils.shinc`, and in `SWIG_SHA256` in `.github/workflows/build-macos-x86_64.yml`. All three verify the download.
4. **Build it into a prefix**, so the system `swig` stays untouched until you choose: `./configure --prefix=$HOME/src/swig-X.Y.Z-install --without-d --disable-ccache && make -j && make install`, then `export PATH=$HOME/src/swig-X.Y.Z-install/bin:$PATH`. On a Mac with the toolchain mismatch below, configure fails with "C compiler cannot create executables" until you set `DEVELOPER_DIR`.
5. **Regenerate into a scratch copy, diff it, and compile it.** A diff alone isn't enough. For 4.5.1 the Java side changed only the `Version` header in all 264 files, and the visible C++ change was argument passing (`SWIG_STD_MOVE(*(&argN))` instead of `SWIG_STD_MOVE(argN)`). But the C++ then failed to compile: SWIG 4.5 re-resolves typedefs inside **C++ default arguments** in `%extend` code. `libtorrent::save_state_flags_t::all()` became `bitfield_flag<std::uint32_t, struct save_state_flags_tag>::all()`, with the tag outside `namespace libtorrent`, so "no viable conversion". Fix: don't write default arguments in `%extend`. Write the overload without the parameter, whose body passes the default; bodies are copied verbatim and the Java API is unchanged (f1365582). Compile-check with the Step 3b `clang++ -fsyntax-only` command, or do the macOS arm64 smoke build, before committing a regeneration.
6. **Don't regenerate while a fuzz run is going.** The next `fuzz` invocation recompiles the Java side, and a changed binding over the old native library gives false crashes.

### Minimum macOS version
The linker, not the compiler, stamps a dylib's minimum OS (`LC_BUILD_VERSION minos`). Without `<linkflags>-mmacosx-version-min=11.3` it uses the SDK's version: 2.0.12.9 shipped arm64 with minos 26.0 and x86_64 with minos 15.0. Check every macOS build:
```bash
otool -l bin/release/macosx/<arch>/libjlibtorrent.<arch>.dylib | grep -A4 LC_BUILD_VERSION   # minos must be 11.3
```
"built for newer 'macOS' version" link warnings name a static library that was built without the flag, so far always `~/src/openssl` (rebuild it with `CFLAGS=... -mmacosx-version-min=11.3`, `./Configure darwin64-<arch>-cc ... --prefix=$HOME/src/openssl`).

### C++ Standard
The build scripts specify `cxxstd=20`. Ensure upstream commits are compatible with C++20. If upstream bumps to C++23, update all `config/*.jam` files.

### Clean Rebuild After Compiler Flag Changes
Adding or removing compiler flags (especially `-frtti`/`-fno-rtti`, `-fvisibility=hidden`, `-mmacosx-version-min`) changes the ABI of compiled code. Cached object files from previous builds will have incompatible vtables and symbol layouts.

**Symptom:** Build succeeds but tests crash with SIGSEGV in seemingly unrelated code (e.g., `asio_detail_posix_thread_function`, random destructor calls, virtual function table corruption).

**Fix:** Always do a clean rebuild when compiler flags change:
```bash
rm -rf swig/bin/release/<os>/<arch>/*
# Then rebuild
```

**Lesson Learned (cb6fe6b9c upgrade):** After adding `-frtti` to fix `smart_ban.cpp` compilation, tests crashed with SIGSEGV in ASIO thread pool cleanup. The crash persisted even after reverting Boost from 1.91.0 to 1.88.0. Only a clean rebuild (`rm -rf bin/release/macosx/arm64/*`) fixed the ABI mismatch.

### Dependency Upgrade Validation
When upgrading Boost or OpenSSL, build and test on ONE platform first. Even if upstream CI uses the new version, platform-specific issues may exist.

**Lesson Learned (cb6fe6b9c upgrade):** Boost 1.91.0 build succeeded but caused SIGSEGV on macOS arm64. Reverted to 1.88.0. OpenSSL 3.6.0 worked without issues.

---

## Common Pitfalls

| Pitfall | Prevention |
|---------|------------|
| **Missed version strings** | `grep -rn` the old version across ALL files before committing Phase 1 |
| **Forgetting `build-utils.shinc`** | Both `Dockerfile` and `build-utils.shinc` must have matching `LIBTORRENT_REVISION` |
| **Missing alert type table entry** | New alerts in `alert_types.hpp` won't reach Java listeners unless added to `Alerts.java` |
| **Android x86_64 overwriting x86** | `build.gradle` paths are architecture-specific; verify each JAR task |
| **Javadoc build failures** | Exclude `**/swig/**` from Javadoc; ensure all NEW public API has Javadoc |
| **JVM crashes in tests** | `forkEvery = 1` in `build.gradle` is mandatory for JNI tests |
| **Changelog ordering** | Newest release must be at the TOP of `changelog.txt` |
| **Tests assuming upstream defaults** | Always verify defaults through the SWIG layer; they can change |
| **Stale object files after flag changes** | `rm -rf swig/bin/release/<os>/<arch>/*` and rebuild from scratch |
| **Dependency upgrades without smoke test** | Build and test ONE platform before rolling out to all platforms |
| **Tests asserting undefined behavior** | C++ functions may not validate input; test JVM survival, not return values |
| **Building Docker on Apple Silicon** | Don't. Use EC2 x86_64 or GitHub Actions for cross-compiled platforms |
| **Releasing before all platforms built** | Tagging creates a GitHub Release event; ensure ALL artifacts are ready first |
| **Stale native jar after a `build.gradle` change** | Gradle can report a native `Zip` jar task up to date; run it with `--rerun-tasks` and `unzip -l` the jar to check the file names |
| **Wrapping upstream internals** | SWIG exposes functions upstream marks `// hidden`; don't add Java wrappers for those |
| **Assuming the old pin lacks a release's fixes** | Diff `ChangeLog` between the old and new commit; tags lag the branch |

---

## Files That Always Change

- `swig/Dockerfile`
- `swig/build-utils.shinc`
- `build.gradle`
- `README.md`
- `BUILD_MANUAL.md`
- `.github/workflows/publish.yml`
- `src/main/java/com/frostwire/jlibtorrent/LibTorrent.java` (Javadoc examples)
- `swig/libtorrent_jni.cpp` (auto-generated)
- `swig/libtorrent_jni.h` (auto-generated)
- `src/main/java/com/frostwire/jlibtorrent/swig/*.java` (auto-generated; `libtorrent_jni.java` and `libtorrent_jni.cpp` carry the version stamped by `run-swig.sh`, check with `grep NewStringUTF swig/libtorrent_jni.cpp`)
- `changelog.txt`
- Potentially `swig/config/*-config.jam` (compiler flags)

---

## Commit Message Templates

**Mechanical bump:**
```
Update libtorrent revision to <hash>

- Mechanical version bumps across Dockerfile, build-utils.shinc, build.gradle
- README, BUILD_MANUAL, publish.yml, LibTorrent.java examples updated
- Changelog placeholder added for X.Y.Z.W
```

**SWIG regeneration:**
```
SWIG regeneration for libtorrent <hash>

- Regenerated libtorrent_jni.cpp / libtorrent_jni.h
- Regenerated Java SWIG wrappers
- New settings: <list>
- New methods: <list>
```

**Wrapper additions:**
```
Add <Class>.<method>() for <feature>

- Wraps SWIG-exposed <c++_symbol>
- Includes Javadoc and fluent API support
```

**Test additions:**
```
Add tests for <feature>

- Default value verification against SWIG layer
- Boundary condition tests
- v1/v2 semantics validation (if applicable)
```

**Build config fix:**
```
Fix <platform> build: add <flag> to <config file>

- Required by upstream commit <hash> introducing <c++_feature>
- Fixes compilation error: <error_message>
```

**Changelog:**
```
Update changelog for version X.Y.Z.W

- libtorrent upgraded to <hash> (N commits)
- New API: <list>
- Build fixes: <list>
- Upstream changes: <list>
```

---

## Post-Release: Update This Skill

After every upgrade cycle, append lessons learned to this skill file. If the process changed significantly, commit the updated skill to the repo under `skills/jlibtorrent-libtorrent-upgrade/SKILL.md` and upload it to the MentisDB skill registry.
