/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class dht_routing_bucket_vector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected dht_routing_bucket_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(dht_routing_bucket_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_dht_routing_bucket_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public dht_routing_bucket_vector() {
    this(libtorrent_jni.new_dht_routing_bucket_vector(), true);
  }

  public long size() {
    return libtorrent_jni.dht_routing_bucket_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.dht_routing_bucket_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.dht_routing_bucket_vector_reserve(swigCPtr, this, n);
  }

  public boolean empty() {
    return libtorrent_jni.dht_routing_bucket_vector_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.dht_routing_bucket_vector_clear(swigCPtr, this);
  }

  public void push_back(dht_routing_bucket x) {
    libtorrent_jni.dht_routing_bucket_vector_push_back(swigCPtr, this, dht_routing_bucket.getCPtr(x), x);
  }

  public dht_routing_bucket get(int i) {
    return new dht_routing_bucket(libtorrent_jni.dht_routing_bucket_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, dht_routing_bucket val) {
    libtorrent_jni.dht_routing_bucket_vector_set(swigCPtr, this, i, dht_routing_bucket.getCPtr(val), val);
  }

}
