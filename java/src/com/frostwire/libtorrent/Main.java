package com.frostwire.libtorrent;

public class Main {

    public static void main(String[] args) {
        String[] paths = {"/Users/aldenml/Downloads/test.pdf"};

        LibTorrent.createTorrent(paths);
    }
}
