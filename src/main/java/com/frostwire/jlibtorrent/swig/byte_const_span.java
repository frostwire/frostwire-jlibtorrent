/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class byte_const_span {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected byte_const_span(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(byte_const_span obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(byte_const_span obj) {
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
        libtorrent_jni.delete_byte_const_span(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public byte_const_span() {
    this(libtorrent_jni.new_byte_const_span(), true);
  }

  public long size() {
    return libtorrent_jni.byte_const_span_size(swigCPtr, this);
  }

  public boolean empty() {
    return libtorrent_jni.byte_const_span_empty(swigCPtr, this);
  }

  public byte front() {
    return libtorrent_jni.byte_const_span_front(swigCPtr, this);
  }

  public byte back() {
    return libtorrent_jni.byte_const_span_back(swigCPtr, this);
  }

  public byte_const_span first(long n) {
    return new byte_const_span(libtorrent_jni.byte_const_span_first(swigCPtr, this, n), true);
  }

  public byte_const_span last(long n) {
    return new byte_const_span(libtorrent_jni.byte_const_span_last(swigCPtr, this, n), true);
  }

  public byte_const_span subspan(long offset) {
    return new byte_const_span(libtorrent_jni.byte_const_span_subspan__SWIG_0(swigCPtr, this, offset), true);
  }

  public byte_const_span subspan(long offset, long count) {
    return new byte_const_span(libtorrent_jni.byte_const_span_subspan__SWIG_1(swigCPtr, this, offset, count), true);
  }

  public byte get(long idx) {
    return libtorrent_jni.byte_const_span_get(swigCPtr, this, idx);
  }

}
