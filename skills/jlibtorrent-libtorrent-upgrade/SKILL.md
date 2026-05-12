# Skill: jlibtorrent-libtorrent-upgrade

## Description

End-to-end workflow for upgrading the underlying libtorrent C++ library in the frostwire-jlibtorrent Java/JNI bindings project. This skill covers the full lifecycle from version bump through SWIG regeneration, Java wrapper additions, native builds across all platforms, testing, and release.

Use this skill whenever a new libtorrent `RC_2_0` commit needs to be integrated.

---

## Prerequisites

- macOS with Xcode / command-line tools (for macOS binaries and SWIG regeneration)
- SWIG 4.3.1 installed locally (`swig -version`)
- JDK 17
- Gradle or `./gradlew`
- Docker (for Linux/Windows/Android cross-compilation) — **see Platform Caveats below**
- EC2 build farm or x86_64 Linux machine (for cross-compiled platforms) — **see Platform Caveats below**

---

## Step 1: Identify Target Commit and Version

1. Determine the target `libtorrent` commit hash on `RC_2_0`.
2. Check `include/libtorrent/version.hpp` in that commit to confirm MAJOR.MINOR.TINY.
3. jlibtorrent version format: `{libtorrent MAJOR}.{libtorrent MINOR}.{libtorrent TINY}.{jlibtorrent patch}`
   - Example: libtorrent `2.0.12` + jlibtorrent patch `8` → version `2.0.12.9`

**Lesson Learned (cb6fe6b9c upgrade):** Even 86 commits on the same RC_2_0 branch can still be libtorrent `2.0.12` with only the jlibtorrent patch incrementing.

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

Ensure the local `libtorrent` repo is at the target commit before running SWIG:
```bash
cd ~/src/libtorrent
git fetch origin <commit>
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

Commit: `SWIG regeneration: <list new features>`

---

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

## Step 6: Smoke Test — SINGLE PLATFORM FIRST

**ALWAYS run a smoke test on ONE platform before fanning out to all platforms.** This catches compile failures, linker errors, and runtime crashes early.

### macOS arm64 smoke test (recommended as primary dev platform)
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

**Lesson Learned (cb6fe6b9c upgrade):** The smoke test caught a `dynamic_cast` compilation failure in `smart_ban.cpp` introduced by upstream commit `d0e59960a`. Fixing this (`-frtti`) on one platform prevented wasting time on 7 parallel Docker builds that would have all failed the same way.

---

## Step 7: Build Native Binaries

### macOS (local machine)
```bash
cd swig
./build-macos-arm64.sh    # Apple Silicon
./build-macos-x86_64.sh   # Intel (if still supported)
```

### Linux / Windows / Android (Docker or EC2)

**⚠️ PLATFORM CAVEAT — Docker on Apple Silicon:**
The `Dockerfile` is designed for **x86_64 host cross-compilation**. It contains steps that run Android arm OpenSSL builds under cross-compilation toolchains that crash with SIGTRAP/Error 133 when executed on Apple Silicon under Docker Desktop's QEMU emulation.

**DO NOT attempt to run the full Dockerfile build on macOS arm64.** Use one of these options:
1. **EC2 x86_64 instance** — native x86_64 host, cross-compilers work correctly
2. **EC2 arm64 instance** — requires Dockerfile refactoring to remove x86_64-centric cross-compiler assumptions
3. **GitHub Actions** — use matrix builds with appropriate runners

**EC2 Build Commands (after checking out repo at correct commit):**
```bash
# Build Docker image (on x86_64 EC2)
docker build -t jlibtorrent-build swig/

# Desktop (Linux x86_64, Linux arm64, Windows x86_64)
docker run --rm -v $(pwd):/frostwire-jlibtorrent jlibtorrent-build /build_desktop.sh

# Android (all 4 architectures)
docker run --rm -v $(pwd):/frostwire-jlibtorrent jlibtorrent-build /build_android_all.sh
```

**Lesson Learned (cb6fe6b9c upgrade):** The Docker build failed at Android arm OpenSSL compilation with Error 133. The entire Docker-based build phase (Linux arm64, Windows, all Android) had to be deferred to EC2. Document this up front in future plans.

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

2. Create GitHub Release, upload all JAR artifacts:
   - `jlibtorrent-X.Y.Z.W.jar`
   - `jlibtorrent-X.Y.Z.W-sources.jar`
   - `jlibtorrent-X.Y.Z.W-javadoc.jar`
   - `jlibtorrent-macosx-arm64-X.Y.Z.W.jar`
   - `jlibtorrent-macosx-x86_64-X.Y.Z.W.jar` (if available)
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

### C++ Standard
The build scripts specify `cxxstd=20`. Ensure upstream commits are compatible with C++20. If upstream bumps to C++23, update all `config/*.jam` files.

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
| **Tests asserting undefined behavior** | C++ functions may not validate input; test JVM survival, not return values |
| **Building Docker on Apple Silicon** | Don't. Use EC2 x86_64 or GitHub Actions for cross-compiled platforms |
| **Releasing before all platforms built** | Tagging creates a GitHub Release event; ensure ALL artifacts are ready first |

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
- `src/main/java/com/frostwire/jlibtorrent/swig/*.java` (auto-generated)
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
