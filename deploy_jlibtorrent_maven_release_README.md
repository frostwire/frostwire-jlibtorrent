# deploy_jlibtorrent_maven_release.sh

## Purpose

This script downloads published jlibtorrent artifacts from GitHub Packages and deploys them to a custom Maven repository at `~/dl.frostwire.com/maven/`.

## What's New

This updated version now downloads `-sources.jar` and `-javadoc.jar` files for the main `jlibtorrent` artifact, in addition to the regular jar and pom files.

## Prerequisites

1. GitHub Personal Access Token (PAT) with `read:packages` permission
2. Access to the custom Maven host server
3. The specified version must already be published to GitHub Packages via the GitHub Action workflow

## Environment Variables

- `GITHUB_PAT` - Your GitHub Personal Access Token

## Usage

```bash
export GITHUB_PAT="your_github_personal_access_token"
./deploy_jlibtorrent_maven_release.sh VERSION
```

**Example:**
```bash
export GITHUB_PAT="ghp_xxxxxxxxxxxxxxxxxxxx"
./deploy_jlibtorrent_maven_release.sh 2.0.12.5
```

## What It Does

1. **Downloads from GitHub Packages:**
   - For each artifact (jlibtorrent, jlibtorrent-windows, jlibtorrent-linux, etc.):
     - Downloads `.jar` file
     - Downloads `.pom` file
     - Downloads `.sha1` checksums
   - For the main `jlibtorrent` artifact only:
     - Downloads `-sources.jar` file ✨ NEW
     - Downloads `-sources.jar.sha1` checksum ✨ NEW
     - Downloads `-javadoc.jar` file ✨ NEW
     - Downloads `-javadoc.jar.sha1` checksum ✨ NEW

2. **Generates Maven Metadata:**
   - Scans all versions for each artifact
   - Creates `maven-metadata.xml` with version list
   - Generates SHA1 checksums for metadata files

3. **Results:**
   - All files are organized in proper Maven repository structure
   - Ready to serve from `https://dl.frostwire.com/maven/`

## Output Structure

```
~/dl.frostwire.com/maven/
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
    ├── jlibtorrent-windows/
    │   └── ... (similar structure, no sources/javadoc)
    └── ... (other platform artifacts)
```

## Error Handling

The script will:
- Exit with error if VERSION parameter is not provided
- Exit with error if cannot change to `~/dl.frostwire.com/maven/` directory
- Show curl errors if downloads fail (but will continue with other artifacts)

## Troubleshooting

### Downloads fail with 401 Unauthorized
- Check that `GITHUB_PAT` environment variable is set correctly
- Verify your PAT has `read:packages` permission
- Verify the specified version exists in GitHub Packages

### Downloads fail with 404 Not Found
- Verify the version has been published to GitHub Packages
- Check that the GitHub Action "Publish Java Package" ran successfully
- Confirm the version number is correct (no `release/` prefix)

### Missing sources or javadoc files
- For versions published before this update, sources/javadoc may not exist
- The script will show curl errors but continue with other files
- Starting from the next release after merging this PR, sources/javadoc will be available

## Full Release Process

1. Build native binaries on Mac and Docker
2. Apply Android patches and build Android jars
3. Create git tag: `release/2.x.y.z`
4. Upload ALL jars to GitHub Release, including:
   - Main class jar: `jlibtorrent-{version}.jar`
   - Sources jar: `jlibtorrent-{version}-sources.jar` ✨ NEW
   - Javadoc jar: `jlibtorrent-{version}-javadoc.jar` ✨ NEW
   - Native platform jars for each platform
5. Run GitHub Action "Publish Java Package" with the release tag
6. Run this script: `deploy_jlibtorrent_maven_release.sh 2.x.y.z`

## Differences from Old Version

**Old version downloaded:**
- `.jar` file for each artifact
- `.pom` file for each artifact
- `.sha1` checksums

**New version downloads:**
- `.jar` file for each artifact
- `.pom` file for each artifact
- `.sha1` checksums
- ✨ `-sources.jar` for main `jlibtorrent` artifact
- ✨ `-sources.jar.sha1` checksum
- ✨ `-javadoc.jar` for main `jlibtorrent` artifact
- ✨ `-javadoc.jar.sha1` checksum

## Script Location

This script should be installed on your custom Maven host at:
```bash
/usr/local/bin/deploy_jlibtorrent_maven_release
```

Or wherever your deployment scripts are located, and ensure it's executable:
```bash
chmod +x deploy_jlibtorrent_maven_release.sh
```
