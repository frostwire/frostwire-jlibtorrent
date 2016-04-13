package com.frostwire.jlibtorrent.android;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import com.frostwire.jlibtorrent.LibTorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public class LoadLibraryTest extends ApplicationTestCase<Application> {

    public LoadLibraryTest() {
        super(Application.class);
    }

    @SmallTest
    public void testVersion() {
        String jrevision = LibTorrent.jrevision();
        if (jrevision == null) {
            throw new NullPointerException();
        }
    }
}
