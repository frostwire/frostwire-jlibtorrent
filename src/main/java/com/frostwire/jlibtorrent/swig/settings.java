/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class settings extends dht_settings {
  private transient long swigCPtr;

  protected settings(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.settings_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(settings obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_settings(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public void setPrefer_verified_node_ids(boolean value) {
    libtorrent_jni.settings_prefer_verified_node_ids_set(swigCPtr, this, value);
  }

  public boolean getPrefer_verified_node_ids() {
    return libtorrent_jni.settings_prefer_verified_node_ids_get(swigCPtr, this);
  }

  public settings() {
    this(libtorrent_jni.new_settings(), true);
  }

}
