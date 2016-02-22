package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.plugins.DhtStorage;
import com.frostwire.jlibtorrent.plugins.DhtStorageBase;
import com.frostwire.jlibtorrent.plugins.DhtStorageConstructor;
import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtStorageTest {

    public static void main(String[] args) throws Throwable {

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
            }
        };

        SettingsPack sp = new SettingsPack();
        sp.setString(settings_pack.string_types.listen_interfaces.swigValue(), "0.0.0.0:0");
        sp.enableDht(false);

        Session s = new Session(sp, false, l);

        s.setDhtStorage(new DhtStorageConstructor() {
            @Override
            public DhtStorage create(Sha1Hash id, DhtSettings settings) {
                return new DhtStorageBase(id, settings, true);
            }
        });

        sp = new SettingsPack();
        sp.enableDht(true);
        s.applySettings(sp);

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.abort();
    }
}
