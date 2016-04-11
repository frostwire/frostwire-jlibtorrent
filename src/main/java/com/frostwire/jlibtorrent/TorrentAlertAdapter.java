package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.libtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class TorrentAlertAdapter implements AlertListener {

    private static InvokeLambda[] TABLE = buildTable();

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

        InvokeLambda l = TABLE[a.type().swig()];
        if (l != null) {
            l.invoke(this, a);
        }
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

    private static InvokeLambda[] buildTable() {
        InvokeLambda[] arr = new InvokeLambda[libtorrent.num_alert_types];

        arr[0] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrent((TorrentAlert) a);
            }
        };
        arr[1] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peer((PeerAlert) a);
            }
        };
        arr[2] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.tracker((TrackerAlert) a);
            }
        };
        arr[3] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentAdded((TorrentAddedAlert) a);
            }
        };
        arr[4] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentRemoved((TorrentRemovedAlert) a);
            }
        };
        arr[5] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.readPiece((ReadPieceAlert) a);
            }
        };
        arr[6] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.fileCompleted((FileCompletedAlert) a);
            }
        };
        arr[7] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.fileRenamed((FileRenamedAlert) a);
            }
        };
        arr[8] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.fileRenameFailed((FileRenameFailedAlert) a);
            }
        };
        arr[9] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.performance((PerformanceAlert) a);
            }
        };
        arr[10] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.stateChanged((StateChangedAlert) a);
            }
        };
        arr[11] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.trackerError((TrackerErrorAlert) a);
            }
        };
        arr[12] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.trackerWarning((TrackerWarningAlert) a);
            }
        };
        arr[13] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.scrapeReply((ScrapeReplyAlert) a);
            }
        };
        arr[14] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.scrapeFailed((ScrapeFailedAlert) a);
            }
        };
        arr[15] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.trackerReply((TrackerReplyAlert) a);
            }
        };
        arr[16] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.dhtReply((DhtReplyAlert) a);
            }
        };
        arr[17] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.trackerAnnounce((TrackerAnnounceAlert) a);
            }
        };
        arr[18] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.hashFailed((HashFailedAlert) a);
            }
        };
        arr[19] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerBan((PeerBanAlert) a);
            }
        };
        arr[20] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerUnsnubbed((PeerUnsnubbedAlert) a);
            }
        };
        arr[21] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerSnubbed((PeerSnubbedAlert) a);
            }
        };
        arr[22] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerError((PeerErrorAlert) a);
            }
        };
        arr[23] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerConnect((PeerConnectAlert) a);
            }
        };
        arr[24] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerDisconnected((PeerDisconnectedAlert) a);
            }
        };
        arr[25] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.invalidRequest((InvalidRequestAlert) a);
            }
        };
        arr[26] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentFinished((TorrentFinishedAlert) a);
            }
        };
        arr[27] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.pieceFinished((PieceFinishedAlert) a);
            }
        };
        arr[28] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.requestDropped((RequestDroppedAlert) a);
            }
        };
        arr[29] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.blockTimeout((BlockTimeoutAlert) a);
            }
        };
        arr[30] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.blockFinished((BlockFinishedAlert) a);
            }
        };
        arr[31] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.blockDownloading((BlockDownloadingAlert) a);
            }
        };
        arr[32] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.unwantedBlock((UnwantedBlockAlert) a);
            }
        };
        arr[33] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.storageMoved((StorageMovedAlert) a);
            }
        };
        arr[34] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.storageMovedFailed((StorageMovedFailedAlert) a);
            }
        };
        arr[35] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentDeleted((TorrentDeletedAlert) a);
            }
        };
        arr[36] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentDeleteFailed((TorrentDeleteFailedAlert) a);
            }
        };
        arr[37] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.saveResumeData((SaveResumeDataAlert) a);
            }
        };
        arr[38] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.saveResumeDataFailed((SaveResumeDataFailedAlert) a);
            }
        };
        arr[39] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentPaused((TorrentPausedAlert) a);
            }
        };
        arr[40] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentResumed((TorrentResumedAlert) a);
            }
        };
        arr[41] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentChecked((TorrentCheckedAlert) a);
            }
        };
        arr[42] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.urlSeed((UrlSeedAlert) a);
            }
        };
        arr[43] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.fileError((FileErrorAlert) a);
            }
        };
        arr[44] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.metadataFailed((MetadataFailedAlert) a);
            }
        };
        arr[45] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.metadataReceived((MetadataReceivedAlert) a);
            }
        };
        arr[46] = null;
        arr[47] = null;
        arr[48] = null;
        arr[49] = null;
        arr[50] = null;
        arr[51] = null;
        arr[52] = null;
        arr[53] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.fastresumeRejected((FastresumeRejectedAlert) a);
            }
        };
        arr[54] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerBlocked((PeerBlockedAlert) a);
            }
        };
        arr[55] = null;
        arr[56] = null;
        arr[57] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.stats((StatsAlert) a);
            }
        };
        arr[58] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.cacheFlushed((CacheFlushedAlert) a);
            }
        };
        arr[59] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.anonymousMode((AnonymousModeAlert) a);
            }
        };
        arr[60] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.lsdPeer((LsdPeerAlert) a);
            }
        };
        arr[61] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.trackerid((TrackeridAlert) a);
            }
        };
        arr[62] = null;
        arr[63] = null;
        arr[64] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentError((TorrentErrorAlert) a);
            }
        };
        arr[65] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentNeedCert((TorrentNeedCertAlert) a);
            }
        };
        arr[66] = null;
        arr[67] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.addTorrent((AddTorrentAlert) a);
            }
        };
        arr[68] = null;
        arr[69] = null;
        arr[70] = null;
        arr[71] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentUpdate((TorrentUpdateAlert) a);
            }
        };
        arr[72] = null;
        arr[73] = null;
        arr[74] = null;
        arr[75] = null;
        arr[76] = null;
        arr[77] = null;
        arr[78] = null;
        arr[79] = null;
        arr[80] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.torrentLog((TorrentLogAlert) a);
            }
        };
        arr[81] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.peerLog((PeerLogAlert) a);
            }
        };
        arr[82] = null;
        arr[83] = null;
        arr[84] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.incomingRequest((IncomingRequestAlert) a);
            }
        };
        arr[85] = null;
        arr[86] = null;
        arr[87] = null;
        arr[88] = null;
        arr[89] = new InvokeLambda() {
            @Override
            public void invoke(TorrentAlertAdapter l, Alert a) {
                l.pickerLog((PickerLogAlert) a);
            }
        };

        return arr;
    }

    private interface InvokeLambda {
        void invoke(TorrentAlertAdapter l, Alert a);
    }
}
