/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class list_files_listener {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected list_files_listener(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(list_files_listener obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(list_files_listener obj) {
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
        libtorrent_jni.delete_list_files_listener(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean pred(String p) {
    return libtorrent_jni.list_files_listener_pred(swigCPtr, this, p);
  }

  public list_files_listener() {
    this(libtorrent_jni.new_list_files_listener(), true);
  }

}
