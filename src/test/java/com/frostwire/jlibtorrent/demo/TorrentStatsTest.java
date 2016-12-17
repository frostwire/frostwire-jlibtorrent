package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentAddedAlert;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author haperlot
 */
public final class TorrentStatsTest {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/maximiliamgierschmann/Downloads/Honey_Larochelle_Hijack_FrostClick_FrostWire_MP3_May_06_2016.torrent"};
        File torrentFile = new File(args[0]);

        final SessionManager sessionManager = new SessionManager();
        final CountDownLatch signal = new CountDownLatch(1);

        long samplingIntervalInMs = 1000; // 1 second
        long maxHistoryInMs = 10*60*1000; // 10 minutes

        //starting sessionManager & torrent download
        sessionManager.start();
        TorrentInfo ti = new TorrentInfo(torrentFile);
        sessionManager.download(ti, torrentFile.getParentFile());


        //getting the torrentHandle for the TorrentStats tracker
        TorrentHandle torrentHandle = sessionManager.find(ti.infoHash());
        // This creates a new TorrentStatsKeeper instance and all the internal alert listeners necessary
        TorrentStats stats = sessionManager.trackStats(torrentHandle, samplingIntervalInMs, maxHistoryInMs);
        // gets all the available upload speed samples (bytes/sec), in this case that'd be <= 600 elements
        // int[] uploadSpeeds = sessionManager.get(TorrentStats.Upload);
        // gets the last 10 available download speed samples or less if less available
        //int[] last10DownloadSpeedSamples = stas.get(TorrentStats.Download, 10);


        //stoping  sessionManager & torrent download
        signal.await();
        sessionManager.stop();


    }


}
