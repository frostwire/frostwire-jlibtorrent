# JLibTorrent Documentation Project - Handoff Guide

## Executive Summary

This document guides future Claude Code sessions in completing world-class javadoc documentation for the jlibtorrent Java API. The project aims to achieve professional-grade documentation matching top-tier libraries like Guava, OkHttp, or Retrofit.

**Status:** 16 of ~50 core classes documented (~32% complete). All critical classes are done; specialized/network classes remain.

**Commits Created:** 5 focused, thematic commits with 2,100+ lines of javadoc added.

---

## Project Context & Architecture

### What is jlibtorrent?

jlibtorrent is a **Java wrapper around libtorrent C++ library** providing BitTorrent functionality:
- **SWIG-generated bindings** (src/main/java/com/frostwire/jlibtorrent/swig/) - Auto-generated, DO NOT EDIT
- **High-level Java API** (src/main/java/com/frostwire/jlibtorrent/) - Handwritten wrapper classes for common use cases
- **Used by FrostWire** - A professional BitTorrent client with millions of downloads

### Key Design Patterns

1. **Wrapper Pattern:** High-level classes wrap native SWIG objects
   - Example: `TorrentHandle` wraps `torrent_handle` (SWIG-generated)
   - Provides: Better API, exception handling, higher-level operations

2. **Alert System:** Event-driven architecture for async operations
   - Native library posts alerts
   - Listeners receive events
   - Used for: torrent completion, peer discoveries, errors, etc.

3. **Session-Centric Architecture:**
   - One `SessionManager` orchestrates everything
   - Multiple `TorrentHandle`s managed by session
   - Each handle represents one active torrent

### Directory Structure

```
src/main/java/com/frostwire/jlibtorrent/
├── SessionManager.java              [DOCUMENTED]
├── TorrentHandle.java               [DOCUMENTED]
├── TorrentInfo.java                 [DOCUMENTED]
├── SessionParams.java               [DOCUMENTED]
├── TorrentStatus.java               [DOCUMENTED]
├── FileStorage.java                 [DOCUMENTED]
├── Priority.java                    [DOCUMENTED]
├── Sha1Hash.java                    [DOCUMENTED]
├── Sha256Hash.java                  [DOCUMENTED]
├── Entry.java                       [DOCUMENTED]
├── AnnounceEntry.java               [DOCUMENTED]
├── PeerInfo.java                    [DOCUMENTED]
├── AddTorrentParams.java            [DOCUMENTED]
├── LibTorrent.java                  [DOCUMENTED]
├── StorageMode.java                 [DOCUMENTED]
├── DhtLookup.java                   [DOCUMENTED]
├── DhtRoutingBucket.java            [DOCUMENTED]
│
├── [PENDING - Network Classes]
├── TcpEndpoint.java
├── UdpEndpoint.java
├── Address.java
├── EnumNet.java
│
├── [PENDING - Block/Piece Info]
├── BlockInfo.java
├── PartialPieceInfo.java
├── PeerRequest.java
│
├── [PENDING - Alert System]
├── AlertListener.java
├── AlertMulticaster.java
│
├── [PENDING - Misc Enums/Utilities]
├── SettingsPack.java
├── TorrentFlags.java
├── PortmapProtocol.java
├── Operation.java
├── ErrorCode.java
├── WebSeedEntry.java
├── And ~8-10 more specialized classes
│
├── alerts/                          [104 auto-generated alert classes]
│   └── [PARTIALLY DOCUMENTED via examples]
│
└── swig/                            [~200 SWIG-generated classes]
    └── DO NOT EDIT - AUTO-GENERATED
```

---

## Documentation Quality Standards

### What "World-Class" Means

Every documented class should read like a professional manual section:

1. **Comprehensive Class Javadoc (50-150 lines)**
   - What is this class?
   - Why would a developer use it?
   - Key responsibilities/roles
   - Architecture context
   - Common patterns
   - Warning/gotcha sections
   - Cross-references to related classes

2. **Working Code Examples (5-10+ per class)**
   - Complete, copy-paste-ready examples
   - Show real-world scenarios
   - Include error handling
   - Demonstrate best practices
   - Cover common use cases
   - Show integration with other classes

3. **Method Documentation**
   - Document public methods when not obvious
   - Include @param, @return, @throws
   - Add examples for complex methods
   - Explain performance implications
   - Reference related methods

4. **Architectural Context**
   - Explain how class fits in larger system
   - Show relationships to other classes
   - Include data flow examples
   - Reference state diagrams where helpful
   - Link to dependent/related classes

