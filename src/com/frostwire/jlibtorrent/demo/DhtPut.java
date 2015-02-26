package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtAnnounceAlert;
import com.frostwire.jlibtorrent.alerts.DhtMutableItemAlert;
import com.frostwire.jlibtorrent.alerts.DhtPutAlert;
import com.frostwire.jlibtorrent.swig.entry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtPut {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new AlertListener() {

            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.getSwig().message());

                if (alert instanceof DhtAnnounceAlert) {
                    String a = ((DhtAnnounceAlert) alert).getIP().getSwig().to_string();
                    int p = ((DhtAnnounceAlert) alert).getPort();
                    s.addDHTNode(new Pair<String, Integer>(a, p));
                    signal.countDown();
                }

                if (alert instanceof DhtPutAlert) {
                    DhtPutAlert put = (DhtPutAlert) alert;
                    System.out.println("DHT put alert with public key:");
                    System.out.println("PK:" + LibTorrent.toHex(put.getPublicKey()));
                    System.out.println("SIG:" + LibTorrent.toHex(put.getSignature()));
                    System.out.println("SALT:" + put.getSalt());

                    s.dhtGetItem(put.getPublicKey(), "ts");
                }

                if (alert instanceof DhtMutableItemAlert) {
                    DhtMutableItemAlert m = (DhtMutableItemAlert) alert;
                    System.out.println(m.getItem());
                    System.out.println("PK:" + LibTorrent.toHex(m.getKey()));
                    System.out.println("SIG:" + LibTorrent.toHex(m.getSignature()));
                    System.out.println("SALT:" + m.getSalt());
                }
            }
        });

        System.out.println("Waiting to DHT bootstrap");

        signal.await(10, TimeUnit.SECONDS);

        System.out.println("DHT with peers");
        System.out.println("Peers: " + s.getStats().getDHTNodes());

        byte[] seed = new byte[Ed25519.SEED_SIZE];
        int r = Ed25519.createSeed(seed);

        byte[] publicKey = new byte[Ed25519.PUBLIC_KEY_SIZE];
        byte[] privateKey = new byte[Ed25519.PRIVATE_KEY_SIZE];

        Ed25519.createKeypair(publicKey, privateKey, seed);
        System.out.println("public key:");
        System.out.println(LibTorrent.toHex(publicKey));

        s.dhtPutItem(publicKey, privateKey, new Entry(new entry("test")), "ts");

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
