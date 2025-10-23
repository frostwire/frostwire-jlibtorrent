package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair;

/**
 * Generic pair container for holding two related values of different types.
 * <p>
 * {@code Pair} mirrors the C++ {@code std::pair} template, providing a simple way to
 * combine two heterogeneous values into a single object. This is commonly used in
 * jlibtorrent for returning multiple values from methods and storing key-value pairs.
 * <p>
 * <b>Understanding Pair Usage:</b>
 * <br/>
 * Pairs are used throughout jlibtorrent to represent:
 * <ul>
 *   <li><b>Key-Value Pairs:</b> Configuration settings, tracker/peer info</li>
 *   <li><b>Coordinate Pairs:</b> Range boundaries like (start, end)</li>
 *   <li><b>Status Tuples:</b> Combined result and status information</li>
 *   <li><b>Native Interop:</b> Mapping between Java and C++ pair types</li>
 * </ul>
 * <p>
 * <b>Creating Pairs:</b>
 * <pre>
 * // String-String pair (e.g., name-value configuration)
 * Pair&lt;String, String&gt; setting = new Pair&lt;&gt;(\"key\", \"value\");
 * System.out.println(\"Key: \" + setting.first);
 * System.out.println(\"Value: \" + setting.second);
 *
 * // String-Integer pair (e.g., name-port)
 * Pair&lt;String, Integer&gt; endpoint = new Pair&lt;&gt;(\"example.com\", 6881);
 * System.out.println(\"Host: \" + endpoint.first);
 * System.out.println(\"Port: \" + endpoint.second);
 *
 * // Generic types
 * Pair&lt;Integer, Long&gt; range = new Pair&lt;&gt;(0, 1024L);
 * System.out.println(\"Start: \" + range.first);
 * System.out.println(\"End: \" + range.second);
 * </pre>
 * <p>
 * <b>Using Pairs in Collections:</b>
 * <pre>
 * // Store multiple tracker/peer pairs
 * java.util.List&lt;Pair&lt;String, Integer&gt;&gt; nodes =
 *     new java.util.ArrayList&lt;&gt;();
 *
 * nodes.add(new Pair&lt;&gt;(\"router.bittorrent.com\", 6881));
 * nodes.add(new Pair&lt;&gt;(\"router.utorrent.com\", 6881));
 * nodes.add(new Pair&lt;&gt;(\"dht.transmissionbt.com\", 6881));
 *
 * for (Pair&lt;String, Integer&gt; node : nodes) {
 *     System.out.println(\"Node: \" + node.first + \":\" + node.second);
 * }
 * </pre>
 * <p>
 * <b>Native Conversion Methods:</b>
 * <p>
 * {@code Pair} provides methods to convert between Java and native C++ pair types:
 * <pre>
 * // Convert String-String pair to native type
 * Pair&lt;String, String&gt; javaPair = new Pair&lt;&gt;(\"key\", \"value\");
 * com.frostwire.jlibtorrent.swig.string_string_pair nativePair =
 *     javaPair.to_string_string_pair();
 *
 * // Convert String-Integer pair to native type
 * Pair&lt;String, Integer&gt; config = new Pair&lt;&gt;(\"port\", 6881);
 * com.frostwire.jlibtorrent.swig.string_int_pair nativeConfig =
 *     config.to_string_int_pair();
 * </pre>
 * <p>
 * <b>Common Use Cases in BitTorrent:</b>
 * <pre>
 * // DHT nodes (hostname:port pairs)
 * Pair&lt;String, Integer&gt; dhtNode = new Pair&lt;&gt;(\"router.bittorrent.com\", 6881);
 *
 * // Trackers (url:tier pairs)
 * Pair&lt;String, Integer&gt; tracker = new Pair&lt;&gt;(
 *     \"http://tracker.example.com/announce\",
 *     0  // tier 0 = primary
 * );
 *
 * // Web seeds (url:priority pairs)
 * Pair&lt;String, Integer&gt; webSeed = new Pair&lt;&gt;(
 *     \"http://example.com/torrent\",
 *     1  // priority
 * );
 * </pre>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li>Pairs are immutable after creation (fields are final)</li>
 *   <li>Both type parameters must be specified for type safety</li>
 *   <li>Native conversion methods only support specific combinations (String-String, String-Integer)</li>
 *   <li>Use diamond operator ({@code <&gt;}) for cleaner syntax in Java 7+</li>
 * </ul>
 *
 * @param <T1> the type of the first element
 * @param <T2> the type of the second element
 *
 * @see java.util.AbstractMap.SimpleEntry - Similar Java utility class
 * @see java.util.Map.Entry - Standard Java pair interface
 *
 * @author gubatron
 * @author aldenml
 */
public final class Pair<T1, T2> {

    /**
     * @param first  first element
     * @param second second element
     */
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * the first element
     */
    public final T1 first;

    /**
     * the second element
     */
    public final T2 second;

    /**
     * @return a native object
     */
    string_string_pair to_string_string_pair() {
        if (!String.class.equals(first.getClass()) || !String.class.equals(second.getClass())) {
            throw new IllegalArgumentException("Incompatible types");
        }

        return new string_string_pair((String) first, (String) second);
    }

    /**
     * @return a native object
     */
    string_int_pair to_string_int_pair() {
        if (!String.class.equals(first.getClass()) || !Integer.class.equals(second.getClass())) {
            throw new IllegalArgumentException("Incompatible types");
        }

        return new string_int_pair((String) first, (Integer) second);
    }
}
