package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtShell {

    public static void main(String[] args) throws Throwable {

        AlertListener mainListener = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                if (type == AlertType.LISTEN_SUCCEEDED) {
                    ListenSucceededAlert a = (ListenSucceededAlert) alert;
                    log(a.message());
                }

                if (type == AlertType.LISTEN_FAILED) {
                    ListenFailedAlert a = (ListenFailedAlert) alert;
                    log(a.message());
                }

                if (type == AlertType.DHT_PUT) {
                    DhtPutAlert a = (DhtPutAlert) alert;
                    log(a.message());
                }
            }
        };

        SessionManager s = new SessionManager();
        s.addListener(mainListener);

        try {
            File f = new File("dht_shell.dat");
            if (f.exists()) {
                byte[] data = Utils.readFileToByteArray(f);
                s.loadState(data);
            }
        } catch (Throwable e) {
            log(e.getMessage());
        }

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");

            String line = in.nextLine().trim();

            if (is_quit(line)) {
                quit(s);
            } else if (is_put(line)) {
                put(s, line);
            } else if (is_get(line)) {
                get(s, line);
            } else if (is_get_peers(line)) {
                get_peers(s, line);
            } else if (is_announce(line)) {
                announce(s, line);
            } else if (is_mkeys(line)) {
                mkeys(line);
            } else if (is_mput(line)) {
                mput(s, line);
            } else if (is_mget(line)) {
                mget(s, line);
            } else if (is_magnet(line)) {
                magnet(s, line);
            } else if (is_count_nodes(line)) {
                count_nodes(s);
            } else if (is_invalid(line)) {
                invalid(line);
            }
        }
    }

    private static void print(String s, boolean dollar) {
        System.out.println();
        System.out.println(s);
        if (dollar) {
            System.out.print("$ ");
        }
    }

    private static void print(String s) {
        print(s, false);
    }

    private static void log(String s) {
        print(s, true);
    }

    private static boolean is_quit(String s) {
        s = s.split(" ")[0];
        return s.equals("quit") || s.equals("exit") || s.equals("stop");
    }

    private static void quit(SessionManager s) {
        print("Exiting...");
        byte[] data = s.saveState();
        try {
            Utils.writeByteArrayToFile(new File("dht_shell.dat"), data, false);
        } catch (Throwable e) {
            print(e.getMessage());
        }
        s.stop();
        System.exit(0);
    }

    private static boolean is_put(String s) {
        return s.startsWith("put ");
    }

    private static void put(SessionManager sm, String s) {
        String data = s.split(" ")[1];
        String sha1 = sm.dhtPutItem(new Entry(data)).toString();
        print("Wait for completion of put for key: " + sha1);
    }

    private static boolean is_get(String s) {
        return s.startsWith("get ");
    }

    private static void get(SessionManager sm, String s) {
        String sha1 = s.split(" ")[1];
        print("Waiting a max of 20 seconds to get data for key: " + sha1);
        Entry data = sm.dhtGetItem(new Sha1Hash(sha1), 20);
        print(data.toString());
    }

    private static boolean is_get_peers(String s) {
        return s.startsWith("get_peers ");
    }

    private static void get_peers(SessionManager sm, String s) {
        String sha1 = s.split(" ")[1];
        print("Waiting a max of 20 seconds to get peers for key: " + sha1);
        ArrayList<TcpEndpoint> peers = sm.dhtGetPeers(new Sha1Hash(sha1), 20);
        print(peers.toString());
    }

    private static boolean is_announce(String s) {
        return s.startsWith("announce ");
    }

    private static void announce(SessionManager sm, String s) {
        String sha1 = s.split(" ")[1];
        sm.dhtAnnounce(new Sha1Hash(sha1), 9000, 0);
        print("Wait for completion of announce for key: " + sha1);
    }

    private static boolean is_mkeys(String s) {
        return s.startsWith("mkeys");
    }

    private static void mkeys(String s) {
        byte[] seed = Ed25519.createSeed();

        Pair<byte[], byte[]> keypair = Ed25519.createKeypair(seed);
        byte[] publicKey = keypair.first;
        byte[] privateKey = keypair.second;


        byte[][] keys = new byte[2][];
        keys[0] = publicKey;
        keys[1] = privateKey;

        String msg = "Save this key pair\n";
        msg += "Public: " + Utils.toHex(keys[0]) + "\n";
        msg += "Private: " + Utils.toHex(keys[1]) + "\n";
        print(msg);
    }

    private static boolean is_mput(String s) {
        return s.startsWith("mput ");
    }

    private static void mput(SessionManager sm, String s) {
        String[] arr = s.split(" ");
        byte[] publicKey = Utils.fromHex(arr[1]);
        byte[] privateKey = Utils.fromHex(arr[2]);
        String data = arr[3];
        sm.dhtPutItem(publicKey, privateKey, new Entry(data), new byte[0]);
        print("Wait for completion of mput for public key: " + arr[1]);
    }

    private static boolean is_mget(String s) {
        return s.startsWith("mget ");
    }

    private static void mget(SessionManager sm, String s) {
        String[] arr = s.split(" ");
        byte[] publicKey = Utils.fromHex(arr[1]);
        print("Waiting a max of 20 seconds to get mutable data for public key: " + arr[1]);
        SessionManager.MutableItem data = sm.dhtGetItem(publicKey, new byte[0], 20);
        print(data.item.toString());
    }

    private static boolean is_magnet(String s) {
        return s.startsWith("magnet ");
    }

    private static void magnet(SessionManager session, String s) {
        String sha1 = s.split(" ")[1];
        String uri = "magnet:?xt=urn:btih:" + sha1;
        print("Waiting a max of 20 seconds to fetch magnet for sha1: " + sha1);
        byte[] data = session.fetchMagnet(uri, 20);
        print(Entry.bdecode(data).toString());
    }

    private static boolean is_count_nodes(String s) {
        return s.startsWith("count_nodes");
    }

    private static void count_nodes(SessionManager s) {
        log("DHT contains " + s.stats().dhtNodes() + " nodes");
    }

    private static boolean is_invalid(String s) {
        return !s.isEmpty();
    }

    private static void invalid(String s) {
        print("Invalid command: " + s + "\n" + "Try ? for help");
    }
}
