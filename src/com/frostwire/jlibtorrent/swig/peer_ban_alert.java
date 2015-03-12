/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class peer_ban_alert extends peer_alert {
  private long swigCPtr;

  protected peer_ban_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.peer_ban_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(peer_ban_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_peer_ban_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public peer_ban_alert(torrent_handle h, tcp_endpoint ep, sha1_hash peer_id) {
    this(libtorrent_jni.new_peer_ban_alert(torrent_handle.getCPtr(h), h, tcp_endpoint.getCPtr(ep), ep, sha1_hash.getCPtr(peer_id), peer_id), true);
  }

  public int type() {
    return libtorrent_jni.peer_ban_alert_type(swigCPtr, this);
  }

  public int category() {
    return libtorrent_jni.peer_ban_alert_category(swigCPtr, this);
  }

  public String what() {
    return libtorrent_jni.peer_ban_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.peer_ban_alert_message(swigCPtr, this);
  }

  public final static int alert_type = libtorrent_jni.peer_ban_alert_alert_type_get();
}
