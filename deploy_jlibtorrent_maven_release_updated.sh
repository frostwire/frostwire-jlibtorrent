#!/bin/bash
# Updated deploy_jlibtorrent_maven_release script
# This script should replace the one mentioned in the issue

USERNAME="${GITHUB_USERNAME}"
PAT="${SECRET_GITHUB_PAT}"
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
)

pushd ~/dl.frostwire.com/maven/ || { echo "deploy_jlibtorrent_maven_release: Error: Failed to change to ~/dl.frostwire.com/maven/"; exit 1; }

# Download artifacts from Github Package
for ARTIFACT in "${ARTIFACTS[@]}"; do
    mkdir -p "com/frostwire/$ARTIFACT/$VERSION"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.jar" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.jar"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.jar.sha1" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.jar.sha1"
    curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom.sha1" \
         "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION.pom.sha1"
    # Add sources/javadoc if published
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
    for VERSION in $VERSIONS; do
        echo "            <version>$VERSION</version>" >> "$OUTPUT_FILE"
    done
    echo "        </versions>
        <lastUpdated>$LAST_UPDATED</lastUpdated>
    </versioning>
</metadata>" >> "$OUTPUT_FILE"
    # Generate SHA1 checksum for maven-metadata.xml
    sha1sum "$OUTPUT_FILE" | awk '{print $1}' > "$OUTPUT_FILE.sha1"
done

popd