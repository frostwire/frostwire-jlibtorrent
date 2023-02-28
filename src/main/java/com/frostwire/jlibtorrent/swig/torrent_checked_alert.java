/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class torrent_checked_alert extends torrent_alert {
  private transient long swigCPtr;

  protected torrent_checked_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.torrent_checked_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(torrent_checked_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(torrent_checked_alert obj) {
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

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_torrent_checked_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.torrent_checked_alert_type(swigCPtr, this);
  }

  public alert_category_t category() {
    return new alert_category_t(libtorrent_jni.torrent_checked_alert_category(swigCPtr, this), true);
  }

  public String what() {
    return libtorrent_jni.torrent_checked_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.torrent_checked_alert_message(swigCPtr, this);
  }

  public final static int priority = libtorrent_jni.torrent_checked_alert_priority_get();
  public final static int alert_type = libtorrent_jni.torrent_checked_alert_alert_type_get();
  public final static alert_category_t static_category = new alert_category_t(libtorrent_jni.torrent_checked_alert_static_category_get(), false);
}
