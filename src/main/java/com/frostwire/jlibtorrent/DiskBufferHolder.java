package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.disk_buffer_holder;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DiskBufferHolder {

    private final disk_buffer_holder b;

    public DiskBufferHolder(disk_buffer_holder b) {
        this.b = b;
    }

    public disk_buffer_holder swig() {
        return b;
    }

    /**
     * Return a pointer to the held buffer (native memory).
     *
     * @return
     */
    public long getPtr() {
        return b.get_ptr();
    }
}
