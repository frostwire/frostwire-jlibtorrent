package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_array_32;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.byte_vector_byte_vector_pair;

import static com.frostwire.jlibtorrent.swig.libtorrent.*;

/**
 * Ed25519 elliptic curve cryptography for DHT and security operations.
 * <p>
 * {@code Ed25519} provides a modern elliptic curve public-key signature system used in BitTorrent
 * for DHT node identification, secure peer communication, and data integrity. Ed25519 is a
 * variant of the Twisted Edwards curve offering better security and performance than RSA or
 * traditional ECDSA, with signatures and keys only 32-64 bytes.
 * <p>
 * <b>Understanding Ed25519:</b>
 * <ul>
 *   <li><b>Public Key (32 bytes):</b> Shared openly, verifies signatures and receives encrypted messages</li>
 *   <li><b>Secret Key (64 bytes):</b> Private key kept secret, used for signing and decryption</li>
 *   <li><b>Signature (64 bytes):</b> Proof that a message was signed with a specific secret key</li>
 *   <li><b>Seed (32 bytes):</b> Random data that generates a keypair (deterministic)</li>
 *   <li><b>Scalar (32 bytes):</b> Parameter for key tweaking operations</li>
 * </ul>
 * <p>
 * <b>Key Generation:</b>
 * <pre>
 * // Create a new random seed
 * byte[] seed = Ed25519.createSeed();  // 32 random bytes
 *
 * // Generate keypair from seed
 * Pair&lt;byte[], byte[]&gt; keypair = Ed25519.createKeypair(seed);
 * byte[] publicKey = keypair.first;    // 32 bytes - can be shared
 * byte[] secretKey = keypair.second;   // 64 bytes - keep private
 *
 * System.out.println("Public key: " + Hex.encode(publicKey));
 * System.out.println("Secret key: " + Hex.encode(secretKey));
 * </pre>
 * <p>
 * <b>Digital Signatures:</b>
 * <pre>
 * // Sign a message
 * byte[] message = "Important data".getBytes();
 * byte[] signature = Ed25519.sign(message, publicKey, secretKey);
 *
 * // Verify signature (can be done with public key only)
 * boolean isValid = Ed25519.verify(signature, message, publicKey);
 *
 * if (isValid) {
 *     System.out.println("Signature is valid - message is authentic");
 * } else {
 *     System.out.println("Signature is invalid - message was tampered with or wrong key");
 * }
 *
 * // Verification fails if message, signature, or key is changed
 * byte[] tamperedMessage = "Modified data".getBytes();
 * Ed25519.verify(signature, tamperedMessage, publicKey);  // false
 * </pre>
 * <p>
 * <b>Typical Workflow - DHT Node Identity:</b>
 * <pre>
 * // Node setup (done once, saved for later)
 * byte[] nodeSeed = Ed25519.createSeed();
 * Pair&lt;byte[], byte[]&gt; nodeKeys = Ed25519.createKeypair(nodeSeed);
 * byte[] nodePublicKey = nodeKeys.first;
 * byte[] nodeSecretKey = nodeKeys.second;
 *
 * // Save these to disk for node persistence
 * // On startup, load the same nodePublicKey and nodeSecretKey
 *
 * // Later: Sign DHT messages
 * byte[] dhtMessage = constructDhtMessage(...);
 * byte[] messageSignature = Ed25519.sign(dhtMessage, nodePublicKey, nodeSecretKey);
 *
 * // Peers verify our identity using nodePublicKey
 * // They can verify without knowing nodeSecretKey
 * </pre>
 * <p>
 * <b>Key Tweaking Operations:</b>
 * <pre>
 * // Scalar operations for advanced key derivation
 * byte[] scalar = ...; // 32-byte scalar parameter
 *
 * // Add scalar to public key (for key derivation)
 * byte[] derivedPublic = Ed25519.addScalarPublic(publicKey, scalar);
 *
 * // Add scalar to secret key (for key derivation)
 * byte[] derivedSecret = Ed25519.addScalarSecret(secretKey, scalar);
 * </pre>
 * <p>
 * <b>Elliptic Curve Diffie-Hellman (ECDH):</b>
 * <pre>
 * // Key exchange to establish shared secret
 * byte[] peerPublicKey = receivePeerPublicKey();
 *
 * // Both parties can compute the same shared secret
 * byte[] sharedSecret = Ed25519.keyExchange(peerPublicKey, localSecretKey);
 *
 * // Peer does: Ed25519.keyExchange(localPublicKey, peerSecretKey)
 * // Result should be identical (symmetric)
 *
 * // Shared secret can be used as encryption key for further communication
 * </pre>
 * <p>
 * <b>Size Reference:</b>
 * <table border="1">
 * <caption>Ed25519 Cryptography Key Information</caption>
 *   <tr><th>Component</th><th>Size (bytes)</th><th>Bits</th><th>Usage</th></tr>
 *   <tr><td>Seed</td><td>32</td><td>256</td><td>Random data for key generation</td></tr>
 *   <tr><td>Public Key</td><td>32</td><td>256</td><td>Shared openly, for verification/encryption</td></tr>
 *   <tr><td>Secret Key</td><td>64</td><td>512</td><td>Private, for signing/decryption</td></tr>
 *   <tr><td>Signature</td><td>64</td><td>512</td><td>Proof of signing with secret key</td></tr>
 *   <tr><td>Scalar</td><td>32</td><td>256</td><td>Parameter for key operations</td></tr>
 *   <tr><td>Shared Secret</td><td>32</td><td>256</td><td>Symmetric key from ECDH</td></tr>
 * </table>
 * <p>
 * <b>Security Properties:</b>
 * <ul>
 *   <li>256-bit security level (pre-quantum)</li>
 *   <li>Deterministic signatures (no randomness needed)</li>
 *   <li>Immune to side-channel timing attacks</li>
 *   <li>Small key size (32 bytes public key)</li>
 *   <li>Fast signing and verification</li>
 *   <li>Used by Signal, WireGuard, and modern protocols</li>
 * </ul>
 * <p>
 * <b>BitTorrent Uses:</b>
 * <ul>
 *   <li><b>DHT v2 Node IDs:</b> Ed25519 public keys identify nodes (replaces 20-byte SHA-1 IDs)</li>
 *   <li><b>Mutable Items:</b> Signed DHT items require Ed25519 signatures</li>
 *   <li><b>Peer Authentication:</b> Secure peer verification without certificates</li>
 *   <li><b>Protocol Encryption:</b> Key exchange for encrypted peer connections</li>
 * </ul>
 *
 * @see PortmapTransport - For complementary network configuration
 *
 * @author gubatron
 * @author aldenml
 */
