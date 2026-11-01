package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * Internal multiplexer for distributing alerts to multiple listeners.
 * <p>
 * {@code AlertMulticaster} enables registering multiple {@code AlertListener}s to receive
 * the same alerts. It's used internally by {@code SessionManager} to support the pattern
 * of multiple independent listeners subscribing to torrent events.
 * <p>
 * <b>How Multicasting Works:</b>
 * <br/>
 * A multicaster holds the registered listeners in an immutable array, in registration
 * order, and calls each of them in turn. Adding or removing a listener builds a new
 * multicaster, so a dispatch already running keeps the array it started with.
 * <p>
 * It used to chain listeners into a binary tree as deep as the number of listeners, and
 * both {@code alert} and {@code remove} recursed through it: an application registering
 * one listener per torrent threw {@code StackOverflowError} with thousands of torrents
 * (frostwire/frostwire#1441). Nothing here recurses now.
 * <p>
 * This class is an internal implementation detail of {@code SessionManager}. Applications
 * typically don't interact with it directly:
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.addListener(listener1);
 * sm.addListener(listener2);
 * // ... both listeners receive events ...
 * sm.removeListener(listener1);  // listener2 still receives events
 * </pre>
 * <p>
 * <b>Performance Characteristics:</b>
 * <ul>
 *   <li>Adding or removing a listener: O(n), copies the array</li>
 *   <li>Alert distribution: O(n), one call per listener, no recursion</li>
 *   <li>Memory: O(n)</li>
 * </ul>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>This class is package-private; not part of public API</li>
 *   <li>Listeners are called in registration order</li>
 *   <li>Removing a listener registered more than once drops its most recent registration</li>
 *   <li>An exception thrown by a listener stops the dispatch of that alert to the listeners
 *   after it; {@code SessionManager} catches and logs it</li>
 * </ul>
 *
 * @see SessionManager#addListener(AlertListener) - Register listeners
 * @see SessionManager#removeListener(AlertListener) - Unregister listeners
 * @see AlertListener - The listener interface being multiplexed
 *
 * @author gubatron
 * @author aldenml
 */
final class AlertMulticaster implements AlertListener {

    private final AlertListener[] listeners;

    private AlertMulticaster(AlertListener[] listeners) {
        this.listeners = listeners;
    }

    @Override
    public int[] types() {
        return null;
    }

    @Override
    public void alert(Alert<?> alert) {
        for (AlertListener listener : listeners) {
            listener.alert(alert);
        }
    }

    /**
     * Returns a listener calling {@code a}'s listeners, then {@code b}'s.
     */
    public static AlertListener add(AlertListener a, AlertListener b) {
        if (a == null) return b;
        if (b == null) return a;
        AlertListener[] first = listenersOf(a);
        AlertListener[] second = listenersOf(b);
        AlertListener[] all = new AlertListener[first.length + second.length];
        System.arraycopy(first, 0, all, 0, first.length);
        System.arraycopy(second, 0, all, first.length, second.length);
        return new AlertMulticaster(all);
    }

    /**
     * Returns {@code l} without the most recent registration of {@code oldl}: {@code l} itself
     * when {@code oldl} isn't in it, a single listener when one is left, or null when none is.
     */
    public static AlertListener remove(AlertListener l, AlertListener oldl) {
        if (l == oldl || l == null) {
            return null;
        }
        if (!(l instanceof AlertMulticaster)) {
            return l; // it's not here
        }
        AlertListener[] current = ((AlertMulticaster) l).listeners;
        int index = -1;
        for (int i = current.length - 1; i >= 0; i--) {
            if (current[i] == oldl) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return l; // it's not here
        }
        if (current.length == 2) {
            return current[1 - index];
        }
        AlertListener[] rest = new AlertListener[current.length - 1];
        System.arraycopy(current, 0, rest, 0, index);
        System.arraycopy(current, index + 1, rest, index, current.length - index - 1);
        return new AlertMulticaster(rest);
    }

    private static AlertListener[] listenersOf(AlertListener l) {
        return l instanceof AlertMulticaster ? ((AlertMulticaster) l).listeners : new AlertListener[]{l};
    }
}
