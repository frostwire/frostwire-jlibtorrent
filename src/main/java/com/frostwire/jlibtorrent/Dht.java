package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;
import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.alerts.DhtMutableItemAlert;
import com.frostwire.jlibtorrent.swig.settings_pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.frostwire.jlibtorrent.alerts.AlertType.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Dht {

    private static final int[] DHT_IMMUTABLE_ITEM_TYPES = {DHT_IMMUTABLE_ITEM.swig()};
    private static final int[] DHT_MUTABLE_ITEM_TYPES = {DHT_MUTABLE_ITEM.swig()};
    private static final int[] DHT_GET_PEERS_REPLY_ALERT_TYPES = {DHT_GET_PEERS_REPLY.swig()};

    private final Session s;

    public Dht(Session s) {
        this.s = s;
    }

    public void start() {
        toggleDHT(true);
    }

    public void stop() {
        toggleDHT(false);
    }

    public boolean running() {
        return s.isDHTRunning();
    }

    /**
     * @param sha1
     * @param timeout in seconds
     * @return
     */
    public Entry get(Sha1Hash sha1, int timeout) {
        final Sha1Hash target = sha1;
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

        //s.addListener(l);

        s.dhtGetItem(target);

        try {
            signal.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }

        //s.removeListener(l);

        return result[0];
    }

    public Sha1Hash put(Entry entry) {
        return s.dhtPutItem(entry);
    }

    /**
     * @param sha1
     * @param timeout in seconds
     * @return
     */
    public ArrayList<TcpEndpoint> getPeers(Sha1Hash sha1, int timeout) {
        final Sha1Hash target = sha1;
        final ArrayList<TcpEndpoint> result = new ArrayList<>();
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
                    if (target.equals(replyAlert.infoHash())) {
                        result.addAll(replyAlert.peers());
                        signal.countDown();
                    }
                }
            }
        };

        //s.addListener(l);

        s.dhtGetPeers(target);

        try {
            signal.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }

        //s.removeListener(l);

        return result;
    }

    public void announce(Sha1Hash sha1, int port, int flags) {
        s.dhtAnnounce(sha1, port, flags);
    }

    public void announce(Sha1Hash sha1) {
        s.dhtAnnounce(sha1);
    }

    public MutableItem mget(final byte[] key, final byte[] salt, int timeout) {
        final MutableItem[] result = {null};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener l = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_MUTABLE_ITEM_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof DhtMutableItemAlert) {
                    DhtMutableItemAlert itemAlert = (DhtMutableItemAlert) alert;
                    boolean sameKey = Arrays.equals(key, itemAlert.key());
                    boolean sameSalt = Arrays.equals(salt, itemAlert.salt());
                    if (sameKey && sameSalt) {
                        MutableItem item = new MutableItem(itemAlert.item(),
                                itemAlert.signature(), itemAlert.seq());
                        result[0] = item;
                        signal.countDown();
                    }
                }
            }
        };

        //s.addListener(l);

        s.dhtGetItem(key, salt);

        try {
            signal.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }

        //s.removeListener(l);

        return result[0];
    }

    public void mput(byte[] publicKey, byte[] privateKey, Entry entry, byte[] salt) {
        s.dhtPutItem(publicKey, privateKey, entry, salt);
    }

    public static byte[][] createKeypair() {
        byte[] seed = new byte[Ed25519.SEED_SIZE];
        Ed25519.createSeed(seed);

        byte[] publicKey = new byte[Ed25519.PUBLIC_KEY_SIZE];
        byte[] privateKey = new byte[Ed25519.PRIVATE_KEY_SIZE];

        Ed25519.createKeypair(publicKey, privateKey, seed);

        byte[][] keys = new byte[2][];
        keys[0] = publicKey;
        keys[1] = privateKey;

        return keys;
    }

    private void toggleDHT(boolean on) {
        SettingsPack pack = new SettingsPack();
        pack.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), on);
        s.applySettings(pack);
    }

    public static final class MutableItem {

        private MutableItem(Entry item, byte[] signature, long seq) {
            this.item = item;
            this.signature = signature;
            this.seq = seq;
        }

        public final Entry item;
        public final byte[] signature;
        public final long seq;
    }
}
