package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class LoggerTest {

    @Test
    public void everyLevelIsForwardedToJavaUtilLogging() {
        java.util.logging.Logger jul = java.util.logging.Logger.getLogger(LoggerTest.class.getName());
        final List<LogRecord> records = new ArrayList<>();
        Handler h = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        h.setLevel(Level.ALL);
        jul.addHandler(h);
        jul.setUseParentHandlers(false);
        jul.setLevel(Level.ALL);
        try {
            Logger log = Logger.getLogger(LoggerTest.class);
            Throwable t = new IllegalStateException("boom");
            log.info("i");
            log.info("ie", t);
            log.warn("w");
            log.warn("we", t);
            log.error("e");
            log.error("ee", t);
            log.debug("d");
            log.debug("de", t);

            assertEquals(8, records.size());
            String[] messages = {"i", "ie", "w", "we", "e", "ee", "d", "de"};
            for (int i = 0; i < messages.length; i++) {
                assertEquals(messages[i], records.get(i).getMessage());
                assertEquals(LoggerTest.class.getName(), records.get(i).getSourceClassName());
                assertEquals(i % 2 == 1, records.get(i).getThrown() != null);
            }
            assertSame(t, records.get(1).getThrown());
            assertTrue(records.get(0).getLevel().intValue() >= Level.INFO.intValue());
        } finally {
            jul.removeHandler(h);
            jul.setUseParentHandlers(true);
        }
    }
}
