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
}
