package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_slice_vector;
import com.frostwire.jlibtorrent.swig.file_storage;

import java.io.File;
import java.util.ArrayList;

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
     * Allocates space for {@code numFiles} in the internal file list. This can
     * be used to avoid reallocating the internal file list when the number
     * of files to be added is known up-front.
     *
     * @param numFiles
     */
    public void reserve(int numFiles) {
        fs.reserve(numFiles);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p/>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p/>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #setName(String)}.
     * <p/>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param path
     * @param fileSize
     * @param fileFlags
     * @param mtime
     * @param symlinkPath
     * @see com.frostwire.jlibtorrent.FileStorage.Flags
     */
    public void addFile(String path, long fileSize, Flags fileFlags, int mtime, String symlinkPath) {
        fs.add_file(path, fileSize, fileFlags.getSwig(), mtime, symlinkPath);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p/>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p/>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #setName(String)}.
     * <p/>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param p
     * @param size
     * @param flags
     * @param mtime
     * @see com.frostwire.jlibtorrent.FileStorage.Flags
     */
    public void addFile(String p, long size, Flags flags, int mtime) {
        fs.add_file(p, size, flags.getSwig(), mtime);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p/>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p/>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #setName(String)}.
     * <p/>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param p
     * @param size
     * @param flags
     * @see com.frostwire.jlibtorrent.FileStorage.Flags
     */
    public void addFile(String p, long size, Flags flags) {
        fs.add_file(p, size, flags.getSwig());
    }

    /**
     * Adds a file to the file storage.
     * <p/>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p/>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #setName(String)}.
     * <p/>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param p
     * @param size
     */
    public void addFile(String p, long size) {
        fs.add_file(p, size);
    }

    /**
     * Renames the file at {@code index} to {@code newFilename}. Keep in mind
     * that filenames are expected to be UTF-8 encoded.
     *
     * @param index
     * @param newFilename
     */
    public void renameFile(int index, String newFilename) {
        fs.rename_file(index, newFilename);
    }

    /**
     * Returns a list of {@link com.frostwire.jlibtorrent.FileSlice} objects representing the portions of
     * files the specified piece index, byte offset and size range overlaps.
     * <p/>
     * This is the inverse mapping of {@link #mapFile(int, long, int)}.
     *
     * @param piece
     * @param offset
     * @param size
     * @return
     */
    public ArrayList<FileSlice> mapBlock(int piece, long offset, int size) {
        file_slice_vector v = fs.map_block(piece, offset, size);
        int vSize = (int) v.size();

        ArrayList<FileSlice> l = new ArrayList<FileSlice>(vSize);
        for (int i = 0; i < vSize; i++) {
            l.add(new FileSlice(v.get(i)));
        }

        return l;
    }

    /**
     * Returns a {@link com.frostwire.jlibtorrent.PeerRequest} representing the piece index, byte offset
     * and size the specified file range overlaps. This is the inverse
     * mapping ove {@link #mapBlock(int, long, int)}.
     *
     * @param file
     * @param offset
     * @param size
     * @return
     */
    public PeerRequest mapFile(int file, long offset, int size) {
        return new PeerRequest(fs.map_file(file, offset, size));
    }

    /**
     * Returns the number of files in the file_storage.
     *
     * @return
     */
    public int getNumFiles() {
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

    /**
     * File attribute flags.
     */
    public enum Flags {

        /**
         * The file is a pad file. It's required to contain zeroes
         * at it will not be saved to disk. Its purpose is to make
         * the following file start on a piece boundary.
         */
        PAD_FILE(file_storage.flags_t.pad_file.swigValue()),

        /**
         * This file has the hidden attribute set. This is primarily
         * a windows attribute
         */
        ATTRIBUTE_HIDDEN(file_storage.flags_t.attribute_hidden.swigValue()),

        /**
         * This file has the executable attribute set.
         */
        ATTRIBUTE_EXECUTABLE(file_storage.flags_t.attribute_executable.swigValue()),

        /**
         * This file is a symbolic link. It should have a link
         * target string associated with it.
         */
        ATTRIBUTE_SYMLINK(file_storage.flags_t.attribute_symlink.swigValue());

        private Flags(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }
    }
}
