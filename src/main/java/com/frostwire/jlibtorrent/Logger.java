package com.frostwire.jlibtorrent;

import java.util.logging.Level;

import static java.util.logging.Level.INFO;

/**
 * Internal logging abstraction wrapping Java's standard logging framework.
 * <p>
 * {@code Logger} provides a simple interface for logging diagnostic information
 * within jlibtorrent internals. It wraps java.util.logging for consistency
 * and uses standard log levels (info, warn, error, debug). Applications can
 * configure logging via standard Java logging configuration.
 * <p>
 * <b>Using jlibtorrent Logging:</b>
 * <pre>
 * // Get logger for a component
 * Logger logger = Logger.getLogger(SomeClass.class);
 *
 * // Log at different levels
 * logger.info("Peer connected");
 * logger.warn("Slow peer detected");
 * logger.error("Disk write failed");
 * logger.debug("Block received from peer");
 *
 * // Log with exceptions
 * try {
 *     riskyOperation();
 * } catch (IOException e) {
 *     logger.error("Failed to read torrent file", e);
 * }
 * </pre>
 * <p>
 * <b>Configuring Java Logging:</b>
 * <pre>
 * // logging.properties
 * .level=INFO
 * java.util.logging.ConsoleHandler.level=INFO
 * java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
 *
 * // Or programmatically
 * java.util.logging.Logger root = java.util.logging.Logger.getLogger("");
 * root.setLevel(java.util.logging.Level.FINE);
 *
 * // Set jlibtorrent component verbosity
 * java.util.logging.Logger jltLogger =
 *     java.util.logging.Logger.getLogger("com.frostwire.jlibtorrent");
 * jltLogger.setLevel(java.util.logging.Level.FINER);
 * </pre>
 * <p>
 * <b>Log Levels:</b>
 * <ul>
 *   <li><b>info():</b> Important milestones (session started, torrent added)</li>
 *   <li><b>warn():</b> Unusual but recoverable (slow peer, retried operation)</li>
 *   <li><b>error():</b> Failures needing attention (I/O error, connection failed)</li>
 *   <li><b>debug():</b> Detailed diagnostic information (block received, piece verified)</li>
 * </ul>
 * <p>
 * <b>Logging Best Practices:</b>
 * <ul>
 *   <li>Don't log at high frequency in performance-critical paths</li>
 *   <li>Include context (peer address, piece number, file name)</li>
 *   <li>Use warn/error for issues that indicate problems</li>
 *   <li>Enable debug logging only during troubleshooting</li>
 * </ul>
 *
 * @author gubatron
 * @author aldenml
 */
final class Logger {

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
