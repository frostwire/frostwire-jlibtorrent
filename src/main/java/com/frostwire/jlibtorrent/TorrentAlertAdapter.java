package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class TorrentAlertAdapter implements AlertListener {

    protected final TorrentHandle th;

    public TorrentAlertAdapter(TorrentHandle th) {
        this.th = th;
    }

    @Override
    public int[] types() {
        return null;
    }

    @Override
    public final void alert(Alert<?> a) {
        if (!(a instanceof TorrentAlert<?>)) {
            return;
        }

        if (!((TorrentAlert<?>) a).getHandle().getSwig().op_eq(th.getSwig())) {
            return;
        }

        switch (a.getSwig().type()) {
            case 0:
                torrent((TorrentAlert) a);
                break;
            case 1:
                peer((PeerAlert) a);
                break;
            case 2:
                tracker((TrackerAlert) a);
                break;
            case 3:
                torrentAdded((TorrentAddedAlert) a);
                break;
            case 4:
                torrentRemoved((TorrentRemovedAlert) a);
                break;
            case 5:
                readPiece((ReadPieceAlert) a);
                break;
            case 6:
                fileCompleted((FileCompletedAlert) a);
                break;
            case 7:
                fileRenamed((FileRenamedAlert) a);
                break;
            case 8:
                fileRenameFailed((FileRenameFailedAlert) a);
                break;
            case 9:
                performance((PerformanceAlert) a);
                break;
            case 10:
                stateChanged((StateChangedAlert) a);
                break;
            case 11:
                trackerError((TrackerErrorAlert) a);
                break;
            case 12:
                trackerWarning((TrackerWarningAlert) a);
                break;
            case 13:
                scrapeReply((ScrapeReplyAlert) a);
                break;
            case 14:
                scrapeFailed((ScrapeFailedAlert) a);
                break;
            case 15:
                trackerReply((TrackerReplyAlert) a);
                break;
            case 16:
                dhtReply((DhtReplyAlert) a);
                break;
            case 17:
                trackerAnnounce((TrackerAnnounceAlert) a);
                break;
            case 18:
                hashFailed((HashFailedAlert) a);
                break;
            case 19:
                peerBan((PeerBanAlert) a);
                break;
            case 20:
                peerUnsnubbed((PeerUnsnubbedAlert) a);
                break;
            case 21:
                peerSnubbed((PeerSnubbedAlert) a);
                break;
            case 22:
                peerError((PeerErrorAlert) a);
                break;
            case 23:
                peerConnect((PeerConnectAlert) a);
                break;
            case 24:
                peerDisconnected((PeerDisconnectedAlert) a);
                break;
            case 25:
                invalidRequest((InvalidRequestAlert) a);
                break;
            case 26:
                torrentFinished((TorrentFinishedAlert) a);
                break;
            case 27:
                pieceFinished((PieceFinishedAlert) a);
                break;
            case 28:
                requestDropped((RequestDroppedAlert) a);
                break;
            case 29:
                blockTimeout((BlockTimeoutAlert) a);
                break;
            case 30:
                blockFinished((BlockFinishedAlert) a);
                break;
            case 31:
                blockDownloading((BlockDownloadingAlert) a);
                break;
            case 32:
                unwantedBlock((UnwantedBlockAlert) a);
                break;
            case 33:
                storageMoved((StorageMovedAlert) a);
                break;
            case 34:
                storageMovedFailed((StorageMovedFailedAlert) a);
                break;
            case 35:
                torrentDeleted((TorrentDeletedAlert) a);
                break;
            case 36:
                torrentDeleteFailed((TorrentDeleteFailedAlert) a);
                break;
            case 37:
                saveResumeData((SaveResumeDataAlert) a);
                break;
            case 38:
                saveResumeDataFailed((SaveResumeDataFailedAlert) a);
                break;
            case 39:
                torrentPaused((TorrentPausedAlert) a);
                break;
            case 40:
                torrentResumed((TorrentResumedAlert) a);
                break;
            case 41:
                torrentChecked((TorrentCheckedAlert) a);
                break;
            case 42:
                urlSeed((UrlSeedAlert) a);
                break;
            case 43:
                fileError((FileErrorAlert) a);
                break;
            case 44:
                metadataFailed((MetadataFailedAlert) a);
                break;
            case 45:
                metadataReceived((MetadataReceivedAlert) a);
                break;
            case 46:
                invokeVoid(a);
                break;
            case 47:
                invokeVoid(a);
                break;
            case 48:
                invokeVoid(a);
                break;
            case 49:
                invokeVoid(a);
                break;
            case 50:
                invokeVoid(a);
                break;
            case 51:
                invokeVoid(a);
                break;
            case 52:
                invokeVoid(a);
                break;
            case 53:
                fastresumeRejected((FastresumeRejectedAlert) a);
                break;
            case 54:
                peerBlocked((PeerBlockedAlert) a);
                break;
            case 55:
                invokeVoid(a);
                break;
            case 56:
                invokeVoid(a);
                break;
            case 57:
                stats((StatsAlert) a);
                break;
            case 58:
                cacheFlushed((CacheFlushedAlert) a);
                break;
            case 59:
                anonymousMode((AnonymousModeAlert) a);
                break;
            case 60:
                lsdPeer((LsdPeerAlert) a);
                break;
            case 61:
                trackerid((TrackeridAlert) a);
                break;
            case 62:
                invokeVoid(a);
                break;
            case 63:
                invokeVoid(a);
                break;
            case 64:
                torrentError((TorrentErrorAlert) a);
                break;
            case 65:
                torrentNeedCert((TorrentNeedCertAlert) a);
                break;
            case 66:
                invokeVoid(a);
                break;
            case 67:
                addTorrent((AddTorrentAlert) a);
                break;
            case 68:
                invokeVoid(a);
                break;
            case 69:
                invokeVoid(a);
                break;
            case 70:
                invokeVoid(a);
                break;
            case 71:
                torrentUpdate((TorrentUpdateAlert) a);
                break;
            case 72:
                invokeVoid(a);
                break;
            case 73:
                invokeVoid(a);
                break;
            case 74:
                invokeVoid(a);
                break;
            case 75:
                invokeVoid(a);
                break;
            case 76:
                invokeVoid(a);
                break;
            case 77:
                invokeVoid(a);
                break;
            case 78:
                invokeVoid(a);
                break;
            case 79:
                invokeVoid(a);
                break;
            case 80:
                torrentLog((TorrentLogAlert) a);
                break;
            case 81:
                peerLog((PeerLogAlert) a);
                break;
            case 82:
                invokeVoid(a);
                break;
            case 83:
                invokeVoid(a);
                break;
            case 84:
                incomingRequest((IncomingRequestAlert) a);
                break;
            case 85:
                invokeVoid(a);
                break;
            case 86:
                invokeVoid(a);
                break;
            case 87:
                invokeVoid(a);
                break;
            case 88:
                invokeVoid(a);
                break;
            case 89:
                pickerLog((PickerLogAlert) a);
                break;
            default:
                invokeVoid(a);
        }
    }

    private void invokeVoid(Alert alert) {
    }

    private void torrent(TorrentAlert alert) {
    }

    private void peer(PeerAlert alert) {
    }

    private void tracker(TrackerAlert alert) {
    }

    public void torrentAdded(TorrentAddedAlert alert) {
    }

    public void torrentFinished(TorrentFinishedAlert alert) {
    }

    public void torrentRemoved(TorrentRemovedAlert alert) {
    }

    public void torrentUpdate(TorrentUpdateAlert alert) {
    }

    public void torrentDeleted(TorrentDeletedAlert alert) {
    }

    public void torrentPaused(TorrentPausedAlert alert) {
    }

    public void torrentResumed(TorrentResumedAlert alert) {
    }

    public void torrentChecked(TorrentCheckedAlert alert) {
    }

    public void torrentNeedCert(TorrentNeedCertAlert alert) {
    }

    public void torrentError(TorrentErrorAlert alert) {
    }

    public void addTorrent(AddTorrentAlert alert) {
    }

    public void blockFinished(BlockFinishedAlert alert) {
    }

    public void metadataReceived(MetadataReceivedAlert alert) {
    }

    public void metadataFailed(MetadataFailedAlert alert) {
    }

    public void saveResumeData(SaveResumeDataAlert alert) {
    }

    public void fastresumeRejected(FastresumeRejectedAlert alert) {
    }

    public void fileCompleted(FileCompletedAlert alert) {
    }

    public void fileRenamed(FileRenamedAlert alert) {
    }

    public void fileRenameFailed(FileRenameFailedAlert alert) {
    }

    public void fileError(FileErrorAlert alert) {
    }

    public void hashFailed(HashFailedAlert alert) {
    }

    public void trackerAnnounce(TrackerAnnounceAlert alert) {
    }

    public void trackerReply(TrackerReplyAlert alert) {
    }

    public void trackerWarning(TrackerWarningAlert alert) {
    }

    public void trackerError(TrackerErrorAlert alert) {
    }

    public void readPiece(ReadPieceAlert alert) {
    }

    public void stateChanged(StateChangedAlert alert) {
    }

    public void dhtReply(DhtReplyAlert alert) {
    }

    public void scrapeReply(ScrapeReplyAlert alert) {
    }

    public void scrapeFailed(ScrapeFailedAlert alert) {
    }

    public void lsdPeer(LsdPeerAlert alert) {
    }

    public void peerBlocked(PeerBlockedAlert alert) {
    }

    public void performance(PerformanceAlert alert) {
    }

    public void pieceFinished(PieceFinishedAlert alert) {
    }

    public void saveResumeDataFailed(SaveResumeDataFailedAlert alert) {
    }

    public void stats(StatsAlert alert) {
    }

    public void storageMoved(StorageMovedAlert alert) {
    }

    public void torrentDeleteFailed(TorrentDeleteFailedAlert alert) {
    }

    public void urlSeed(UrlSeedAlert alert) {
    }

    public void invalidRequest(InvalidRequestAlert alert) {
    }

    public void peerBan(PeerBanAlert alert) {
    }

    public void peerUnsnubbed(PeerUnsnubbedAlert alert) {
    }

    public void peerConnect(PeerConnectAlert alert) {
    }

    public void peerDisconnected(PeerDisconnectedAlert alert) {
    }

    public void peerError(PeerErrorAlert alert) {
    }

    public void peerSnubbed(PeerSnubbedAlert alert) {
    }

    public void peerUnsnubbe(PeerUnsnubbedAlert alert) {
    }

    public void requestDropped(RequestDroppedAlert alert) {
    }

    public void anonymousMode(AnonymousModeAlert alert) {
    }

    public void blockDownloading(BlockDownloadingAlert alert) {
    }

    public void blockTimeout(BlockTimeoutAlert alert) {
    }

    public void cacheFlushed(CacheFlushedAlert alert) {
    }

    public void storageMovedFailed(StorageMovedFailedAlert alert) {
    }

    public void trackerid(TrackeridAlert alert) {
    }

    public void unwantedBlock(UnwantedBlockAlert alert) {
    }

    public void torrentLog(TorrentLogAlert alert) {
    }

    public void peerLog(PeerLogAlert alert) {
    }

    public void incomingRequest(IncomingRequestAlert alert) {
    }

    public void pickerLog(PickerLogAlert alert) {
    }

    public void torrentPrioritize(TorrentPrioritizeAlert alert) {
    }
}
