package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_entry;

/**
 * information about a file in a file_storage
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileEntry {

    private final file_entry e;

    public FileEntry(file_entry e) {
        this.e = e;
    }

    public file_entry getSwig() {
        return e;
    }

    /**
     * the full path of this file. The paths are unicode strings
     * encoded in UTF-8.
     *
     * @return
     */
    public String getPath() {
        return e.getPath();
    }

    /**
     * the path which this is a symlink to, or empty if this is
     * not a symlink. This field is only used if the ``symlink_attribute`` is set.
     *
     * @return
     */
    public String getSymlinkPath() {
        return e.getSymlink_path();
    }

    /**
     * the offset of this file inside the torrent.
     *
     * @return
     */
    public long getOffset() {
        return e.getOffset();
    }

    /**
     * the size of the file (in bytes) and ``offset`` is the byte offset
     * of the file within the torrent. i.e. the sum of all the sizes of the files
     * before it in the list.
     *
     * @return
     */
    public long getSize() {
        return e.getSize();
    }

    /**
     * the offset in the file where the storage should start. The normal
     * case is to have this set to 0, so that the storage starts saving data at the start
     * if the file. In cases where multiple files are mapped into the same file though,
     * the ``file_base`` should be set to an offset so that the different regions do
     * not overlap. This is used when mapping "unselected" files into a so-called part
     * file.
     *
     * @return
     */
    public long getFileBase() {
        return e.getFile_base();
    }

    /**
     * the modification time of this file specified in posix time.
     *
     * @return
     */
    public int getMTime() {
        return e.getMtime();
    }

    /**
     * a sha-1 hash of the content of the file, or zeroes, if no
     * file hash was present in the torrent file. It can be used to potentially
     * find alternative sources for the file.
     *
     * @return
     */
    public Sha1Hash getFileHash() {
        return new Sha1Hash(e.getFilehash());
    }

    /**
     * set to true for files that are not part of the data of the torrent.
     * They are just there to make sure the next file is aligned to a particular byte offset
     * or piece boundry. These files should typically be hidden from an end user. They are
     * not written to disk.
     *
     * @return
     */
    public boolean isPadFile() {
        return e.getPad_file();
    }

    /**
     * true if the file was marked as hidden (on windows).
     *
     * @return
     */
    public boolean hasHiddenAttribute() {
        return e.getHidden_attribute();
    }

    /**
     * true if the file was marked as executable (posix)
     *
     * @return
     */
    public boolean hasExecutableAttribute() {
        return e.getExecutable_attribute();
    }

    /**
     * true if the file was a symlink. If this is the case
     * the ``symlink_index`` refers to a string which specifies the original location
     * where the data for this file was found.
     *
     * @return
     */
    public boolean hasSymlinkAttribute() {
        return e.getSymlink_attribute();
    }
}
