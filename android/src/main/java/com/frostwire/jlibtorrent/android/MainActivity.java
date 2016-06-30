package com.frostwire.jlibtorrent.android;

import android.app.Activity;
import android.os.Bundle;
import com.frostwire.jlibtorrent.LibTorrent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(LibTorrent.version());
    }
}
