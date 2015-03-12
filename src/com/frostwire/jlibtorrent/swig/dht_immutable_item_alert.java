/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.4
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class dht_immutable_item_alert extends alert {
  private long swigCPtr;

  protected dht_immutable_item_alert(long cPtr, boolean cMemoryOwn) {
    super(libtorrent_jni.dht_immutable_item_alert_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(dht_immutable_item_alert obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_dht_immutable_item_alert(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public dht_immutable_item_alert(sha1_hash t, entry i) {
    this(libtorrent_jni.new_dht_immutable_item_alert(sha1_hash.getCPtr(t), t, entry.getCPtr(i), i), true);
  }

  public int type() {
    return libtorrent_jni.dht_immutable_item_alert_type(swigCPtr, this);
  }

  public int category() {
    return libtorrent_jni.dht_immutable_item_alert_category(swigCPtr, this);
  }

  public String what() {
    return libtorrent_jni.dht_immutable_item_alert_what(swigCPtr, this);
  }

  public String message() {
    return libtorrent_jni.dht_immutable_item_alert_message(swigCPtr, this);
  }

  public boolean discardable() {
    return libtorrent_jni.dht_immutable_item_alert_discardable(swigCPtr, this);
  }

  public void setTarget(sha1_hash value) {
    libtorrent_jni.dht_immutable_item_alert_target_set(swigCPtr, this, sha1_hash.getCPtr(value), value);
  }

  public sha1_hash getTarget() {
    long cPtr = libtorrent_jni.dht_immutable_item_alert_target_get(swigCPtr, this);
    return (cPtr == 0) ? null : new sha1_hash(cPtr, false);
  }

  public void setItem(entry value) {
    libtorrent_jni.dht_immutable_item_alert_item_set(swigCPtr, this, entry.getCPtr(value), value);
  }

  public entry getItem() {
    return new entry(libtorrent_jni.dht_immutable_item_alert_item_get(swigCPtr, this), true);
  }

  public final static int alert_type = libtorrent_jni.dht_immutable_item_alert_alert_type_get();
  public final static int static_category = libtorrent_jni.dht_immutable_item_alert_static_category_get();
}
