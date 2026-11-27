package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.CloseReason;
import com.frostwire.jlibtorrent.alerts.DhtLogAlert;
import com.frostwire.jlibtorrent.alerts.DhtPktAlert;
import com.frostwire.jlibtorrent.alerts.IncomingConnectionAlert;
import com.frostwire.jlibtorrent.alerts.PeerBlockedAlert;
import com.frostwire.jlibtorrent.alerts.PeerLogAlert;
import com.frostwire.jlibtorrent.alerts.PerformanceAlert;
import com.frostwire.jlibtorrent.alerts.SocketType;
import com.frostwire.jlibtorrent.alerts.TrackerAnnounceAlert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Every enum that mirrors a native libtorrent enum maps its constants to the native value
 * ({@code swig()} / {@code getSwig()}) and back ({@code fromSwig(int)}). The mapping must be a
 * round trip for each constant that owns its native value.
 *
 * @author gubatron
 * @author aldenml
 */
public class EnumRoundTripTest {

    private static final Class<?>[] ENUMS = {
            WebSeedEntry.Type.class,
            PeerInfo.ConnectionType.class,
            Priority.class,
            BlockInfo.BlockState.class,
            PortmapTransport.class,
            TorrentStatus.State.class,
            StorageMode.class,
            PortmapProtocol.class,
            Operation.class,
            TrackerAnnounceAlert.TrackerAnnounceEvent.class,
            PeerLogAlert.Direction.class,
            PeerBlockedAlert.Reason.class,
            DhtLogAlert.DhtModule.class,
            PerformanceAlert.PerformanceWarning.class,
            DhtPktAlert.Direction.class,
            IncomingConnectionAlert.SocketType.class,
            SocketType.class,
            CloseReason.class,
            AlertType.class,
    };

    @Test
    public void everyConstantRoundTripsThroughItsNativeValue() throws Exception {
        for (Class<?> type : ENUMS) {
            Method toSwig = swigGetter(type);
            Method fromSwig = type.getMethod("fromSwig", int.class);
            Object[] constants = type.getEnumConstants();
            assertTrue(type + " has constants", constants.length > 0);

            Map<Integer, Integer> owners = new HashMap<>();
            for (Object c : constants) {
                owners.merge((Integer) toSwig.invoke(c), 1, Integer::sum);
            }
            for (Object c : constants) {
                if ("UNKNOWN".equals(((Enum<?>) c).name())) {
                    // the catch-all constant is a fallback, not a native value that can be looked up
                    continue;
                }
                int value = (Integer) toSwig.invoke(c);
                Object back = fromSwig.invoke(null, value);
                assertNotNull(type + " " + c + " maps back to something", back);
                if (owners.get(value) == 1) {
                    assertSame(type + " round trip of " + c, c, back);
                } else {
                    assertEquals(type + " shared native value " + value + " maps to a constant with that value",
                            value, (int) (Integer) toSwig.invoke(back));
                }
            }
        }
    }

    @Test
    public void valueOfFindsEveryConstantByName() {
        for (Class<?> type : ENUMS) {
            for (Object c : type.getEnumConstants()) {
                assertSame(c, Enum.valueOf((Class<? extends Enum>) type.asSubclass(Enum.class), ((Enum<?>) c).name()));
            }
        }
    }

    /** Native values no constant has fall back to UNKNOWN, or are refused. */
    @Test
    public void unknownNativeValuesFallBack() throws Exception {
        for (Class<?> type : ENUMS) {
            Method fromSwig = type.getMethod("fromSwig", int.class);
            Object unknown = null;
            for (Object c : type.getEnumConstants()) {
                if ("UNKNOWN".equals(((Enum<?>) c).name())) {
                    unknown = c;
                }
            }
            Object result;
            try {
                result = fromSwig.invoke(null, 987_654_321);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // an enum with no UNKNOWN constant refuses what it doesn't know
                assertNull(type + " has UNKNOWN but threw " + e.getCause(), unknown);
                assertTrue(type + " threw " + e.getCause(), e.getCause() instanceof IllegalArgumentException);
                continue;
            }
            if (unknown != null) {
                assertSame(type + " falls back to UNKNOWN", unknown, result);
            } else {
                assertTrue(type + " answered " + result, result == null || type.isInstance(result));
            }
        }
    }

    @Test
    public void alertTypeFromSwigAnswersUnknownOutOfRange() {
        assertSame(AlertType.UNKNOWN, AlertType.fromSwig(-1));
        assertSame(AlertType.UNKNOWN, AlertType.fromSwig(-1000));
        assertSame(AlertType.UNKNOWN, AlertType.fromSwig(Integer.MIN_VALUE));
        assertSame(AlertType.UNKNOWN, AlertType.fromSwig(Integer.MAX_VALUE));
    }

    private static Method swigGetter(Class<?> type) throws NoSuchMethodException {
        try {
            return type.getMethod("swig");
        } catch (NoSuchMethodException e) {
            return type.getMethod("getSwig");
        }
    }
}
