/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class web_seed_entry_vector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected web_seed_entry_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(web_seed_entry_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_web_seed_entry_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public web_seed_entry_vector() {
    this(libtorrent_jni.new_web_seed_entry_vector(), true);
  }

  public long size() {
    return libtorrent_jni.web_seed_entry_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.web_seed_entry_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.web_seed_entry_vector_reserve(swigCPtr, this, n);
  }

  public boolean empty() {
    return libtorrent_jni.web_seed_entry_vector_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.web_seed_entry_vector_clear(swigCPtr, this);
  }

  public void push_back(web_seed_entry x) {
    libtorrent_jni.web_seed_entry_vector_push_back(swigCPtr, this, web_seed_entry.getCPtr(x), x);
  }

  public web_seed_entry get(int i) {
    return new web_seed_entry(libtorrent_jni.web_seed_entry_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, web_seed_entry val) {
    libtorrent_jni.web_seed_entry_vector_set(swigCPtr, this, i, web_seed_entry.getCPtr(val), val);
  }

}
