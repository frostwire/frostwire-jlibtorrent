package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_storage;

import java.io.File;

/**
 * This class represents a file list and the piece
 * size. Everything necessary to interpret a regular bittorrent storage
 * file structure.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileStorage {

    private final file_storage fs;

    public FileStorage(file_storage fs) {
        this.fs = fs;
    }

    public file_storage getSwig() {
        return fs;
    }

    /**
     * Returns true if the piece length has been initialized
     * on the file_storage. This is typically taken as a proxy
     * of whether the file_storage as a whole is initialized or
     * not.
     *
     * @return
     */
    public boolean isValid() {
        return fs.is_valid();
    }

    /**
     * Returns the number of files in the file_storage.
     *
     * @return
     */
    public int geNumFiles() {
        return fs.num_files();
    }

    /**
     * Returns the total number of bytes all the files in this torrent spans.
     *
     * @return
     */
    public long getTotalSize() {
        return fs.total_size();
    }

    /**
     * Get the name of this torrent. For multi-file torrents, this is also
     * the name of the root directory all the files are stored in.
     *
     * @return
     */
    public String getName() {
        return fs.name();
    }

    /**
     * Set the name of this torrent. For multi-file torrents, this is also
     * the name of the root directory all the files are stored in.
     *
     * @param name
     */
    public void setName(String name) {
        fs.set_name(name);
    }

    /**
     * Is a sha-1 hash of the file, or 0 if none was
     * provided in the torrent file. This can potentially be used to
     * join a bittorrent network with other file sharing networks.
     *
     * @param index
     * @return
     */
    public Sha1Hash getHash(int index) {
        return new Sha1Hash(fs.hash(index));
    }

    /**
     * returns the full path to a file.
     *
     * @param index
     * @param savePath
     * @return
     */
    public String getFilePath(int index, String savePath) {
        // not calling the corresponding swig function because internally,
        // the use of the function GetStringUTFChars does not consider the case of
        // a copy not made
        return savePath + File.separator + fs.file_path(index);
    }

    /**
     * returns the full path to a file.
     *
     * @param index
     * @return
     */
    public String getFilePath(int index) {
        return fs.file_path(index);
    }

    /**
     * returns *just* the name of the file, whereas
     * ``file_path()`` returns the path (inside the torrent file) with
     * the filename appended.
     *
     * @param index
     * @return
     */
    public String getFileName(int index) {
        return fs.file_name(index);
    }

    /**
     * returns the size of a file.
     *
     * @param index
     * @return
     */
    public long getFileSize(int index) {
        return fs.file_size(index);
    }

    /**
     * returns true if the file at the given
     * index is a pad-file.
     *
     * @param index
     * @return
     */
    public boolean isPadFileAt(int index) {
        return fs.pad_file_at(index);
    }

    /**
     * returns the byte offset within the torrent file
     * where this file starts. It can be used to map the file to a piece
     * index (given the piece size).
     *
     * @param index
     * @return
     */
    public long getFileOffset(int index) {
        return fs.file_offset(index);
    }
}
