package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.error_code;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * A SWIG proxy used after delete() passed a null pointer to native code, which dereferenced it and
 * killed the JVM: fetchMagnet freed its error_code and then called ec.clear() (2.0.15.0-2.0.15.2).
 * Every native method now checks its object and throws IllegalStateException instead.
 */
public class UseAfterDeleteTest {

    @Test
    public void callingAMethodOfADeletedErrorCodeThrows() {
        error_code ec = new error_code();
        ec.delete();
        IllegalStateException e = assertThrows(IllegalStateException.class, ec::clear);
        assertTrue(e.getMessage(), e.getMessage().contains("error_code used after delete()"));
    }

    @Test
    public void constMethodsAndFieldAccessorsThrowToo() {
        error_code ec = new error_code();
        ec.delete();
        assertThrows(IllegalStateException.class, ec::value); // const method

        add_torrent_params p = add_torrent_params.create_instance();
        p.delete();
        assertThrows(IllegalStateException.class, p::getName); // field getter
        assertThrows(IllegalStateException.class, () -> p.setName("x")); // field setter

        entry e = new entry("x");
        e.delete();
        assertThrows(IllegalStateException.class, e::string);
    }

    @Test
    public void aLiveObjectStillWorks() {
        error_code ec = new error_code();
        try {
            ec.clear();
            assertTrue(ec.value() == 0);
        } finally {
            ec.delete();
        }
    }
}
