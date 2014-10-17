package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Ed25519;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.LibTorrent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtItemTest {

    public static void main(String[] args) throws Throwable {

        List<Object> l = new ArrayList<Object>();

        String salt = "";
        int seq = 1;

        l.add("127.0.0.1");
        l.add("localhost");

        Entry e = Entry.fromList(l);
        System.out.println(e);

        byte[] out = new byte[1200];

        int size = DHT.canonicalString(e, seq, salt, out);

        System.out.println(LibTorrent.toHex(out));

        //final byte[] seed = new byte[Ed25519.SEED_SIZE];
        final byte[] seed = new byte[]{ //length Ed25519.SEED_SIZE = 32 bytes
                0x10, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa,
                0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14,
                0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
                0x1e, 0x1f
        };
        final byte[] pk = new byte[Ed25519.PUBLIC_KEY_SIZE];
        final byte[] sk = new byte[Ed25519.PRIVATE_KEY_SIZE];

        //Ed25519.createSeed(seed);
        Ed25519.createKeypair(pk, sk, seed);
        System.out.println("PK:" + LibTorrent.toHex(pk));
        System.out.println("SK:" + LibTorrent.toHex(sk));

        byte[] sig = new byte[Ed25519.SIGNATURE_SIZE];
        DHT.signMutableItem(e, salt, seq, pk, sk, sig);

        System.out.println(LibTorrent.toHex(sig));

        System.out.println(DHT.verifyMutableItem(e, salt, seq, pk, sig));
    }
}
