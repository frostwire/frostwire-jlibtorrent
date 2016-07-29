package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.plugins.StoragePlugin;
import com.frostwire.jlibtorrent.plugins.SwigStoragePlugin;

import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CreateStoragePlugin {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        byte[] data = Utils.resourceBytes("Honey_Larochelle_Hijack_FrostClick_FrostWire_MP3_May_06_2016.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(data);

        SwigStoragePlugin p = new SwigStoragePlugin(new StoragePlugin() {
            @Override
            public void setParams(StorageParams params) {
                System.out.println("setParams");
            }

            @Override
            public void initialize(StorageError ec) {
                System.out.println("initialize");
            }

            @Override
            public int read(long iov_base, long iov_len, int piece, int offset, int flags, StorageError ec) {
                System.out.println("read");
                return 0;
            }

            @Override
            public int write(long iov_base, long iov_len, int piece, int offset, int flags, StorageError ec) {
                System.out.println("write");
                return 0;
            }

            @Override
            public boolean hasAnyFile(StorageError ec) {
                System.out.println("hasAnyFile");
                return false;
            }

            @Override
            public void setFilePriority(byte[] prio, StorageError ec) {
                System.out.println("setFilePriority");
            }

            @Override
            public int moveStorage(String save_path, int flags, StorageError ec) {
                System.out.println("moveStorage");
                return 0;
            }

            @Override
            public boolean verifyResumeData(AddTorrentParams rd, ArrayList<String> links, StorageError ec) {
                System.out.println("verifyResumeData");
                return false;
            }

            @Override
            public void releaseFiles(StorageError ec) {
                System.out.println("releaseFiles");
            }

            @Override
            public void renameFile(int index, String new_filename, StorageError ec) {
                System.out.println("renameFile");
            }

            @Override
            public void deleteFiles(StorageError ec) {
                System.out.println("deleteFiles");
            }

            @Override
            public boolean tick() {
                System.out.println("tick");
                return false;
            }
        });

        final TorrentHandle th = SessionManager.download(s, ti, Utils.home("Downloads"), p);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                s.removeTorrent(th);
            }

            @Override
            public void pieceFinished(PieceFinishedAlert alert) {
                System.out.println("Piece finished");
            }
        });

        th.resume();

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
