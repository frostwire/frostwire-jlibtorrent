package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

/**
 * peer plugins are associated with a specific peer. A peer could be
 * // both a regular bittorrent peer (``bt_peer_connection``) or one of the
 * // web seed connections (``web_peer_connection`` or ``http_seed_connection``).
 * // In order to only attach to certain peers, make your
 * // torrent_plugin::new_connection only return a plugin for certain peer
 * // connection types
 *
 * @author gubatron
 * @author aldenml
 */
public final class SwigPeerPlugin extends swig_peer_plugin {

    private final PeerPlugin p;

    public SwigPeerPlugin(PeerPlugin p) {
        this.p = p;
    }

    @Override
    public String type() {
        return p.type();
    }

    @Override
    public void add_handshake(entry e) {
        p.addHandshake(new Entry(e));
    }

    @Override
    public void on_disconnect(error_code ec) {
        p.onDisconnect(new ErrorCode(ec));
    }

    @Override
    public void on_connected() {
        p.onConnected();
    }

    @Override
    public boolean on_handshake(String reserved_bits) {
        // TODO: handle the byte array from the native side
        return p.onHandshake(null);
    }

    @Override
    public boolean on_extension_handshake(bdecode_node n) {
        return p.onExtensionHandshake(n);
    }

    @Override
    public boolean on_choke() {
        return p.onChoke();
    }

    @Override
    public boolean on_unchoke() {
        return p.onUnchoke();
    }

    @Override
    public boolean on_interested() {
        return p.onInterested();
    }

    @Override
    public boolean on_not_interested() {
        return p.onNotInterested();
    }

    @Override
    public boolean on_have(int index) {
        return p.onHave(index);
    }

    @Override
    public boolean on_dont_have(int index) {
        return p.onDontHave(index);
    }

    @Override
    public boolean on_bitfield(bitfield bitfield) {
        return p.onBitfield(new Bitfield(bitfield));
    }

    @Override
    public boolean on_have_all() {
        return p.onHaveAll();
    }

    @Override
    public boolean on_have_none() {
        return p.onHaveNone();
    }

    @Override
    public boolean on_allowed_fast(int index) {
        return p.onAllowedFast(index);
    }

    @Override
    public boolean on_request(peer_request r) {
        return p.onRequest(new PeerRequest(r));
    }

    @Override
    public boolean on_piece(peer_request piece, disk_buffer_holder data) {
        return p.onPiece(new PeerRequest(piece), new DiskBufferHolder(data));
    }

    @Override
    public boolean on_cancel(peer_request r) {
        return p.onCancel(new PeerRequest(r));
    }

    @Override
    public boolean on_reject(peer_request r) {
        return p.onReject(new PeerRequest(r));
    }

    @Override
    public boolean on_suggest(int index) {
        return p.onSuggest(index);
    }

    @Override
    public void sent_unchoke() {
        p.sentUnchoke();
    }

    @Override
    public void sent_payload(int bytes) {
        p.sentPayload(bytes);
    }

    @Override
    public boolean can_disconnect(error_code ec) {
        return p.canDisconnect(new ErrorCode(ec));
    }

    @Override
    public void on_piece_pass(int index) {
        p.onPiecePass(index);
    }

    @Override
    public void on_piece_failed(int index) {
        p.onPieceFailed(index);
    }

    @Override
    public void tick() {
        p.tick();
    }

    @Override
    public boolean write_request(peer_request r) {
        return p.writeRequest(new PeerRequest(r));
    }
}
