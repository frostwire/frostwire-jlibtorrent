package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Ed25519;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtBootstrapAlert;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersAlert;
import com.frostwire.jlibtorrent.alerts.DhtPutAlert;
import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.entry_vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gubatron on 10/10/14.
 *
 * A rought proof of concept for a simple distributed
 * domain name system service that runs on top
 * of the Bittorrent DHT.
 * <p/>
 * The idea is to map a <scheme>://<domain name>
 * to a data structure that can mutate over time
 * which represents one or more servers.
 * <p/>
 * Keys are the SHA1("<scheme>://<domain name>")
 * A simple bencoded JSON string.
 * Values are in a dictionary
 */
public class DhtNs {

    public static void registerName(final Session s,
                                    final String name,
                                    final List<String> serverIps,
                                    final PrivateKey keys) {
        if (!s.isDHTRunning()) {
            System.out.println("Wait a little longer, connecting to the DHT...");
        } else {
            entry lt_entry = new entry(entry.data_type.list_t);
            lt_entry.list_v().reserve(serverIps.size());

            for (String ip : serverIps) {
                entry ipStringEntry = new entry(ip);
                lt_entry.list_v().add(ipStringEntry);
            }

            s.dhtPutItem(getDHTKey("ob://foo.dont.squat.here.com"),
                    keys.getPrivateKey(),
                    new Entry(lt_entry),
                    toHex(keys.getSalt()));
        }
    }

    public static void main(String[] args) throws IOException {
        final long dht_bootstrap_time_start = System.currentTimeMillis();
        final Session s = new Session();
        s.addListener(new AlertListener() {
            @Override
            public void alert(Alert<?> alert) {
                System.out.println("");
                System.out.println(alert.getSwig().message());

                if (alert instanceof DhtBootstrapAlert) {
                    final long dht_bootstrap_time_end = System.currentTimeMillis();
                    final long dht_bootstrap_time = dht_bootstrap_time_end - dht_bootstrap_time_start;
                    System.out.println("\nConnected to the DHT network in " + dht_bootstrap_time + " ms");
                }

                if (alert instanceof DhtPutAlert) {
                    DhtPutAlert put = (DhtPutAlert) alert;
                    System.out.println("DHT put alert with public key:");
                    System.out.println(toHex(put.getPublicKey()));
                }

            }
        });

        PrivateKey fooKey = createPrivateKey();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("$ ");//prompt
            String line = br.readLine().trim();
            if (line.contains("quit")) {
                break;
            } else if (line.startsWith("?") || line.toLowerCase().startsWith("help")) {
                System.out.println("Commands: register <name>, check <name>, quit");
            } else if (line.toLowerCase().startsWith("register")) {
                String[] split = line.split(" ");
                String name = split[1].trim();
                List<String> servers = new ArrayList<String>();
                servers.add("127.0.0.1");
                registerName(s, "foo://" + name, servers, createPrivateKey());
            } else if (!line.isEmpty()) {
                System.out.println("Invalid command: " + line);
                System.out.println("Try ? for help");
            }
        }
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

    private static byte[] getDHTKey(String key) {
        byte[] keySha1Hash = sha1(key);
        byte[] result = new byte[Ed25519.SEED_SIZE];
        System.arraycopy(keySha1Hash,0,result,0,32);
        return result;
    }

    private static byte[] sha1(String str) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(str.getBytes());
        return digest.digest();
    }

    /**
     * @return
     */
    private static PrivateKey createPrivateKey() {
        /** I'm guessing the seed could be the hash of a secondary public key
         * for instance.
         */
        byte[] seed = new byte[] { //length Ed25519.SEED_SIZE = 32 bytes
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa,
            0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14,
            0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
            0x1e, 0x1f
        };


        /** the private key could be the sha1hash of a PGP private key for example */
        //dummy key for the test
        byte[] privateKey = new byte[] {
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa,
            0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14,
            0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e,
            0x1f } ;

        //Ed25519.createKeypair(publicKey, privateKey, seed);

        System.out.println("seed:     " + toHex(seed));
        System.out.println("priv key: " + toHex(privateKey));

        return new PrivateKey(privateKey, seed);
    }

    private static class PrivateKey {
        private final byte[] pri;
        private final byte[] salt;

        PrivateKey(byte[] privateKey, byte[] salt) {
            pri = privateKey;
            this.salt = salt;
        }

        public byte[] getPrivateKey() {
            return pri;
        }

        public byte[] getSalt() {
            return salt;
        }
    }
}
