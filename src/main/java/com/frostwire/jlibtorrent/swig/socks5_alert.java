/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class socks5_alert extends alert {
  private transient long swigCPtr;

  protected socks5_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.socks5_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(socks5_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_socks5_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.socks5_alert_type(swigCPtr, this);
  }

  public alert_category_t category() {
    return new alert_category_t(libtorrent_jni.socks5_alert_category(swigCPtr, this), true);
  }

  public String what() {
    return libtorrent_jni.socks5_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.socks5_alert_message(swigCPtr, this);
  }

  public void setError(error_code value) {
    libtorrent_jni.socks5_alert_error_set(swigCPtr, this, error_code.getCPtr(value), value);
  }

  public error_code getError() {
    long cPtr = libtorrent_jni.socks5_alert_error_get(swigCPtr, this);
    return (cPtr == 0) ? null : new error_code(cPtr, false);
  }

  public void setOp(operation_t value) {
    libtorrent_jni.socks5_alert_op_set(swigCPtr, this, value.swigValue());
  }

  public operation_t getOp() {
    return operation_t.swigToEnum(libtorrent_jni.socks5_alert_op_get(swigCPtr, this));
  }

  public void setIp(SWIGTYPE_p_libtorrent__aux__noexcept_movableT_libtorrent__tcp__endpoint_t value) {
    libtorrent_jni.socks5_alert_ip_set(swigCPtr, this, SWIGTYPE_p_libtorrent__aux__noexcept_movableT_libtorrent__tcp__endpoint_t.getCPtr(value));
  }

  public SWIGTYPE_p_libtorrent__aux__noexcept_movableT_libtorrent__tcp__endpoint_t getIp() {
    long cPtr = libtorrent_jni.socks5_alert_ip_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_libtorrent__aux__noexcept_movableT_libtorrent__tcp__endpoint_t(cPtr, false);
  }

  public final static int priority = libtorrent_jni.socks5_alert_priority_get();
  public final static int alert_type = libtorrent_jni.socks5_alert_alert_type_get();
  public final static alert_category_t static_category = new alert_category_t(libtorrent_jni.socks5_alert_static_category_get(), false);
}
