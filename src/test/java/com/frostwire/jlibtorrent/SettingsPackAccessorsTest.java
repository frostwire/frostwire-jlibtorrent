package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Every typed accessor of {@link SettingsPack} stores its value in the native settings pack and
 * reads it back.
 *
 * @author gubatron
 * @author aldenml
 */
public class SettingsPackAccessorsTest {

    private static void intProperty(String name, Function<SettingsPack, Integer> get,
                                    BiConsumer<SettingsPack, Integer> set, int value) {
        SettingsPack sp = new SettingsPack();
        set.accept(sp, value);
        assertEquals(name, value, (int) get.apply(sp));
    }

    private static void boolProperty(String name, Function<SettingsPack, Boolean> get,
                                     BiConsumer<SettingsPack, Boolean> set) {
        SettingsPack sp = new SettingsPack();
        set.accept(sp, true);
        assertTrue(name, get.apply(sp));
        set.accept(sp, false);
        assertFalse(name, get.apply(sp));
    }

    private static void stringProperty(String name, Function<SettingsPack, String> get,
                                       BiConsumer<SettingsPack, String> set, String value) {
        SettingsPack sp = new SettingsPack();
        set.accept(sp, value);
        assertEquals(name, value, get.apply(sp));
    }

    @Test
    public void integerProperties() {
        intProperty("downloadRateLimit", SettingsPack::downloadRateLimit, SettingsPack::downloadRateLimit, 1001);
        intProperty("uploadRateLimit", SettingsPack::uploadRateLimit, SettingsPack::uploadRateLimit, 1002);
        intProperty("activeDownloads", SettingsPack::activeDownloads, SettingsPack::activeDownloads, 11);
        intProperty("activeSeeds", SettingsPack::activeSeeds, SettingsPack::activeSeeds, 12);
        intProperty("activeChecking", SettingsPack::activeChecking, SettingsPack::activeChecking, 13);
        intProperty("activeDhtLimit", SettingsPack::activeDhtLimit, SettingsPack::activeDhtLimit, 14);
        intProperty("dhtUploadRate", SettingsPack::dhtUploadRate, SettingsPack::dhtUploadRate, 15);
        intProperty("activeTrackerLimit", SettingsPack::activeTrackerLimit, SettingsPack::activeTrackerLimit, 16);
        intProperty("activeLsdLimit", SettingsPack::activeLsdLimit, SettingsPack::activeLsdLimit, 17);
        intProperty("activeLimit", SettingsPack::activeLimit, SettingsPack::activeLimit, 18);
        intProperty("connectionsLimit", SettingsPack::connectionsLimit, SettingsPack::connectionsLimit, 19);
        intProperty("maxPeerlistSize", SettingsPack::maxPeerlistSize, SettingsPack::maxPeerlistSize, 20);
        intProperty("maxQueuedDiskBytes", SettingsPack::maxQueuedDiskBytes, SettingsPack::maxQueuedDiskBytes, 21);
        intProperty("sendBufferWatermark", SettingsPack::sendBufferWatermark, SettingsPack::sendBufferWatermark, 22);
        intProperty("tickInterval", SettingsPack::tickInterval, SettingsPack::tickInterval, 333);
        intProperty("inactivityTimeout", SettingsPack::inactivityTimeout, SettingsPack::inactivityTimeout, 24);
        intProperty("minWebsocketAnnounceInterval", SettingsPack::minWebsocketAnnounceInterval, SettingsPack::minWebsocketAnnounceInterval, 25);
        intProperty("webtorrentConnectionTimeout", SettingsPack::webtorrentConnectionTimeout, SettingsPack::webtorrentConnectionTimeout, 26);
        intProperty("maxWebtorrentOffers", SettingsPack::maxWebtorrentOffers, SettingsPack::maxWebtorrentOffers, 27);
        intProperty("stopTrackerTimeout", SettingsPack::stopTrackerTimeout, SettingsPack::stopTrackerTimeout, 28);
        intProperty("alertQueueSize", SettingsPack::alertQueueSize, SettingsPack::alertQueueSize, 2900);
        intProperty("natpmpLeaseDuration", SettingsPack::natpmpLeaseDuration, SettingsPack::natpmpLeaseDuration, 3000);
        intProperty("maxMetadataSize", SettingsPack::getMaxMetadataSize, SettingsPack::setMaxMetadataSize, 3100);
    }

