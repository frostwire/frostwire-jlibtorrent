package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_string_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair_vector;
import com.frostwire.jlibtorrent.swig.web_seed_entry;

import java.util.ArrayList;

/**
 * Information about a web seed (HTTP/FTP server) providing torrent content.
 * <p>
 * {@code WebSeedEntry} represents a web server URL that provides files for a torrent.
 * Web seeding allows distributing content via HTTP/FTP servers alongside peer-to-peer
 * transfer. Useful for seeding large files from CDNs or reducing peer dependency.
 * See BEP 17 (URL seeds) and BEP 19 (HTTP/FTP seeding) for protocol details.
 * <p>
 * <b>Understanding Web Seeds:</b>
 * <br/>
 * Web seeds provide an alternative source for torrent data:
 * <ul>
 *   <li><b>HTTP Downloads:</b> Fetch pieces from web server (like regular HTTP)</li>
 *   <li><b>Redundancy:</b> Supplement peer downloads if peers are slow/unavailable</li>
 *   <li><b>Seeding:</b> Host files on web server without running bittorrent client</li>
 *   <li><b>CDN Integration:</b> Use CDN to distribute content efficiently</li>
 * </ul>
 * <p>
 * <b>Web Seed Types:</b>
 * <pre>
 * URL_SEED (BEP 17):
 *   - Standard URL seed format
 *   - Request: GET /path/to/file.torrent/16384-131072
 *   - Requests byte range from file
 *   - Works with any HTTP server supporting range requests
 *
 * HTTP_SEED (BEP 19):
 *   - HTTP seed format by John Hoffman
 *   - Different request format for range retrieval
 *   - Optimized for seeding (no full file on each seed)
 *   - Less common than URL seeds
 * </pre>
 * <p>
 * <b>Accessing Web Seed Information:</b>
 * <pre>
 * // From TorrentInfo
 * TorrentInfo info = new TorrentInfo(torrentFile);
 * java.util.List&lt;WebSeedEntry&gt; webSeeds = info.webSeeds();
 *
 * for (WebSeedEntry seed : webSeeds) {
 *     // Get seed URL
 *     String url = seed.url();
 *     System.out.println(\"Web seed: \" + url);
 *
 *     // Get type
 *     WebSeedEntry.Type type = seed.type();
 *     System.out.println(\"Type: \" + type);
 *
 *     // Get authentication (if needed)
 *     String auth = seed.auth();
 *     if (auth != null &amp;&amp; !auth.isEmpty()) {
 *         System.out.println(\"Requires auth\");
 *     }
 *
 *     // Get custom HTTP headers
 *     java.util.ArrayList&lt;Pair&lt;String, String&gt;&gt; headers = seed.extraHeaders();
 *     for (Pair&lt;String, String&gt; header : headers) {
 *         System.out.println(\"Header: \" + header.first() + \" = \" + header.second());
 *     }
 * }
 * </pre>
 * <p>
 * <b>Adding Web Seeds Programmatically:</b>
 * <pre>
 * AddTorrentParams params = new AddTorrentParams();
 *
 * // Add web seed URL
 * // Web seeds can be added to params for torrents without seeds in .torrent
 * </pre>
 * <p>
 * <b>Web Seed Authentication:</b>
 * <pre>
 * // From WebSeedEntry
 * String auth = seed.auth();
 * // Format: "username:password" (HTTP Basic Auth)
 *
 * if (auth != null &amp;&amp; !auth.isEmpty()) {
 *     String[] parts = auth.split(\":\", 2);
 *     String username = parts[0];
 *     String password = parts.length &gt; 1 ? parts[1] : \"\";
 *     // Use for HTTP Basic Auth in request headers
 * }
 * </pre>
 * <p>
 * <b>Custom HTTP Headers:</b>
 * <pre>
 * // Get extra headers for web seed requests
 * java.util.ArrayList&lt;Pair&lt;String, String&gt;&gt; headers = seed.extraHeaders();
 *
 * // Headers may include:
 * // - User-Agent
 * // - Accept-Language
 * // - Custom application headers
 * // - Authorization tokens
 *
 * for (Pair&lt;String, String&gt; header : headers) {
 *     String name = header.first();
 *     String value = header.second();
 *     // Add to HTTP request
 * }
 * </pre>
 * <p>
 * <b>URL Format Examples:</b>
 * <pre>
 * // URL seed for single file
 * http://example.com/downloads/file.iso
 *
 * // URL seed for multi-file torrent
 * http://cdn.example.com/content/
 *
 * // With custom port
 * http://example.com:8080/torrents/archive.tar.gz
 *
 * // Authenticated URL seed
 * // auth field contains credentials separately
 * </pre>
 * <p>
 * <b>Performance Considerations:</b>
 * <ul>
 *   <li>Web seeds supplement peer downloads; don't replace them</li>
 *   <li>Download speed limited by web server bandwidth</li>
 *   <li>HTTP requests have TCP setup overhead</li>
 *   <li>CDN web seeds typically faster than peer downloads</li>
 *   <li>Range requests must be supported by server (HTTP 206)</li>
 * </ul>
 *
 * @see TorrentInfo#webSeeds() - Get web seeds from torrent
 * @see AddTorrentParams - Add web seeds to torrent
 * @see WebSeedEntry.Type - URL_SEED vs HTTP_SEED
 *
 * @author gubatron
 * @author aldenml
 */
public final class WebSeedEntry {

    private final web_seed_entry e;

    public WebSeedEntry(web_seed_entry e) {
        this.e = e;
    }

    public web_seed_entry swig() {
        return e;
    }

    /**
     * The URL of the web seed.
     *
     * @return
     */
    public String url() {
        return e.getUrl();
    }

    /**
     * Optional authentication. If this is set, it's passed
     * in as HTTP basic auth to the web seed. The format is:
     * username:password.
     *
     * @return
     */
    public String auth() {
        return e.getAuth();
    }

    /**
     * Any extra HTTP headers that need to be passed to the web seed.
     *
     * @return
     */
    public ArrayList<Pair<String, String>> extraHeaders() {
        string_string_pair_vector v = e.getExtra_headers();
        int size = (int) v.size();

        ArrayList<Pair<String, String>> l = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            string_string_pair p = v.get(i);
            l.add(new Pair<>(p.getFirst(), p.getSecond()));
        }

        return l;
    }

    /**
     * The type of web seed.
     *
     * @return
     * @see Type
     */
    public Type type() {
        return Type.fromSwig(e.getType());
    }

    /**
     * Http seeds are different from url seeds in the
     * protocol they use. http seeds follows the original
     * http seed spec. by John Hoffman
     */
    public enum Type {

        /**
         *
         */
        URL_SEED(web_seed_entry.type_t.url_seed.swigValue()),

        /**
         *
         */
        HTTP_SEED(web_seed_entry.type_t.http_seed.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Type(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue
         * @return
         */
        public static Type fromSwig(int swigValue) {
            Type[] enumValues = Type.class.getEnumConstants();
            for (Type ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
