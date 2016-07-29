package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.StorageError;
import com.frostwire.jlibtorrent.StorageParams;

import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public interface StoragePlugin {

    void setParams(StorageParams params);

    void initialize(StorageError ec);

    int read(long iov_base, long iov_len, int piece, int offset, int flags, StorageError ec);

    int write(long iov_base, long iov_len, int piece, int offset, int flags, StorageError ec);

    boolean hasAnyFile(StorageError ec);

    void setFilePriority(byte[] prio, StorageError ec);

    int moveStorage(String save_path, int flags, StorageError ec);

    boolean verifyResumeData(AddTorrentParams rd, ArrayList<String> links, StorageError ec);

    void releaseFiles(StorageError ec);

    void renameFile(int index, String new_filename, StorageError ec);

    void deleteFiles(StorageError ec);

    boolean tick();
}
