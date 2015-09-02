/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class default_storage {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected default_storage(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(default_storage obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_default_storage(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public default_storage(storage_params params) {
    this(libtorrent_jni.new_default_storage(storage_params.getCPtr(params), params), true);
  }

  public boolean has_any_file(storage_error ec) {
    return libtorrent_jni.default_storage_has_any_file(swigCPtr, this, storage_error.getCPtr(ec), ec);
  }

  public void set_file_priority(unsigned_char_vector prio, storage_error ec) {
    libtorrent_jni.default_storage_set_file_priority(swigCPtr, this, unsigned_char_vector.getCPtr(prio), prio, storage_error.getCPtr(ec), ec);
  }

  public void rename_file(int index, String new_filename, storage_error ec) {
    libtorrent_jni.default_storage_rename_file(swigCPtr, this, index, new_filename, storage_error.getCPtr(ec), ec);
  }

  public void release_files(storage_error ec) {
    libtorrent_jni.default_storage_release_files(swigCPtr, this, storage_error.getCPtr(ec), ec);
  }

  public void delete_files(storage_error ec) {
    libtorrent_jni.default_storage_delete_files(swigCPtr, this, storage_error.getCPtr(ec), ec);
  }

  public void initialize(storage_error ec) {
    libtorrent_jni.default_storage_initialize(swigCPtr, this, storage_error.getCPtr(ec), ec);
  }

  public int move_storage(String save_path, int flags, storage_error ec) {
    return libtorrent_jni.default_storage_move_storage(swigCPtr, this, save_path, flags, storage_error.getCPtr(ec), ec);
  }

  public boolean verify_resume_data(bdecode_node rd, string_vector links, storage_error error) {
    return libtorrent_jni.default_storage_verify_resume_data(swigCPtr, this, bdecode_node.getCPtr(rd), rd, string_vector.getCPtr(links), links, storage_error.getCPtr(error), error);
  }

  public void write_resume_data(entry rd, storage_error ec) {
    libtorrent_jni.default_storage_write_resume_data(swigCPtr, this, entry.getCPtr(rd), rd, storage_error.getCPtr(ec), ec);
  }

  public boolean tick() {
    return libtorrent_jni.default_storage_tick(swigCPtr, this);
  }

  public file_storage files() {
    return new file_storage(libtorrent_jni.default_storage_files(swigCPtr, this), false);
  }

  public static boolean disk_write_access_log() {
    return libtorrent_jni.default_storage_disk_write_access_log__SWIG_0();
  }

  public static void disk_write_access_log(boolean enable) {
    libtorrent_jni.default_storage_disk_write_access_log__SWIG_1(enable);
  }

}