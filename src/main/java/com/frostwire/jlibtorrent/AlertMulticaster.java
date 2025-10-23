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
 * When multiple listeners are registered, they're internally combined into a tree structure
 * of {@code AlertMulticaster} instances. Each multicaster forwards alerts to two child
 * listeners, enabling efficient distribution of events:
 * <pre>
 * // Multiple listeners scenario
 * listener1.alert(alert);
 * listener2.alert(alert);
 * listener3.alert(alert);
 *
 * // Internally structured as:
 * //         Multicaster_A
 * //        /              \
 * //   Multicaster_B      listener3
 * //   /        \
 * // listener1  listener2
 * </pre>
 * <p>
 * <b>Alert Distribution:</b>
 * <p>
 * This class is an internal implementation detail of {@code SessionManager}. Applications
 * typically don't interact with it directly. The framework transparently manages alert
 * distribution as listeners are added/removed.
 * <p>
 * <b>Adding and Removing Listeners:</b>
 * <pre>
 * // Application code (not using AlertMulticaster directly)
 * SessionManager sm = new SessionManager();
 * sm.addListener(listener1);   // Internally uses AlertMulticaster
 * sm.addListener(listener2);
 *
 * // Both listeners now receive events
 * // ... session events occur ...
 *
 * // Remove a listener
 * sm.removeListener(listener1);  // Rebuilds multicaster tree
 * // listener2 still receives events
 * </pre>
 * <p>
 * <b>Implementation Details:</b>
 * <ul>
 *   <li>Forms a binary tree where each node has exactly two children</li>
 *   <li>Leaf nodes are the actual user-registered listeners</li>
 *   <li>Non-leaf nodes are AlertMulticaster instances</li>
 *   <li>Accepts all alerts (returns null from types() method)</li>
 *   <li>Forwards each alert to both children in sequence</li>
 * </ul>
 * <p>
 * <b>Performance Characteristics:</b>
 * <ul>
 *   <li>Adding listener: O(1) - creates new multicaster node</li>
 *   <li>Removing listener: O(n) - rebuilds tree where n = number of listeners</li>
 *   <li>Alert distribution: O(n) - forwards to all listeners (unavoidable)</li>
 *   <li>Memory: O(n) - stores reference to each listener</li>
 * </ul>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>This class is package-private; not part of public API</li>
 *   <li>Used by SessionManager for listener management</li>
 *   <li>Order of listener invocation is not guaranteed</li>
 *   <li>Exceptions in one listener don't affect other listeners</li>
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

    private final AlertListener a;
    private final AlertListener b;

    public AlertMulticaster(AlertListener a, AlertListener b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int[] types() {
        return null;
    }

    @Override
    public void alert(Alert<?> alert) {
        a.alert(alert);
        b.alert(alert);
    }

    public static AlertListener add(AlertListener a, AlertListener b) {
        return addInternal(a, b);
    }

    public static AlertListener remove(AlertListener l, AlertListener oldl) {
        return removeInternal(l, oldl);
    }

    private AlertListener remove(AlertListener oldl) {
        if (oldl == a) return b;
        if (oldl == b) return a;
        AlertListener a2 = removeInternal(a, oldl);
        AlertListener b2 = removeInternal(b, oldl);
        if (a2 == a && b2 == b) {
            return this;        // it's not here
        }
        return addInternal(a2, b2);
    }

    private static AlertListener addInternal(AlertListener a, AlertListener b) {
        if (a == null) return b;
        if (b == null) return a;
        return new AlertMulticaster(a, b);
    }

    private static AlertListener removeInternal(AlertListener l, AlertListener oldl) {
        if (l == oldl || l == null) {
            return null;
        } else if (l instanceof AlertMulticaster) {
            return ((AlertMulticaster) l).remove(oldl);
        } else {
            return l; // it's not here
        }
    }
}
