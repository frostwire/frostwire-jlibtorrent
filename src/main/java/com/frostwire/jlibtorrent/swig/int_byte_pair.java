/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class int_byte_pair {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected int_byte_pair(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(int_byte_pair obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(int_byte_pair obj) {
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
        libtorrent_jni.delete_int_byte_pair(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public int_byte_pair() {
    this(libtorrent_jni.new_int_byte_pair__SWIG_0(), true);
  }

  public int_byte_pair(int first, byte second) {
    this(libtorrent_jni.new_int_byte_pair__SWIG_1(first, second), true);
  }

  public int_byte_pair(int_byte_pair other) {
    this(libtorrent_jni.new_int_byte_pair__SWIG_2(int_byte_pair.getCPtr(other), other), true);
  }

  public void setFirst(int value) {
    libtorrent_jni.int_byte_pair_first_set(swigCPtr, this, value);
  }

  public int getFirst() {
    return libtorrent_jni.int_byte_pair_first_get(swigCPtr, this);
  }

  public void setSecond(byte value) {
    libtorrent_jni.int_byte_pair_second_set(swigCPtr, this, value);
  }

  public byte getSecond() {
    return libtorrent_jni.int_byte_pair_second_get(swigCPtr, this);
  }

}