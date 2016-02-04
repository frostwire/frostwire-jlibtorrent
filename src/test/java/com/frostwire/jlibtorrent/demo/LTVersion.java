package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LTVersion extends Tool<String> {

    public LTVersion(String[] args) {
        super(args);
    }

    @Override
    protected String usage() {
        return "usage:";
    }

    @Override
    protected ParseCmd parser(com.frostwire.jlibtorrent.demo.ParseCmd.Builder b) {
        return b.build();
    }

    @Override
    public String run() {
        return "LibTorrent Version: " + LibTorrent.version();
    }

    public static void main(String[] args) {
        System.out.println(new LTVersion(args).run());
    }
}
