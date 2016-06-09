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
  <version>1.1.0.33</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.frostwire:jlibtorrent:1.1.0.33'
```

Note that there are multiple version of jlibtorrent for different platforms: `jlibtorrent`, `jlibtorrent-windows`, `jlibtorrent-linux`, `jlibtorrent-macosx` and `jlibtorrent-android-<arch>`. These are all different artifacts.

For examples look at https://github.com/frostwire/frostwire-jlibtorrent/tree/master/src/test/java/com/frostwire/jlibtorrent/demo

Architectures supported:

- Android (armeabi-v7a, arm64-v8a)
- Linux (x86, x86_64, armhf, arm64)
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
			"Principal": {"AWS":"arn:aws:iam::<find your id here>:user/user1"},
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
- Go to the settings of the repository and add these variables
`S3_ACCESS_KEY, S3_SECRET_KEY, S3_BUCKET` using the values of the
credentials and the bucket name.
- Clone locally your repo, let's assume to the `jlibtorrent` folder and
checkout the stable branch:
```bash
$ git clone <your fork repo url> jlibtorrent
$ cd jlibtorrent
$ git checkout libtorrent-RC_1_1
```
- Create a new branch with the name `travis-build`:
```bash
$ git checkout -b travis-build
$ git push --set-upstream origin travis-build
```
- Verify in your travis online if the build already started. The build
 could take about 40 minutes, be patient.
- When finished, check your s3 bucket for the binaries.
- To trigger a new build, just make a change or merge new changes from
 the stable branch, commit and push.

NodeJs JLibTorrent Module (alpha)
======
We have recently added support for a NodeJS binary module so you can now have all the power of libtorrent in Javascript/Node.

*Help Wanted:* At the moment we can only do simple things, like creating torrents and opening torrents, but nothing related to an actual torrenting session is still available, we need to figure out how to add a thread to process libtorrent alerts and then invoke the necessary callbacks for these alerts.

For now it builds, it can be imported in node and you can do simple things.

```bash
MBP:swig aldenml$ node
> var jlib = require("./build/Release/jlibtorrent");
undefined
> var s = new jlib.session()
undefined
> var ti = new jlib.torrent_info("/Users/aldenml/Downloads/Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015.torrent");
undefined
> ti
torrent_info {}
> ti.name()
'Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015'
>
```

[Here is an example of how to create a simple .torrent](https://gist.github.com/gubatron/afc811c5d3c9ff99a860) using the low level libtorrent API already available in Javascript/Node.

If you want to build the NodeJS module, you will need to install [node-gyp](https://github.com/nodejs/node-gyp)
```
npm install -g node-gyp
```

then from the `swig/` folder you can build the NodeJS jlibtorrent module with
```
node-gyp build
```

after the build is done, you can test by going to the `swig/build/Release` folder and issuing:
```bash
$ node -e "var jlibtorrent = require('./jlibtorrent'); console.log(jlibtorrent.LIBTORRENT_VERSION);"
1.1.0.0
```

**Contributions are rewarded instantly with our Bitcoin donations fund**

[![tip for next commit](https://tip4commit.com/projects/983.svg)](https://tip4commit.com/github/frostwire/frostwire-jlibtorrent)

License
========

This software is offered under the MIT License, available [here](License.md).
