package com.frostwire.jlibtorrent.android;

import android.app.Activity;
import android.os.Bundle;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.swig.libtorrent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean arm_neon_support = libtorrent.getArm_neon_support();
        boolean arm_crc32c_support = libtorrent.getArm_crc32c_support();
        System.out.println(LibTorrent.version());
        System.out.println("neon=" + arm_neon_support + ", crc32c=" + arm_crc32c_support);
    }
}
