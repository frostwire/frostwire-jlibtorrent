package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.lazy_entry;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractPeerPlugin implements PeerPlugin {

    @Override
    public boolean handleOperation(Operation op) {
        return false;
    }

    @Override
    public String type() {
        return "swig";
    }

    @Override
    public void addHandshake(Entry e) {

    }

    @Override
    public void onDisconnect(ErrorCode ec) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public boolean onHandshake(byte[] reservedBits) {
        return true;
    }

    @Override
    public boolean onExtensionHandshake(lazy_entry n) {
        return true;
    }

    @Override
    public boolean onChoke() {
        return false;
    }

    @Override
    public boolean onUnchoke() {
        return false;
    }

    @Override
    public boolean onInterested() {
        return false;
    }

    @Override
    public boolean onNotInterested() {
        return false;
    }

    @Override
    public boolean onHave(int index) {
        return false;
    }

    @Override
    public boolean onDontHave(int index) {
        return false;
    }

    @Override
    public boolean onBitfield(Bitfield bitfield) {
        return false;
    }

    @Override
    public boolean onHaveAll() {
        return false;
    }

    @Override
    public boolean onHaveNone() {
        return false;
    }

    @Override
    public boolean onAllowedFast(int index) {
        return false;
    }

    @Override
    public boolean onRequest(PeerRequest r) {
        return false;
    }

    @Override
    public boolean onPiece(PeerRequest piece, DiskBufferHolder data) {
        return false;
    }

    @Override
    public boolean onCancel(PeerRequest r) {
        return false;
    }

    @Override
    public boolean onReject(PeerRequest r) {
        return false;
    }

    @Override
    public boolean onSuggest(int index) {
        return false;
    }

    @Override
    public void sentUnchoke() {

    }

    @Override
    public void sentPayload(int bytes) {

    }

    @Override
    public boolean canDisconnect(ErrorCode ec) {
        return true;
    }

    @Override
    public void onPiecePass(int index) {

    }

    @Override
    public void onPieceFailed(int index) {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean writeRequest(PeerRequest r) {
        return false;
    }
}
