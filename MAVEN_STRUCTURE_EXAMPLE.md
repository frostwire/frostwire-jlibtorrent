# Maven Repository Structure After Changes

## Before (Current State)

```
maven/com/frostwire/jlibtorrent/2.0.12.5/
├── jlibtorrent-2.0.12.5.jar
├── jlibtorrent-2.0.12.5.jar.sha1
├── jlibtorrent-2.0.12.5.pom
└── jlibtorrent-2.0.12.5.pom.sha1
```

## After (With Sources and Javadoc)

```
maven/com/frostwire/jlibtorrent/2.0.12.5/
├── jlibtorrent-2.0.12.5.jar                  (Java class files)
├── jlibtorrent-2.0.12.5.jar.sha1
├── jlibtorrent-2.0.12.5-sources.jar          ✨ NEW - Source code
├── jlibtorrent-2.0.12.5-sources.jar.sha1     ✨ NEW
├── jlibtorrent-2.0.12.5-javadoc.jar          ✨ NEW - Javadoc
├── jlibtorrent-2.0.12.5-javadoc.jar.sha1     ✨ NEW
├── jlibtorrent-2.0.12.5.pom
└── jlibtorrent-2.0.12.5.pom.sha1
```

## Complete Maven Repository Structure

```
dl.frostwire.com/maven/
└── com/frostwire/
    ├── jlibtorrent/
    │   ├── 2.0.12.5/
    │   │   ├── jlibtorrent-2.0.12.5.jar
    │   │   ├── jlibtorrent-2.0.12.5.jar.sha1
    │   │   ├── jlibtorrent-2.0.12.5-sources.jar       ✨ NEW
    │   │   ├── jlibtorrent-2.0.12.5-sources.jar.sha1  ✨ NEW
    │   │   ├── jlibtorrent-2.0.12.5-javadoc.jar       ✨ NEW
    │   │   ├── jlibtorrent-2.0.12.5-javadoc.jar.sha1  ✨ NEW
    │   │   ├── jlibtorrent-2.0.12.5.pom
    │   │   └── jlibtorrent-2.0.12.5.pom.sha1
    │   └── maven-metadata.xml
    ├── jlibtorrent-macosx-arm64/
    │   ├── 2.0.12.5/
    │   │   ├── jlibtorrent-macosx-arm64-2.0.12.5.jar
    │   │   ├── jlibtorrent-macosx-arm64-2.0.12.5.jar.sha1
    │   │   ├── jlibtorrent-macosx-arm64-2.0.12.5.pom
    │   │   └── jlibtorrent-macosx-arm64-2.0.12.5.pom.sha1
    │   └── maven-metadata.xml
    ├── jlibtorrent-macosx-x86_64/
    │   └── ... (similar structure, no sources/javadoc)
    ├── jlibtorrent-windows/
    │   └── ... (similar structure, no sources/javadoc)
    ├── jlibtorrent-linux/
    │   └── ... (similar structure, no sources/javadoc)
    ├── jlibtorrent-android-arm/
    │   └── ... (similar structure, no sources/javadoc)
    ├── jlibtorrent-android-arm64/
    │   └── ... (similar structure, no sources/javadoc)
    ├── jlibtorrent-android-x86/
    │   └── ... (similar structure, no sources/javadoc)
    └── jlibtorrent-android-x86_64/
        └── ... (similar structure, no sources/javadoc)
```

## IDE Experience

### Before
When users add `com.frostwire:jlibtorrent:2.0.12.5` to their project:
- Can see class names and method signatures
- **Cannot** see source code
- **Cannot** see javadoc comments
- Must visit GitHub to read source or documentation

### After
When users add `com.frostwire:jlibtorrent:2.0.12.5` to their project:
- Can see class names and method signatures ✅
- **Can** see full source code in IDE ✅ NEW
- **Can** see javadoc comments inline ✅ NEW
- Can navigate to implementation ✅ NEW
- Better debugging experience ✅ NEW

## Example IDE Usage

### IntelliJ IDEA
```
When user Ctrl+Click (Cmd+Click on Mac) on a jlibtorrent class:

BEFORE: Shows decompiled bytecode
AFTER:  Shows actual Java source code with comments
```

### Eclipse
```
When user hovers over a method:

BEFORE: Shows only method signature
AFTER:  Shows full javadoc with parameter descriptions, return values, examples
```

### VS Code with Java Extension
```
When user presses F12 (Go to Definition):

BEFORE: Shows stub with no implementation
AFTER:  Shows full source code with implementation
```

## Gradle Dependency Example

```groovy
dependencies {
    implementation 'com.frostwire:jlibtorrent:2.0.12.5'
    // IDE will automatically download and attach:
    // - jlibtorrent-2.0.12.5-sources.jar (for source code viewing)
    // - jlibtorrent-2.0.12.5-javadoc.jar (for documentation)
}
```

## Maven Dependency Example

```xml
<dependency>
    <groupId>com.frostwire</groupId>
    <artifactId>jlibtorrent</artifactId>
    <version>2.0.12.5</version>
    <!-- IDE will automatically download and attach:
         - jlibtorrent-2.0.12.5-sources.jar (for source code viewing)
         - jlibtorrent-2.0.12.5-javadoc.jar (for documentation) -->
</dependency>
```

## Why Only Main Artifact?

The sources and javadoc are only published for the main `jlibtorrent` artifact because:

1. **Native artifacts** (`jlibtorrent-windows`, `jlibtorrent-linux`, etc.) contain only binary `.dll`, `.so`, or `.dylib` files
2. **Java source code** is platform-independent and resides in the main `jlibtorrent` jar
3. **Javadoc** documents the Java API, not the native binaries
4. Publishing sources/javadoc for native artifacts would be redundant and confusing

## Verification

After deploying, users can verify the files are available:

```bash
# Check GitHub Packages
curl -u username:token \
  https://maven.pkg.github.com/frostwire/frostwire-jlibtorrent/com/frostwire/jlibtorrent/2.0.12.5/jlibtorrent-2.0.12.5-sources.jar \
  -o jlibtorrent-sources.jar

# Check Custom Maven Repository
curl https://dl.frostwire.com/maven/com/frostwire/jlibtorrent/2.0.12.5/jlibtorrent-2.0.12.5-sources.jar \
  -o jlibtorrent-sources.jar

# Verify it's a valid jar
unzip -l jlibtorrent-sources.jar | head -20
```
