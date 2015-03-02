package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * The ``settings_pack`` struct, contains the names of all settings as
 * enum values. These values are passed in to the ``set_str()``,
 * ``set_int()``, ``set_bool()`` functions, to specify the setting to
 * change.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SettingsPack {

    private final settings_pack sp;

    public SettingsPack(settings_pack sp) {
        this.sp = sp;
    }

    public SettingsPack() {
        this(new settings_pack());
    }

    public settings_pack getSwig() {
        return sp;
    }

    public boolean getBoolean(int name) {
        return sp.get_bool(name);
    }

    public void setBoolean(int name, boolean value) {
        sp.set_bool(name, value);
    }

    public int getInteger(int name) {
        return sp.get_int(name);
    }

    public void setInteger(int name, int value) {
        sp.set_int(name, value);
    }
}