### Documentation Conventions Used

```java
/**
 * [Single line summary in imperative mood]
 * <p>
 * [Detailed explanation of class purpose, 3-5 paragraphs]
 * <p>
 * <b>Usage Pattern 1:</b>
 * [Describe scenario]
 * <pre>
 * // Working code example
 * </pre>
 * <p>
 * <b>Usage Pattern 2:</b>
 * [Another scenario]
 * <pre>
 * // Another working example
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * [If relevant: thread safety, blocking operations, expensive calls]
 * <pre>
 * // Example showing performance implications
 * </pre>
 * <p>
 * <b>Warning - [Common Gotcha]:</b>
 * [Explain the pitfall and how to avoid]
 * <pre>
 * // Example of wrong vs right way
 * </pre>
 *
 * @see RelatedClass1 - [Why related]
 * @see RelatedClass2 - [Why related]
 *
 * @author gubatron
 * @author aldenml
 */
```

### Code Example Best Practices

- **Completeness:** Each example should compile and run (mentally)
- **Readability:** Use clear variable names, proper indentation
- **Comments:** Explain non-obvious parts of examples
- **Realism:** Use practical scenarios from tests/demos where possible
- **Variants:** Show multiple ways to accomplish same task when relevant
- **Error Handling:** Include try-catch or null checks where appropriate

---

## Completed Classes (16 Total)

### Core Session Management (4 classes)
1. **SessionManager** (150+ lines)
   - Master orchestrator
   - Lifecycle: create → start → download → stop
   - Alert system and listeners
   - Threading model and async operations
   - Configuration management

2. **TorrentHandle** (90+ lines)
   - Per-torrent operations
   - Handle validity and lifecycle
   - Status queries (with performance notes)
   - File priorities, pause/resume
   - Peer information and piece operations

3. **TorrentInfo** (70+ lines)
   - Metadata container for .torrent files
   - Creation patterns (file/bytes/buffer)
   - File listing and inspection
   - Tracker management
   - File remapping

4. **SessionParams** (70+ lines)
   - Session initialization configuration
   - Default features (UPnP, DHT, NAT-PMP, LSD)
   - Storage backend selection
   - Session state persistence

### Torrent Status & Storage (3 classes)
5. **TorrentStatus** (100+ lines)
   - Real-time snapshot of torrent state
   - Progress, speed, peer metrics
   - Error handling and state checking
   - Statistics types (session vs all-time)

6. **FileStorage** (80+ lines)
   - File-to-piece mapping
   - File listing and properties
   - Piece operations
   - ASCII diagrams of piece-file relationships

7. **Priority** (90+ lines)
   - Priority levels 0-7 explained
   - Availability vs priority balance
   - Selective download strategies
   - Practical use cases

### Hash & Cryptography (2 classes)
8. **Sha1Hash** (60+ lines)
   - 20-byte SHA-1 wrapper
   - BitTorrent v1 info-hashes
   - Peer IDs, DHT node IDs
   - Lookup patterns

9. **Sha256Hash** (75+ lines)
   - 32-byte SHA-256 for v2 torrents
   - v1 vs v2 vs Hybrid concept
   - Fallback patterns

### Data Structures (2 classes)
10. **Entry** (130+ lines)
    - Bencoding format (4 types)
    - .torrent file parsing
    - DHT item structures
    - Creation and re-encoding

11. **AnnounceEntry** (130+ lines)
    - Tracker metadata
    - Tier-based redundancy
    - Tracker URL schemes
    - Private vs public torrent detection

### Peer & Network Info (1 class)
12. **PeerInfo** (125+ lines)
    - Connected peer statistics
    - Speed monitoring
    - Progress tracking
    - Peer quality assessment

### Configuration & Parameters (2 classes)
13. **AddTorrentParams** (130+ lines)
    - Three torrent sources (.torrent, magnet, hash)
    - Resume data usage
    - Metadata extensions
    - File priorities and flags

14. **StorageMode** (120+ lines)
    - SPARSE (default) vs ALLOCATE
    - Fragmentation vs speed trade-offs
    - Comparison table
    - Practical scenarios

### Utilities (2 classes)
15. **LibTorrent** (120+ lines)
    - Version information
    - Dependency versions
    - Platform detection (ARM NEON)
    - Metrics discovery

16. **DhtLookup** (160+ lines)
    - DHT peer discovery operations
    - Lookup statistics
    - Branch factors and timeouts
    - Performance analysis

