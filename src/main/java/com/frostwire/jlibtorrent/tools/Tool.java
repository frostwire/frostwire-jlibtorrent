package com.frostwire.jlibtorrent.tools;

import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class Tool {

    protected final String[] args;
    protected final Map<String, String> map;

    public Tool(String[] args) {
        this.args = args;

        ParseCmd cmd = parser();

        String err = cmd.validate(args);
        if (err == null || err.length() == 0) {
            map = cmd.parse(args);
        } else {
            throw new IllegalArgumentException(err);
        }
    }

    public abstract void run();

    protected abstract String usage();

    protected abstract ParseCmd parser();
}
