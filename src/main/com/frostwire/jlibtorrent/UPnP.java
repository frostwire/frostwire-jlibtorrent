package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.upnp;

import java.util.Locale;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UPnP {

    private final upnp p;

    public UPnP(upnp p) {
        this.p = p;
    }

    public upnp getSwig() {
        return p;
    }

    /**
     * Attempts to add a port mapping for the specified protocol.
     * <p/>
     * ``external_port`` is the port on the external address that will be mapped. This
     * is a hint, you are not guaranteed that this port will be available, and it may
     * end up being something else. In the portmap_alert_ notification, the actual
     * external port is reported.
     * <p/>
     * ``local_port`` is the port in the local machine that the mapping should forward
     * to.
     * <p/>
     * The return value is an index that identifies this port mapping. This is used
     * to refer to mappings that fails or succeeds in the portmap_error_alert_ and
     * portmap_alert_ respectively. If The mapping fails immediately, the return value
     * is -1, which means failure. There will not be any error alert notification for
     * mappings that fail with a -1 return value.
     *
     * @param p
     * @param externalPort
     * @param localPort
     * @return
     */
    public int addMapping(ProtocolType p, int externalPort, int localPort) {
        return this.p.add_mapping(p.getSwig(), externalPort, localPort);
    }

    /**
     * This function removes a port mapping. ``mapping_index`` is the index that refers
     * to the mapping you want to remove, which was returned from add_mapping().
     *
     * @param mappingIndex
     */
    public void deleteMapping(int mappingIndex) {
        p.delete_mapping(mappingIndex);
    }

    /**
     * @param mappingIndex
     * @return
     */
    public Mapping getMapping(int mappingIndex) {
        int_vector v = new int_vector();
        v.add(0);
        v.add(0);
        v.add(0);

        boolean r = p.get_mapping(mappingIndex, v);

        if (r) {
            int index = mappingIndex;
            ProtocolType protocol = ProtocolType.fromSwig(upnp.protocol_type.swigToEnum(v.get(2)));
            int localPort = v.get(0);
            int externalPort = v.get(1);

            return new Mapping(index, protocol, localPort, externalPort);
        } else {
            return null;
        }
    }

    /**
     *
     */
    public void discoverDevice() {
        p.discover_device();
    }

    /**
     *
     */
    public void close() {
        p.close();
    }

    /**
     * This is only available for UPnP routers. If the model is advertised by
     * the router, it can be queried through this function.
     *
     * @return
     */
    public String routerModel() {
        return p.router_model();
    }

    /**
     *
     */
    public enum ProtocolType {

        /**
         *
         */
        NONE(upnp.protocol_type.none),

        /**
         *
         */
        UDP(upnp.protocol_type.udp),

        /**
         *
         */
        TCP(upnp.protocol_type.tcp);

        private ProtocolType(upnp.protocol_type swigObj) {
            this.swigObj = swigObj;
        }

        private final upnp.protocol_type swigObj;

        /**
         * @return
         */
        public upnp.protocol_type getSwig() {
            return swigObj;
        }

        /**
         * @param swigObj
         * @return
         */
        public static ProtocolType fromSwig(upnp.protocol_type swigObj) {
            ProtocolType[] enumValues = ProtocolType.class.getEnumConstants();
            for (ProtocolType ev : enumValues) {
                if (ev.getSwig() == swigObj) {
                    return ev;
                }
            }
            throw new IllegalArgumentException("No enum " + ProtocolType.class + " with swig value " + swigObj);
        }
    }

    /**
     *
     */
    public static final class Mapping {

        private Mapping(int index, ProtocolType protocol, int localPort, int externalPort) {
            this.index = index;
            this.protocol = protocol;
            this.localPort = localPort;
            this.externalPort = externalPort;
        }

        /**
         *
         */
        public final int index;

        /**
         *
         */
        public final ProtocolType protocol;

        /**
         *
         */
        public final int localPort;

        /**
         *
         */
        public final int externalPort;

        @Override
        public String toString() {
            return String.format(Locale.US, "UPnP mapping - index: %d, protocol: %s, local port: %d, external port: %d", index, protocol, localPort, externalPort);
        }
    }
}
