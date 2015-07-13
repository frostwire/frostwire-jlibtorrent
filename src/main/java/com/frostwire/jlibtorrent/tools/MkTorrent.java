package com.frostwire.jlibtorrent.tools;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MkTorrent extends Tool {

    public MkTorrent(String[] args) {
        super(args);
    }

    public void run() {

    }

    @Override
    protected String usage() {
        return "usage: -i <file|dir>";
    }

    @Override
    protected ParseCmd parser() {
        return new ParseCmd.Builder()
                .help(usage())
                .parm("-i", "<file|dir>").req()
                .build();
    }

    public static void main(String[] args) {
        MkTorrent t = new MkTorrent(args);
        t.run();
    }
}
