package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Ed25519;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.dht_item;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.libtorrent;

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

        entry e = Entry.fromList(l).getSwig();
        String msg = e.to_string();
        System.out.println(msg);

        char_vector out = Vectors.new_char_vector(1200);

        int size = dht_item.canonical_string(e.bencode(), seq, salt, out);

        System.out.println(Vectors.char_vector2string(out, size));

        final byte[] seed = new byte[Ed25519.SEED_SIZE];
        final byte[] pk = new byte[Ed25519.PUBLIC_KEY_SIZE];
        final byte[] sk = new byte[Ed25519.PRIVATE_KEY_SIZE];

        Ed25519.createSeed(seed);
        Ed25519.createKeypair(pk, sk, seed);

        char_vector sig = Vectors.new_char_vector(Ed25519.SIGNATURE_SIZE);
        dht_item.sign_mutable_item(e.bencode(), salt, seq, Vectors.bytes2char_vector(pk), Vectors.bytes2char_vector(sk), sig);

        System.out.println(libtorrent.to_hex(sig));

        System.out.println(dht_item.verify_mutable_item(e.bencode(), salt, seq, Vectors.bytes2char_vector(pk), sig));
    }
}
