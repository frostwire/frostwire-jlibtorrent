package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.byte_array_32;
import com.frostwire.jlibtorrent.swig.byte_array_64;
import com.frostwire.jlibtorrent.swig.dht_put_alert;

/**
 * This is posted when a DHT put operation completes. This is useful if the
 * client is waiting for a put to complete before shutting down for instance.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtPutAlert extends AbstractAlert<dht_put_alert> {

    DhtPutAlert(dht_put_alert alert) {
        super(alert);
    }

    /**
     * The target hash the item was stored under if this was an *immutable*
     * item.
     *
     * @return
     */
    public Sha1Hash target() {
        return new Sha1Hash(alert.getTarget());
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public byte[] publicKey() {
        byte_array_32 publicKey = alert.get_public_key();
        return Vectors.byte_array2bytes(publicKey);
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public byte[] signature() {
        byte_array_64 signature = alert.get_signature();
        return Vectors.byte_array2bytes(signature);
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public byte[] salt() {
        return Vectors.byte_vector2bytes(alert.get_salt());
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public long seq() {
        return alert.get_seq();
    }
}
