package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Variant container for bencoded data structures.
 * <p>
 * {@code Entry} represents a single node in a bencoded hierarchical data structure.
 * Bencoding is the binary encoding format used by BitTorrent for .torrent files, magnet
 * links metadata, DHT messages, and tracker communications. It can encode four types:
 * <ul>
 *   <li><b>String:</b> Binary-safe byte sequences</li>
 *   <li><b>Integer:</b> Signed 64-bit numbers</li>
 *   <li><b>List:</b> Ordered sequences of other entries</li>
 *   <li><b>Dictionary:</b> Key-value maps (keys are always strings)</li>
 * </ul>
 * <p>
 * <b>Parsing Bencoded Data:</b>
 * <pre>
 * // Parse .torrent file
 * byte[] torrentData = Files.readAllBytes(Paths.get("download.torrent"));
 * Entry root = Entry.bdecode(torrentData);
 *
 * // Or from file directly
 * Entry root = Entry.bdecode(new File("download.torrent"));
 *
 * // Root is typically a dictionary
 * Map&lt;String, Entry&gt; dict = root.dictionary();
 *
 * Entry announce = dict.get("announce");
 * String trackerUrl = announce.string();
 * </pre>
 * <p>
 * <b>Understanding the Structure:</b>
 * <pre>
 * // Bencoded data structure:
 * // {
 * //   "announce": "http://tracker.example.com:80/announce",
 * //   "info": {
 * //     "name": "filename.iso",
 * //     "piece length": 262144,
 * //     "pieces": [binary hash data],
 * //     "length": 4294967296
 * //   },
 * //   "creation date": 1234567890
 * // }
 *
 * Entry root = Entry.bdecode(torrentData);
 * Map&lt;String, Entry&gt; dict = root.dictionary();
 *
 * // Strings
 * String announce = dict.get("announce").string();
 *
 * // Nested dictionaries
 * Map&lt;String, Entry&gt; info = dict.get("info").dictionary();
 * String name = info.get("name").string();
 * long pieceLength = info.get("piece length").integer();
 *
 * // Lists (piece hashes are typically a binary string with 20-byte chunks)
 * Entry pieces = info.get("pieces");
 * String piecesData = pieces.string();
 * </pre>
 * <p>
 * <b>Creating Bencoded Data:</b>
 * <pre>
 * // Create from primitive types
 * Entry stringEntry = new Entry("hello");
 * Entry intEntry = new Entry(42L);
 *
 * // Create from Java collections
 * List&lt;Object&gt; list = Arrays.asList(
 *     "item1",
 *     42,
 *     new Entry("nested")
 * );
 * Entry listEntry = Entry.fromList(list);
 *
 * // Create nested dictionary
 * Map&lt;String, Object&gt; dict = new HashMap&lt;&gt;();
 * dict.put("name", "My Torrent");
 * dict.put("size", 1000000);
 * Entry dictEntry = Entry.fromMap(dict);
 * </pre>
 * <p>
 * <b>Encoding and Converting:</b>
 * <pre>
 * Entry entry = Entry.bdecode(torrentData);
 *
 * // Get string representation (for debugging)
 * String debug = entry.toString();  // Human-readable format
 *
 * // Re-encode to bencoded binary
 * byte[] bencoded = entry.bencode();
 * Files.write(Paths.get("output.torrent"), bencoded);
 *
 * // Extract values
 * String str = entry.string();      // For string entries
 * long num = entry.integer();       // For integer entries
 * List&lt;Entry&gt; list = entry.list();     // For list entries
 * Map&lt;String, Entry&gt; dict = entry.dictionary(); // For dictionary entries
 * </pre>
 * <p>
 * <b>Common Patterns - DHT Immutable Items:</b>
 * <pre>
 * // DHT can store and retrieve bencoded data
 * SessionManager sm = ...;
 * Sha1Hash key = new Sha1Hash("...");
 *
 * // Retrieve immutable DHT item
 * Entry item = sm.dhtGetItem(key, timeout);
 * if (item != null) {
 *     System.out.println("DHT item: " + item.toString());
 *
 *     // If it's a dictionary
 *     Map&lt;String, Entry&gt; data = item.dictionary();
 *     for (String k : data.keySet()) {
 *         System.out.println(k + " = " + data.get(k));
 *     }
 * }
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * - Creating from lists/maps involves copying data into bencoded structures
 * - Large files may have corresponding large Entry objects in memory
 * - Consider streaming for very large torrent files
 *
 * @see BDecodeNode - For low-level bdecode parsing
 * @see TorrentInfo - Uses Entry internally for parsing .torrent files
 * @see SessionManager#dhtGetItem(Sha1Hash, int) - Returns Entry for DHT items
 *
 * @author gubatron
 * @author aldenml
 */
