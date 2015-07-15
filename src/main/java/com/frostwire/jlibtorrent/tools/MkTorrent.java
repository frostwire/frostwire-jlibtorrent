package com.frostwire.jlibtorrent.tools;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MkTorrent extends Tool<Entry> {

    private final String id;
    private MkTorrentListener listener;

    public MkTorrent(String[] args) {
        super(args);
        this.id = UUID.randomUUID().toString();
    }

    public MkTorrentListener listener() {
        return listener;
    }

    public void listener(MkTorrentListener listener) {
        this.listener = listener;
    }

    @Override
    protected String usage() {
        return "usage: -i <file|dir> [-t1 <tracker1>]";
    }

    @Override
    protected ParseCmd parser(ParseCmd.Builder b) {
        return b.parm("-i", "<file|dir>").req().rex(".*")
                .parm("-t1", "udp://tracker.openbittorrent.com:80").rex(".*")
                .build();
    }

    @Override
    public Entry run() {
        File f = new File(arg("-i"));
        if (!f.exists()) {
            throw new IllegalStateException("File or directory " + f + " does not exists");
        }

        file_storage fs = new file_storage();

        if (listener != null) {
            add_files_listener l = new add_files_listener() {
                @Override
                public boolean pred(String id, String p) {
                    if (MkTorrent.this.id.equals(id)) {
                        return listener.pred(MkTorrent.this, p);
                    } else {
                        return false;
                    }
                }
            };
            libtorrent.add_files(id, fs, f.getAbsolutePath(), 0L, l);
        } else {
            libtorrent.add_files(fs, f.getAbsolutePath());
        }

        create_torrent ct = new create_torrent(fs);
        ct.add_tracker(arg("-t1"));

        error_code ec = new error_code();
        if (listener != null) {
            set_piece_hashes_listener l = new set_piece_hashes_listener() {
                @Override
                public void progress(String id, int num_pieces, int i) {
                    if (MkTorrent.this.id.equals(id)) {
                        listener.progress(MkTorrent.this, num_pieces, i);
                    }
                }
            };

            libtorrent.set_piece_hashes(id, ct, f.getParent(), ec, l);
        } else {
            libtorrent.set_piece_hashes(ct, f.getParent(), ec);
        }

        if (ec.value() != 0) {
            throw new IllegalStateException(ec.message());
        }

        Entry e = new Entry(ct.generate());

        if (listener != null) {
            listener.done(this, e);
        }

        return e;
    }

    public interface MkTorrentListener {

        boolean pred(MkTorrent mkt, String p);

        void progress(MkTorrent mkt, int numPieces, int i);

        void done(MkTorrent mkt, Entry e);
    }

    public static void main(String[] args) {
        MkTorrent t = new MkTorrent(args);

        t.listener(new MkTorrentListener() {
            @Override
            public boolean pred(MkTorrent mkt, String p) {
                File f = new File(p);
                if (f.isHidden()) {
                    System.out.println("Skipping hidden file: " + f);
                    return false;
                }
                if (f.isDirectory()) {
                    System.out.println("Entering directory: " + f);
                }
                if (f.isFile()) {
                    System.out.println("Adding file: " + f);
                }
                return true;
            }

            @Override
            public void progress(MkTorrent mkt, int numPieces, int i) {
                System.out.println("Calculated hash for piece: " + (i + 1) + " of " + numPieces);
            }

            @Override
            public void done(MkTorrent mkt, Entry e) {
                String torrent = mkt.arg("-i") + ".torrent";
                try {
                    FileOutputStream fos = new FileOutputStream(torrent);
                    fos.write(e.bencode());
                    fos.close();
                    System.out.println("Torrent file created at: " + torrent);
                } catch (Throwable t) {
                    System.out.println("Error creating torrent file");
                    t.printStackTrace();
                }
            }
        });

        t.run();
    }
}
