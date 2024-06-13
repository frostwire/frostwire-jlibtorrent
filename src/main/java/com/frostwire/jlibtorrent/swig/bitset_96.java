/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class bitset_96 {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected bitset_96(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(bitset_96 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(bitset_96 obj) {
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
        libtorrent_jni.delete_bitset_96(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean test(long pos) {
    return libtorrent_jni.bitset_96_test(swigCPtr, this, pos);
  }

  public boolean all() {
    return libtorrent_jni.bitset_96_all(swigCPtr, this);
  }

  public boolean any() {
    return libtorrent_jni.bitset_96_any(swigCPtr, this);
  }

  public boolean none() {
    return libtorrent_jni.bitset_96_none(swigCPtr, this);
  }

  public long count() {
    return libtorrent_jni.bitset_96_count(swigCPtr, this);
  }

  public long size() {
    return libtorrent_jni.bitset_96_size(swigCPtr, this);
  }

  public boolean get(long pos) {
    return libtorrent_jni.bitset_96_get(swigCPtr, this, pos);
  }

  public bitset_96() {
    this(libtorrent_jni.new_bitset_96(), true);
  }

}
