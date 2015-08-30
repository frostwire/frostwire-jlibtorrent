package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public interface StoragePlugin {

    void initialize(storage_error ec);

    int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec);

    int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec);

    boolean hasAnyFile(storage_error ec);

    void setFilePriority(unsigned_char_vector prio, storage_error ec);

    int moveStorage(String save_path, int flags, storage_error ec);

    boolean verifyResumeData(bdecode_node rd, string_vector links, storage_error ec);

    void writeResumeData(entry rd, storage_error ec);

    void releaseFiles(storage_error ec);

    void renameFile(int index, String new_filename, storage_error ec);

    void deleteFiles(storage_error ec);

    boolean tick();
}
