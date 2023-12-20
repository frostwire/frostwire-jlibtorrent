/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class bt_peer_connection_handle extends peer_connection_handle {
  private transient long swigCPtr;

  protected bt_peer_connection_handle(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.bt_peer_connection_handle_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(bt_peer_connection_handle obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(bt_peer_connection_handle obj) {
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
        libtorrent_jni.delete_bt_peer_connection_handle(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public bt_peer_connection_handle(peer_connection_handle pc) {
    this(libtorrent_jni.new_bt_peer_connection_handle(peer_connection_handle.getCPtr(pc), pc), true);
  }

  public boolean packet_finished() {
    return libtorrent_jni.bt_peer_connection_handle_packet_finished(swigCPtr, this);
  }

  public boolean support_extensions() {
    return libtorrent_jni.bt_peer_connection_handle_support_extensions(swigCPtr, this);
  }

  public boolean supports_encryption() {
    return libtorrent_jni.bt_peer_connection_handle_supports_encryption(swigCPtr, this);
  }

}
