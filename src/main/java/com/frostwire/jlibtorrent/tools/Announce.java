package com.frostwire.jlibtorrent.tools;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Announce extends Tool<Void> {

    private static final int[] ALERT_TYPES = new int[]{
            AlertType.DHT_GET_PEERS_REPLY_ALERT.getSwig()
    };

    private final Session s;

    public Announce(Session s, String[] args) {
        super(args);
        this.s = s;

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return ALERT_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.toString());
                if (alert instanceof DhtGetPeersReplyAlert) {
                    ArrayList<TcpEndpoint> peers = ((DhtGetPeersReplyAlert) alert).peers();
                    for (int i = 0; i < peers.size(); i++) {
                        System.out.println(peers.get(i));
                    }
                }
            }
        });
    }

    public Announce(String[] args) {
        this(new Session(), args);
    }

    @Override
    protected String usage() {
        return "usage: -sha1 <hash> -port <port>";
    }

    @Override
    protected ParseCmd parser(ParseCmd.Builder b) {
        return b.parm("-sha1", "<hash>").req().rex(".*")
                .parm("-port", "<port>").rex(".*")
                .build();
    }

    @Override
    public Void run() {
        System.out.println("Waiting 10 seconds for DHT nodes...");
        sleep(10);

        Sha1Hash sha1 = new Sha1Hash(arg("-sha1"));
        int port = Integer.parseInt(arg("-port"));

        while (true) {

            s.dhtAnnounce(sha1, port, 0);
            System.out.println("Sent announce, waiting 10 seconds for get_peers");
            sleep(10);

            s.dhtGetPeers(sha1);
            System.out.println("Sent get_peers, waiting 10 seconds for re-announce");
            sleep(10);
        }
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Announce t = new Announce(args);

        t.run();
    }
}
