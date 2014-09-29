package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Ed25519;
import com.frostwire.jlibtorrent.Session;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtPut {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        short[] seed = new short[Ed25519.SEED_SIZE];
        int r = Ed25519.createSeed(seed);
        System.out.println("r = " + r);
        for (int i = 0; i < seed.length; i++) {
            System.out.print(seed[i]);
        }
    }
}
