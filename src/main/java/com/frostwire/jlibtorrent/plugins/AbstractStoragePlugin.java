package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractStoragePlugin implements StoragePlugin {

    @Override
    public void initialize(storage_error ec) {
    }

    @Override
    public abstract int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec);

    @Override
    public abstract int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec);

    @Override
    public boolean hasAnyFile(storage_error ec) {
        return false;
    }

    @Override
    public int moveStorage(String save_path, int flags, storage_error ec) {
        return 0;
    }

    @Override
    public boolean verifyResumeData(add_torrent_params rd, string_vector links, storage_error ec) {
        return false;
    }

    @Override
    public void writeResumeData(entry rd, storage_error ec) {
    }

    @Override
    public void releaseFiles(storage_error ec) {
    }

    @Override
    public void renameFile(int index, String new_filename, storage_error ec) {
    }

    @Override
    public void deleteFiles(storage_error ec) {
    }

    @Override
    public boolean tick() {
        return false;
    }
}
