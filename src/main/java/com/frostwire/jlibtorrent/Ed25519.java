package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;

import static com.frostwire.jlibtorrent.swig.libtorrent.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Ed25519 {

    public final static int SEED_SIZE = ed25519_seed_size;
    public final static int PRIVATE_KEY_SIZE = ed25519_private_key_size;
    public final static int PUBLIC_KEY_SIZE = ed25519_public_key_size;
    public final static int SIGNATURE_SIZE = ed25519_signature_size;
    public final static int SCALAR_SIZE = ed25519_scalar_size;
    public final static int SHARED_SECRET_SIZE = ed25519_shared_secret_size;

    private Ed25519() {
    }

    public static void createSeed(byte[] seed) {
        if (seed == null || seed.length != SEED_SIZE) {
            throw new IllegalArgumentException("seed buffer must be not null and of size " + SEED_SIZE);
        }
        byte_vector v = Vectors.new_byte_vector(SEED_SIZE);
        //ed25519_create_seed(v);

        Vectors.byte_vector2bytes(v, seed);
    }

    public static void createKeypair(byte[] publicKey, byte[] privateKey, byte[] seed) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key buffer must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (privateKey == null || privateKey.length != PRIVATE_KEY_SIZE) {
            throw new IllegalArgumentException("private key buffer must be not null and of size " + PRIVATE_KEY_SIZE);
        }

        byte_vector v1 = Vectors.new_byte_vector(PUBLIC_KEY_SIZE);
        byte_vector v2 = Vectors.new_byte_vector(PRIVATE_KEY_SIZE);

        //ed25519_create_keypair(v1, v2, Vectors.bytes2byte_vector(seed));

        Vectors.byte_vector2bytes(v1, publicKey);
        Vectors.byte_vector2bytes(v2, privateKey);
    }

    public static void sign(byte[] signature, byte[] message, byte[] publicKey, byte[] privateKey) {
        if (signature == null || signature.length != SIGNATURE_SIZE) {
            throw new IllegalArgumentException("signature buffer must be not null and of size " + SIGNATURE_SIZE);
        }

        byte_vector v1 = Vectors.new_byte_vector(SIGNATURE_SIZE);

        //ed25519_sign(v1, Vectors.bytes2byte_vector(message), Vectors.bytes2byte_vector(publicKey), Vectors.bytes2byte_vector(privateKey));

        Vectors.byte_vector2bytes(v1, signature);
    }

    public static int verify(byte[] signature, byte[] message, byte[] privateKey) {
        return 0;//ed25519_verify(Vectors.bytes2byte_vector(signature), Vectors.bytes2byte_vector(message), Vectors.bytes2byte_vector(privateKey));
    }

    public static void addScalar(byte[] publicKey, byte[] privateKey, byte[] scalar) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key buffer must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (privateKey == null || privateKey.length != PRIVATE_KEY_SIZE) {
            throw new IllegalArgumentException("private key buffer must be not null and of size " + PRIVATE_KEY_SIZE);
        }
        if (scalar == null || scalar.length != SCALAR_SIZE) {
            throw new IllegalArgumentException("scalar must be not null and of size " + SCALAR_SIZE);
        }

        byte_vector v1 = Vectors.bytes2byte_vector(publicKey);
        byte_vector v2 = Vectors.bytes2byte_vector(privateKey);
        byte_vector v3 = Vectors.bytes2byte_vector(scalar);

        //ed25519_add_scalar(v1, v2, v3);

        Vectors.byte_vector2bytes(v1, publicKey);
        Vectors.byte_vector2bytes(v2, privateKey);
    }

    public static void keyExchange(byte[] sharedSecret, byte[] publicKey, byte[] privateKey) {
        if (sharedSecret == null || sharedSecret.length != SHARED_SECRET_SIZE) {
            throw new IllegalArgumentException("shared secret buffer must be not null and of size " + SHARED_SECRET_SIZE);
        }

        byte_vector v1 = Vectors.new_byte_vector(SHARED_SECRET_SIZE);

        //ed25519_key_exchange(v1, Vectors.bytes2byte_vector(publicKey), Vectors.bytes2byte_vector(privateKey));

        Vectors.byte_vector2bytes(v1, sharedSecret);
    }
}
