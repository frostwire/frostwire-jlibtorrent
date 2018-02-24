package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;

import java.io.File;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * To test issue https://github.com/frostwire/frostwire-jlibtorrent/issues/195
 *
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet5 {

    public static void main(String[] args) throws Throwable {

        final String magnet = "magnet:?xt=urn:btih:737d38ed01da1df727a3e0521a6f2c457cb812de&dn=HOME+-+a+film+by+Yann+Arthus-Bertrand+%282009%29+%5BEnglish%5D+%5BHD+MP4%5D&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969";

        final SessionManager s = new SessionManager();

        startdl(magnet, s);

        System.in.read();
        s.stop();
    }

    private static void waitForNodesInDHT(final SessionManager s) throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long nodes = s.stats().dhtNodes();
                if (nodes >= 10) {
                    System.out.println("DHT contains " + nodes + " nodes");
                    signal.countDown();
                    timer.cancel();
                }
            }
        }, 0, 1000);

        System.out.println("Waiting for nodes in DHT (10 seconds)...");
        boolean r = signal.await(10, TimeUnit.SECONDS);
        if (!r) {
            System.out.println("DHT bootstrap timeout");
            System.exit(0);
        }
    }

    private static void log(String s) {
        System.out.println(s);
    }

    private static void startdl(String magnetLink, SessionManager sm) throws InterruptedException {
        //Torrent dbTorrent = torrentService.findByTorrentName(name);
        String link = magnetLink;//dbTorrent.getPathToTorrent();
        File saveDir = new File("torrents/");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        final SessionManager s = sm;//storrent.getSessionManager();
        AlertListener l = new AlertListener() {
            private int grade = 0;

            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();
                switch (type) {
                    case ADD_TORRENT:
                        //((AddTorrentAlert) alert).handle().setFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
                        ((AddTorrentAlert) alert).handle().resume();
                        break;
                    case PIECE_FINISHED:
                        int progress = (int) (((PieceFinishedAlert) alert).handle().status().progress() * 100);
                        if (grade < progress / 20) {
                            int index = (int) (((PieceFinishedAlert) alert).pieceIndex());
                            log("index: " + index);
                            grade += 1;
                            s.downloadRate();
                            log(progress + " %  downloaded");
                        }
                        System.out.println("PIECE_FINISHED");
                        break;
                    case TORRENT_FINISHED:
                        grade = 0;
                        ((TorrentFinishedAlert) alert).handle().pause();
                        System.out.println("TORRENT_FINISHED");
                        break;
                    case TORRENT_ERROR:
                        log(((TorrentErrorAlert) alert).what());
                        log("is paused = " + ((TorrentErrorAlert) alert).handle().status());
                        break;
                    case BLOCK_FINISHED:
                        System.out.println("HERE: " + ((BlockFinishedAlert) alert).handle().status().progress());
                        progress = (int) (((BlockFinishedAlert) alert).handle().status().progress() * 100);
                        if (grade < progress / 20) {
                            int index = (int) (((BlockFinishedAlert) alert).pieceIndex());
                            log("index: " + index);
                            grade += 1;
                            s.downloadRate();
                            log(progress + " %  downloaded");
                        }
                        System.out.println("BLOCK_FINISHED");
                        break;
                    case STATE_UPDATE:
                        log(((StateUpdateAlert) alert).message());
                        break;
                    case METADATA_RECEIVED:
                        log("metadata received");
                        break;
                    case DHT_ERROR:
                        log("dht error");
                        log(((DhtErrorAlert) alert).message());
                        break;
                    default:
                        break;
                }
            }
        };
        s.addListener(l);
        if (s.isRunning() != true)
            s.start();
        if (link.startsWith("magnet:?")) {
            waitForNodesInDHT(s);
            byte[] data = s.fetchMagnet(link, 30);
            TorrentInfo ti = TorrentInfo.bdecode(data);
            log(Entry.bdecode(data).toString());
            log("is valid ? =" + ti.isValid());
            s.download(ti, saveDir);
            log("torrent added with name = " + ti.name());
            //storrent.addTh(s.find(ti.infoHash()), name);
            log(s.find(ti.infoHash()).isValid() + " isvalid");
            log("torrent added to session");
            //this.videoname = ti.name();
            int i = 0;
            while (i < 20) {
                TimeUnit.SECONDS.sleep(1);
                log(s.find(ti.infoHash()).status().state() + " state");
                log(s.find(ti.infoHash()).status().progress() * 100 + " progress");
                i++;
            }
        }
    }
}
