package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.dht_mutable_item_alert;

/**
 * This alert is posted as a response to a call to session::get_item(),
 * specifically the overload for looking up mutable items in the DHT.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtMutableItemAlert extends AbstractAlert<dht_mutable_item_alert> {

    public DhtMutableItemAlert(dht_mutable_item_alert alert) {
        super(alert);
    }

    /**
     * The public key that was looked up.
     *
     * @return
     */
    public byte[] getKey() {
        return Vectors.char_vector2bytes(alert.key_v());
    }

    /**
     * The signature of the data. This is not the signature of the
     * plain encoded form of the item, but it includes the sequence number
     * and possibly the hash as well. See the dht_store document for more
     * information. This is primarily useful for echoing back in a store
     * request.
     *
     * @return
     */
    public byte[] getSignature() {
        return Vectors.char_vector2bytes(alert.signature_v());
    }

    /**
     * The sequence number of this item.
     *
     * @return
     */
    public long getSeq() {
        return alert.getSeq().longValue();
    }

    /**
     * The salf, if any, used to lookup and store this item. If no
     * salt was used, this is an empty string.
     *
     * @return
     */
    public String getSalt() {
        return alert.getSalt();
    }

    /**
     * The data for this item.
     *
     * @return
     */
    public Entry getItem() {
        return new Entry(alert.getItem());
    }
}
