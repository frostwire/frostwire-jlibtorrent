/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class announce_endpoint_vector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected announce_endpoint_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(announce_endpoint_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_announce_endpoint_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public announce_endpoint_vector() {
    this(libtorrent_jni.new_announce_endpoint_vector(), true);
  }

  public long size() {
    return libtorrent_jni.announce_endpoint_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.announce_endpoint_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.announce_endpoint_vector_reserve(swigCPtr, this, n);
  }

  public boolean empty() {
    return libtorrent_jni.announce_endpoint_vector_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.announce_endpoint_vector_clear(swigCPtr, this);
  }

  public void push_back(announce_endpoint x) {
    libtorrent_jni.announce_endpoint_vector_push_back(swigCPtr, this, announce_endpoint.getCPtr(x), x);
  }

  public announce_endpoint get(int i) {
    return new announce_endpoint(libtorrent_jni.announce_endpoint_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, announce_endpoint val) {
    libtorrent_jni.announce_endpoint_vector_set(swigCPtr, this, i, announce_endpoint.getCPtr(val), val);
  }

}
