package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.sha1_bloom_filter;

/**
 * Created by gubatron on 2/23/15.
 */
public class Sha1BloomFilterTest {
    public static void main(String[] args) {
        Sha1Hash[] hashes = new Sha1Hash[] {
                new Sha1Hash("b20789734afd63a6bd82208b38f9fe3fb2eb0be6"),
                new Sha1Hash("bfb6379af528df67c2b1b2d105d2e20a94f0589a"),
                new Sha1Hash("5360bda4c6d1da74492ba4c9ff8ca980724ce46d"),
                new Sha1Hash("5d72b80db6299dfdfbb08f752b48ad151ef3d16d")};

        sha1_bloom_filter bloomFilter = new sha1_bloom_filter();

        for (Sha1Hash h : hashes) {
            System.out.println("Has " + h + "? -> " + bloomFilter.find(h.getSwig()));
            bloomFilter.set(h.getSwig());
        }

        System.out.println("=== now after they've been added ===");

        for (Sha1Hash h : hashes) {
            System.out.println("Has " + h + "? -> " + bloomFilter.find(h.getSwig()));
        }

        System.out.println("=== Test finished. ===");

    }
}
