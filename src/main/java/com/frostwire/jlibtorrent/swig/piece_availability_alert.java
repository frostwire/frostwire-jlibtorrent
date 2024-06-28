/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class piece_availability_alert extends torrent_alert {
  private transient long swigCPtr;

  protected piece_availability_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.piece_availability_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(piece_availability_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(piece_availability_alert obj) {
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
        libtorrent_jni.delete_piece_availability_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.piece_availability_alert_type(swigCPtr, this);
  }

  public alert_category_t category() {
    return new alert_category_t(libtorrent_jni.piece_availability_alert_category(swigCPtr, this), true);
  }

  public String what() {
    return libtorrent_jni.piece_availability_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.piece_availability_alert_message(swigCPtr, this);
  }

  public void setPiece_availability(int_vector value) {
    libtorrent_jni.piece_availability_alert_piece_availability_set(swigCPtr, this, int_vector.getCPtr(value), value);
  }

  public int_vector getPiece_availability() {
    long cPtr = libtorrent_jni.piece_availability_alert_piece_availability_get(swigCPtr, this);
    return (cPtr == 0) ? null : new int_vector(cPtr, false);
  }

  public final static alert_priority priority = alert_priority.swigToEnum(libtorrent_jni.piece_availability_alert_priority_get());
  public final static int alert_type = libtorrent_jni.piece_availability_alert_alert_type_get();
  public final static alert_category_t static_category = new alert_category_t(libtorrent_jni.piece_availability_alert_static_category_get(), false);
}