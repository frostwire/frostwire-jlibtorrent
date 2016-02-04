package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CreateNTorrents {

    public static void main(String[] args) throws Throwable {
        System.out.println("Version: " + LibTorrent.fullVersion());

        Path torrentsPath = Paths.get(System.getProperty("user.home"), "Downloads", "ntorrents");
        if (Files.notExists(torrentsPath)) {
            Files.createDirectory(torrentsPath);
        }

        int n = 10000;

        for (int i = 0; i < 10000; i++) {
            System.out.println("Creating torrent for index: " + i);
        }
    }
}
