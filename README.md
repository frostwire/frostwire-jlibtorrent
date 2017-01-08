frostwire-jlibtorrent
=====================
![JLibtorrent Logo](logo/jlibtorrent_logo_color.png)

A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

Using
========

Download [the latest JAR](https://search.maven.org/remote_content?g=com.frostwire&a=jlibtorrent&v=LATEST) or get the dependency via Maven:
```xml
<dependency>
  <groupId>com.frostwire</groupId>
  <artifactId>jlibtorrent</artifactId>
  <version>1.2.x.x</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.frostwire:jlibtorrent:1.2.x.x'
```

Note that there are multiple version of jlibtorrent for different platforms: `jlibtorrent`, `jlibtorrent-windows`, `jlibtorrent-linux`, `jlibtorrent-macosx` and `jlibtorrent-android-<arch>`. These are all different artifacts.

For examples look at https://github.com/frostwire/frostwire-jlibtorrent/tree/master/src/test/java/com/frostwire/jlibtorrent/demo

Architectures supported:

- Android (armeabi-v7a, arm64-v8a, x86, x86_64)
- Linux (x86, x86_64)
- Windows (x86, x86_64)
- Mac OS X (x86_64)

Building
========

You need:

- Setup a travis account at http://travis-ci.org and get familiar with
the service if necessary.
- Open an account with Amazon Web Services (AWS) and get familiar with
S3 (for storage) and IAM (for users).
- Some familiarity with `git` commands.

The process is:

- Create a user in amazon IAM, let's suppose it is `user1`. Download
credentials for the keys.
- Create a bucket in amazon S3, let's suppose it is `jlibtorrent1`.
- Set the permission of the bucket according to your workflow, but at
least the `user1` should have permission to put/upload to the bucket.
See for example this bucket policy:
```json
{
	"Statement": [
		{
			"Effect": "Allow",
			"Principal": {"AWS":"arn:aws:iam::<user1's ARN here>:user/user1"},
			"Action": "s3:PutObject",
			"Resource": "arn:aws:s3:::jlibtorrent1/*"
		},
		{
			"Effect": "Allow",
			"Principal": "*",
			"Action": "s3:GetObject",
			"Resource": "arn:aws:s3:::jlibtorrent1/*"
		}
	]
}
```
- Fork the project in github.
- Go to travis an enable the repository.
- Go to 'More options' > 'Settings' > 'Environment Variables' and set the
`S3_ACCESS_KEY, S3_SECRET_KEY, S3_BUCKET` variables using the values in the
credentials file for the user you created and the bucket name you created.
- Clone locally your repo, let's assume to the `jlibtorrent` folder and
checkout the stable branch:
```bash
$ git clone <your fork repo url> jlibtorrent
$ cd jlibtorrent
$ git checkout master
```
- Verify in your travis online if the build already started. The build
 could take about 40 minutes, be patient.
- When finished, check your s3 bucket for the binaries.
- To trigger a new build, just make a change or merge new changes from
 the stable branch, commit and push.

Projects using jLibtorrent
==========================
- [FrostWire](http://www.frostwire.com) (both desktop and android editions)
- [TorrentStream-Android](https://github.com/mianharisali/TorrentStream-Android)
- [TorrentTunes-Client](https://github.com/dessalines/torrenttunes-client)
- [LibreTorrent](https://github.com/proninyaroslav/libretorrent)

License
========

This software is offered under the MIT License, available [here](License.md).
