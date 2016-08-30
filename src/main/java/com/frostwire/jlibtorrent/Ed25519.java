package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.byte_vectors_pair;

import static com.frostwire.jlibtorrent.swig.libtorrent.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Ed25519 {

    public final static int SEED_SIZE = 32;
    public final static int PUBLIC_KEY_SIZE = 32;
    public final static int SECRET_KEY_SIZE = 64;
    public final static int SIGNATURE_SIZE = 64;
    public final static int SCALAR_SIZE = 32;
    public final static int SHARED_SECRET_SIZE = 32;

    private Ed25519() {
    }

    /**
     * @return
     */
    public static byte[] createSeed() {
        byte_vector seed = ed25519_create_seed();

        return Vectors.byte_vector2bytes(seed);
    }

    /**
     * @param seed
     * @return
     */
    public static Pair<byte[], byte[]> createKeypair(byte[] seed) {
        if (seed == null || seed.length != SEED_SIZE) {
            throw new IllegalArgumentException("seed must be not null and of size " + SEED_SIZE);
        }

        byte_vectors_pair keypair = ed25519_create_keypair(Vectors.bytes2byte_vector(seed));

        return new Pair<>(Vectors.byte_vector2bytes(keypair.getFirst()),
                Vectors.byte_vector2bytes(keypair.getSecond()));
    }

    /**
     * @param message
     * @param publicKey
     * @param secretKey
     * @return
     */
    public static byte[] sign(byte[] message, byte[] publicKey, byte[] secretKey) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (secretKey == null || secretKey.length != SECRET_KEY_SIZE) {
            throw new IllegalArgumentException("secret key must be not null and of size " + SECRET_KEY_SIZE);
        }

        byte_vector signature = ed25519_sign(Vectors.bytes2byte_vector(message),
                Vectors.bytes2byte_vector(publicKey),
                Vectors.bytes2byte_vector(secretKey));

        return Vectors.byte_vector2bytes(signature);
    }

    /**
     * @param signature
     * @param message
     * @param publicKey
     * @return
     */
    public static boolean verify(byte[] signature, byte[] message, byte[] publicKey) {
        if (signature == null || signature.length != SIGNATURE_SIZE) {
            throw new IllegalArgumentException("signature must be not null and of size " + SIGNATURE_SIZE);
        }
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        return ed25519_verify(Vectors.bytes2byte_vector(signature),
                Vectors.bytes2byte_vector(message), Vectors.bytes2byte_vector(publicKey));
    }

    /**
     * @param publicKey
     * @param scalar
     * @return
     */
    public static byte[] addScalarPublic(byte[] publicKey, byte[] scalar) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (scalar == null || scalar.length != SCALAR_SIZE) {
            throw new IllegalArgumentException("scalar must be not null and of size " + SCALAR_SIZE);
        }

        byte_vector ret = ed25519_add_scalar_public(Vectors.bytes2byte_vector(publicKey), Vectors.bytes2byte_vector(scalar));

        return Vectors.byte_vector2bytes(ret);
    }

    /**
     * @param secretKey
     * @param scalar
     * @return
     */
    public static byte[] addScalarSecret(byte[] secretKey, byte[] scalar) {
        if (secretKey == null || secretKey.length != SECRET_KEY_SIZE) {
            throw new IllegalArgumentException("public key must be not null and of size " + SECRET_KEY_SIZE);
        }
        if (scalar == null || scalar.length != SCALAR_SIZE) {
            throw new IllegalArgumentException("scalar must be not null and of size " + SCALAR_SIZE);
        }

        byte_vector ret = ed25519_add_scalar_secret(Vectors.bytes2byte_vector(secretKey), Vectors.bytes2byte_vector(scalar));

        return Vectors.byte_vector2bytes(ret);
    }

    /**
     * @param publicKey
     * @param secretKey
     * @return
     */
    public byte[] keyExchange(byte[] publicKey, byte[] secretKey) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (secretKey == null || secretKey.length != SECRET_KEY_SIZE) {
            throw new IllegalArgumentException("private key must be not null and of size " + SECRET_KEY_SIZE);
        }

        byte_vector secret = ed25519_key_exchange(Vectors.bytes2byte_vector(publicKey),
                Vectors.bytes2byte_vector(secretKey));

        return Vectors.byte_vector2bytes(secret);
    }
}
