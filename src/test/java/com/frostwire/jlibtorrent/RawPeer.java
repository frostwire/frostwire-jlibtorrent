package com.frostwire.jlibtorrent;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * A hand-written BitTorrent peer: just enough of the wire protocol (BEP 3) to say hello to a
 * libtorrent session and to behave badly in ways a well-behaved client never would.
 *
 * @author gubatron
 * @author aldenml
 */
final class RawPeer implements Closeable {

    static final int CHOKE = 0;
    static final int UNCHOKE = 1;
    static final int INTERESTED = 2;
    static final int HAVE = 4;
    static final int BITFIELD = 5;
    static final int REQUEST = 6;
    static final int PIECE = 7;

    /** A received message; {@code id} is -1 for a keep-alive. */
    static final class Message {
        final int id;
        final byte[] payload;

        Message(int id, byte[] payload) {
            this.id = id;
            this.payload = payload;
        }

        int int32(int offset) {
            return ((payload[offset] & 0xff) << 24) | ((payload[offset + 1] & 0xff) << 16)
                    | ((payload[offset + 2] & 0xff) << 8) | (payload[offset + 3] & 0xff);
        }
    }

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    RawPeer(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(10_000);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    /** Connects to a listening session on loopback. */
    static RawPeer connect(int port) throws IOException {
        return new RawPeer(new Socket(InetAddress.getByName("127.0.0.1"), port));
    }

    /** Sends our handshake and reads the remote one; returns the remote info hash. */
    byte[] handshake(byte[] infoHash) throws IOException {
        sendHandshake(infoHash);
        return readHandshake();
    }

    /** Sends our handshake only. */
    void sendHandshake(byte[] infoHash) throws IOException {
        out.writeByte(19);
        out.write("BitTorrent protocol".getBytes(StandardCharsets.US_ASCII));
        out.write(new byte[8]);
        out.write(infoHash);
        byte[] id = new byte[20];
        System.arraycopy("-RAW000-".getBytes(StandardCharsets.US_ASCII), 0, id, 0, 8);
        out.write(id);
        out.flush();
    }

    /** Reads the remote handshake, which the remote sends before or after ours. */
    byte[] readHandshake() throws IOException {
        int len = in.readUnsignedByte();
        byte[] proto = new byte[len];
        in.readFully(proto);
        in.readFully(new byte[8]);
        byte[] infoHash = new byte[20];
        in.readFully(infoHash);
        in.readFully(new byte[20]);
        return infoHash;
    }

    void send(int id, byte[] payload) throws IOException {
        out.writeInt(1 + payload.length);
        out.writeByte(id);
        out.write(payload);
        out.flush();
    }

    void sendPiece(int piece, int begin, byte[] block) throws IOException {
        byte[] payload = new byte[8 + block.length];
        putInt(payload, 0, piece);
        putInt(payload, 4, begin);
        System.arraycopy(block, 0, payload, 8, block.length);
        send(PIECE, payload);
    }

    void sendRequest(int piece, int begin, int length) throws IOException {
        byte[] payload = new byte[12];
        putInt(payload, 0, piece);
        putInt(payload, 4, begin);
        putInt(payload, 8, length);
        send(REQUEST, payload);
    }

    /** Sends a length prefix with no message behind it. */
    void sendRawLength(int length) throws IOException {
        out.writeInt(length);
        out.flush();
    }

    /** Blocks for the next message, or null once the remote closed the connection or went quiet for 10 s. */
    Message read() throws IOException {
        try {
            int length = in.readInt();
            if (length == 0) {
                return new Message(-1, new byte[0]);
            }
            int id = in.readUnsignedByte();
            byte[] payload = new byte[length - 1];
            in.readFully(payload);
            return new Message(id, payload);
        } catch (EOFException | SocketTimeoutException e) {
            return null;
        } catch (java.net.SocketException e) {
            return null; // reset by the remote
        }
    }

    /** Reads messages until one with the given id arrives; null if the connection ends first. */
    Message readUntil(int id) throws IOException {
        for (Message m = read(); m != null; m = read()) {
            if (m.id == id) {
                return m;
            }
        }
        return null;
    }

    static void putInt(byte[] b, int offset, int value) {
        b[offset] = (byte) (value >>> 24);
        b[offset + 1] = (byte) (value >>> 16);
        b[offset + 2] = (byte) (value >>> 8);
        b[offset + 3] = (byte) value;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
