package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.session_params;

import java.io.File;
import java.io.IOException;

/**
 * Configuration parameters for initializing a bittorrent session.
 * <p>
 * {@code SessionParams} encapsulates all settings and options needed to configure
 * a new libtorrent session before calling {@link SessionManager#start(SessionParams)}.
 * It holds both general settings and advanced options like disk I/O backend selection.
 * <p>
 * <b>Creating SessionParams:</b>
 * <pre>
 * // With default settings
 * SessionParams params = new SessionParams();
 *
 * // With custom settings
 * SessionParams params = new SessionParams();
 * SettingsPack settings = params.getSettings();
 * settings.downloadRateLimit(1000);     // 1 MB/s max
 * settings.uploadRateLimit(500);        // 500 KB/s max
 * settings.activeDownloads(3);          // Max 3 concurrent downloads
 * settings.connectionsLimit(200);       // Max 200 connections
 * // ... more settings ...
 *
 * // From saved session state
 * SessionParams params = new SessionParams(new File("session-state.bin"));
 * </pre>
 * <p>
 * <b>Default Configuration:</b>
 * When using the default constructor, SessionParams activates:
 * <ul>
 *   <li>Default plugins (ut_metadata, ut_pex, smart_ban)</li>
 *   <li>UPnP port mapping (for NAT traversal)</li>
 *   <li>NAT-PMP port mapping (Apple/AirPort compatible)</li>
 *   <li>DHT (Distributed Hash Table)</li>
 *   <li>LSD (Local Service Discovery)</li>
 * </ul>
 * <p>
 * <b>Storage Backend Selection:</b>
 * By default, jlibtorrent uses memory-mapped I/O for fast disk access. For compatibility
 * with systems that don't support it (e.g., some Android devices), use POSIX disk I/O:
 * <pre>
 * SessionParams params = new SessionParams();
 * params.setPosixDiskIO(); // Use portable POSIX file I/O instead of mmap
 * sm.start(params);
 * </pre>
 * <p>
 * <b>Session State Persistence:</b>
 * You can save and restore session state (known peers, DHT nodes, etc.) by:
 * <pre>
 * // Save state from a running session
 * byte[] state = sm.swig().session_state();
 * Files.write(Paths.get("session-state.bin"), state);
 *
 * // Restore state in new SessionManager instance
 * SessionParams params = new SessionParams(new File("session-state.bin"));
 * sessionManager2.start(params);
 * </pre>
 * <p>
 * <b>Performance Tuning:</b>
 * Via the embedded {@link SettingsPack}, you can tune:
 * <ul>
 *   <li>Bandwidth limits (downloadRateLimit, uploadRateLimit)</li>
 *   <li>Connection limits (connectionsLimit, maxPeerlistSize)</li>
 *   <li>Active torrent limits (activeDownloads, activeSeeds)</li>
 *   <li>Encryption settings (incomingEncryption, outgoingEncryption)</li>
 *   <li>Announce interval and timing</li>
 * </ul>
 *
 * @see SessionManager#start(SessionParams) - To start a session with these params
 * @see SettingsPack - For detailed settings available
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
     * This constructor can be used to start with the default plugins
     * (ut_metadata, ut_pex and smart_ban). The default values in the
     * settings is to start the default features like upnp, nat-pmp,
     * and dht for example.
     *
     * @param settings the initial settings pack
     */
    public SessionParams(SettingsPack settings) {
        this(new session_params(settings.swig()));
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
