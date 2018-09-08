package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

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
    private final torrent_info ti;

    /**
     * @param fs the native object
     */
    public FileStorage(file_storage fs) {
        this(fs, null);
    }

    /**
     * Used to keep the torrent info reference around.
     *
     * @param fs the native object
     * @param ti the torrent info to pin
     */
    FileStorage(file_storage fs, torrent_info ti) {
        this.fs = fs;
        this.ti = ti;
    }

    /**
     * @return the native object
     */
    public file_storage swig() {
        return fs;
    }

    /**
     * This methods returns the internal torrent info or null
     * if it was constructed without one.
     * <p>
     * This also prevent premature garbage collection in case
     * the storage was created from a torrent info.
     *
     * @return the pinned torrent info
     */
    public torrent_info ti() {
        return ti;
    }

    /**
     * Returns true if the piece length has been initialized
     * on the file_storage. This is typically taken as a proxy
     * of whether the file_storage as a whole is initialized or
     * not.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return fs.is_valid();
    }

    /**
     * Allocates space for {@code numFiles} in the internal file list. This can
     * be used to avoid reallocating the internal file list when the number
     * of files to be added is known up-front.
     *
     * @param numFiles the number of files
     */
    public void reserve(int numFiles) {
        fs.reserve(numFiles);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #name(String)}.
     * <p>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param path    the path
     * @param size    the file size
     * @param flags   the file flags
     * @param mtime   the time
     * @param symlink the symlink
     */
    public void addFile(String path, long size, file_flags_t flags, int mtime, String symlink) {
        fs.add_file(path, size, flags, mtime, symlink);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #name(String)}.
     * <p>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param path  the path
     * @param size  the file size
     * @param flags the file flags
     * @param mtime the time
     */
    public void addFile(String path, long size, file_flags_t flags, int mtime) {
        fs.add_file(path, size, flags, mtime);
    }

    /**
     * Adds a file to the file storage. The {@code flags} argument sets attributes on the file.
     * The file attributes is an extension and may not work in all bittorrent clients.
     * <p>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #name(String)}.
     * <p>
     * The built in functions to traverse a directory to add files will
     * make sure this requirement is fulfilled.
     *
     * @param path  the path
     * @param size  the file size
     * @param flags the file flags
     */
    public void addFile(String path, long size, file_flags_t flags) {
        fs.add_file(path, size, flags);
    }

    /**
     * Adds a file to the file storage.
     * <p>
     * If more files than one are added, certain restrictions to their paths apply.
     * In a multi-file file storage (torrent), all files must share the same root directory.
     * <p>
     * That is, the first path element of all files must be the same.
     * This shared path element is also set to the name of the torrent. It
     * can be changed by calling {@link #name(String)}.
     * <p>
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
     * <p>
     * This is the inverse mapping of {@link #mapFile(int, long, int)}.
     *
     * @param piece
     * @param offset
     * @param size
     * @return
     */
    public ArrayList<FileSlice> mapBlock(int piece, long offset, int size) {
        return mapBlock(fs.map_block(piece, offset, size));
    }

    /**
     * Returns a {@link com.frostwire.jlibtorrent.PeerRequest} representing the
     * piece index, byte offset and size the specified file range overlaps.
     * This is the inverse mapping of {@link #mapBlock(int, long, int)}.
     * <p>
     * Note that the {@link PeerRequest} return type
     * is meant to hold bittorrent block requests, which may not be larger
     * than 16 kiB. Mapping a range larger than that may return an overflown
     * integer.
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
    public int numFiles() {
        return fs.num_files();
    }

    /**
     * Returns the total number of bytes all the files in this torrent spans.
     *
     * @return
     */
    public long totalSize() {
        return fs.total_size();
    }

    /**
     * Returns the number of pieces in the torrent.
     *
     * @return the number of pieces in the torrent
     */
    public int numPieces() {
        return fs.num_pieces();
    }

    /**
     * Set the number of pieces in the torrent.
     *
     * @param n
     */
    public void numPieces(int n) {
        fs.set_num_pieces(n);
    }

    /**
     * Get the size of each piece in this torrent. This size is typically an even power
     * of 2. It doesn't have to be though. It should be divisible by 16kiB however.
     *
     * @return
     */
    public int pieceLength() {
        return fs.piece_length();
    }

    /**
     * Set the size of each piece in this torrent. This size is typically an even power
     * of 2. It doesn't have to be though. It should be divisible by 16kiB however.
     *
     * @param l
     */
    public void pieceLength(int l) {
        fs.set_piece_length(l);
    }

    /**
     * Returns the piece size of {@code index}. This will be the same as {@link #pieceLength()},
     * except for the last piece, which may be shorter.
     *
     * @param index
     * @return
     */
    public int pieceSize(int index) {
        return fs.piece_size(index);
    }

    /**
     * Get the name of this torrent. For multi-file torrents, this is also
     * the name of the root directory all the files are stored in.
     *
     * @return
     */
    public String name() {
        return fs.name();
    }

    /**
     * Set the name of this torrent. For multi-file torrents, this is also
     * the name of the root directory all the files are stored in.
     *
     * @param name
     */
    public void name(String name) {
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
    public Sha1Hash hash(int index) {
        return new Sha1Hash(fs.hash(index));
    }

    /**
     * returns the full path to a file.
     *
     * @param index
     * @param savePath
     * @return
     */
    public String filePath(int index, String savePath) {
        // not calling the corresponding swig function because internally,
        // the use of the function GetStringUTFChars does not consider the case of
        // a copy not made
        return savePath + File.separator + fs.file_path(index);
    }

    /**
     * Returns the full path to a file.
     *
     * @param index the file index
     * @return the full path
     */
    public String filePath(int index) {
        return fs.file_path(index);
    }

    /**
     * Returns only the name of the file, whereas
     * {@link #filePath(int)} returns the path (inside the torrent file) with
     * the filename appended.
     *
     * @param index the file index
     * @return the file name
     */
    public String fileName(int index) {
        byte_vector v = fs.file_name(index).to_bytes();
        return Vectors.byte_vector2utf8(v);
    }

    /**
     * returns the size of a file.
     *
     * @param index
     * @return
     */
    public long fileSize(int index) {
        return fs.file_size(index);
    }

    /**
     * returns true if the file at the given
     * index is a pad-file.
     *
     * @param index
     * @return
     */
    public boolean padFileAt(int index) {
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
    public long fileOffset(int index) {
        return fs.file_offset(index);
    }

    /**
     * @return
     */
    public ArrayList<String> paths() {
        string_vector v = fs.paths();
        int size = (int) v.size();
        ArrayList<String> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    /**
     * This file is a pad file. The creator of the
     * torrent promises the file is entirely filled with
     * zeroes and does not need to be downloaded. The
     * purpose is just to align the next file to either
     * a block or piece boundary.
     */
    public static final file_flags_t FLAG_PAD_FILE = file_storage.flag_pad_file;

    /**
     * This file is hidden (sets the hidden attribute
     * on windows).
     */
    public static final file_flags_t FLAG_HIDDEN = file_storage.flag_hidden;

    /**
     * This file is executable (sets the executable bit
     * on posix like systems).
     */
    public static final file_flags_t FLAG_EXECUTABLE = file_storage.flag_executable;

    /**
     * This file is a symlink. The symlink target is
     * specified in a separate field
     */
    public static final file_flags_t FLAG_SYMLINK = file_storage.flag_symlink;

    /**
     * Returns a bitmask of flags from {@link file_flags_t} that apply
     * to file at {@code index}.
     *
     * @param index
     * @return the flags
     */
    public file_flags_t fileFlags(int index) {
        return fs.file_flags(index);
    }

    /**
     * Returns true if the file at the specified index has been renamed to
     * have an absolute path, i.e. is not anchored in the save path of the
     * torrent.
     *
     * @param index
     * @return
     */
    public boolean fileAbsolutePath(int index) {
        return fs.file_absolute_path(index);
    }

    /**
     * Returns the index of the file at the given offset in the torrent.
     *
     * @param offset
     * @return
     */
    public int fileIndexAtOffset(long offset) {
        return fs.file_index_at_offset(offset);
    }

    static ArrayList<FileSlice> mapBlock(file_slice_vector v) {
        int size = (int) v.size();

        ArrayList<FileSlice> l = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            l.add(new FileSlice(v.get(i)));
        }

        return l;
    }
}
