package com.frostwire.jlibtorrent.tools;

import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class Tool<TOut> {

    private final Map<String, String> args;

    public Tool(String[] args) {
        ParseCmd cmd = parser(new ParseCmd.Builder().help(usage()));

        String err = cmd.validate(args);
        if (err == null || err.length() == 0) {
            this.args = cmd.parse(args);
        } else {
            throw new IllegalArgumentException(err);
        }
    }

    public String arg(String name) {
        return args.get(name);
    }

    protected abstract String usage();

    protected abstract ParseCmd parser(ParseCmd.Builder b);

    public abstract TOut run();
}
