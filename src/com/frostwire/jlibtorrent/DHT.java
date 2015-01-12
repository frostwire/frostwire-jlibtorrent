package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;
import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.dht_item;
import com.frostwire.jlibtorrent.swig.sha1_hash;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.frostwire.jlibtorrent.alerts.AlertType.DHT_GET_PEERS_REPLY_ALERT;
import static com.frostwire.jlibtorrent.alerts.AlertType.DHT_IMMUTABLE_ITEM;

/**
 * This class provides a lens only functionality.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DHT {

    private static final int[] DHT_IMMUTABLE_ITEM_TYPES = {DHT_IMMUTABLE_ITEM.getSwig()};
    private static final int[] DHT_GET_PEERS_REPLY_ALERT_TYPES = {DHT_GET_PEERS_REPLY_ALERT.getSwig()};

    private final Session s;

    public DHT(Session s) {
        this.s = s;
    }

    public void start() {
        s.startDHT();
    }

    public void stop() {
        s.stopDHT();
    }

    public boolean isRunning() {
        return s.isDHTRunning();
    }

    public void waitNodes(int nodes) {
        boolean ready = false;
        while (!ready) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }

            ready = s.getStatus().getDHTNodes() > nodes;
        }
    }

    public int nodes() {
        return s.getStatus().getDHTNodes();
    }

    public void get(String sha1) {
        s.dhtGetItem(new Sha1Hash(sha1));
    }

    public Entry get(String sha1, long timeout, TimeUnit unit) {
        final Sha1Hash target = new Sha1Hash(sha1);
        final Entry[] result = {null};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener l = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_IMMUTABLE_ITEM_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof DhtImmutableItemAlert) {
                    DhtImmutableItemAlert itemAlert = (DhtImmutableItemAlert) alert;
                    if (target.equals(itemAlert.getTarget())) {
                        result[0] = itemAlert.getItem();
                        signal.countDown();
                    }
                }
            }
        };

        s.addListener(l);

        s.dhtGetItem(target);

        try {
            signal.await(timeout, unit);
        } catch (InterruptedException e) {
            // ignore
        }

        s.removeListener(l);

        return result[0];
    }

    public String put(Entry entry) {
        return s.dhtPutItem(entry).toString();
    }

    public void getPeers(String sha1) {
        s.dhtGetPeers(new Sha1Hash(sha1));
    }

    public ArrayList<TcpEndpoint> getPeers(String sha1, long timeout, TimeUnit unit) {
        final Sha1Hash target = new Sha1Hash(sha1);
        final Object[] result = {new ArrayList<TcpEndpoint>()};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener l = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_GET_PEERS_REPLY_ALERT_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof DhtGetPeersReplyAlert) {
                    DhtGetPeersReplyAlert replyAlert = (DhtGetPeersReplyAlert) alert;
                    if (target.equals(replyAlert.getInfoHash())) {
                        result[0] = replyAlert.getPeers();
                        signal.countDown();
                    }
                }
            }
        };

        s.addListener(l);

        s.dhtGetPeers(target);

        try {
            signal.await(timeout, unit);
        } catch (InterruptedException e) {
            // ignore
        }

        s.removeListener(l);

        return (ArrayList<TcpEndpoint>) result[0];
    }

    public void announce(String sha1, int port, int flags) {
        s.dhtAnnounce(new Sha1Hash(sha1), port, flags);
    }

    public void announce(String sha1) {
        s.dhtAnnounce(new Sha1Hash(sha1));
    }

    /**
     * calculate the target hash for an immutable item.
     *
     * @param e
     * @return
     */
    public static Sha1Hash itemTargetId(Entry e) {
        return new Sha1Hash(dht_item.item_target_id(e.getSwig().bencode()));
    }

    /**
     * calculate the target hash for a mutable item.
     *
     * @param salt
     * @param pk
     * @return
     */
    public static Sha1Hash itemTargetId(String salt, byte[] pk) {
        sha1_hash h = dht_item.item_target_id(Vectors.string2char_vector(salt), Vectors.bytes2char_vector(pk));
        return new Sha1Hash(h);
    }

    /**
     * Given a byte range ``v`` and an optional byte range ``salt``, a
     * sequence number, public key ``pk`` (must be 32 bytes) and a secret key
     * ``sk`` (must be 64 bytes), this function produces a signature which
     * is written into a 64 byte buffer pointed to by ``sig``. The caller
     * is responsible for allocating the destination buffer that's passed in
     * as the ``sig`` argument. Typically it would be allocated on the stack.
     *
     * @param e
     * @param salt
     * @param seq
     * @param pk
     * @param sk
     * @param sig
     */
    public static void signMutableItem(Entry e, String salt, int seq, byte[] pk, byte[] sk, byte[] sig) {
        if (sig == null || sig.length != Ed25519.SIGNATURE_SIZE) {
            throw new IllegalArgumentException("The signature array must be a valid one with length " + Ed25519.SIGNATURE_SIZE);
        }

        char_vector sig_v = Vectors.new_char_vector(Ed25519.SIGNATURE_SIZE);

        dht_item.sign_mutable_item(e.getSwig().bencode(), salt, seq,
                Vectors.bytes2char_vector(pk), Vectors.bytes2char_vector(sk), sig_v);

        Vectors.char_vector2bytes(sig_v, sig);
    }

    public static boolean verifyMutableItem(Entry e, String salt, int seq, byte[] pk, byte[] sig) {
        return dht_item.verify_mutable_item(e.getSwig().bencode(), salt, seq,
                Vectors.bytes2char_vector(pk),
                Vectors.bytes2char_vector(sig));
    }

    public static int canonicalString(Entry e, int seq, String salt, byte[] out) {
        if (out == null || out.length != 1200) {
            throw new IllegalArgumentException("The out array must be a valid one with length 1200");
        }

        char_vector out_v = Vectors.new_char_vector(1200);
        int r = dht_item.canonical_string(e.getSwig().bencode(),
                seq, salt, out_v);

        Vectors.char_vector2bytes(out_v, out);

        return r;
    }
}
