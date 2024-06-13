/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class peer_flags_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected peer_flags_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(peer_flags_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(peer_flags_t obj) {
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
        libtorrent_jni.delete_peer_flags_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public peer_flags_t() {
    this(libtorrent_jni.new_peer_flags_t(), true);
  }

  public static peer_flags_t all() {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_all(), true);
  }

  public boolean non_zero() {
    return libtorrent_jni.peer_flags_t_non_zero(swigCPtr, this);
  }

  public boolean nonZero() {
    return libtorrent_jni.peer_flags_t_nonZero(swigCPtr, this);
  }

  public boolean eq(peer_flags_t f) {
    return libtorrent_jni.peer_flags_t_eq(swigCPtr, this, peer_flags_t.getCPtr(f), f);
  }

  public boolean ne(peer_flags_t f) {
    return libtorrent_jni.peer_flags_t_ne(swigCPtr, this, peer_flags_t.getCPtr(f), f);
  }

  public peer_flags_t or_(peer_flags_t other) {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_or_(swigCPtr, this, peer_flags_t.getCPtr(other), other), true);
  }

  public peer_flags_t and_(peer_flags_t other) {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_and_(swigCPtr, this, peer_flags_t.getCPtr(other), other), true);
  }

  public peer_flags_t xor(peer_flags_t other) {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_xor(swigCPtr, this, peer_flags_t.getCPtr(other), other), true);
  }

  public peer_flags_t inv() {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_inv(swigCPtr, this), true);
  }

  public int to_int() {
    return libtorrent_jni.peer_flags_t_to_int(swigCPtr, this);
  }

  public static peer_flags_t from_int(int val) {
    return new peer_flags_t(libtorrent_jni.peer_flags_t_from_int(val), true);
  }

}
