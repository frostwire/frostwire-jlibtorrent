/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class string_string_pair {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected string_string_pair(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(string_string_pair obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_string_string_pair(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public string_string_pair() {
    this(libtorrent_jni.new_string_string_pair__SWIG_0(), true);
  }

  public string_string_pair(String first, String second) {
    this(libtorrent_jni.new_string_string_pair__SWIG_1(first, second), true);
  }

  public string_string_pair(string_string_pair other) {
    this(libtorrent_jni.new_string_string_pair__SWIG_2(string_string_pair.getCPtr(other), other), true);
  }

  public void setFirst(String value) {
    libtorrent_jni.string_string_pair_first_set(swigCPtr, this, value);
  }

  public String getFirst() {
    return libtorrent_jni.string_string_pair_first_get(swigCPtr, this);
  }

  public void setSecond(String value) {
    libtorrent_jni.string_string_pair_second_set(swigCPtr, this, value);
  }

  public String getSecond() {
    return libtorrent_jni.string_string_pair_second_get(swigCPtr, this);
  }

}