public final class Entry {

    private final entry e;

    public Entry(entry e) {
        this.e = e;
    }

    public Entry(String s) {
        this(new entry(s));
    }

    public Entry(long n) {
        this(new entry(n));
    }

    public entry swig() {
        return e;
    }

    public byte[] bencode() {
        return Vectors.byte_vector2bytes(e.bencode());
    }

    public String string() {
        return e.string();
    }

    public long integer() {
        return e.integer();
    }

    public List<Entry> list() {
        return new EntryList(e.list());
    }

    public Map<String, Entry> dictionary() {
        return new EntryMap(e.dict());
    }

    @Override
    public String toString() {
        return e.to_string();
    }

    /**
     * Two entries are equal when they hold the same value: same type and the same bencoded form.
     * <pre>{@code
     * Entry a = Entry.bdecode("d3:fooi1ee".getBytes());
     * Entry b = Entry.bdecode("d3:fooi1ee".getBytes());
     * a.equals(b);                            // true
     * a.dictionary().equals(b.dictionary());  // true
     * }</pre>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }
        Entry other = (Entry) o;
        return e.type() == other.e.type() && Arrays.equals(bencode(), other.bencode());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bencode());
    }

    /**
     * Decodes bencoded data, e.g. a .torrent file, fast-resume data or a DHT item, into an entry tree.
     * <pre>{@code
     * Entry torrent = Entry.bdecode(Files.readAllBytes(Paths.get("ubuntu.torrent")));
     * String name = torrent.dictionary().get("info").dictionary().get("name").string();
     * }</pre>
     * Decoding stops at the end of the first complete value; any bytes after it are ignored. For large
     * inputs where only a few keys are read, {@link BDecodeNode#bdecode(byte[])} avoids copying the
     * whole tree.
     *
     * @param data bencoded bytes
     * @return the decoded entry
     * @throws IllegalArgumentException if {@code data} is not valid bencode (truncated, malformed, or
     *         nested deeper than libtorrent's limit); the message is libtorrent's
     */
    public static Entry bdecode(byte[] data) {
        error_code ec = new error_code();
        entry e = entry.bdecode(Vectors.bytes2byte_vector(data), ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException("Can't decode data: " + ec.message());
        }
        return new Entry(e);
    }

    public static Entry bdecode(File file) throws IOException {
        byte[] data = Files.bytes(file);
        return bdecode(data);
    }

    public static Entry fromList(List<?> list) {
        entry e = new entry(entry.data_type.list_t);

        entry_vector d = e.list();
        for (Object v : list) {
            if (v instanceof String) {
                d.add(new entry((String) v));
            } else if (v instanceof Integer) {
                d.add(new entry((Integer) v));
            } else if (v instanceof Entry) {
                d.add(((Entry) v).swig());
            } else if (v instanceof entry) {
                d.add((entry) v);
            } else if (v instanceof List) {
                d.add(fromList((List<?>) v).swig());
            } else if (v instanceof Map) {
                d.add(fromMap((Map<String, ?>) v).swig());
            } else {
                d.add(new entry(v.toString()));
            }
        }

        return new Entry(e);
    }

    public static Entry fromMap(Map<String, ?> map) {
        entry e = new entry(entry.data_type.dictionary_t);

        string_entry_map d = e.dict();
        for (String k : map.keySet()) {
            Object v = map.get(k);

            if (v instanceof String) {
                d.put(k, new entry((String) v));
            } else if (v instanceof Integer) {
                d.put(k, new entry((Integer) v));
            } else if (v instanceof Entry) {
                d.put(k, ((Entry) v).swig());
            } else if (v instanceof entry) {
                d.put(k, (entry) v);
            } else if (v instanceof List) {
                d.put(k, fromList((List<?>) v).swig());
            } else if (v instanceof Map) {
                d.put(k, fromMap((Map<String, ?>) v).swig());
            } else {
                d.put(k, new entry(v.toString()));
            }
        }

        return new Entry(e);
    }

    private static final class EntryList extends AbstractList<Entry> {

        private final entry_vector v;

        public EntryList(entry_vector v) {
            this.v = v;
        }

        @Override
        public Entry get(int index) {
            return new Entry(v.get(index));
        }

        @Override
        public boolean add(Entry entry) {
            v.add(entry.swig());
            return true;
        }

