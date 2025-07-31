package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public final class ResumeTest {

    public static void main(String[] args) throws Throwable {
        File torrentFile = new File("/Users/aldenml/Downloads/FROSTCLICK_FROSTWIRE_CREATIVE_COMMONS_MIXTAPE_VOL_6__MP3__December_2017.torrent");

        final SessionManager s = new SessionManager();
        final CountDownLatch signal = new CountDownLatch(10);
        final CountDownLatch signalResumeData = new CountDownLatch(1);

        s.swig().post_torrent_updates();
        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                switch (type) {
                    case ADD_TORRENT:
                        System.out.println("Torrent added");
                        ((AddTorrentAlert) alert).handle().resume();
                        s.swig().post_torrent_updates();
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished");
                        s.swig().post_torrent_updates();
                        //((TorrentFinishedAlert) alert).handle().saveResumeData(TorrentHandle.SAVE_INFO_DICT);
                        break;
                    case TORRENT_PAUSED:
                        System.out.println("Torrent paused");
                        s.swig().post_torrent_updates();
                        break;
                    case SAVE_RESUME_DATA:
                        System.out.println("Torrent saveResumeData");
                        serializeResumeData((SaveResumeDataAlert) alert);
                        signalResumeData.countDown();
                        s.swig().post_torrent_updates();
                        break;
                    case STATE_UPDATE:
                        StateUpdateAlert sua = (StateUpdateAlert) alert;
                        sua.status().forEach(ts -> {
                            System.out.printf("state update: name:%s seeding time=%d active time=%d\n",
                                    ts.name(),
                                    ts.seedingDuration(),
                                    ts.activeDuration());

                            TorrentHandle th = new TorrentHandle(ts.swig().getHandle());
                            if (th.status().isFinished()) {
                                signal.countDown();
                            }
                        });
                        break;
                }
            }
        });

        s.start();
        TorrentInfo ti = new TorrentInfo(torrentFile);
        s.download(ti, torrentFile.getParentFile());
        signal.await();

        // save resume data just before restarting session
        s.find(ti.infoHashV1()).saveResumeData(TorrentHandle.SAVE_INFO_DICT);

        signalResumeData.await();

        System.out.println(Entry.bdecode(new File("resume.dat")).toString());

        s.restart();

        s.download(ti, // torrent info
                torrentFile.getParentFile(), // save path
                new File("resume.dat"), // resume file
                null, // priorities
                null, // peers (endpoints)
                new torrent_flags_t());

        System.in.read();
        s.stop();
    }

    private static void serializeResumeData(SaveResumeDataAlert alert) {
        File resume = new File("resume.dat");
        if (resume.exists())
            return;
        byte_vector data = add_torrent_params.write_resume_data(alert.params().swig()).bencode();
        try {
            Utils.writeByteArrayToFile(resume, Vectors.byte_vector2bytes(data), false);
        } catch (IOException e) {
            System.err.println("Error saving resume data");
        }
    }
}
