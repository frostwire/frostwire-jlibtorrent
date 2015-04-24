package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * The ``proxy_settings`` structs contains the information needed to
 * direct certain traffic to a proxy.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ProxySettings {

    private final settings_pack s;

    public ProxySettings(settings_pack s) {
        this.s = s;
    }

    public settings_pack getSwig() {
        return s;
    }

    /**
     * the name or IP of the proxy server. ``port`` is the port number the
     * proxy listens to. If required, ``username`` and ``password`` can be
     * set to authenticate with the proxy.
     *
     * @return
     */
    public String getHostname() {
        return s.get_str(settings_pack.string_types.proxy_hostname.swigValue());
    }

    /**
     * the name or IP of the proxy server. ``port`` is the port number the
     * proxy listens to. If required, ``username`` and ``password`` can be
     * set to authenticate with the proxy.
     *
     * @param value
     */
    public void setHostname(String value) {
        s.set_str(settings_pack.string_types.proxy_hostname.swigValue(), value);
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @return
     */
    public String getUsername() {
        return s.get_str(settings_pack.string_types.proxy_username.swigValue());
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @param value
     */
    public void setUsername(String value) {
        s.set_str(settings_pack.string_types.proxy_username.swigValue(), value);
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @return
     */
    public String getPassword() {
        return s.get_str(settings_pack.string_types.proxy_password.swigValue());
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @param value
     */
    public void setPassword(String value) {
        s.set_str(settings_pack.string_types.proxy_password.swigValue(), value);
    }

    /**
     * tells libtorrent what kind of proxy server it is. See proxy_type
     * enum for options
     *
     * @return
     */
    public ProxyType getType() {
        return ProxyType.fromSwig(s.get_int(settings_pack.int_types.proxy_type.swigValue()));
    }

    /**
     * tells libtorrent what kind of proxy server it is. See proxy_type
     * enum for options
     *
     * @param value
     */
    public void setType(ProxyType value) {
        s.set_int(settings_pack.int_types.proxy_type.swigValue(), value.getSwig());
    }

    /**
     * the port the proxy server is running on
     *
     * @return
     */
    public int getPort() {
        return s.get_int(settings_pack.int_types.proxy_port.swigValue());
    }

    /**
     * the port the proxy server is running on
     *
     * @param value
     */
    public void setPort(int value) {
        s.set_int(settings_pack.int_types.proxy_port.swigValue(), value);
    }

    /**
     * defaults to true. It means that hostnames should be attempted to be
     * resolved through the proxy instead of using the local DNS service.
     * This is only supported by SOCKS5 and HTTP.
     *
     * @return
     */
    public boolean getProxyHostnames() {
        return s.get_bool(settings_pack.bool_types.proxy_hostnames.swigValue());
    }

    /**
     * defaults to true. It means that hostnames should be attempted to be
     * resolved through the proxy instead of using the local DNS service.
     * This is only supported by SOCKS5 and HTTP.
     *
     * @param value
     */
    public void setProxyHostnames(boolean value) {
        s.set_bool(settings_pack.bool_types.proxy_hostnames.swigValue(), value);
    }

    /**
     * determines whether or not to excempt peer and web seed connections
     * from using the proxy. This defaults to true, i.e. peer connections are
     * proxied by default.
     *
     * @return
     */
    public boolean getProxyPeerConnections() {
        return s.get_bool(settings_pack.bool_types.proxy_peer_connections.swigValue());
    }

    /**
     * determines whether or not to excempt peer and web seed connections
     * from using the proxy. This defaults to true, i.e. peer connections are
     * proxied by default.
     *
     * @param value
     */
    public void setProxyPeerConnections(boolean value) {
        s.set_bool(settings_pack.bool_types.proxy_peer_connections.swigValue(), value);
    }

    /**
     * the type of proxy to use. Assign one of these to the
     * proxy_settings::type field.
     */
    public enum ProxyType {

        /**
         * This is the default, no proxy server is used, all other fields are
         * ignored.
         */
        NONE(settings_pack.proxy_type_t.none.swigValue()),

        /**
         * The server is assumed to be a `SOCKS4 server`_ that requires a
         * username.
         * <p/>
         * .. _`SOCKS4 server`: http://www.ufasoft.com/doc/socks4_protocol.htm
         */
        SOCKS4(settings_pack.proxy_type_t.socks4.swigValue()),

        /**
         * The server is assumed to be a SOCKS5 server (`RFC 1928`_) that does
         * not require any authentication. The username and password are
         * ignored.
         * <p/>
         * .. _`RFC 1928`: http://www.faqs.org/rfcs/rfc1928.html
         */
        SOCKS5(settings_pack.proxy_type_t.socks5.swigValue()),

        /**
         * The server is assumed to be a SOCKS5 server that supports plain
         * text username and password authentication (`RFC 1929`_). The
         * username and password specified may be sent to the proxy if it
         * requires.
         * <p/>
         * .. _`RFC 1929`: http://www.faqs.org/rfcs/rfc1929.html
         */
        SOCKS5_PW(settings_pack.proxy_type_t.socks5_pw.swigValue()),

        /**
         * The server is assumed to be an HTTP proxy. If the transport used
         * for the connection is non-HTTP, the server is assumed to support
         * the CONNECT_ method. i.e. for web seeds and HTTP trackers, a plain
         * proxy will suffice. The proxy is assumed to not require
         * authorization. The username and password will not be used.
         * <p/>
         * .. _CONNECT: http://tools.ietf.org/html/draft-luotonen-web-proxy-tunneling-01
         */
        HTTP(settings_pack.proxy_type_t.http.swigValue()),

        /**
         * The server is assumed to be an HTTP proxy that requires user
         * authorization. The username and password will be sent to the proxy.
         */
        HTTP_PW(settings_pack.proxy_type_t.http_pw.swigValue()),

        /**
         * route through a i2p SAM proxy
         */
        I2P_PROXY(settings_pack.proxy_type_t.i2p_proxy.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private ProxyType(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static ProxyType fromSwig(int swigValue) {
            ProxyType[] enumValues = ProxyType.class.getEnumConstants();
            for (ProxyType ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }

            return UNKNOWN;
        }
    }
}
