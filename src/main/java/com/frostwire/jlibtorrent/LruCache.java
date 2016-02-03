package com.frostwire.jlibtorrent;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
final class LruCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    public LruCache(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
