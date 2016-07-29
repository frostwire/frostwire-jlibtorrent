package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.StorageError;
import com.frostwire.jlibtorrent.StorageParams;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigStoragePlugin extends swig_storage {

    private static final WeakHashMap

    private final StoragePlugin p;

    public SwigStoragePlugin(StoragePlugin p) {
        this.p = p;
    }

    @Override
    public void set_params(storage_params params) {
        p.setParams(new StorageParams(params));
    }

    @Override
    public void initialize(storage_error ec) {
        p.initialize(ec);
    }

    @Override
    public int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        return p.read(iov_base, iov_len, piece, offset, flags, ec);
    }

    @Override
    public int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        return p.write(iov_base, iov_len, piece, offset, flags, ec);
    }

    @Override
    public boolean has_any_file(storage_error ec) {
        return p.hasAnyFile(new StorageError(ec));
    }

    @Override
    public void set_file_priority_vector(byte_vector prio, storage_error ec) {
        byte[] arr = new byte[(int) prio.size()];
        p.setFilePriority(arr, new StorageError(ec));
        Vectors.bytes2byte_vector(arr, prio);
    }

    @Override
    public int move_storage(String save_path, int flags, storage_error ec) {
        return p.moveStorage(save_path, flags, new StorageError(ec));
    }

    @Override
    public boolean verify_resume_data(add_torrent_params rd, string_vector links, storage_error ec) {
        ArrayList<String> l = new ArrayList<>();
        boolean r = p.verifyResumeData(new AddTorrentParams(rd), l, new StorageError(ec));
        for (int i = 0; i < l.size(); i++) {
            links.set(i, l.get(i));
        }
        return r;
    }

    @Override
    public void release_files(storage_error ec) {
        p.releaseFiles(new StorageError(ec));
    }

    @Override
    public void rename_file(int index, String new_filename, storage_error ec) {
        p.renameFile(index, new_filename, new StorageError(ec));
    }

    @Override
    public void delete_files(int options, storage_error ec) {
        p.deleteFiles(new StorageError(ec));
    }

    @Override
    public boolean tick() {
        return p.tick();
    }
}