public final class Ed25519 {

    /**
     * Size of the random seed for key generation: 32 bytes.
     */
    public final static int SEED_SIZE = 32;

    /**
     * Size of public key: 32 bytes. This is shared openly.
     */
    public final static int PUBLIC_KEY_SIZE = 32;

    /**
     * Size of secret (private) key: 64 bytes. Keep this private.
     */
    public final static int SECRET_KEY_SIZE = 64;

    /**
     * Size of signature: 64 bytes. Proof of signing with secret key.
     */
    public final static int SIGNATURE_SIZE = 64;

    /**
     * Size of scalar parameter for key tweaking: 32 bytes.
     */
    public final static int SCALAR_SIZE = 32;

    /**
     * Size of shared secret from key exchange: 32 bytes.
     */
    @SuppressWarnings("unused")
    public final static int SHARED_SECRET_SIZE = 32;

    private Ed25519() {
    }

    public static byte[] createSeed() {
        byte_array_32 seed = ed25519_create_seed();
        return Vectors.byte_array2bytes(seed);
    }

    public static Pair<byte[], byte[]> createKeypair(byte[] seed) {
        if (seed == null || seed.length != SEED_SIZE) {
            throw new IllegalArgumentException("seed must be not null and of size " + SEED_SIZE);
        }

        byte_vector_byte_vector_pair keypair = ed25519_create_keypair(Vectors.bytes2byte_vector(seed));

        return new Pair<>(Vectors.byte_vector2bytes(keypair.getFirst()),
                Vectors.byte_vector2bytes(keypair.getSecond()));
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
