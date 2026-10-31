#!/bin/bash

# FULL RELEASE PROCESS (see the README, "Building a release with GitHub Actions")
# 1. Run the "Build release" workflow (build-release.yml) with release_tag release/2.x.y.z: it builds
#    the 12 jars and the Android AAR and attaches them to a draft release.
# 2. Publish the draft release.
# 3. Run the "Publish Java Package" workflow (publish.yml) with the same tag.
# 4. Run this script on virginia1 with the version number (not the tag), e.g.
#    $ deploy_jlibtorrent_maven_release 2.0.15.2
#
# www.frostwire.com/downloads follows the version this script deploys: includes/config.php reads the
# <release> of com/frostwire/jlibtorrent/maven-metadata.xml, written below.

# Binary .jar and .aar artifacts are served directly from GitHub release assets via a
# 302 redirect configured in nginx (frostwire-tools configs/nginx/virginia1/sites/60-dl.conf,
# dl.frostwire.com). This script therefore only fetches the
# Maven metadata (.pom/.sha1) that GitHub releases do not carry, and regenerates
# the maven-metadata.xml index for each artifact.

if [ -z "$1" ]; then
    echo "deploy_jlibtorrent_maven_release: Error: VERSION parameter is required (e.g., ./deploy.sh 2.0.12.0)"
    exit 1
fi

USERNAME="gubatron"
PAT="${GITHUB_PAT}"
REPO="frostwire/frostwire-jlibtorrent"
VERSION="$1"
ARTIFACTS=(
    "jlibtorrent"
    "jlibtorrent-macosx-arm64"
    "jlibtorrent-macosx-x86_64"
    "jlibtorrent-windows"
    "jlibtorrent-linux-x86_64"
    "jlibtorrent-linux-arm64"
    "jlibtorrent-android-arm"
    "jlibtorrent-android-x86"
    "jlibtorrent-android-arm64"
    "jlibtorrent-android-x86_64"
    "jlibtorrent-android"
)

# The binary's extension: the Android library is an AAR (since 2.0.15.2), everything else a jar.
binary_extension() {
    if [ "$1" = "jlibtorrent-android" ]; then echo "aar"; else echo "jar"; fi
}

pushd ~/dl.frostwire.com/maven/ || { echo "deploy_jlibtorrent_maven_release: Error: Failed to change to ~/dl.frostwire.com/maven/"; exit 1; }

# Download Maven metadata (.pom/.sha1) from GitHub Packages for the new version.
# The .jar binaries are NOT mirrored here: lighttpd 302-redirects .jar requests
# to the public GitHub release assets, so GitHub serves the bytes for free.
for ARTIFACT in "${ARTIFACTS[@]}"; do
    EXT=$(binary_extension "$ARTIFACT")
    mkdir -p "com/frostwire/$ARTIFACT/$VERSION"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom.sha1" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom.sha1"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.$EXT.sha1" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.$EXT.sha1"
done

# Generate maven-metadata.xml for each artifact
LAST_UPDATED=$(date -u +%Y%m%d%H%M%SZ)
for ARTIFACT in "${ARTIFACTS[@]}"; do
    OUTPUT_FILE="com/frostwire/$ARTIFACT/maven-metadata.xml"
    echo "Scanning versions for $ARTIFACT in com/frostwire/$ARTIFACT/..."
    VERSIONS=$(find "com/frostwire/$ARTIFACT" -maxdepth 1 -type d -name '[0-9]*' -exec basename {} \; | sort -V)
    echo "Found versions for $ARTIFACT: $VERSIONS"
    if [ -z "$VERSIONS" ]; then
        echo "No versions found for $ARTIFACT, skipping metadata generation"
        continue
    fi
    LATEST=$(echo "$VERSIONS" | tail -n 1)
    echo "Generating $OUTPUT_FILE with latest version $LATEST..."
    echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<metadata>
    <groupId>com.frostwire</groupId>
    <artifactId>$ARTIFACT</artifactId>
    <versioning>
        <release>$LATEST</release>
        <versions>" > "$OUTPUT_FILE"
    for V in $VERSIONS; do
        echo "            <version>$V</version>" >> "$OUTPUT_FILE"
    done
    echo "        </versions>
        <lastUpdated>$LAST_UPDATED</lastUpdated>
    </versioning>
</metadata>" >> "$OUTPUT_FILE"
    # Generate SHA1 checksum for maven-metadata.xml
    sha1sum "$OUTPUT_FILE" | awk '{print $1}' > "$OUTPUT_FILE.sha1"
done

popd

echo "deploy_jlibtorrent_maven_release: deployed $VERSION; www.frostwire.com/downloads now links jlibtorrent $(sed -n 's:.*<release>\(.*\)</release>.*:\1:p' ~/dl.frostwire.com/maven/com/frostwire/jlibtorrent/maven-metadata.xml)"
