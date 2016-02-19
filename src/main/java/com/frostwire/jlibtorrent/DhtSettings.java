package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_settings;

/**
 * Structure used to hold configuration options for the DHT.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtSettings {

    private final dht_settings s;

    public DhtSettings(dht_settings s) {
        this.s = s;
    }

    public DhtSettings() {
        this(new dht_settings());
    }

    public dht_settings swig() {
        return s;
    }

    /**
     * the maximum number of peers to send in a reply to ``get_peers``.
     *
     * @return
     */
    public int maxPeersReply() {
        return s.getMax_peers_reply();
    }

    /**
     * the maximum number of peers to send in a reply to ``get_peers``.
     *
     * @param value
     */
    public void maxPeersReply(int value) {
        s.setMax_peers_reply(value);
    }

    /**
     * the number of concurrent search request the node will send when
     * announcing and refreshing the routing table. This parameter is called
     * alpha in the kademlia paper.
     *
     * @return
     */
    public int getSearchBranching() {
        return s.getSearch_branching();
    }

    /**
     * the number of concurrent search request the node will send when
     * announcing and refreshing the routing table. This parameter is called
     * alpha in the kademlia paper.
     *
     * @param value
     */
    public void setSearchBranching(int value) {
        s.setSearch_branching(value);
    }

    /**
     * the maximum number of failed tries to contact a node before it is
     * removed from the routing table. If there are known working nodes that
     * are ready to replace a failing node, it will be replaced immediately,
     * this limit is only used to clear out nodes that don't have any node
     * that can replace them.
     *
     * @return
     */
    public int getMaxFailCount() {
        return s.getMax_fail_count();
    }

    /**
     * the maximum number of failed tries to contact a node before it is
     * removed from the routing table. If there are known working nodes that
     * are ready to replace a failing node, it will be replaced immediately,
     * this limit is only used to clear out nodes that don't have any node
     * that can replace them.
     *
     * @param value
     */
    public void setMaxFailCount(int value) {
        s.setMax_fail_count(value);
    }

    /**
     * the total number of torrents to track from the DHT. This is simply an
     * upper limit to make sure malicious DHT nodes cannot make us allocate
     * an unbounded amount of memory.
     *
     * @return
     */
    public int maxTorrents() {
        return s.getMax_torrents();
    }

    /**
     * the total number of torrents to track from the DHT. This is simply an
     * upper limit to make sure malicious DHT nodes cannot make us allocate
     * an unbounded amount of memory.
     *
     * @param value
     */
    public void maxTorrents(int value) {
        s.setMax_torrents(value);
    }

    /**
     * max number of items the DHT will store.
     *
     * @return
     */
    public int getMaxDhtItems() {
        return s.getMax_dht_items();
    }

    /**
     * max number of items the DHT will store.
     *
     * @param value
     */
    public void setMaxDhtItems(int value) {
        s.setMax_dht_items(value);
    }

    /**
     * The max number of peers to store per torrent (for the DHT).
     *
     * @return
     */
    public int maxPeers() {
        return s.getMax_peers();
    }

    /**
     * The max number of peers to store per torrent (for the DHT).
     *
     * @param value
     */
    public void maxPeers(int value) {
        s.setMax_peers(value);
    }

    /**
     * the max number of torrents to return in a torrent search query to the
     * DHT.
     *
     * @return
     */
    public int getMaxTorrentSearchReply() {
        return s.getMax_torrent_search_reply();
    }

    /**
     * the max number of torrents to return in a torrent search query to the
     * DHT.
     *
     * @param value
     */
    public void setMaxTorrentSearchReply(int value) {
        s.setMax_torrent_search_reply(value);
    }

    /**
     * determines if the routing table entries should restrict entries to one
     * per IP. This defaults to true, which helps mitigate some attacks on
     * the DHT. It prevents adding multiple nodes with IPs with a very close
     * CIDR distance.
     * <p/>
     * when set, nodes whose IP address that's in the same /24 (or /64 for
     * IPv6) range in the same routing table bucket. This is an attempt to
     * mitigate node ID spoofing attacks also restrict any IP to only have a
     * single entry in the whole routing table.
     *
     * @return
     */
    public boolean isRestrictRoutingIPs() {
        return s.getRestrict_routing_ips();
    }

    /**
     * determines if the routing table entries should restrict entries to one
     * per IP. This defaults to true, which helps mitigate some attacks on
     * the DHT. It prevents adding multiple nodes with IPs with a very close
     * CIDR distance.
     * <p/>
     * when set, nodes whose IP address that's in the same /24 (or /64 for
     * IPv6) range in the same routing table bucket. This is an attempt to
     * mitigate node ID spoofing attacks also restrict any IP to only have a
     * single entry in the whole routing table.
     *
     * @param value
     */
    public void setRestrictRoutingIPs(boolean value) {
        s.setRestrict_routing_ips(value);
    }

    /**
     * determines if DHT searches should prevent adding nodes with IPs with
     * very close CIDR distance. This also defaults to true and helps
     * mitigate certain attacks on the DHT.
     *
     * @return
     */
    public boolean isRestrictSearchIPs() {
        return s.getRestrict_search_ips();
    }

    /**
     * determines if DHT searches should prevent adding nodes with IPs with
     * very close CIDR distance. This also defaults to true and helps
     * mitigate certain attacks on the DHT.
     *
     * @param value
     */
    public void setRestrictSearchIPs(boolean value) {
        s.setRestrict_search_ips(value);
    }

    /**
     * makes the first buckets in the DHT routing table fit 128, 64, 32 and
     * 16 nodes respectively, as opposed to the standard size of 8. All other
     * buckets have size 8 still.
     *
     * @return
     */
    public boolean isExtendedRoutingTable() {
        return s.getExtended_routing_table();
    }

    /**
     * makes the first buckets in the DHT routing table fit 128, 64, 32 and
     * 16 nodes respectively, as opposed to the standard size of 8. All other
     * buckets have size 8 still.
     *
     * @param value
     */
    public void setExtendedRoutingTable(boolean value) {
        s.setExtended_routing_table(value);
    }

    /**
     * slightly changes the lookup behavior in terms of how many outstanding
     * requests we keep. Instead of having branch factor be a hard limit, we
     * always keep *branch factor* outstanding requests to the closest nodes.
     * i.e. every time we get results back with closer nodes, we query them
     * right away. It lowers the lookup times at the cost of more outstanding
     * queries.
     *
     * @return
     */
    public boolean isAggressiveLookups() {
        return s.getAggressive_lookups();
    }

    /**
     * slightly changes the lookup behavior in terms of how many outstanding
     * requests we keep. Instead of having branch factor be a hard limit, we
     * always keep *branch factor* outstanding requests to the closest nodes.
     * i.e. every time we get results back with closer nodes, we query them
     * right away. It lowers the lookup times at the cost of more outstanding
     * queries.
     *
     * @param value
     */
    public void getAggressiveLookups(boolean value) {
        s.setAggressive_lookups(value);
    }

    /**
     * when set, perform lookups in a way that is slightly more expensive,
     * but which minimizes the amount of information leaked about you.
     *
     * @return
     */
    public boolean isPrivacyLookups() {
        return s.getPrivacy_lookups();
    }

    /**
     * when set, perform lookups in a way that is slightly more expensive,
     * but which minimizes the amount of information leaked about you.
     *
     * @param value
     */
    public void setPrivacyLookups(boolean value) {
        s.setPrivacy_lookups(value);
    }

    /**
     * when set, node's whose IDs that are not correctly generated based on
     * its external IP are ignored. When a query arrives from such node, an
     * error message is returned with a message saying "invalid node ID".
     *
     * @return
     */
    public boolean isEnforceNodeId() {
        return s.getEnforce_node_id();
    }

    /**
     * when set, node's whose IDs that are not correctly generated based on
     * its external IP are ignored. When a query arrives from such node, an
     * error message is returned with a message saying "invalid node ID".
     *
     * @param value
     */
    public void setEnforceNodeId(boolean value) {
        s.setEnforce_node_id(value);
    }

    /**
     * ignore DHT messages from parts of the internet we wouldn't expect to
     * see any traffic from
     *
     * @return
     */
    public boolean isIgnoreDarkInternet() {
        return s.getIgnore_dark_internet();
    }

    /**
     * ignore DHT messages from parts of the internet we wouldn't expect to
     * see any traffic from
     *
     * @param value
     */
    public void setIgnoreDarkInternet(boolean value) {
        s.setIgnore_dark_internet(value);
    }

    /**
     * The number of seconds a DHT node is banned if it exceeds the rate
     * limit. The rate limit is averaged over 10 seconds to allow for bursts
     * above the limit.
     *
     * @return
     */
    public int blockTimeout() {
        return s.getBlock_timeout();
    }

    /**
     * The number of seconds a DHT node is banned if it exceeds the rate
     * limit. The rate limit is averaged over 10 seconds to allow for bursts
     * above the limit.
     *
     * @param value
     */
    public void blockTimeout(int value) {
        s.setBlock_timeout(value);
    }

    /**
     * The max number of packets per second a DHT node is allowed to send
     * without getting banned.
     *
     * @return
     */
    public int blockRatelimit() {
        return s.getBlock_ratelimit();
    }

    /**
     * The max number of packets per second a DHT node is allowed to send
     * without getting banned.
     *
     * @param value
     */
    public void blockRatelimit(int value) {
        s.setBlock_ratelimit(value);
    }

    /**
     * When set, the other nodes won't keep this node in their routing
     * tables, it's meant for low-power and/or ephemeral devices that
     * cannot support the DHT, it is also useful for mobile devices which
     * are sensitive to network traffic and battery life.
     * this node no longer responds to 'query' messages, and will place a
     * 'ro' key (value = 1) in the top-level message dictionary of outgoing
     * query messages.
     *
     * @return
     */
    public boolean readOnly() {
        return s.getRead_only();
    }

    /**
     * When set, the other nodes won't keep this node in their routing
     * tables, it's meant for low-power and/or ephemeral devices that
     * cannot support the DHT, it is also useful for mobile devices which
     * are sensitive to network traffic and battery life.
     * this node no longer responds to 'query' messages, and will place a
     * 'ro' key (value = 1) in the top-level message dictionary of outgoing
     * query messages.
     *
     * @param value
     */
    public void readOnly(boolean value) {
        s.setRead_only(value);
    }

    /**
     * The number of seconds a immutable/mutable item will be expired.
     * default is 0, means never expires.
     *
     * @return
     */
    public int itemLifetime() {
        return s.getItem_lifetime();
    }

    /**
     * The number of seconds a immutable/mutable item will be expired.
     * default is 0, means never expires.
     *
     * @param value
     */
    public void itemLifetime(int value) {
        s.setItem_lifetime(value);
    }
}
