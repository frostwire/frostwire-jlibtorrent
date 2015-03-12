frostwire-jlibtorrent
=====================
A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

Here's a simple example of how to create a .torrent downloader using **frostwire-jlibtorrent**.

```
public final class DownloadTorrent {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void onBlockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                System.out.println("Progress: " + p);
            }

            @Override
            public void onTorrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                signal.countDown();
            }
        });

        signal.await();
    }
}
```

=======
frostwire-jlibtorrent is currently compatible with libtorrent-rasterbar-1.0.2

Building
========

**Requirements**

You will have to build libtorrent first on your system, we've included build scripts in the `build` folder.

If you have not built libtorrent yet, you can get [libtorrent sources from sourceforge](https://sourceforge.net/p/libtorrent/code/HEAD/tree/trunk/).

If you are building for Windows, we suggest you use boost 1.55, [boost 1.56 introduced severe bugs in Windows networking](http://forum.frostwire.com/viewtopic.php?f=1&t=23421#p60796).

**Building the shared library**

make libtorrent, and then, go to the [build/](https://github.com/frostwire/frostwire-jlibtorrent/tree/master/build) folder of our project and execute the [run_swig.sh](https://github.com/frostwire/frostwire-jlibtorrent/blob/master/build/run_swig.sh) script and then the `build_xxx.sh` script corresponding to the OS you want to build for. 

The result will be a `libjlibtorrent.dylib`, or `libjlibtorrent.so` or `jlibtorrent.dll` which you can then use on your Java project along with the [Java sources](https://github.com/frostwire/frostwire-jlibtorrent/tree/master/src/com/frostwire/jlibtorrent) of the frostwire-jlibtorrent api. Make sure the `.dylib`, `.so` or `.dll` is on your project's java lib path.

You can always clone the project to your development environment and add it to the build path of your project as a dependency (which would help us in the event you find a bug and you submit a pull request), or copy the sources directly in your project source folder, however you can always just create the `frostwire-jlibtorrent.jar` and add it to your buildpath and classpath by using the gradle script in the scripts folder.

**Building the frostwire-jlibtorrent.jar**

inside the `build/` folder just invoke 
`gradle build` 

you will find the resulting `frostwire-jlibtorrent.jar` at `build/build/libs/frostwire-jlibtorrent.jar`.

**Contributions are rewarded instantly with our Bitcoin donations fund**

[![tip for next commit](https://tip4commit.com/projects/983.svg)](https://tip4commit.com/github/frostwire/frostwire-jlibtorrent)

License
========

This software is offered under the MIT License, available [here](License.md).