    @Test
    public void booleanProperties() {
        boolProperty("seedingOutgoingConnections", SettingsPack::seedingOutgoingConnections, SettingsPack::seedingOutgoingConnections);
        boolProperty("anonymousMode", SettingsPack::anonymousMode, SettingsPack::anonymousMode);
        boolProperty("enableDht", SettingsPack::isEnableDht, SettingsPack::setEnableDht);
        boolProperty("enableLsd", SettingsPack::isEnableLsd, SettingsPack::setEnableLsd);
        boolProperty("applyFilterToDht", SettingsPack::applyFilterToDht, SettingsPack::applyFilterToDht);
        boolProperty("validateHttpsTrackers", SettingsPack::validateHttpsTrackers, SettingsPack::validateHttpsTrackers);
        boolProperty("diskDisableCopyOnWrite", SettingsPack::diskDisableCopyOnWrite, SettingsPack::diskDisableCopyOnWrite);
        boolProperty("allowMultipleConnectionsPerPid", SettingsPack::allowMultipleConnectionsPerPid, SettingsPack::allowMultipleConnectionsPerPid);
    }

    @Test
    public void stringProperties() {
        stringProperty("dhtBootstrapNodes", SettingsPack::getDhtBootstrapNodes, SettingsPack::setDhtBootstrapNodes, "router.example:6881");
        stringProperty("webtorrentStunServer", SettingsPack::getWebtorrentStunServer, SettingsPack::setWebtorrentStunServer, "stun.example:3478");
        stringProperty("listenInterfaces", SettingsPack::listenInterfaces, SettingsPack::listenInterfaces, "127.0.0.1:0");
        stringProperty("natpmpGateway", SettingsPack::natpmpGateway, SettingsPack::natpmpGateway, "192.168.1.1");
    }

    @Test
    public void settersAreFluent() {
        SettingsPack sp = new SettingsPack();
        assertSame(sp, sp.downloadRateLimit(1));
        assertSame(sp, sp.listenInterfaces("127.0.0.1:0"));
        assertSame(sp, sp.anonymousMode(false));
        assertSame(sp, sp.setInteger(settings_pack.int_types.connections_limit.swigValue(), 5));
        assertSame(sp, sp.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), false));
        assertSame(sp, sp.setString(settings_pack.string_types.user_agent.swigValue(), "ua"));
    }

    @Test
    public void rawAccessorsAndHasValue() {
        SettingsPack sp = new SettingsPack();
        assertNotNull(sp.swig());
        int conn = settings_pack.int_types.connections_limit.swigValue();
        int dht = settings_pack.bool_types.enable_dht.swigValue();
        int ua = settings_pack.string_types.user_agent.swigValue();

        sp.clear();
        assertFalse(sp.hasValue(conn));
        sp.setInteger(conn, 77);
        sp.setBoolean(dht, true);
        sp.setString(ua, "jlibtorrent-test");
        assertTrue(sp.hasValue(conn));
        assertEquals(77, sp.getInteger(conn));
        assertTrue(sp.getBoolean(dht));
        assertEquals("jlibtorrent-test", sp.getString(ua));

        sp.clear(conn);
        assertFalse(sp.hasValue(conn));
        assertTrue(sp.hasValue(ua));
        sp.clear();
        assertFalse(sp.hasValue(ua));
    }

    @Test
    public void peerFingerprintIsStoredAsBytes() {
        SettingsPack sp = new SettingsPack();
        byte[] fp = "-FW0001-".getBytes(StandardCharsets.US_ASCII);
        sp.setPeerFingerprint(fp);
        assertArrayEquals(fp, sp.getPeerFingerprint());

        int ua = settings_pack.string_types.user_agent.swigValue();
        sp.setBytes(ua, new byte[]{'a', 'b', 'c'});
        assertArrayEquals(new byte[]{'a', 'b', 'c'}, sp.getBytes(ua));
    }
}
