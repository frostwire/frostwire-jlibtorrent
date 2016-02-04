package com.frostwire.jlibtorrent.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Map;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.TorrentAlertAdapter;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.BlockDownloadingAlert;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.BlockTimeoutAlert;
import com.frostwire.jlibtorrent.alerts.DhtReplyAlert;
import com.frostwire.jlibtorrent.alerts.FastresumeRejectedAlert;
import com.frostwire.jlibtorrent.alerts.FileErrorAlert;
import com.frostwire.jlibtorrent.alerts.HashFailedAlert;
import com.frostwire.jlibtorrent.alerts.PeerBanAlert;
import com.frostwire.jlibtorrent.alerts.PeerBlockedAlert;
import com.frostwire.jlibtorrent.alerts.PeerConnectAlert;
import com.frostwire.jlibtorrent.alerts.PeerDisconnectedAlert;
import com.frostwire.jlibtorrent.alerts.PeerErrorAlert;
import com.frostwire.jlibtorrent.alerts.PeerSnubbedAlert;
import com.frostwire.jlibtorrent.alerts.PeerUnsnubbedAlert;
import com.frostwire.jlibtorrent.alerts.PerformanceAlert;
import com.frostwire.jlibtorrent.alerts.RequestDroppedAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataFailedAlert;
import com.frostwire.jlibtorrent.alerts.ScrapeFailedAlert;
import com.frostwire.jlibtorrent.alerts.ScrapeReplyAlert;
import com.frostwire.jlibtorrent.alerts.StateChangedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentAddedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentCheckedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentDeleteFailedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentDeletedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentErrorAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentPausedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentRemovedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentResumedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentUpdateAlert;
import com.frostwire.jlibtorrent.alerts.TrackerAnnounceAlert;
import com.frostwire.jlibtorrent.alerts.TrackerErrorAlert;
import com.frostwire.jlibtorrent.alerts.TrackerReplyAlert;
import com.frostwire.jlibtorrent.alerts.TrackerWarningAlert;
import com.frostwire.jlibtorrent.alerts.UnwantedBlockAlert;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.libtorrent;


public class MillionTorrents {

	public static void main(String[] args) throws IOException {
		
		Session session = new Session();
		SettingsPack sessionSettings = new SettingsPack();

	
		sessionSettings.setActiveDownloads(10);
//		sessionSettings.setActiveLimit(999999);
		sessionSettings.setActiveSeeds(999999);
//		sessionSettings.setActiveTrackerLimit(999999);
//		session.stopLSD();
//		session.stopDHT();
		
	
		
		session.applySettings(sessionSettings);
		
//		session.setSettings(sessionSettings);
		
		
		for (int i = 0; i < 10000; i++) {
			System.out.println(i);
			File torrentFile = File.createTempFile("t_" + i + "_", ".torrent");
			File inputFileOrDir = File.createTempFile("f_" + i + "_", ".tmp");
			java.nio.file.Files.write(Paths.get(inputFileOrDir.getAbsolutePath()), "1".getBytes());
			
			File tFile = createAndSaveTorrent(torrentFile, inputFileOrDir);
			
			TorrentHandle torrent = session.addTorrent(tFile, inputFileOrDir.getParentFile());
		
			
			
			torrent.setAutoManaged(false);
			torrent.resume();
			
			addListeners(session, torrent);
			
			System.out.println("Added file " + tFile.getAbsolutePath());
			
		}
		
        System.out.println("Press ENTER to exit");
        System.in.read();
        
	
	}
	
	private static File createAndSaveTorrent(File torrentFile, File inputFileOrDir) {

		file_storage fs = new file_storage();

		// Add the file
		libtorrent.add_files(fs, inputFileOrDir.getAbsolutePath());

		create_torrent t = new create_torrent(fs);


		// Add trackers in tiers
//		for (URI announce : DataSources.ANNOUNCE_LIST()) {
//			t.add_tracker(announce.toASCIIString());
//		}


		t.set_priv(false);
		t.set_creator(System.getProperty("user.name"));

		error_code ec = new error_code();


		// reads the files and calculates the hashes
		libtorrent.set_piece_hashes(t, inputFileOrDir.getParent(), ec);

		if (ec.value() != 0) {
			System.out.println(ec.message());
		}

		// Get the bencode and write the file
		Entry entry =  new Entry(t.generate());

		Map<String, Entry> entryMap = entry.dictionary();
		Entry entryFromUpdatedMap = Entry.fromMap(entryMap);
		final byte[] bencode = entryFromUpdatedMap.bencode();

		try {
			FileOutputStream fos;

			fos = new FileOutputStream(torrentFile);

			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(bencode);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			System.err.println("Couldn't write file");
			e.printStackTrace();
		}

		return torrentFile;
	}
	
	private static void addListeners(Session session, TorrentHandle torrent) {
		// Add the listeners
				session.addListener(new TorrentAlertAdapter(torrent) {

					//			@Override
					//			public void stats(StatsAlert alert) {
					//				TorrentStats ts = TorrentStats.create(torrent);
					//				System.out.println(ts.toString());
					//
					//				super.stats(alert);
					//			}

					@Override
					public void stateChanged(StateChangedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}
					
					@Override
					public void scrapeFailed(ScrapeFailedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}
					

					@Override
					public void blockFinished(BlockFinishedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentFinished(TorrentFinishedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
						//				torrent.saveResumeData();
					}

					@Override
					public void blockDownloading(BlockDownloadingAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void peerConnect(PeerConnectAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}
					@Override
					public void peerSnubbed(PeerSnubbedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void peerUnsnubbe(PeerUnsnubbedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void requestDropped(RequestDroppedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void saveResumeData(SaveResumeDataAlert alert) {
//						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
//						Entry srdata = alert.getResumeData();
//						try {
//							String srDataPath = DataSources.TORRENTS_DIR() + "/srdata_" + torrent.getName();
//							Files.write(Paths.get(srDataPath), 
//									srdata.bencode());
//							System.out.println("Wrote save_resume_data: " + srDataPath);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}



					}


					@Override
					public void saveResumeDataFailed(SaveResumeDataFailedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void peerDisconnected(PeerDisconnectedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void peerBan(PeerBanAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void peerError(PeerErrorAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void addTorrent(AddTorrentAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void peerBlocked(PeerBlockedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void trackerAnnounce(TrackerAnnounceAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void trackerReply(TrackerReplyAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
						//				torrent.setAutoManaged(false);
						//				torrent.pause();
						//				torrent.saveResumeData();
					}

					@Override
					public void trackerWarning(TrackerWarningAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}

					@Override
					public void trackerError(TrackerErrorAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}


					@Override
					public void dhtReply(DhtReplyAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
						//				torrent.setAutoManaged(true);
					}

					@Override
					public void torrentPaused(TorrentPausedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

//						torrent.saveResumeData();
						//				torrent.resume();
					}

					@Override
					public void torrentError(TorrentErrorAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentResumed(TorrentResumedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentUpdate(TorrentUpdateAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}


					@Override
					public void torrentChecked(TorrentCheckedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentRemoved(TorrentRemovedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentAdded(TorrentAddedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentDeleted(TorrentDeletedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void torrentDeleteFailed(TorrentDeleteFailedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());

					}


					@Override
					public void fastresumeRejected(FastresumeRejectedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void blockTimeout(BlockTimeoutAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void fileError(FileErrorAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void hashFailed(HashFailedAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void performance(PerformanceAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void scrapeReply(ScrapeReplyAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}

					@Override
					public void unwantedBlock(UnwantedBlockAlert alert) {
						System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
					}


				});



    
	}
}
