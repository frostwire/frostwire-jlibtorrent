package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_request;

/**
 * Represents a byte range request within a piece in the BitTorrent protocol.
 * <p>
 * {@code PeerRequest} describes a contiguous byte range inside a piece that a peer
 * has requested (or that you're requesting from a peer). BitTorrent breaks pieces into
 * smaller blocks for transmission; when requesting blocks from peers or receiving requests
 * from peers, each request specifies a piece number, byte offset within that piece, and
 * the length of bytes to transfer.
 * <p>
 * <b>Understanding Piece Requests:</b>
 * <br/>
 * Piece requests are the fundamental unit of BitTorrent data transfer:
 * <ul>
 *   <li><b>Block Size:</b> Typically 16KB (16,384 bytes) per request</li>
 *   <li><b>Piece Composition:</b> Pieces are split into many blocks</li>
 *   <li><b>Request Protocol:</b> "request" message sends PeerRequest to peer</li>
 *   <li><b>Incoming Requests:</b> Peers request blocks from you</li>
 *   <li><b>Verification:</b> Each received block is verified after transfer</li>
 * </ul>
 * <p>
 * <b>PeerRequest Structure:</b>
 * <pre>
 * Piece #5 (256 KB total)
 * ┌────────────────────────────────────┐
 * │ Block 0 (16KB) [bytes 0-16383]    │
 * │ Block 1 (16KB) [bytes 16384-32767]│ ← PeerRequest(5, 16384, 16384)
 * │ Block 2 (16KB) [bytes 32768-49151]│
 * │ ...                                │
 * └────────────────────────────────────┘
 * </pre>
 * <p>
 * <b>Creating PeerRequests:</b>
 * <p>
 * PeerRequest objects are not typically constructed directly; they're provided by the
 * library when receiving incoming peer requests or mapped from file offsets.
 * <pre>
 * // From TorrentInfo - map a file offset to a piece request
 * TorrentInfo info = new TorrentInfo(torrentFile);
 *
 * // Request first 32KB of file 0
 * // This gets mapped to piece/block coordinates
 * PeerRequest req = info.mapFile(0, 0, 32768);
 * System.out.println("Piece: " + req.piece());      // e.g., 0
 * System.out.println("Start: " + req.start());      // e.g., 0
 * System.out.println("Length: " + req.length());    // e.g., 32768
 * </pre>
 * <p>
 * <b>Interpreting PeerRequest Components:</b>
 * <pre>
 * PeerRequest req = ...;  // Received from alert or library
 *
 * // Get piece index
 * int pieceIdx = req.piece();
 * System.out.println("Requesting piece " + pieceIdx);
 *
 * // Get byte offset within that piece
 * int offset = req.start();
 * System.out.println("Starting at byte " + offset + " in piece");
 *
 * // Get length of block/range
 * int blockSize = req.length();
 * System.out.println("Block size: " + blockSize + " bytes");
 *
 * // Typical block is 16KB
 * if (blockSize == 16384) {
 *     System.out.println("Standard block size\");
 * } else if (blockSize &lt; 16384) {
 *     System.out.println("Last block in piece (partial)\");
 * }
 * </pre>
 * <p>
 * <b>Incoming Peer Requests:</b>
 * <p>
 * When peers request data from you, you receive IncomingRequestAlert containing
 * the peer's requested block:
 * <pre>
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.INCOMING_REQUEST.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         IncomingRequestAlert a = (IncomingRequestAlert) alert;
 *         PeerRequest req = a.getRequest();
 *
 *         System.out.println("Peer requesting: \" + req);
 *         // Output: PeerRequest(piece: 0, start: 0, length: 16384)
 *
 *         // Load the requested block and send it
 *         byte[] blockData = loadBlock(req.piece(), req.start(), req.length());
 *         // ... send blockData to peer ...
 *     }
 * });
 * </pre>
 * <p>
 * <b>Invalid Peer Requests:</b>
 * <p>
 * Peers sometimes request blocks outside torrent bounds or with invalid parameters.
 * The library reports these as InvalidRequestAlert:
 * <pre>
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.INVALID_REQUEST.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         InvalidRequestAlert a = (InvalidRequestAlert) alert;
 *         PeerRequest badReq = a.getRequest();
 *
 *         System.err.println(\"Peer sent invalid request: \" + badReq);
 *         // This peer is misbehaving or buggy
 *     }
 * });
 * </pre>
 * <p>
 * <b>Block Sizes in BitTorrent:</b>
 * <pre>
 * // Standard block size (most common)
 * int STANDARD_BLOCK_SIZE = 16384;  // 16 KB
 *
 * // Typical piece breakdown (assuming 256 KB piece)
 * // Piece 0:
 * //   Block 0: bytes 0-16383 (16 KB)
 * //   Block 1: bytes 16384-32767 (16 KB)
 * //   Block 2: bytes 32768-49151 (16 KB)
 * //   ... (15 blocks total)
 *
 * // Last piece may have fewer blocks
 * // If torrent is 1MB and pieces are 256KB:
 * // Piece 3: bytes 786432-1048575 (262143 bytes, not aligned to blocks)
 * </pre>
 * <p>
 * <b>Mapping File Offsets to PeerRequests:</b>
 * <pre>
 * TorrentInfo info = new TorrentInfo(torrentFile);
 *
 * // What piece/block is at byte offset 50000 of file 0?
 * PeerRequest fileReq = info.mapFile(0, 50000, 1);
 *
 * System.out.println("File byte 50000 is in piece \" + fileReq.piece());
 * System.out.println(\"At offset \" + fileReq.start());
 *
 * // Map a 32KB range starting at byte 100000 of file 0
 * PeerRequest rangeReq = info.mapFile(0, 100000, 32768);
 * System.out.println(\"File bytes 100000-132767 span pieces:\");
 * System.out.println(\"  Start: piece \" + rangeReq.piece() + \", offset \" + rangeReq.start());
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>PeerRequest is a lightweight value object; safe to create/pass frequently</li>
 *   <li>Typical BitTorrent sessions handle thousands of PeerRequest objects</li>
 *   <li>Block transfers are pipelined; multiple requests outstanding per peer</li>
 *   <li>Request/response is the fundamental BitTorrent protocol message</li>
 * </ul>
 *
 * @see BlockInfo represents state of a block being downloaded
 * @see PartialPieceInfo state of incomplete pieces with block progress
 * @see TorrentInfo map file offsets to piece requests
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerRequest {

    private final peer_request r;

    // internal
    public PeerRequest(peer_request r) {
        this.r = r;
    }

    /**
     * @return native object
     */
    public peer_request swig() {
        return r;
    }

    /**
     * The index of the piece in which the range starts.
     *
     * @return the piece index
     */
    public int piece() {
        return r.getPiece();
    }

    /**
     * The offset within that piece where the range starts.
     *
     * @return the start offset
     */
    public int start() {
        return r.getStart();
    }

    /**
     * The size of the range, in bytes.
     *
     * @return the range length
     */
    public int length() {
        return r.getLength();
    }

    /**
     * @return string representation
     */
    @Override
    public String toString() {
        return "PeerRequest(piece: " + piece() + ", start: " + start() + ", length: " + length() + ")";
    }
}
