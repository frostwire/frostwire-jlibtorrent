package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The alert category constants on {@link Alert}.
 *
 * @author gubatron
 * @author aldenml
 */
public class AlertConstantsTest {

    @Test
    public void everyCategoryIsAFlagContainedInAllCategories() throws IllegalAccessException {
        List<alert_category_t> categories = new ArrayList<>();
        for (Field f : Alert.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && f.getType() == alert_category_t.class) {
                alert_category_t c = (alert_category_t) f.get(null);
                assertNotNull(f.getName(), c);
                categories.add(c);
            }
        }
        assertEquals(23, categories.size());

        int all = Alert.ALL_CATEGORIES.to_int();
        for (alert_category_t c : categories) {
            assertTrue(c.to_int() != 0);
            assertEquals(c.to_int(), c.and_(Alert.ALL_CATEGORIES).to_int());
            assertEquals(c.to_int(), all & c.to_int());
        }
        assertTrue(Alert.ERROR_NOTIFICATION.to_int() != Alert.PEER_NOTIFICATION.to_int());
        assertEquals(Alert.ERROR_NOTIFICATION.or_(Alert.PEER_NOTIFICATION).to_int(),
                Alert.PEER_NOTIFICATION.or_(Alert.ERROR_NOTIFICATION).to_int());
    }
}
