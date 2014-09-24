package com.frostwire.jlibtorrent;

import java.util.logging.Level;

/**
 * 
 * @author gubatron
 * @author aldenml
 *
 */
public final class Logger {

    private final java.util.logging.Logger jul;

    Logger(java.util.logging.Logger jul) {
        this.jul = jul;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(java.util.logging.Logger.getLogger(clazz.getName()));
    }

    public void info(String msg) {
        jul.info(msg);
    }

    public void info(String msg, Throwable e) {
        jul.log(Level.INFO, msg, e);
    }

    public void warn(String msg) {
        jul.info(msg);
    }

    public void warn(String msg, Throwable e) {
        jul.log(Level.WARNING, msg, e);
    }

    public void error(String msg) {
        jul.info(msg);
    }

    public void error(String msg, Throwable e) {
        jul.log(Level.INFO, msg, e);
    }

    public void debug(String msg) {
        jul.info(msg);
    }

    public void debug(String msg, Throwable e) {
        jul.log(Level.INFO, msg, e);
    }
}
