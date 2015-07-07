package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.disk_buffer_holder;

/**
 * The disk buffer holder acts like a ``scoped_ptr`` that frees a disk buffer
 * // when it's destructed, unless it's released. ``release`` returns the disk
 * // buffer and transferres ownership and responsibility to free it to the caller.
 * //
 * // A disk buffer is freed by passing it to ``session_impl::free_disk_buffer()``.
 * //
 * // ``buffer()`` returns the pointer without transferring responsibility. If
 * // this buffer has been released, ``buffer()`` will return 0.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DiskBufferHolder {

    private final disk_buffer_holder b;

    public DiskBufferHolder(disk_buffer_holder b) {
        this.b = b;
    }

    public disk_buffer_holder getSwig() {
        return b;
    }
}
