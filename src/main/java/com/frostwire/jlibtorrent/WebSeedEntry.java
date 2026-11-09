package com.frostwire.jlibtorrent;

import java.util.ArrayList;

/**
 * A web seed of a torrent: an HTTP server that has the torrent's files (BEP 19, "url-list").
 * <p>
 * libtorrent 2.1 supports only this kind of web seed. It removed BEP 17 HTTP seeds ("httpseeds")
 * and keeps a torrent's web seeds as plain URLs ({@code add_torrent_params::url_seeds}), so since
 * jlibtorrent 2.1.2.0 an entry is just its URL: {@link #auth()} is always empty,
 * {@link #extraHeaders()} always empty and {@link #type()} always {@link Type#URL_SEED}.
 * <pre>
 * for (WebSeedEntry seed : ti.webSeeds()) {
 *     System.out.println(seed.url());
 * }
 * </pre>
 *
 * @author gubatron
 * @author aldenml
 * @see TorrentInfo#webSeeds()
 */
public final class WebSeedEntry {

    private final String url;

    /**
     * @param url the web seed's URL
     */
    public WebSeedEntry(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url can't be null");
        }
        this.url = url;
    }

    /**
     * @return the web seed's URL
     */
    public String url() {
        return url;
    }

    /**
     * Always empty since libtorrent 2.1, which keeps no authentication per web seed. Put
     * credentials in the URL instead.
     *
     * @return an empty string
     */
    public String auth() {
        return "";
    }

    /**
     * Always empty since libtorrent 2.1, which keeps no HTTP headers per web seed.
     *
     * @return an empty list
     */
    public ArrayList<Pair<String, String>> extraHeaders() {
        return new ArrayList<>(0);
    }

    /**
     * Always {@link Type#URL_SEED}: libtorrent 2.1 removed BEP 17 HTTP seeds.
     *
     * @return {@link Type#URL_SEED}
     */
    public Type type() {
        return Type.URL_SEED;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WebSeedEntry && url.equals(((WebSeedEntry) o).url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return url;
    }

    /**
     * The kind of web seed.
     */
    public enum Type {

        /**
         * A BEP 19 web seed ("url-list"), the only kind libtorrent 2.1 supports.
         */
        URL_SEED(0),

        /**
         * A BEP 17 HTTP seed ("httpseeds"). libtorrent 2.1 removed them: never returned.
         *
         * @deprecated libtorrent 2.1 removed BEP 17 HTTP seeds
         */
        @Deprecated
        HTTP_SEED(1),

        UNKNOWN(-1);

        Type(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the value libtorrent 2.0's {@code web_seed_entry::type_t} had for this type
         */
        public int swig() {
            return swigValue;
        }

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
