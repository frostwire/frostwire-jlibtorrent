# jlibtorrent Upgrade: libtorrent cb6fe6b9c → 2.0.12.9

**Skill:** `skills/jlibtorrent-libtorrent-upgrade/SKILL.md` — generic upgrade workflow with lessons learned.

**Target commit:** `cb6fe6b9c28735b42edd77740a554d1709acad02`  
**Previous commit:** `46cc651d150be6356c311446647b6d0d8bf2eb4f`  
**Delta:** 86 commits on `RC_2_0`  
**libtorrent version:** `2.0.12` (unchanged)  
**jlibtorrent version:** `2.0.12.8` → `2.0.12.9`

---

## Status

| Phase | Status | Commit |
|-------|--------|--------|
| 1. Mechanical bumps | ✅ Done | `d804bfa2` |
| 2. SWIG regeneration | ✅ Done | `4c6c2025` |
| 3. Diff review | ✅ Done (3 settings + 1 method) | — |
| 4. Java wrappers | ✅ Done | `fd96fe9d`, `a99f52d6` |
| 5. Tests | ✅ Done + improved | `6b768ad3`, `496c44cf`, `296e814f` |
| 6. Test fixes | ✅ Done | `a6fe083b` |
| 7. Changelog | ✅ Done | `36f85cf7` |
| 8. macOS arm64 build | ✅ Done | — |
| 9. macOS arm64 tests | ✅ 56 passing | — |
| 10. Linux arm64 Docker | ❌ BLOCKED on Apple Silicon | — |
| 11. EC2 builds | ⏳ PENDING | — |
| 12. Release | ⏳ PENDING (blocked on EC2) | — |

---

## New API Added

- `SettingsPack.natpmpGateway()` / `natpmpGateway(String)`
- `SettingsPack.allowMultipleConnectionsPerPid()` / `allowMultipleConnectionsPerPid(boolean)`
- `SettingsPack.natpmpLeaseDuration()` / `natpmpLeaseDuration(int)`
- `TorrentInfo.pieceSizeForReq(int)` — v2-aware piece size for hash requests

---

## Lessons Applied to Skill

See `skills/jlibtorrent-libtorrent-upgrade/SKILL.md` for full details. Key lessons from this cycle:

1. **Docker on Apple Silicon is broken** — the Dockerfile fails at Android arm OpenSSL cross-compilation (SIGTRAP/Error 133). All cross-compiled platforms (Linux x86_64, arm64, Windows, Android) must be built on x86_64 host or EC2.
2. **Compiler flags may need updating** — upstream added `dynamic_cast` in `smart_ban.cpp`, requiring `-frtti` in `macosx-arm64-config.jam` and `linux-arm64-config.jam`.
3. **Test assumptions get invalidated** — `diskDisableCopyOnWrite` default changed upstream. `pieceSizeForReq()` v2 semantics are subtle (pad blocks excluded, not monotonic, sum < totalSize).
4. **More files need version bumps than expected** — missed `publish.yml`, `LibTorrent.java`, and `BUILD_MANUAL.md` on first pass.
5. **Smoke test gates parallel builds** — catching the smart_ban compile error on macOS arm64 first saved wasting time on 7 Docker builds.

---

## Artifacts Ready (Local)

- `jlibtorrent-2.0.12.9.jar` (platform-independent)
- `jlibtorrent-2.0.12.9-sources.jar`
- `jlibtorrent-2.0.12.9-javadoc.jar`
- `jlibtorrent-macosx-arm64-2.0.12.9.jar`

---

## EC2 Pending

Remaining platforms for the build farm:
- Linux arm64, Linux x86_64
- Windows x86_64
- Android arm, arm64, x86, x86_64
- macOS x86_64 (needs Intel Mac or cross-compile; may drop)

**Do NOT tag `release/2.0.12.9` until EC2 builds complete and all artifacts are assembled.**