### Plus DHT (bonus!)
17. **DhtRoutingBucket** (180+ lines)
    - Routing table buckets
    - Kademlia concepts
    - Network health assessment
    - Bootstrap process

---

## Remaining Tasks (30+ classes)

### High Priority (Network & Connectivity)
- **TcpEndpoint** - IP:port pairs for peers
- **UdpEndpoint** - UDP address representation
- **Address** - IP address utilities
- **EnumNet** - Network enumeration and interfaces
- **WebSeedEntry** - HTTP seed configuration

### Medium Priority (Block/Piece Information)
- **BlockInfo** - Downloaded block metadata
- **PartialPieceInfo** - Incomplete piece state
- **PeerRequest** - Block request details
- **TorrentBuilder** - Creating torrents programmatically
- **TorrentStats** - Historical statistics

### Medium Priority (Flags & Enums)
- **TorrentFlags** - Torrent behavior flags
- **PortmapProtocol** - UPnP/NAT-PMP protocols
- **Operation** - Error context operations
- **ErrorCode** - Error code wrapper
- **MoveFlags** - Storage move options

### Lower Priority (Alert System & Specialized)
- **AlertListener** - Event callback interface
- **AlertMulticaster** - Multiple listener support
- **BDecodeNode** - Low-level bdecode parsing
- **Vectors** - Array conversion utilities
- **Hex** - Hex encoding/decoding
- **Logger** - Internal logging
- **StatsMetric** - Metric descriptors
- **PiecesTracker** - Piece completion tracking
- **SessionHandle** - Session-level operations
- **Sha256Hash** - SHA-256 support (if not done)

---

## How to Continue Documentation

### Step 1: Pick a Class
Start with high-priority network classes or flag enums. They're small and well-understood.

### Step 2: Understand the Class
```bash
# Read the source file
# Look at test files for usage patterns
# Search for usages in demo code
find /Users/gubatron/workspace/frostwire-jlibtorrent/src/test -name "*.java" -exec grep -l "TcpEndpoint" {} \;
find /Users/gubatron/workspace/frostwire/desktop -name "*.java" -exec grep -l "TcpEndpoint" {} \;
```

### Step 3: Reference Implementation
Look at existing documented classes as templates:
- `SessionManager.java` - Comprehensive example
- `TorrentHandle.java` - Mid-size example
- `StorageMode.java` - Enum example
- `Priority.java` - Enum with patterns

### Step 4: Write Documentation
1. Write class-level javadoc (50-150 lines)
2. Add 3-5 working code examples
3. Document key public methods (if not trivial)
4. Add @see references to related classes
5. Include performance notes where relevant
6. Add warning sections for common mistakes

### Step 5: Commit
```bash
git add src/main/java/com/frostwire/jlibtorrent/YourClass.java
git commit -m "Add comprehensive documentation to YourClass

Documents [description of what class does] with detailed explanations:

## Classes Documented:
- YourClass: [Brief description] (XX+ lines)

## Documentation Features:
- [Feature 1]
- [Feature 2]
- [Feature 3]

[Brief description of examples and features]

🤖 Generated with Claude Code

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Key Learning from Project

### BitTorrent Concepts to Understand
- **Info-Hash:** Unique identifier for torrent content (20 or 32 bytes)
- **Pieces:** Fixed-size chunks of data (typically 256KB-16MB)
- **Trackers:** Servers that coordinate peer discovery (HTTP/HTTPS/UDP)
- **DHT:** Decentralized peer discovery (Kademlia algorithm)
- **Peer Exchange (PEX):** Peers share other peers' contact info
- **Metadata Extension:** Peers can retrieve .torrent data from each other
- **Resume Data:** Binary format for saving torrent state for fast resume
- **Magnet Links:** Minimal torrent info (just info-hash + trackers)

### jlibtorrent-Specific Patterns
1. **Synchronous Queries Block:** Methods like `status()`, `peerInfo()` are synchronous
   - Always warn about this in documentation
   - Show caching patterns
   - Suggest alert-based alternatives
2. **Alert-Driven:** Most operations are async via alerts
   - SessionManager runs background alert loop
   - Listeners registered via `addListener()`
   - Document which alerts to listen for
3. **Handle Validity:** Handles become invalid when torrent removed
   - Always check `isValid()` before using stored handles
   - Show proper error handling
4. **Thread Safety:** SessionManager uses locks internally
   - Most operations thread-safe
   - But stop() blocks and can take seconds
   - Warn about this in documentation

### Documentation Patterns That Work Well
1. **Scenario-Based Examples:** Show 3-4 real use cases per class
2. **Visual Aids:** ASCII diagrams, comparison tables, flow diagrams
3. **Architecture Links:** Show how class fits with others via @see tags
4. **Performance Notes:** Always mention if operation is blocking/expensive
5. **Warning Sections:** "Don't do X, do Y instead" with examples
6. **Copy-Paste Ready:** Code examples should compile and run with minimal changes

---

## Testing & Validation

### How to Verify Documentation Quality
1. **Read Test Files:** Does code match your examples?
2. **Check FrostWire Codebase:** How is the class actually used?
   ```bash
   grep -r "TcpEndpoint" /Users/gubatron/workspace/frostwire/desktop/src
   ```
3. **Run Examples:** Mentally compile each code example
4. **Link Verification:** Check @see references exist and make sense
5. **Completeness:** Ensure public methods are all documented

### FrostWire Reference Locations
- Desktop client: `/Users/gubatron/workspace/frostwire/desktop`
- Common code: `/Users/gubatron/workspace/frostwire/common`
- Look for: How are SessionManager, TorrentHandle, etc. actually used?

---

## Useful Commands

```bash
# Find classes that use a specific class
grep -r "SessionManager" src/test --include="*.java"

