package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigStoragePlugin extends swig_storage {

    private static final Logger LOG = Logger.getLogger(SwigStoragePlugin.class);

    private final StoragePlugin p;

    public SwigStoragePlugin(StoragePlugin p) {
        this.p = p;
    }

    @Override
    public void initialize(storage_error ec) {
        try {
            p.initialize(ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (initialize)", e);
        }
    }

    @Override
    public int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        try {
            return p.read(iov_base, iov_len, piece, offset, flags, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (read)", e);
        }

        return 0;
    }

    @Override
    public int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        try {
            return p.write(iov_base, iov_len, piece, offset, flags, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (write)", e);
        }

        return 0;
    }

    @Override
    public boolean has_any_file(storage_error ec) {
        try {
            p.hasAnyFile(ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (has_any_file)", e);
        }

        return false;
    }

    @Override
    public int move_storage(String save_path, int flags, storage_error ec) {
        try {
            p.moveStorage(save_path, flags, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (move_storage)", e);
        }
        return 0;
    }

    @Override
    public boolean verify_resume_data(bdecode_node rd, string_vector links, storage_error ec) {
        try {
            p.verifyResumeData(rd, links, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (verify_resume_data)", e);
        }
        return false;
    }

    @Override
    public void write_resume_data(entry rd, storage_error ec) {
        try {
            p.writeResumeData(rd, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (write_resume_data)", e);
        }
    }

    @Override
    public void release_files(storage_error ec) {
        try {
            p.releaseFiles(ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (release_files)", e);
        }
    }

    @Override
    public void rename_file(int index, String new_filename, storage_error ec) {
        try {
            p.renameFile(index, new_filename, ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (rename_file)", e);
        }
    }

    @Override
    public void delete_files(int options, storage_error ec) {
        try {
            p.deleteFiles(ec);
        } catch (Throwable e) {
            LOG.error("Error in plugin (delete_files)", e);
        }
    }

    @Override
    public boolean tick() {
        try {
            p.tick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (tick)", e);
        }
        return false;
    }
}
