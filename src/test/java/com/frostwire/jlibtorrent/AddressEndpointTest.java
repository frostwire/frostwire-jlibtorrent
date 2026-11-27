package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author gubatron
 * @author aldenml
 */
public class AddressEndpointTest {

    @Test
    public void addressKinds() {
        Address v4 = new Address("127.0.0.1");
        assertTrue(v4.isV4());
        assertFalse(v4.isV6());
        assertTrue(v4.isLoopback());
        assertFalse(v4.isMulticast());
        assertFalse(v4.isUnspecified());
        assertEquals("127.0.0.1", v4.toString());
        assertNotNull(v4.swig());

        Address v6 = new Address("::1");
        assertTrue(v6.isV6());
        assertTrue(v6.isLoopback());

        assertTrue(new Address("224.0.0.1").isMulticast());
        assertTrue(new Address().isUnspecified());
        assertTrue(new Address("0.0.0.0").isUnspecified());
    }

    @Test
    public void addressOrderingAndCloning() {
        Address a = new Address("10.0.0.1");
        Address b = new Address("10.0.0.2");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        Address c = a.clone();
        assertEquals(0, a.compareTo(c));
        assertEquals("10.0.0.1", c.toString());
    }

    @Test
    public void badAddressIsRejected() {
        try {
            new Address("not an address");
            fail("bad address");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void tcpEndpoints() {
        TcpEndpoint empty = new TcpEndpoint();
        assertEquals(0, empty.port());

        TcpEndpoint e = new TcpEndpoint(new Address("127.0.0.1"), 6881);
        assertEquals(6881, e.port());
        assertEquals("127.0.0.1", e.address().toString());

        TcpEndpoint byString = new TcpEndpoint("127.0.0.2", 7000);
        assertEquals(7000, byString.port());
        assertEquals(byString.toString(), byString.clone().toString());
        assertEquals(7000, byString.clone().port());

        try {
            new TcpEndpoint("bogus", 1);
            fail("bad address");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void udpEndpoints() {
        UdpEndpoint empty = new UdpEndpoint();
        assertEquals(0, empty.port());
        assertNotNull(empty.swig());

        UdpEndpoint e = new UdpEndpoint(new Address("127.0.0.1"), 6881);
        assertEquals(6881, e.port());
        assertEquals("127.0.0.1", e.address().toString());
        assertEquals("udp://127.0.0.1:6881", e.toString());

        UdpEndpoint byString = new UdpEndpoint("::1", 7000);
        assertEquals(7000, byString.port());
        assertTrue(byString.address().isV6());
        UdpEndpoint copy = byString.clone();
        assertEquals(byString.toString(), copy.toString());

        try {
            new UdpEndpoint("bogus", 1);
            fail("bad address");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
