/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class incoming_connection_alert extends alert {
  private transient long swigCPtr;

  protected incoming_connection_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.incoming_connection_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(incoming_connection_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(incoming_connection_alert obj) {
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
        libtorrent_jni.delete_incoming_connection_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public int type() {
    return libtorrent_jni.incoming_connection_alert_type(swigCPtr, this);
  }

  public alert_category_t category() {
    return new alert_category_t(libtorrent_jni.incoming_connection_alert_category(swigCPtr, this), true);
  }

  public String what() {
    return libtorrent_jni.incoming_connection_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.incoming_connection_alert_message(swigCPtr, this);
  }

  public void setSocket_type(SWIGTYPE_p_socket_type_t value) {
    libtorrent_jni.incoming_connection_alert_socket_type_set(swigCPtr, this, SWIGTYPE_p_socket_type_t.getCPtr(value));
  }

  public SWIGTYPE_p_socket_type_t getSocket_type() {
    return new SWIGTYPE_p_socket_type_t(libtorrent_jni.incoming_connection_alert_socket_type_get(swigCPtr, this), true);
  }

  public tcp_endpoint get_endpoint() {
    return new tcp_endpoint(libtorrent_jni.incoming_connection_alert_get_endpoint(swigCPtr, this), true);
  }

  public final static alert_priority priority = alert_priority.swigToEnum(libtorrent_jni.incoming_connection_alert_priority_get());
  public final static int alert_type = libtorrent_jni.incoming_connection_alert_alert_type_get();
  public final static alert_category_t static_category = new alert_category_t(libtorrent_jni.incoming_connection_alert_static_category_get(), false);
}
