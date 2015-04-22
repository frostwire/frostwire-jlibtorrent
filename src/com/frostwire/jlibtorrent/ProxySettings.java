package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.proxy_settings;

/**
 * The ``proxy_settings`` structs contains the information needed to
 * direct certain traffic to a proxy.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ProxySettings {

    private final proxy_settings s;

    public ProxySettings(proxy_settings s) {
        this.s = s;
    }

    public proxy_settings getSwig() {
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
        return s.getHostname();
    }

    /**
     * the name or IP of the proxy server. ``port`` is the port number the
     * proxy listens to. If required, ``username`` and ``password`` can be
     * set to authenticate with the proxy.
     *
     * @param value
     */
    public void setHostname(String value) {
        s.setHostname(value);
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @return
     */
    public String getUsername() {
        return s.getUsername();
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @param value
     */
    public void setUsername(String value) {
        s.setUsername(value);
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @return
     */
    public String getPassword() {
        return s.getPassword();
    }

    /**
     * when using a proy type that requires authentication, the username
     * and password fields must be set to the credentials for the proxy.
     *
     * @param value
     */
    public void setPassword(String value) {
        s.setPassword(value);
    }

    /**
     * tells libtorrent what kind of proxy server it is. See proxy_type
     * enum for options
     *
     * @return
     */
    public ProxyType getType() {
        return ProxyType.fromSwig(s.getType());
    }

    /**
     * tells libtorrent what kind of proxy server it is. See proxy_type
     * enum for options
     *
     * @param value
     */
    public void setType(ProxyType value) {
        s.setType((short) value.getSwig());
    }

    /**
     * the port the proxy server is running on
     *
     * @return
     */
    public int getPort() {
        return s.getPort();
    }

    /**
     * the port the proxy server is running on
     *
     * @param value
     */
    public void setPort(int value) {
        s.setPort(value);
    }

    /**
     * defaults to true. It means that hostnames should be attempted to be
     * resolved through the proxy instead of using the local DNS service.
     * This is only supported by SOCKS5 and HTTP.
     *
     * @return
     */
    public boolean getProxyHostnames() {
        return s.getProxy_hostnames();
    }

    /**
     * defaults to true. It means that hostnames should be attempted to be
     * resolved through the proxy instead of using the local DNS service.
     * This is only supported by SOCKS5 and HTTP.
     *
     * @param value
     */
    public void setProxyHostnames(boolean value) {
        s.setProxy_hostnames(value);
    }

    /**
     * determines whether or not to excempt peer and web seed connections
     * from using the proxy. This defaults to true, i.e. peer connections are
     * proxied by default.
     *
     * @return
     */
    public boolean getProxyPeerConnections() {
        return s.getProxy_peer_connections();
    }

    /**
     * determines whether or not to excempt peer and web seed connections
     * from using the proxy. This defaults to true, i.e. peer connections are
     * proxied by default.
     *
     * @param value
     */
    public void setProxyPeerConnections(boolean value) {
        s.setProxy_peer_connections(value);
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
        NONE(proxy_settings.proxy_type.none.swigValue()),

        /**
         * The server is assumed to be a `SOCKS4 server`_ that requires a
         * username.
         * <p/>
         * .. _`SOCKS4 server`: http://www.ufasoft.com/doc/socks4_protocol.htm
         */
        SOCKS4(proxy_settings.proxy_type.socks4.swigValue()),

        /**
         * The server is assumed to be a SOCKS5 server (`RFC 1928`_) that does
         * not require any authentication. The username and password are
         * ignored.
         * <p/>
         * .. _`RFC 1928`: http://www.faqs.org/rfcs/rfc1928.html
         */
        SOCKS5(proxy_settings.proxy_type.socks5.swigValue()),

        /**
         * The server is assumed to be a SOCKS5 server that supports plain
         * text username and password authentication (`RFC 1929`_). The
         * username and password specified may be sent to the proxy if it
         * requires.
         * <p/>
         * .. _`RFC 1929`: http://www.faqs.org/rfcs/rfc1929.html
         */
        SOCKS5_PW(proxy_settings.proxy_type.socks5_pw.swigValue()),

        /**
         * The server is assumed to be an HTTP proxy. If the transport used
         * for the connection is non-HTTP, the server is assumed to support
         * the CONNECT_ method. i.e. for web seeds and HTTP trackers, a plain
         * proxy will suffice. The proxy is assumed to not require
         * authorization. The username and password will not be used.
         * <p/>
         * .. _CONNECT: http://tools.ietf.org/html/draft-luotonen-web-proxy-tunneling-01
         */
        HTTP(proxy_settings.proxy_type.http.swigValue()),

        /**
         * The server is assumed to be an HTTP proxy that requires user
         * authorization. The username and password will be sent to the proxy.
         */
        HTTP_PW(proxy_settings.proxy_type.http_pw.swigValue()),

        /**
         * route through a i2p SAM proxy
         */
        I2P_PROXY(proxy_settings.proxy_type.i2p_proxy.swigValue()),

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
