package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.dht_put_alert;

/**
 * This is posted when a DHT put operation completes. This is useful if the
 * client is waiting for a put to complete before shutting down for instance.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtPutAlert extends AbstractAlert<dht_put_alert> {

    public DhtPutAlert(dht_put_alert alert) {
        super(alert);
    }

    /**
     * The target hash the item was stored under if this was an *immutable*
     * item.
     *
     * @return
     */
    public Sha1Hash getTarget() {
        return new Sha1Hash(alert.getTarget());
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public byte[] getPublicKey() {
        return Vectors.char_vector2bytes(alert.public_key_v());
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public byte[] getSignature() {
        return Vectors.char_vector2bytes(alert.signature_v());
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public String getSalt() {
        return alert.getSalt();
    }

    /**
     * if a mutable item was stored, these are the public key, signature,
     * salt and sequence number the item was stored under.
     *
     * @return
     */
    public long getSeq() {
        return alert.getSeq().longValue();
    }
}
