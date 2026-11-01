package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * AlertMulticaster chained listeners into a tree as deep as the number of listeners, and both
 * remove() and alert() recursed through it: an application registering one listener per torrent
 * threw StackOverflowError with thousands of torrents (frostwire/frostwire#1441).
 */
public class AlertMulticasterTest {

    private static final class Recording implements AlertListener {
        private final List<Recording> calls;

        Recording(List<Recording> calls) {
            this.calls = calls;
        }

        @Override
        public int[] types() {
            return null;
        }

        @Override
        public void alert(Alert<?> alert) {
            calls.add(this);
        }
    }

    @Test
    public void handlesHundredsOfThousandsOfListeners() {
        List<Recording> calls = new ArrayList<>();
        List<Recording> listeners = new ArrayList<>();
        AlertListener multicaster = null;
        for (int i = 0; i < 200_000; i++) {
            Recording listener = new Recording(calls);
            listeners.add(listener);
            multicaster = AlertMulticaster.add(multicaster, listener);
        }

        multicaster = AlertMulticaster.remove(multicaster, listeners.get(0));
        multicaster.alert(null);

        assertEquals(199_999, calls.size());
    }

    @Test
    public void deliversInRegistrationOrder() {
        List<Recording> calls = new ArrayList<>();
        Recording a = new Recording(calls);
        Recording b = new Recording(calls);
        Recording c = new Recording(calls);
        AlertListener multicaster = AlertMulticaster.add(AlertMulticaster.add(a, b), c);

        multicaster.alert(null);

        assertEquals(List.of(a, b, c), calls);
    }

    @Test
    public void removeDropsOnlyTheMostRecentRegistration() {
        List<Recording> calls = new ArrayList<>();
        Recording a = new Recording(calls);
        Recording b = new Recording(calls);
        AlertListener multicaster =
                AlertMulticaster.add(AlertMulticaster.add(AlertMulticaster.add(a, b), a), b);

        multicaster = AlertMulticaster.remove(multicaster, a);
        multicaster.alert(null);

        assertEquals(List.of(a, b, b), calls);
    }

    @Test
    public void removeDownToNothing() {
        List<Recording> calls = new ArrayList<>();
        Recording a = new Recording(calls);
        Recording b = new Recording(calls);
        AlertListener multicaster = AlertMulticaster.add(a, b);

        multicaster = AlertMulticaster.remove(multicaster, a);
        assertSame(b, multicaster);
        assertSame(b, AlertMulticaster.remove(b, new Recording(calls)));
        assertNull(AlertMulticaster.remove(multicaster, b));
        assertNull(AlertMulticaster.remove(null, b));
        assertSame(a, AlertMulticaster.add(null, a));
        assertSame(a, AlertMulticaster.add(a, null));
    }

    @Test
    public void removingAnUnknownListenerKeepsTheSameMulticaster() {
        List<Recording> calls = new ArrayList<>();
        AlertListener multicaster =
                AlertMulticaster.add(new Recording(calls), new Recording(calls));

        assertSame(multicaster, AlertMulticaster.remove(multicaster, new Recording(calls)));
    }
}
