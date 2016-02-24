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

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        AlertListener mainListener = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.getType();

                if (type == AlertType.LISTEN_SUCCEEDED) {
                    ListenSucceededAlert a = (ListenSucceededAlert) alert;
                    log(a.getMessage());
                }

                if (type == AlertType.LISTEN_FAILED) {
                    ListenFailedAlert a = (ListenFailedAlert) alert;
                    log(a.getMessage());
                }

                if (type == AlertType.DHT_PUT) {
                    DhtPutAlert a = (DhtPutAlert) alert;
                    log(a.getMessage());
                }

                if (alert.getType().equals(AlertType.DHT_STATS)) {
                    DhtStatsAlert a = (DhtStatsAlert) alert;
                    long nodes = a.totalNodes();
                    log("DHT contains " + nodes + " nodes");
                }
            }
        };

        Session s = new Session("0.0.0.0:0,[::]:0", 0, false, mainListener);
        DHT dht = new DHT(s);
        Downloader downloader = new Downloader(s);

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
                put(dht, line);
            } else if (is_get(line)) {
                get(dht, line);
            } else if (is_get_peers(line)) {
                get_peers(dht, line);
            } else if (is_announce(line)) {
                announce(dht, line);
            } else if (is_mkeys(line)) {
                mkeys(line);
            } else if (is_mput(line)) {
                mput(dht, line);
            } else if (is_mget(line)) {
                mget(dht, line);
            } else if (is_magnet(line)) {
                magnet(downloader, line);
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

    private static void quit(Session s) {
        print("Exiting...");
        byte[] data = s.saveState();
        try {
            Utils.writeByteArrayToFile(new File("dht_shell.dat"), data, false);
        } catch (Throwable e) {
            print(e.getMessage());
        }
        s.abort();
        System.exit(0);
    }

    private static boolean is_put(String s) {
        return s.startsWith("put ");
    }

    private static void put(DHT dht, String s) {
        String data = s.split(" ")[1];
        String sha1 = dht.put(new Entry(data)).toString();
        print("Wait for completion of put for key: " + sha1);
    }

    private static boolean is_get(String s) {
        return s.startsWith("get ");
    }

    private static void get(DHT dht, String s) {
        String sha1 = s.split(" ")[1];
        print("Waiting a max of 20 seconds to get data for key: " + sha1);
        Entry data = dht.get(new Sha1Hash(sha1), 20);
        print(data.toString());
    }

    private static boolean is_get_peers(String s) {
        return s.startsWith("get_peers ");
    }

    private static void get_peers(DHT dht, String s) {
        String sha1 = s.split(" ")[1];
        print("Waiting a max of 20 seconds to get peers for key: " + sha1);
        ArrayList<TcpEndpoint> peers = dht.getPeers(new Sha1Hash(sha1), 20);
        print(peers.toString());
    }

    private static boolean is_announce(String s) {
        return s.startsWith("announce ");
    }

    private static void announce(DHT dht, String s) {
        String sha1 = s.split(" ")[1];
        dht.announce(new Sha1Hash(sha1), 9000, 0);
        print("Wait for completion of announce for key: " + sha1);
    }

    private static boolean is_mkeys(String s) {
        return s.startsWith("mkeys");
    }

    private static void mkeys(String s) {
        byte[][] keys = DHT.createKeypair();
        String msg = "Save this key pair\n";
        msg += "Public: " + Utils.toHex(keys[0]) + "\n";
        msg += "Private: " + Utils.toHex(keys[1]) + "\n";
        print(msg);
    }

    private static boolean is_mput(String s) {
        return s.startsWith("mput ");
    }

    private static void mput(DHT dht, String s) {
        String[] arr = s.split(" ");
        byte[] publicKey = Utils.fromHex(arr[1]);
        byte[] privateKey = Utils.fromHex(arr[2]);
        String data = arr[3];
        dht.mput(publicKey, privateKey, new Entry(data), new byte[0]);
        print("Wait for completion of mput for public key: " + arr[1]);
    }

    private static boolean is_mget(String s) {
        return s.startsWith("mget ");
    }

    private static void mget(DHT dht, String s) {
        String[] arr = s.split(" ");
        byte[] publicKey = Utils.fromHex(arr[1]);
        print("Waiting a max of 20 seconds to get mutable data for public key: " + arr[1]);
        DHT.MutableItem data = dht.mget(publicKey, new byte[0], 20);
        print(data.item.toString());
    }

    private static boolean is_magnet(String s) {
        return s.startsWith("magnet ");
    }

    private static void magnet(Downloader downloader, String s) {
        String sha1 = s.split(" ")[1];
        String uri = "magnet:?xt=urn:btih:" + sha1;
        print("Waiting a max of 20 seconds to fetch magnet for sha1: " + sha1);
        byte[] data = downloader.fetchMagnet(uri, 20);
        print(Entry.bdecode(data).toString());
    }

    private static boolean is_count_nodes(String s) {
        return s.startsWith("count_nodes");
    }

    private static void count_nodes(Session s) {
        s.postDHTStats();
    }

    private static boolean is_invalid(String s) {
        return !s.isEmpty();
    }

    private static void invalid(String s) {
        print("Invalid command: " + s + "\n" + "Try ? for help");
    }
}
