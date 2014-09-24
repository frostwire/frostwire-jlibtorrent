frostwire-jlibtorrent
=====================
A swig Java interface for libtorrent by the makers of FrostWire.

Develop libtorrent based apps with the joy of coding in Java.

Here's a simple example of how to create a .torrent downloader using **jlibtorrent**.

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
