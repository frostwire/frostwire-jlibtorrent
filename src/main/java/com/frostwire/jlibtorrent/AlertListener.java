package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * Callback interface for receiving notifications from the bittorrent session.
 * <p>
 * {@code AlertListener} is the primary mechanism for applications to receive real-time
 * notifications about torrent activity, errors, and network events from jlibtorrent.
 * The listener pattern allows asynchronous event handling without polling, making it
 * ideal for responsive user interfaces and server applications.
 * <p>
 * <b>Alert-Based Event System:</b>
 * <p>
 * Instead of synchronous method calls, jlibtorrent uses an event-driven alert system.
 * The native libtorrent session generates alerts (events) which are queued and dispatched
 * to registered listeners. This decouples the fast network processing thread from your
 * application logic, enabling both high performance and clean architecture.
 * <p>
 * <b>Basic Usage:</b>
 * <pre>
 * // Create listener for torrent completion events only
 * AlertListener listener = new AlertListener() {
 *     public int[] types() {
 *         // Only listen to torrent finished alerts
 *         return new int[] {AlertType.TORRENT_FINISHED.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         TorrentFinishedAlert a = (TorrentFinishedAlert) alert;
 *         System.out.println("Download complete: " + a.handle().name());
 *         // Notify user, update UI, etc.
 *     }
 * };
 *
 * // Register with session manager
 * sm.addListener(listener);
 * </pre>
 * <p>
 * <b>Listening to Multiple Event Types:</b>
 * <pre>
 * AlertListener listener = new AlertListener() {
 *     public int[] types() {
 *         // Listen to multiple alert types
 *         return new int[] {
 *             AlertType.TORRENT_FINISHED.swig(),
 *             AlertType.TORRENT_ERROR.swig(),
 *             AlertType.PEER_DISCONNECTED.swig()
 *         };
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         switch (alert.type()) {
 *             case TORRENT_FINISHED:
 *                 handleFinished((TorrentFinishedAlert) alert);
 *                 break;
 *             case TORRENT_ERROR:
 *                 handleError((TorrentErrorAlert) alert);
 *                 break;
 *             case PEER_DISCONNECTED:
 *                 handleDisconnect((PeerDisconnectedAlert) alert);
 *                 break;
 *         }
 *     }
 * };
 *
 * sm.addListener(listener);
 * </pre>
 * <p>
 * <b>Listening to All Alerts:</b>
 * <pre>
 * // Listen to every alert type
 * AlertListener universalListener = new AlertListener() {
 *     public int[] types() {
 *         return null;  // null means listen to ALL alert types
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         System.out.println("Alert: " + alert.type() + " - " + alert.message());
 *         // Debugging, monitoring, or comprehensive event handling
 *     }
 * };
 *
 * sm.addListener(universalListener);
 * </pre>
 * <p>
 * <b>Common Alert Types and Patterns:</b>
 * <pre>
 * // Torrent lifecycle
 * AlertType.ADD_TORRENT           // Torrent added to session
 * AlertType.TORRENT_FINISHED      // Download complete
 * AlertType.TORRENT_REMOVED       // Torrent removed from session
 * AlertType.TORRENT_ERROR         // Torrent encountered an error
 *
 * // Network activity
 * AlertType.PEER_CONNECTED        // New peer connection established
 * AlertType.PEER_DISCONNECTED     // Peer connection lost
 * AlertType.STATE_UPDATE          // Periodic torrent status snapshot
 *
 * // Metadata and DHT
 * AlertType.METADATA_RECEIVED     // Metadata fetched from magnet link
 * AlertType.DHT_BOOTSTRAP         // DHT network ready
 *
 * // Statistics
 * AlertType.SESSION_STATS         // Session-wide statistics update
 * </pre>
 * <p>
 * <b>Thread Safety and Performance:</b>
 * <ul>
 *   <li>Alert callbacks are invoked from jlibtorrent's internal alert-processing thread</li>
 *   <li>Keep alert handlers fast - don't block or do expensive I/O in alert()</li>
 *   <li>Use thread-safe collections if accessing shared state from alert callbacks</li>
 *   <li>Consider queuing work for a thread pool instead of handling in alert()</li>
 *   <li>Use Collections.synchronizedList() or CopyOnWriteArrayList for thread-safe storage</li>
 * </ul>
 * <p>
 * <b>Alert Type Filtering Best Practices:</b>
 * <pre>
 * // GOOD: Filter specific types you care about
 * public int[] types() {
 *     return new int[] {
 *         AlertType.TORRENT_FINISHED.swig(),
 *         AlertType.STATE_UPDATE.swig()
 *     };
 * }
 * // Avoids processing irrelevant alerts, reduces GC pressure
 *
 * // LESS EFFICIENT: Listen to everything
 * public int[] types() {
 *     return null;  // Process every single alert
 * }
 * // Use only for debugging or monitoring all activity
 * </pre>
 * <p>
 * <b>Handling Type-Unsafe Casts:</b>
 * <pre>
 * public void alert(Alert&lt;?&gt; alert) {
 *     // Always verify the alert type before casting
 *     if (alert.type() == AlertType.TORRENT_FINISHED) {
 *         TorrentFinishedAlert a = (TorrentFinishedAlert) alert;
 *         // Now safe to use type-specific methods
 *     }
 * }
 * </pre>
 * <p>
 * <b>Multiple Listeners:</b>
 * <pre>
 * // Register multiple independent listeners
 * sm.addListener(listener1);  // Handles UI updates
 * sm.addListener(listener2);  // Handles database logging
 * sm.addListener(listener3);  // Handles notifications
 *
 * // Each receives the same alerts independently
 * // Order of invocation is not guaranteed
 * </pre>
 *
 * @see Alert - The base interface for all alert types
 * @see SessionManager#addListener(AlertListener) - Register a listener
 * @see SessionManager#removeListener(AlertListener) - Unregister a listener
 * @see AlertType - All available alert types
 *
 * @author gubatron
 * @author aldenml
 */
public interface AlertListener {

    /**
     * List of alert types this listener wants to receive.
     * <p>
     * Returning specific types filters alerts before they reach your handler,
     * improving performance by avoiding unnecessary callback invocations.
     * Return null to receive all alert types.
     *
     * @return array of alert type codes (from AlertType.X.swig()), or null for all types
     */
    int[] types();

    /**
     * Invoked when an alert matching this listener's type filter is generated.
     * <p>
     * This method is called from jlibtorrent's internal alert-processing thread,
     * so implementations should be fast and thread-safe. Do not block on I/O,
     * network operations, or locks held by other threads processing alerts.
     *
     * @param alert the alert event with type-specific information
     */
    void alert(Alert<?> alert);
}
