/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class ip_route_vector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ip_route_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ip_route_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_ip_route_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ip_route_vector() {
    this(libtorrent_jni.new_ip_route_vector(), true);
  }

  public long size() {
    return libtorrent_jni.ip_route_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.ip_route_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.ip_route_vector_reserve(swigCPtr, this, n);
  }

  public boolean empty() {
    return libtorrent_jni.ip_route_vector_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.ip_route_vector_clear(swigCPtr, this);
  }

  public void push_back(ip_route x) {
    libtorrent_jni.ip_route_vector_push_back(swigCPtr, this, ip_route.getCPtr(x), x);
  }

  public ip_route get(int i) {
    return new ip_route(libtorrent_jni.ip_route_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, ip_route val) {
    libtorrent_jni.ip_route_vector_set(swigCPtr, this, i, ip_route.getCPtr(val), val);
  }

}
