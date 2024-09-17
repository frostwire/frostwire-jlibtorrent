package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.session_params;

import java.io.File;
import java.io.IOException;

/**
 * This is a parameters pack for configuring the session
 * before it's started.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionParams {

    private final session_params s_p;

    /**
     * @return the native object
     */
    public session_params swig() {
        return s_p;
    }


    /**
     * @param p the native object
     */
    public SessionParams(session_params p) {
        this.s_p = p;
    }

    /**
     * This constructor can be used to start with the default plugins
     * (ut_metadata, ut_pex and smart_ban). The default values in the
     * settings is to start the default features like upnp, nat-pmp,
     * and dht for example.
     */
    public SessionParams() {
        this(new session_params());
    }

    /**
     * This constructor can be used to start with the default plugins
     * (ut_metadata, ut_pex and smart_ban). The default values in the
     * settings is to start the default features like upnp, nat-pmp,
     * and dht for example.
     *
     */
    public SessionParams(File data) {
        this(bdecode0(data));
    }

    @SuppressWarnings("unused")
    public SessionParams(byte[] data) {
        this(bdecode0(data));
    }

    /**
     * @return the settings pack
     */
    @SuppressWarnings("unused")
    public SettingsPack getSettings() {
        return new SettingsPack(s_p.getSettings());
    }

    /**
     * The settings to configure the session with.
     *
     * @param settings the settings pack
     */
    @SuppressWarnings("unused")
    public void setSettings(SettingsPack settings) {
        s_p.setSettings(settings.swig());
    }

    /**
     * Internally set the session to use a simple posix disk I/O back-end, used
     * for systems that don't have a 64-bit virtual address space or don't support
     * memory mapped files.
     * It's implemented using portable C file functions and is single-threaded.
     * This is an advance feature, only to use in particular situations, like
     * Android devices with faulty drivers.
     */
    @SuppressWarnings("unused")
    public void setPosixDiskIO() {
        s_p.set_posix_disk_io_constructor();
    }

    /**
     * Internally set the session to use the more appropriate disk I/O back-end.
     * On systems that support memory mapped files (and a 64-bit address space) the
     * memory mapped storage will be constructed, otherwise the portable posix storage.
     */
    @SuppressWarnings("unused")
    public void setDefaultDiskIO() {
        s_p.set_default_disk_io_constructor();
    }

    private static session_params bdecode0(File file) {
        try {
            byte[] data = Files.bytes(file);
            return bdecode0(data);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't decode data from file: " + file, e);
        }
    }

    private static session_params bdecode0(byte[] data) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            session_params params = session_params.read_session_params(n);
            buffer.clear(); // prevents GC
            return params;
        } else {
            throw new IllegalArgumentException("Can't decode data: " + ec.message());
        }
    }
}
