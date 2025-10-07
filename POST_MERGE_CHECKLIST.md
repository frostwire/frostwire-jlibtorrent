# Post-Merge Checklist

## After Merging This PR

Use this checklist for the first release after merging to ensure sources and javadoc are published correctly.

---

## 1. Pre-Release Build ✅

- [ ] Build native binaries on Mac (arm64 and x86_64)
- [ ] Build Docker container and desktop builds
- [ ] Apply android-patches and build Android jars
- [ ] Run `./gradlew clean build` to generate all jars

**Verify these files exist in `build/libs/`:**
- [ ] `jlibtorrent-{version}.jar`
- [ ] `jlibtorrent-{version}-sources.jar` ⬅️ NEW
- [ ] `jlibtorrent-{version}-javadoc.jar` ⬅️ NEW
- [ ] All platform-specific jars (windows, linux, macosx, android)

---

## 2. Create Release Tag

- [ ] Create git tag: `release/2.x.y.z`
- [ ] Push tag to GitHub

```bash
git tag release/2.x.y.z
git push origin release/2.x.y.z
```

---

## 3. Upload to GitHub Release ⚠️ IMPORTANT

Upload ALL jars to the GitHub release page, including the new ones:

**Main jlibtorrent jars:**
- [ ] `jlibtorrent-{version}.jar` (existing)
- [ ] `jlibtorrent-{version}-sources.jar` ⬅️ NEW - Don't forget!
- [ ] `jlibtorrent-{version}-javadoc.jar` ⬅️ NEW - Don't forget!

**Platform-specific jars:**
- [ ] `jlibtorrent-macosx-arm64-{version}.jar`
- [ ] `jlibtorrent-macosx-x86_64-{version}.jar`
- [ ] `jlibtorrent-windows-{version}.jar`
- [ ] `jlibtorrent-linux-{version}.jar`
- [ ] `jlibtorrent-android-arm-{version}.jar`
- [ ] `jlibtorrent-android-x86-{version}.jar`
- [ ] `jlibtorrent-android-arm64-{version}.jar`
- [ ] `jlibtorrent-android-x86_64-{version}.jar`

**⚠️ Critical:** If you forget to upload sources/javadoc jars, the GitHub Action will fail!

---

## 4. Run GitHub Action

- [ ] Go to: https://github.com/frostwire/frostwire-jlibtorrent/actions/workflows/publish.yml
- [ ] Click "Run workflow"
- [ ] Enter the release tag (e.g., `release/2.0.12.5`)
- [ ] Click "Run workflow"

**Monitor the workflow:**
- [ ] Check that "Download Release Assets" step succeeds
- [ ] Verify sources and javadoc jars are downloaded (check logs)
- [ ] Verify "Publish to GitHub Packages" step succeeds

---

## 5. Verify GitHub Packages

Check that all files were published:

```bash
# Set your credentials
export USERNAME="gubatron"
export TOKEN="your_github_pat"
export VERSION="2.0.12.5"  # Replace with actual version

# Check main jar
curl -u "$USERNAME:$TOKEN" \
  https://maven.pkg.github.com/frostwire/frostwire-jlibtorrent/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION.jar \
  -I

# Check sources jar ⬅️ NEW
curl -u "$USERNAME:$TOKEN" \
  https://maven.pkg.github.com/frostwire/frostwire-jlibtorrent/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION-sources.jar \
  -I

# Check javadoc jar ⬅️ NEW
curl -u "$USERNAME:$TOKEN" \
  https://maven.pkg.github.com/frostwire/frostwire-jlibtorrent/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION-javadoc.jar \
  -I
```

**Expected response for all:** `HTTP/2 200 OK`

- [ ] Main jar returns 200
- [ ] Sources jar returns 200 ⬅️ NEW
- [ ] Javadoc jar returns 200 ⬅️ NEW
- [ ] POM file returns 200

---

## 6. Run Deployment Script

On your custom Maven host server:

```bash
# Set environment variable
export GITHUB_PAT="your_github_personal_access_token"

# Run the updated script
deploy_jlibtorrent_maven_release 2.0.12.5  # Replace with actual version
```

