/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class posix_stat_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected posix_stat_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(posix_stat_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(posix_stat_t obj) {
    long ptr = 0;
    if (obj != null) {
      if (!obj.swigCMemOwn)
        throw new RuntimeException("Cannot release ownership as memory is not owned");
      ptr = obj.swigCPtr;
      obj.swigCMemOwn = false;
      obj.delete();
    }
    return ptr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_posix_stat_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setSize(long value) {
    libtorrent_jni.posix_stat_t_size_set(swigCPtr, this, value);
  }

  public long getSize() {
    return libtorrent_jni.posix_stat_t_size_get(swigCPtr, this);
  }

  public void setAtime(long value) {
    libtorrent_jni.posix_stat_t_atime_set(swigCPtr, this, value);
  }

  public long getAtime() {
    return libtorrent_jni.posix_stat_t_atime_get(swigCPtr, this);
  }

  public void setMtime(long value) {
    libtorrent_jni.posix_stat_t_mtime_set(swigCPtr, this, value);
  }

  public long getMtime() {
    return libtorrent_jni.posix_stat_t_mtime_get(swigCPtr, this);
  }

  public void setCtime(long value) {
    libtorrent_jni.posix_stat_t_ctime_set(swigCPtr, this, value);
  }

  public long getCtime() {
    return libtorrent_jni.posix_stat_t_ctime_get(swigCPtr, this);
  }

  public void setMode(int value) {
    libtorrent_jni.posix_stat_t_mode_set(swigCPtr, this, value);
  }

  public int getMode() {
    return libtorrent_jni.posix_stat_t_mode_get(swigCPtr, this);
  }

  public posix_stat_t() {
    this(libtorrent_jni.new_posix_stat_t(), true);
  }

}
