/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class sha1_hash {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected sha1_hash(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(sha1_hash obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_sha1_hash(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static long size() {
    return libtorrent_jni.sha1_hash_size();
  }

  public sha1_hash() {
    this(libtorrent_jni.new_sha1_hash__SWIG_0(), true);
  }

  public sha1_hash(sha1_hash other) {
    this(libtorrent_jni.new_sha1_hash__SWIG_1(sha1_hash.getCPtr(other), other), true);
  }

  public static sha1_hash max() {
    return new sha1_hash(libtorrent_jni.sha1_hash_max(), true);
  }

  public static sha1_hash min() {
    return new sha1_hash(libtorrent_jni.sha1_hash_min(), true);
  }

  public void clear() {
    libtorrent_jni.sha1_hash_clear(swigCPtr, this);
  }

  public boolean is_all_zeros() {
    return libtorrent_jni.sha1_hash_is_all_zeros(swigCPtr, this);
  }

  public int count_leading_zeroes() {
    return libtorrent_jni.sha1_hash_count_leading_zeroes(swigCPtr, this);
  }

  public sha1_hash(byte_vector s) {
    this(libtorrent_jni.new_sha1_hash__SWIG_2(byte_vector.getCPtr(s), s), true);
  }

  public int hash_code() {
    return libtorrent_jni.sha1_hash_hash_code(swigCPtr, this);
  }

  public byte_vector to_bytes() {
    return new byte_vector(libtorrent_jni.sha1_hash_to_bytes(swigCPtr, this), true);
  }

  public String to_hex() {
    return libtorrent_jni.sha1_hash_to_hex(swigCPtr, this);
  }

  public boolean op_eq(sha1_hash n) {
    return libtorrent_jni.sha1_hash_op_eq(swigCPtr, this, sha1_hash.getCPtr(n), n);
  }

  public boolean op_ne(sha1_hash n) {
    return libtorrent_jni.sha1_hash_op_ne(swigCPtr, this, sha1_hash.getCPtr(n), n);
  }

  public boolean op_lt(sha1_hash n) {
    return libtorrent_jni.sha1_hash_op_lt(swigCPtr, this, sha1_hash.getCPtr(n), n);
  }

  public static int compare(sha1_hash h1, sha1_hash h2) {
    return libtorrent_jni.sha1_hash_compare(sha1_hash.getCPtr(h1), h1, sha1_hash.getCPtr(h2), h2);
  }

}
