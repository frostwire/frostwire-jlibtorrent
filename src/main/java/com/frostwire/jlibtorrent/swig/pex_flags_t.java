/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class pex_flags_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected pex_flags_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(pex_flags_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_pex_flags_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static pex_flags_t all() {
    return new pex_flags_t(libtorrent_jni.pex_flags_t_all(), true);
  }

  public boolean nonZero() {
    return libtorrent_jni.pex_flags_t_nonZero(swigCPtr, this);
  }

  public boolean eq(pex_flags_t f) {
    return libtorrent_jni.pex_flags_t_eq(swigCPtr, this, pex_flags_t.getCPtr(f), f);
  }

  public boolean ne(pex_flags_t f) {
    return libtorrent_jni.pex_flags_t_ne(swigCPtr, this, pex_flags_t.getCPtr(f), f);
  }

  public pex_flags_t or_(pex_flags_t other) {
    return new pex_flags_t(libtorrent_jni.pex_flags_t_or_(swigCPtr, this, pex_flags_t.getCPtr(other), other), true);
  }

  public pex_flags_t and_(pex_flags_t other) {
    return new pex_flags_t(libtorrent_jni.pex_flags_t_and_(swigCPtr, this, pex_flags_t.getCPtr(other), other), true);
  }

  public pex_flags_t xor(pex_flags_t other) {
    return new pex_flags_t(libtorrent_jni.pex_flags_t_xor(swigCPtr, this, pex_flags_t.getCPtr(other), other), true);
  }

  public pex_flags_t inv() {
    return new pex_flags_t(libtorrent_jni.pex_flags_t_inv(swigCPtr, this), true);
  }

  public int to_int() {
    return libtorrent_jni.pex_flags_t_to_int(swigCPtr, this);
  }

  public pex_flags_t() {
    this(libtorrent_jni.new_pex_flags_t(), true);
  }

}
