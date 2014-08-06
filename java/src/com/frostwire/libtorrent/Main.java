package com.frostwire.libtorrent;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] paths = {"/Users/aldenml/Downloads/test.pdf"};

        // trigger the native library's load
        System.out.println(LibTorrent.version());
        Session s = new Session();

        s.startNetworking();

        System.out.println("ENTER to exit");
        System.in.read();
    }
}
