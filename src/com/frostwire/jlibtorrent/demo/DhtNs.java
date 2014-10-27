package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gubatron on 10/10/14.
 * <p/>
 * A rough proof of concept for a simple distributed
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

    public static void putName(final Session s,
                               final String name,
                               final List<String> serverIps) {
        if (!s.isDHTRunning()) {
            System.out.println("Wait a little longer, connecting to the DHT...");
        } else {
            entry lt_entry = new entry(entry.data_type.list_t);
            //lt_entry.list_v().reserve(serverIps.size());

            for (String ip : serverIps) {
                entry ipStringEntry = new entry(ip);
                lt_entry.list().push_back(ipStringEntry);
            }

            byte[] dhtKey = getDHTKey(name);
            String dhtHexKey = toHex(dhtKey);
            System.out.println("register " + name + " (" + dhtHexKey + ")");

            Entry entry = new Entry(lt_entry);
            System.out.println(entry);

            Sha1Hash sha1Hash = s.dhtPutItem(entry);
            System.out.println("Putting entry, the sha1Hash is " + sha1Hash.toString());
        }
    }

    public static void getName(final Session s, String sha1Hex) {
        System.out.println("Get immutable with key: " + sha1Hex);
        s.dhtGetItem(new Sha1Hash(sha1Hex));
    }


    public static void registerName(final Session s,
                                    final String name,
                                    final List<String> serverIps,
                                    final KeyPair keys) {
        if (!s.isDHTRunning()) {
            System.out.println("Wait a little longer, connecting to the DHT...");
        } else {
            entry lt_entry = new entry(entry.data_type.list_t);
            //lt_entry.list_v().reserve(serverIps.size());

            for (String ip : serverIps) {
                entry ipStringEntry = new entry(ip);
                lt_entry.list().push_back(ipStringEntry);
            }

            byte[] dhtKey = getDHTKey(name);
            String dhtHexKey = toHex(dhtKey);
            System.out.println("register " + name + " (" + dhtHexKey + ")");

            Entry entry = new Entry(lt_entry);
            System.out.println(entry);

            s.dhtPutItem(keys.getPublicKey(),
                    keys.getPrivateKey(),
                    entry);
        }
    }

    public static void checkName(final Session s, final String name, final KeyPair keys) {
        if (!s.isDHTRunning()) {
            System.out.println("Wait a little longer, connecting to the DHT...");
        } else {
            byte[] dhtKey = getDHTKey(name);
            String dhtHexKey = toHex(dhtKey);
            System.out.println("check " + name + " (" + dhtHexKey + ")");
            s.dhtGetItem(keys.getPublicKey());
        }
    }

    public static void main(String[] args) throws IOException {
        final long dht_bootstrap_time_start = System.currentTimeMillis();
        final Session s = new Session();
        //s.stopUPnP();

        final HashMap<String, Alert> keyAlertMap = new HashMap<String, Alert>();

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                String alertType = alert.getType().toString();

                if (alertType.equals("PORTMAP_LOG") ||
                        alertType.equals("EXTERNAL_IP") ||
                        alertType.equals("LISTEN_SUCCEEDED") ||
                        alertType.equals("DHT_GET_PEERS")) {
                    return;
                }


                System.out.println("");
                System.out.println(alert.getType().toString() + "(" + alert.getClass() + "): " + alert.getSwig().message());

                if (alert instanceof DhtBootstrapAlert ||
                        alert instanceof DhtPutAlert ||
                        alert instanceof DhtReplyAlert ||
                        alert instanceof DhtImmutableItemAlert ||
                        alert instanceof DhtMutableItemAlert ||
                        alert instanceof DhtErrorAlert ||
                        alert instanceof DhtAnnounceAlert) {

                    if (alert instanceof DhtErrorAlert) {
                        System.out.println("DHT ERROR!: " + ((DhtErrorAlert) alert).getError().message());
                    }

                    if (alert instanceof DhtAnnounceAlert) {
                        String a = ((DhtAnnounceAlert) alert).getIP().getSwig().to_string();
                        int p = ((DhtAnnounceAlert) alert).getPort();
                        s.addDHTNode(new Pair<String, Integer>(a, p));
                        System.out.println("Added node: " + a + ":" + p);
                    }

                    if (alert instanceof DhtBootstrapAlert) {
                        DhtBootstrapAlert bstrap = (DhtBootstrapAlert) alert;
                        System.out.println("DhtBoostrapAlert: message(" + bstrap.getSwig().message() + ")\n" +
                                "what: (" + bstrap.getSwig().what() + ")");
                        final long dht_bootstrap_time_end = System.currentTimeMillis();
                        final long dht_bootstrap_time = dht_bootstrap_time_end - dht_bootstrap_time_start;
                        System.out.println("\nConnected to the DHT network in " + dht_bootstrap_time + " ms");
                    }

                    if (alert instanceof DhtPutAlert) {
                        DhtPutAlert put = (DhtPutAlert) alert;
                        System.out.println("DHT put alert with public key: " + toHex(put.getPublicKey()) + "\n");
                    }

                    if (alert instanceof DhtReplyAlert) {
                        DhtReplyAlert reply = (DhtReplyAlert) alert;
                        System.out.println("DHT reply alert getUrl() => " + reply.getUrl());
                    }

                    if (alert instanceof DhtImmutableItemAlert) {
                        DhtImmutableItemAlert immutableAlert = (DhtImmutableItemAlert) alert;
                        Entry item = immutableAlert.getItem();
                        System.out.println("DHT Immutable Item Alert (" + immutableAlert.getSwig().getTarget().to_hex() + ") => ");
                        System.out.println("\t\t\t\t" + item);
                    }

                    if (alert instanceof DhtMutableItemAlert) {
                        DhtMutableItemAlert mutableAlert = (DhtMutableItemAlert) alert;
                        Entry item = mutableAlert.getItem();
                        char_vector key_char_vector = mutableAlert.getSwig().key_v();
                        byte[] keyInBytes = new byte[(int) key_char_vector.size()];
                        Vectors.char_vector2bytes(key_char_vector, keyInBytes);
                        System.out.println("DHT Mutable Item Alert (" + toHex(keyInBytes) + ") => ");
                        System.out.println("\t\t\t\t" + item);
                        System.out.println("Seq: " + mutableAlert.getSeq());
                        System.out.println("Signature: " + toHex(mutableAlert.getSignature()));
                    }
                }
            }
        });

        //this would be made probably out of the hashes of your PGP key pair.
        final byte[] seed = new byte[]{ //length Ed25519.SEED_SIZE = 32 bytes
                0x10, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa,
                0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14,
                0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
                0x1e, 0x1f
        };

        //Ed25519.createSeed(seed);

        KeyPair fooKeyPairs = new KeyPair(seed);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("$ ");//prompt
            String line = br.readLine().trim();
            if (line.toLowerCase().startsWith("quit") || line.toLowerCase().startsWith("stop")) {
                s.stopDHT();
                System.out.println("stop DHT");
                s.stopLSD();
                System.out.println("stop LSD");
                s.stopNATPMP();
                System.out.println("stop NATPMP");
                break;
            } else if (line.startsWith("?") || line.toLowerCase().startsWith("help")) {
                System.out.println("Commands: register <name>, check <name>, quit");
            } else if (line.toLowerCase().startsWith("register")) {
                String[] split = line.split(" ");
                if (split.length > 1) {
                    String name = split[1].trim();
                    List<String> servers = new ArrayList<String>();
                    servers.add("127.0.0.1");
                    servers.add("localhost");
                    registerName(s, "foo://" + name, servers, fooKeyPairs);
                }
            } else if (line.toLowerCase().startsWith("put")) {
                String[] split = line.split(" ");
                if (split.length > 1) {
                    String name = split[1].trim();
                    List<String> servers = new ArrayList<String>();
                    servers.add("127.0.0.1");
                    servers.add("localhost");
                    putName(s, "foo://" + name, servers);
                }
            } else if (line.toLowerCase().startsWith("get")) {
                String[] split = line.split(" ");
                if (split.length > 1) {
                    String hex = split[1].trim();
                    getName(s, hex);
                }
            } else if (line.toLowerCase().startsWith("check")) {
                String[] split = line.split(" ");
                if (split.length > 1) {
                    String name = split[1].trim();
                    checkName(s, name, fooKeyPairs);
                }
            } else if (line.toLowerCase().startsWith("upnp")) {
                System.out.println("starting upnp...");
                s.startUPnP();
            } else if (!line.isEmpty()) {
                System.out.println("Invalid command: " + line);
                System.out.println("Try ? for help");
            }
        }
    }

    private static String toHex(byte[] arr) {
        return LibTorrent.toHex(arr);
    }

    /**
     * returns 20 byte array out of the sha1 of the key given
     */
    private static byte[] getDHTKey(String key) {
        return sha1(key);
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

    private static class KeyPair {
        private final byte[] pub = new byte[Ed25519.PUBLIC_KEY_SIZE];
        private final byte[] pri = new byte[Ed25519.PRIVATE_KEY_SIZE];
        private byte[] seed;

        KeyPair(byte[] seed) {
            Ed25519.createKeypair(pub, pri, seed);
            System.out.println("seed    : " + toHex(seed));
            System.out.println("pub key : " + toHex(pub));
            System.out.println("priv key: " + toHex(pri));
            this.seed = seed;
        }

        public byte[] getPrivateKey() {
            return pri;
        }

        public byte[] getPublicKey() {
            return pub;
        }
    }
}
