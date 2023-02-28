/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class ip_interface_vector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ip_interface_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ip_interface_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(ip_interface_vector obj) {
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

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_ip_interface_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ip_interface_vector() {
    this(libtorrent_jni.new_ip_interface_vector(), true);
  }

  public long size() {
    return libtorrent_jni.ip_interface_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.ip_interface_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.ip_interface_vector_reserve(swigCPtr, this, n);
  }

  public boolean empty() {
    return libtorrent_jni.ip_interface_vector_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.ip_interface_vector_clear(swigCPtr, this);
  }

  public void push_back(ip_interface x) {
    libtorrent_jni.ip_interface_vector_push_back(swigCPtr, this, ip_interface.getCPtr(x), x);
  }

  public ip_interface get(int i) {
    return new ip_interface(libtorrent_jni.ip_interface_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, ip_interface val) {
    libtorrent_jni.ip_interface_vector_set(swigCPtr, this, i, ip_interface.getCPtr(val), val);
  }

}
