package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigPeerPlugin extends swig_peer_plugin {

    private static final Logger LOG = Logger.getLogger(SwigPeerPlugin.class);

    private final PeerPlugin p;
    final peer_connection pc;

    public SwigPeerPlugin(PeerPlugin p, peer_connection pc) {
        this.p = p;
        this.pc = pc;
    }

    @Override
    public String type() {
        return p.type();
    }

    @Override
    public void add_handshake(entry e) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ADD_HANDSHAKE)) {
                p.addHandshake(new Entry(e));
            }
        } catch (Throwable t) {
            LOG.error("Error in plugin (add_handshake)", t);
        }
    }

    @Override
    public void on_disconnect(error_code ec) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_DISCONNECT)) {
                p.onDisconnect(new ErrorCode(ec));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_disconnect)", e);
        }
    }

    @Override
    public void on_connected() {
        try {
            p.onConnected();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_connected)", e);
        }
    }

    @Override
    public boolean on_handshake(String reserved_bits) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_HANDSHAKE)) {
                // TODO: handle the byte array from the native side
                return p.onHandshake(null);
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_handshake)", e);
        }

        return true;
    }

    @Override
    public boolean on_extension_handshake(bdecode_node n) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_EXTENSION_HANDSHAKE)) {
                return p.onExtensionHandshake(n);
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_extension_handshake)", e);
        }

        return true;
    }

    @Override
    public boolean on_choke() {
        try {
            return p.onChoke();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_choke)", e);
        }

        return false;
    }

    @Override
    public boolean on_unchoke() {
        try {
            return p.onUnchoke();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_unchoke)", e);
        }

        return false;
    }

    @Override
    public boolean on_interested() {
        try {
            return p.onInterested();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_interested)", e);
        }

        return false;
    }

    @Override
    public boolean on_not_interested() {
        try {
            return p.onNotInterested();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_not_interested)", e);
        }

        return false;
    }

    @Override
    public boolean on_have(int index) {
        try {
            return p.onHave(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_have)", e);
        }

        return false;
    }

    @Override
    public boolean on_dont_have(int index) {
        try {
            return p.onDontHave(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_dont_have)", e);
        }

        return false;
    }

    @Override
    public boolean on_bitfield(bitfield bitfield) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_BITFIELD)) {
                return p.onBitfield(new Bitfield(bitfield));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_bitfield)", e);
        }

        return false;
    }

    @Override
    public boolean on_have_all() {
        try {
            return p.onHaveAll();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_have_all)", e);
        }

        return false;
    }

    @Override
    public boolean on_have_none() {
        try {
            return p.onHaveNone();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_have_none)", e);
        }

        return false;
    }

    @Override
    public boolean on_allowed_fast(int index) {
        try {
            return p.onAllowedFast(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_allowed_fast)", e);
        }

        return false;
    }

    @Override
    public boolean on_request(peer_request r) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_REQUEST)) {
                return p.onRequest(new PeerRequest(r));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_request)", e);
        }

        return false;
    }

    @Override
    public boolean on_piece(peer_request piece, disk_buffer_holder data) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_PIECE)) {
                return p.onPiece(new PeerRequest(piece), new DiskBufferHolder(data));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece)", e);
        }

        return false;
    }

    @Override
    public boolean on_cancel(peer_request r) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_CANCEL)) {
                return p.onCancel(new PeerRequest(r));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_cancel)", e);
        }

        return false;
    }

    @Override
    public boolean on_reject(peer_request r) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.ON_REJECT)) {
                return p.onReject(new PeerRequest(r));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_reject)", e);
        }

        return false;
    }

    @Override
    public boolean on_suggest(int index) {
        try {
            return p.onSuggest(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_suggest)", e);
        }

        return false;
    }

    @Override
    public void sent_unchoke() {
        try {
            p.sentUnchoke();
        } catch (Throwable e) {
            LOG.error("Error in plugin (sent_unchoke)", e);
        }
    }

    @Override
    public void sent_payload(int bytes) {
        try {
            p.sentPayload(bytes);
        } catch (Throwable e) {
            LOG.error("Error in plugin (sent_payload)", e);
        }
    }

    @Override
    public boolean can_disconnect(error_code ec) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.CAN_DISCONNECT)) {
                return p.canDisconnect(new ErrorCode(ec));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (can_disconnect)", e);
        }

        return true;
    }

    @Override
    public void on_piece_pass(int index) {
        try {
            p.onPiecePass(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece_pass)", e);
        }
    }

    @Override
    public void on_piece_failed(int index) {
        try {
            p.onPieceFailed(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece_failed)", e);
        }
    }

    @Override
    public void tick() {
        try {
            p.tick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (tick)", e);
        }
    }

    @Override
    public boolean write_request(peer_request r) {
        try {
            if (p.handleOperation(PeerPlugin.Operation.WRITE_REQUEST)) {
                return p.writeRequest(new PeerRequest(r));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (write_request)", e);
        }

        return false;
    }
}
