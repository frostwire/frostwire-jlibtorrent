/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class resume_data_flags_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected resume_data_flags_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(resume_data_flags_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(resume_data_flags_t obj) {
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
        libtorrent_jni.delete_resume_data_flags_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static resume_data_flags_t all() {
    return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_all(), true);
  }

  public boolean nonZero() {
    return libtorrent_jni.resume_data_flags_t_nonZero(swigCPtr, this);
  }

  public boolean eq(resume_data_flags_t f) {
    return libtorrent_jni.resume_data_flags_t_eq(swigCPtr, this, resume_data_flags_t.getCPtr(f), f);
  }

  public boolean ne(resume_data_flags_t f) {
    return libtorrent_jni.resume_data_flags_t_ne(swigCPtr, this, resume_data_flags_t.getCPtr(f), f);
  }

  public resume_data_flags_t or_(resume_data_flags_t other) {
    return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_or_(swigCPtr, this, resume_data_flags_t.getCPtr(other), other), true);
  }

  public resume_data_flags_t and_(resume_data_flags_t other) {
    return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_and_(swigCPtr, this, resume_data_flags_t.getCPtr(other), other), true);
  }

  public resume_data_flags_t xor(resume_data_flags_t other) {
    return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_xor(swigCPtr, this, resume_data_flags_t.getCPtr(other), other), true);
  }

  public resume_data_flags_t inv() {
    return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_inv(swigCPtr, this), true);
  }

  public int to_int() {
    return libtorrent_jni.resume_data_flags_t_to_int(swigCPtr, this);
  }

  public resume_data_flags_t() {
    this(libtorrent_jni.new_resume_data_flags_t(), true);
  }

}
