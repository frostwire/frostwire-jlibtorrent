package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.plugins.Plugin;
import com.frostwire.jlibtorrent.plugins.SwigPlugin;
import com.frostwire.jlibtorrent.plugins.TorrentPlugin;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PluginsTest {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        Plugin p = new Plugin() {
            @Override
            public TorrentPlugin newTorrent(TorrentHandle th) {
                return new TorrentPlugin() {
                    @Override
                    public void tick() {
                        System.out.println("TorrentPlugin: tick");
                    }

                    @Override
                    public void onPiecePass(int index) {
                        System.out.println("TorrentPlugin: onPiecePass(" + index + ")");
                    }
                };
            }

            @Override
            public void onTick() {
                System.out.println("Plugin: onTick");
            }
        };

        s.getSwig().add_swig_extension(new SwigPlugin(p));

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                //System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
            }
        });

        File torrentFile = new File("/Users/aldenml/Downloads/test.torrent");
        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        th.resume();

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
