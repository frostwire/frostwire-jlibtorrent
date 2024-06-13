/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class scrape_failed_alert extends tracker_alert {
  private transient long swigCPtr;

  protected scrape_failed_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.scrape_failed_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(scrape_failed_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(scrape_failed_alert obj) {
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
        libtorrent_jni.delete_scrape_failed_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.scrape_failed_alert_type(swigCPtr, this);
  }

  public alert_category_t category() {
    return new alert_category_t(libtorrent_jni.scrape_failed_alert_category(swigCPtr, this), true);
  }

  public String what() {
    return libtorrent_jni.scrape_failed_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.scrape_failed_alert_message(swigCPtr, this);
  }

  public error_code getError() {
    long cPtr = libtorrent_jni.scrape_failed_alert_error_get(swigCPtr, this);
    return (cPtr == 0) ? null : new error_code(cPtr, false);
  }

  public String error_message() {
    return libtorrent_jni.scrape_failed_alert_error_message(swigCPtr, this);
  }

  public void setVersion(SWIGTYPE_p_protocol_version value) {
    libtorrent_jni.scrape_failed_alert_version_set(swigCPtr, this, SWIGTYPE_p_protocol_version.getCPtr(value));
  }

  public SWIGTYPE_p_protocol_version getVersion() {
    return new SWIGTYPE_p_protocol_version(libtorrent_jni.scrape_failed_alert_version_get(swigCPtr, this), true);
  }

  public final static alert_priority priority = alert_priority.swigToEnum(libtorrent_jni.scrape_failed_alert_priority_get());
  public final static int alert_type = libtorrent_jni.scrape_failed_alert_alert_type_get();
  public final static alert_category_t static_category = new alert_category_t(libtorrent_jni.scrape_failed_alert_static_category_get(), false);
}