# Show diff of documentation changes
git diff HEAD~1 src/main/java/com/frostwire/jlibtorrent/YourClass.java

# View recent commits
git log --oneline -10

# Show lines of documentation added
git diff HEAD~1 --stat

# Create new branch for documentation work
git checkout -b document-network-classes

# View specific commits with full message
git show 6d24cc88
```

---

## Known Issues & Notes

### SWIG-Generated Classes
- Located in `swig/` directory
- DO NOT EDIT - automatically generated
- These are low-level JNI bindings
- Use wrapper classes from main directory instead
- Examples: `torrent_info`, `torrent_handle`, `session`, etc.

### Alert Classes
- 104 auto-generated alert subclasses in `alerts/` directory
- Mostly auto-generated from C++
- Could use documentation enhancements
- Show examples of how to use each alert type
- Link to TorrentHandle/SessionManager methods that generate them

### Library Dependencies
- **libtorrent:** C++ BitTorrent library (version info in LibTorrent.java)
- **Boost:** Utility library (linked via libtorrent)
- **OpenSSL:** Cryptography library
- **SWIG:** Java bindings generator

---

## Next Steps for Future Sessions

1. **Start with network classes:** TcpEndpoint, UdpEndpoint, Address, EnumNet
2. **Then flags/enums:** TorrentFlags, Operation, ErrorCode, PortmapProtocol
3. **Then block info:** BlockInfo, PartialPieceInfo, PeerRequest
4. **Finally specialized:** WebSeedEntry, remaining utilities

Each batch should be 3-5 classes per documentation session to maintain quality.

---

## Questions to Ask When Documenting

When writing javadoc for a new class, ask yourself:

1. **What problem does this class solve?**
2. **When would a developer use this vs alternatives?**
3. **What are the failure modes?**
4. **What are common mistakes developers make?**
5. **How does this fit in the larger architecture?**
6. **What other classes depend on this?**
7. **Are there performance implications?**
8. **Is this thread-safe?**
9. **Can I give 3-5 realistic examples?**
10. **What would confuse a new developer?**

---

## Success Criteria

A class is "fully documented" when:
- ✅ Class-level javadoc is 50-150 lines
- ✅ Includes 5+ working code examples
- ✅ All public methods have documentation
- ✅ Performance implications noted
- ✅ Common mistakes warned about
- ✅ Cross-references to related classes
- ✅ Easy for new developer to understand purpose and usage
- ✅ Reads like a professional API manual

---

## References

### Excellent Documentation Examples to Study
- **Guava:** Google's Java libraries (guava.dev)
- **OkHttp:** HTTP client (github.com/square/okhttp)
- **Retrofit:** REST client (github.com/square/retrofit)
- **libtorrent C++ docs:** What jlibtorrent wraps

### BitTorrent Resources
- BEP 3: BitTorrent Protocol Specification
- BEP 9: Extension for Peers to Send Metadata Files
- BEP 38: Mutable Torrents
- BEP 52: BitTorrent Protocol v2
- Kademlia DHT specification

---

**Created by Claude Code on:** October 23, 2025
**Project Branch:** world-class-documentation
**Total Documentation Added:** 2,100+ lines across 5 commits
**Classes Documented:** 17 of ~50 (~34% complete)
