package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_string_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair_vector;
import com.frostwire.jlibtorrent.swig.web_seed_entry;

import java.util.ArrayList;
import java.util.List;

/**
 * The web_seed_entry holds information about a web seed (also known
 * as URL seed or HTTP seed). It is essentially a URL with some state
 * associated with it. For more information, see `BEP 17`_ and `BEP 19`_.
 *
 * @author gubatron
 * @author aldenml
 */
public final class WebSeedEntry {

    private final web_seed_entry e;

    public WebSeedEntry(web_seed_entry e) {
        this.e = e;
    }

    public web_seed_entry getSwig() {
        return e;
    }

    /**
     * The URL of the web seed.
     *
     * @return
     */
    public String getUrl() {
        return e.getUrl();
    }

    /**
     * The type of web seed (see type_t).
     *
     * @return
     */
    public Type getType() {
        return Type.fromSwig(e.getType());
    }

    /**
     * Optional authentication. If this is set, it's passed
     * in as HTTP basic auth to the web seed. The format is:
     * username:password.
     *
     * @return
     */
    public String getAuth() {
        return e.getAuth();
    }

    /**
     * Any extra HTTP headers that need to be passed to the web seed.
     *
     * @return
     */
    public List<Pair<String, String>> getExtraHeaders() {
        string_string_pair_vector v = e.getExtra_headers();
        int size = (int) v.size();

        List<Pair<String, String>> l = new ArrayList<Pair<String, String>>(size);
        for (int i = 0; i < size; i++) {
            string_string_pair p = v.get(i);
            l.add(new Pair<String, String>(p.getFirst(), p.getSecond()));
        }

        return l;
    }

    /**
     *
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

        private Type(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Type fromSwig(int swigValue) {
            Type[] enumValues = Type.class.getEnumConstants();
            for (Type ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
