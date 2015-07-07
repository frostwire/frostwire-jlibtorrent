package com.frostwire.jlibtorrent;

import java.util.logging.Level;

import static java.util.logging.Level.INFO;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Logger {

    private final java.util.logging.Logger jul;
    private final String name;

    Logger(java.util.logging.Logger jul) {
        this.jul = jul;
        this.name = jul.getName();
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(java.util.logging.Logger.getLogger(clazz.getName()));
    }

    public void info(String msg) {
        jul.logp(INFO, name, "", msg);
    }

    public void info(String msg, Throwable e) {
        jul.logp(Level.INFO, name, "", msg, e);
    }

    public void warn(String msg) {
        jul.logp(INFO, name, "", msg);
    }

    public void warn(String msg, Throwable e) {
        jul.logp(Level.INFO, name, "", msg, e);
    }

    public void error(String msg) {
        jul.logp(INFO, name, "", msg);
    }

    public void error(String msg, Throwable e) {
        jul.logp(Level.INFO, name, "", msg, e);
    }

    public void debug(String msg) {
        jul.logp(INFO, name, "", msg);
    }

    public void debug(String msg, Throwable e) {
        jul.logp(Level.INFO, name, "", msg, e);
    }
}
