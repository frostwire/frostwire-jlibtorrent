package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.ed25519;
import com.frostwire.jlibtorrent.swig.unsigned_char_vector;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Ed25519 {

    public final static int SEED_SIZE = ed25519.seed_size;
    public final static int PRIVATE_KEY_SIZE = ed25519.private_key_size;
    public final static int PUBLIC_KEY_SIZE = ed25519.public_key_size;
    public final static int SIGNATURE_SIZE = ed25519.signature_size;
    public final static int SCALAR_SIZE = ed25519.scalar_size;
    public final static int SHARED_SECRET_SIZE = ed25519.shared_secret_size;

    private Ed25519() {
    }

    public static int createSeed(short[] seed) {
        if (seed == null || seed.length != SEED_SIZE) {
            throw new IllegalArgumentException("seed buffer must be not null and of size " + SEED_SIZE);
        }
        unsigned_char_vector v = Vectors.new_unsigned_char_vector(SEED_SIZE);
        int r = ed25519.create_seed(v);

        for (int i = 0; i < SEED_SIZE; i++) {
            seed[i] = v.get(i);
        }

        return r;
    }
}
