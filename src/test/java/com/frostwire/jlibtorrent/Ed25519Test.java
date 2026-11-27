package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author gubatron
 * @author aldenml
 */
public class Ed25519Test {

    private static final byte[] MESSAGE = "a message to sign".getBytes(StandardCharsets.UTF_8);

    private static Pair<byte[], byte[]> keypair() {
        return Ed25519.createKeypair(Ed25519.createSeed());
    }

    @Test
    public void seedsAreRandomAndSized() {
        byte[] a = Ed25519.createSeed();
        byte[] b = Ed25519.createSeed();
        assertEquals(Ed25519.SEED_SIZE, a.length);
        assertFalse(Arrays.equals(a, b));
    }

    @Test
    public void keypairIsDeterministicForASeed() {
        byte[] seed = Ed25519.createSeed();
        Pair<byte[], byte[]> k1 = Ed25519.createKeypair(seed);
        Pair<byte[], byte[]> k2 = Ed25519.createKeypair(seed);
        assertEquals(Ed25519.PUBLIC_KEY_SIZE, k1.first.length);
        assertEquals(Ed25519.SECRET_KEY_SIZE, k1.second.length);
        assertArrayEquals(k1.first, k2.first);
        assertArrayEquals(k1.second, k2.second);
    }

    @Test
    public void signaturesVerifyOnlyForTheSignedMessageAndKey() {
        Pair<byte[], byte[]> k = keypair();
        byte[] sig = Ed25519.sign(MESSAGE, k.first, k.second);
        assertEquals(Ed25519.SIGNATURE_SIZE, sig.length);
        assertTrue(Ed25519.verify(sig, MESSAGE, k.first));
        assertFalse(Ed25519.verify(sig, "another message".getBytes(StandardCharsets.UTF_8), k.first));
        assertFalse(Ed25519.verify(sig, MESSAGE, keypair().first));
    }

    @Test
    public void addingAScalarKeepsThePairConsistent() {
        Pair<byte[], byte[]> k = keypair();
        byte[] scalar = new byte[Ed25519.SCALAR_SIZE];
        scalar[0] = 5;
        byte[] pub = Ed25519.addScalarPublic(k.first, scalar);
        byte[] sec = Ed25519.addScalarSecret(k.second, scalar);
        assertEquals(Ed25519.PUBLIC_KEY_SIZE, pub.length);
        assertEquals(Ed25519.SECRET_KEY_SIZE, sec.length);
        assertFalse(Arrays.equals(k.first, pub));

        byte[] sig = Ed25519.sign(MESSAGE, pub, sec);
        assertTrue(Ed25519.verify(sig, MESSAGE, pub));
    }

    @Test
    public void keyExchangeAgreesOnASharedSecret() {
        Pair<byte[], byte[]> a = keypair();
        Pair<byte[], byte[]> b = keypair();
        Ed25519 ed = instance();
        byte[] s1 = ed.keyExchange(b.first, a.second);
        byte[] s2 = ed.keyExchange(a.first, b.second);
        assertEquals(Ed25519.SHARED_SECRET_SIZE, s1.length);
        assertArrayEquals(s1, s2);
        assertNotEquals(0, s1.length);
    }

    @Test
    public void badSizesAreRejected() {
        Pair<byte[], byte[]> k = keypair();
        byte[] sig = Ed25519.sign(MESSAGE, k.first, k.second);
        byte[] scalar = new byte[Ed25519.SCALAR_SIZE];
        Ed25519 ed = instance();

        expectIae(() -> Ed25519.createKeypair(null));
        expectIae(() -> Ed25519.createKeypair(new byte[31]));
        expectIae(() -> Ed25519.sign(MESSAGE, null, k.second));
        expectIae(() -> Ed25519.sign(MESSAGE, k.first, new byte[63]));
        expectIae(() -> Ed25519.verify(null, MESSAGE, k.first));
        expectIae(() -> Ed25519.verify(sig, MESSAGE, new byte[31]));
        expectIae(() -> Ed25519.addScalarPublic(null, scalar));
        expectIae(() -> Ed25519.addScalarPublic(k.first, new byte[31]));
        expectIae(() -> Ed25519.addScalarSecret(new byte[10], scalar));
        expectIae(() -> Ed25519.addScalarSecret(k.second, null));
        expectIae(() -> ed.keyExchange(null, k.second));
        expectIae(() -> ed.keyExchange(k.first, new byte[10]));
    }

    private static void expectIae(Runnable r) {
        try {
            r.run();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    /**
     * The constructor is private and {@code keyExchange} is an instance method, so the public API
     * can't reach it: reflection is the only way in (see the bug report on Ed25519.keyExchange).
     */
    private static Ed25519 instance() {
        try {
            java.lang.reflect.Constructor<Ed25519> c = Ed25519.class.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
