package com.frostwire.jlibtorrent.demo;


import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AnnounceTest {
    public static void main(String[] args) {
        final Session s = new Session();

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{
                        AlertType.DHT_GET_PEERS.getSwig(),
                        AlertType.DHT_GET_PEERS_REPLY_ALERT.getSwig(),
                        AlertType.DHT_BOOTSTRAP.getSwig()
                };
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.toString());
            }
        });

        final DHT dht = new DHT(s);

        System.out.println("DHT waiting for nodes...");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Get peers...");
        dht.getPeers("849087ba9a7abcbec6b96133de41ef76ff264cda");

        System.out.println("");
        //dht.waitNodes(1);


        System.out.println("[Wait for DHT_BOOTSTRAP and press any key to continue]");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Announcing...");
        dht.announce("849087ba9a7abcbec6b96133de41ef76ff264cda", 9999, 0);

        System.out.println("[Press any key to stop waiting for alerts...]");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Get peers...");
        dht.getPeers("849087ba9a7abcbec6b96133de41ef76ff264cda");

        System.out.println("[Press any key to stop waiting for alerts...]");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
