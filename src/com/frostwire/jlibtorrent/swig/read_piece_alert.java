/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.4
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class read_piece_alert extends torrent_alert {
  private long swigCPtr;

  protected read_piece_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.read_piece_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(read_piece_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_read_piece_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.read_piece_alert_type(swigCPtr, this);
  }

  public int category() {
    return libtorrent_jni.read_piece_alert_category(swigCPtr, this);
  }

  public String what() {
    return libtorrent_jni.read_piece_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.read_piece_alert_message(swigCPtr, this);
  }

  public boolean discardable() {
    return libtorrent_jni.read_piece_alert_discardable(swigCPtr, this);
  }

  public void setEc(error_code value) {
    libtorrent_jni.read_piece_alert_ec_set(swigCPtr, this, error_code.getCPtr(value), value);
  }

  public error_code getEc() {
    long cPtr = libtorrent_jni.read_piece_alert_ec_get(swigCPtr, this);
    return (cPtr == 0) ? null : new error_code(cPtr, false);
  }

  public void setPiece(int value) {
    libtorrent_jni.read_piece_alert_piece_set(swigCPtr, this, value);
  }

  public int getPiece() {
    return libtorrent_jni.read_piece_alert_piece_get(swigCPtr, this);
  }

  public void setSize(int value) {
    libtorrent_jni.read_piece_alert_size_set(swigCPtr, this, value);
  }

  public int getSize() {
    return libtorrent_jni.read_piece_alert_size_get(swigCPtr, this);
  }

  public final static int alert_type = libtorrent_jni.read_piece_alert_alert_type_get();
  public final static int static_category = libtorrent_jni.read_piece_alert_static_category_get();
}