        @Override
        public int size() {
            return (int) v.size();
        }

        @Override
        public void clear() {
            v.clear();
        }

        @Override
        public boolean isEmpty() {
            return v.isEmpty();
        }
    }

    private static final class EntryMap extends AbstractMap<String, Entry> {

        private final string_entry_map m;

        public EntryMap(string_entry_map m) {
            this.m = m;
        }

        @Override
        public com.frostwire.jlibtorrent.Entry get(Object key) {
            String k = key.toString();
            return m.containsKey(k) ? new com.frostwire.jlibtorrent.Entry(m.get(key.toString())) : null;
        }

        @Override
        public com.frostwire.jlibtorrent.Entry put(String key, com.frostwire.jlibtorrent.Entry value) {
            com.frostwire.jlibtorrent.Entry r = get(key);
            m.put(key, value.swig());
            return r;
        }

        @Override
        public int size() {
            return (int) m.size();
        }

        @Override
        public void clear() {
            m.clear();
        }

        @Override
        public boolean containsKey(Object key) {
            return m.containsKey(key.toString());
        }

        @Override
        public boolean isEmpty() {
            return m.isEmpty();
        }

        @Override
        public Set<String> keySet() {
            return m.keySet();
        }

        @Override
        public com.frostwire.jlibtorrent.Entry remove(Object key) {
            String k = key.toString();
            if (!m.containsKey(k)) {
                return null;
            }
            return new com.frostwire.jlibtorrent.Entry(m.remove(k));
        }

        /**
         * A view over the native map, so every {@link Map} operation works: iteration, {@code values()},
         * {@code forEach}, {@code equals}, {@code toString}. Entries come in bencode (byte) order and
         * hold native iterators, so {@code getValue()} and {@code setValue()} reach the right element
         * even for keys that aren't valid UTF-8, like a v2 torrent's binary {@code piece layers} keys
         * (frostwire/frostwire-jlibtorrent#331, #335). {@code get}, {@code containsKey} and
         * {@code remove} take a {@code String}, so they can't address such keys; iterate instead.
         */
        @Override
        public Set<Map.Entry<String, com.frostwire.jlibtorrent.Entry>> entrySet() {
            return new AbstractSet<Map.Entry<String, com.frostwire.jlibtorrent.Entry>>() {
                @Override
                public Iterator<Map.Entry<String, com.frostwire.jlibtorrent.Entry>> iterator() {
                    List<Map.Entry<String, entry>> snapshot = new ArrayList<>(m.entrySet());
                    snapshot.sort((x, y) -> compareAsBytes(x.getKey(), y.getKey()));
                    Iterator<Map.Entry<String, entry>> natives = snapshot.iterator();
                    return new Iterator<Map.Entry<String, com.frostwire.jlibtorrent.Entry>>() {
                        private Map.Entry<String, entry> current;

                        @Override
                        public boolean hasNext() {
                            return natives.hasNext();
                        }

                        @Override
                        public Map.Entry<String, com.frostwire.jlibtorrent.Entry> next() {
                            current = natives.next();
                            return new LiveEntry(current);
                        }

                        @Override
                        public void remove() {
                            if (current == null) {
                                throw new IllegalStateException();
                            }
                            m.remove(current.getKey());
                            current = null;
                        }
                    };
                }

                @Override
                public int size() {
                    return (int) m.size();
                }
            };
        }

        private static int compareAsBytes(String a, String b) {
            return Arrays.compareUnsigned(a.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    b.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }

        private static final class LiveEntry implements Map.Entry<String, com.frostwire.jlibtorrent.Entry> {
            private final Map.Entry<String, entry> nativeEntry;

            private LiveEntry(Map.Entry<String, entry> nativeEntry) {
                this.nativeEntry = nativeEntry;
            }

            @Override
            public String getKey() {
                return nativeEntry.getKey();
            }

            @Override
            public com.frostwire.jlibtorrent.Entry getValue() {
                return new com.frostwire.jlibtorrent.Entry(nativeEntry.getValue());
            }

            @Override
            public com.frostwire.jlibtorrent.Entry setValue(com.frostwire.jlibtorrent.Entry value) {
                return new com.frostwire.jlibtorrent.Entry(nativeEntry.setValue(value.swig()));
            }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
                return getKey().equals(other.getKey()) && Objects.equals(getValue(), other.getValue());
            }

            @Override
            public int hashCode() {
                return getKey().hashCode() ^ Objects.hashCode(getValue());
            }

            @Override
            public String toString() {
                return getKey() + "=" + getValue();
            }
        }
    }
}
