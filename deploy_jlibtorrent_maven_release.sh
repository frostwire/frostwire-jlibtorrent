#!/bin/bash

# FULL RELEASE PROCESS
# 1. Build macosx-arm64 and macosx-x86_64 in the mac laptops natively
# 2. Build the latest Docker container in the AWS jlibtorrent-builder on-demand machine
# 3. Build the desktop builds on that linux machine
# 4. Apply android-patches script inside the docker container and build android jars
# 5. git tag the release "release/2.x.y.z"
# 6. Upload all the jars to the release
# 7. Run the "Publish Java Package" action on github https://github.com/frostwire/frostwire-jlibtorrent/actions/workflows/publish.yml with the latest release version
# 8. Run this script from any folder in this server passing the version number (not the tag), e.g. $ deploy_jlibtorrent_maven_release 2.0.12.0


# Check if VERSION is provided as an argument
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
    
    # Download sources and javadoc jars for main jlibtorrent artifact
    if [ "$ARTIFACT" = "jlibtorrent" ]; then
        curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-sources.jar" \
             "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-sources.jar"
        curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-sources.jar.sha1" \
             "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-sources.jar.sha1"
        curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-javadoc.jar" \
             "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-javadoc.jar"
        curl -u "$USERNAME:$PAT" -L -o "com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-javadoc.jar.sha1" \
             "https://maven.pkg.github.com/$REPO/com/frostwire/$ARTIFACT/$VERSION/$ARTIFACT-$VERSION-javadoc.jar.sha1"
    fi
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
