package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Ed25519;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtBootstrapAlert;
import com.frostwire.jlibtorrent.alerts.DhtPutAlert;
import com.frostwire.jlibtorrent.swig.entry;

import java.util.concurrent.CountDownLatch;

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
            public boolean accept(Alert<?> alert) {
                return true;
            }

            @Override
            public void alert(Alert<?> alert) {
                //System.out.println(alert.getSwig().message());

                if (alert instanceof DhtBootstrapAlert) {
                    signal.countDown();
                }

                if (alert instanceof DhtPutAlert) {
                    DhtPutAlert put = (DhtPutAlert) alert;
                    System.out.println("DHT put alert with public key:");
                    System.out.println(toHex(put.getPublicKey()));
                }
            }
        });

        System.out.println("Waiting to DHT bootstrap");

        signal.await();

        System.out.println("DHT bootstraped");

        byte[] seed = new byte[Ed25519.SEED_SIZE];
        int r = Ed25519.createSeed(seed);

        byte[] publicKey = new byte[Ed25519.PUBLIC_KEY_SIZE];
        byte[] privateKey = new byte[Ed25519.PRIVATE_KEY_SIZE];

        Ed25519.createKeypair(publicKey, privateKey, seed);

        s.dhtPutItem(publicKey, privateKey, new Entry(new entry("test")));

        System.out.println("public key:");
        System.out.println(toHex(publicKey));

        System.out.println("Press ENTER to exit");
        System.in.read();
    }

    private static String toHex(byte[] arr) {
        String hex = "";
        for (int i = 0; i < arr.length; i++) {
            String t = Integer.toHexString(arr[i] & 0xFF);
            if (t.length() < 2) {
                t = "0" + t;
            }
            hex += t;
        }

        return hex;
    }
}
