/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class dht_routing_bucket {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected dht_routing_bucket(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(dht_routing_bucket obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_dht_routing_bucket(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setNum_nodes(int value) {
    libtorrent_jni.dht_routing_bucket_num_nodes_set(swigCPtr, this, value);
  }

  public int getNum_nodes() {
    return libtorrent_jni.dht_routing_bucket_num_nodes_get(swigCPtr, this);
  }

  public void setNum_replacements(int value) {
    libtorrent_jni.dht_routing_bucket_num_replacements_set(swigCPtr, this, value);
  }

  public int getNum_replacements() {
    return libtorrent_jni.dht_routing_bucket_num_replacements_get(swigCPtr, this);
  }

  public void setLast_active(int value) {
    libtorrent_jni.dht_routing_bucket_last_active_set(swigCPtr, this, value);
  }

  public int getLast_active() {
    return libtorrent_jni.dht_routing_bucket_last_active_get(swigCPtr, this);
  }

  public dht_routing_bucket() {
    this(libtorrent_jni.new_dht_routing_bucket(), true);
  }

}