**Monitor output:**
- [ ] All jars download successfully
- [ ] Sources jar downloads for jlibtorrent ⬅️ NEW
- [ ] Javadoc jar downloads for jlibtorrent ⬅️ NEW
- [ ] Maven metadata generated for all artifacts
- [ ] Script completes without errors

---

## 7. Verify Custom Maven Repository

Check that files are accessible on `dl.frostwire.com`:

```bash
export VERSION="2.0.12.5"  # Replace with actual version

# Check main jar
curl -I https://dl.frostwire.com/maven/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION.jar

# Check sources jar ⬅️ NEW
curl -I https://dl.frostwire.com/maven/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION-sources.jar

# Check javadoc jar ⬅️ NEW
curl -I https://dl.frostwire.com/maven/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION-javadoc.jar

# Download and verify content
curl -o sources.jar https://dl.frostwire.com/maven/com/frostwire/jlibtorrent/$VERSION/jlibtorrent-$VERSION-sources.jar
unzip -l sources.jar | head -20  # Should show .java files
```

**Expected:**
- [ ] All files return `HTTP/1.1 200 OK`
- [ ] Sources jar contains `.java` files
- [ ] Javadoc jar contains `.html` files

---

## 8. Test User Experience

Create a test project to verify IDE integration:

**build.gradle:**
```groovy
repositories {
    maven {
        url "https://dl.frostwire.com/maven"
    }
}

dependencies {
    implementation 'com.frostwire:jlibtorrent:2.0.12.5'  # Use actual version
}
```

**Test in IDE:**
- [ ] Open project in IntelliJ IDEA / Eclipse / VS Code
- [ ] Navigate to a jlibtorrent class (e.g., `SessionParams`)
- [ ] Ctrl+Click (or Cmd+Click) on the class name

**Expected:**
- [ ] IDE shows actual Java source code (not decompiled bytecode)
- [ ] Source code includes comments and javadoc
- [ ] Can navigate through implementation
- [ ] Hovering over methods shows javadoc

---

## 9. Update Release Notes

Add to the release notes that sources and javadoc are now available:

```markdown
## Changes in 2.0.12.5

### New
- ✨ Sources and Javadoc jars are now published to Maven repositories
- Users will now see source code and documentation in their IDEs

### Files Available
- jlibtorrent-{version}.jar
- jlibtorrent-{version}-sources.jar (NEW)
- jlibtorrent-{version}-javadoc.jar (NEW)
- All platform-specific jars

### For IDE Users
Add the dependency as usual, and your IDE will automatically download and attach the sources and javadoc for enhanced development experience.
```

---

## 10. Troubleshooting

### GitHub Action Fails with "404 Not Found"
**Problem:** Sources or javadoc jar not found in release
**Solution:** 
1. Check the release page - are sources/javadoc jars uploaded?
2. Upload them manually if missing
3. Re-run the workflow

### Deployment Script Can't Download Sources
**Problem:** GitHub Packages doesn't have sources/javadoc
**Solution:**
1. Verify the GitHub Action ran successfully
2. Check GitHub Packages manually (step 5)
3. Re-run GitHub Action if needed

### IDE Doesn't Show Sources
**Problem:** IDE not downloading sources automatically
**Solution:**
1. Clear Gradle/Maven cache
2. Refresh dependencies in IDE
3. Manually download and attach sources

---

## Success Criteria

✅ All checks passed when:
- [ ] All files uploaded to GitHub release (including sources/javadoc)
- [ ] GitHub Action completed successfully
- [ ] All files exist in GitHub Packages
- [ ] Deployment script completed successfully
- [ ] All files accessible on dl.frostwire.com
- [ ] IDE shows source code when navigating jlibtorrent classes
- [ ] IDE shows javadoc when hovering over methods

---

## Contact

If you encounter any issues with this process:
1. Check the documentation files in this repository
2. Review GitHub Action logs
3. Verify deployment script output
4. Test manually with curl commands above

## Documentation References

- `CHANGES_REVIEW.md` - Detailed code changes
- `SUMMARY.md` - Implementation overview
- `deploy_jlibtorrent_maven_release_README.md` - Script usage
- `SOURCES_JAVADOC_PUBLISH_UPDATE.md` - Technical details
- `MAVEN_STRUCTURE_EXAMPLE.md` - Directory structure
