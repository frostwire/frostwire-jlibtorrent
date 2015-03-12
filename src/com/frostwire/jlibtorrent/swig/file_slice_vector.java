/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.4
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class file_slice_vector {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected file_slice_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(file_slice_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_file_slice_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public file_slice_vector() {
    this(libtorrent_jni.new_file_slice_vector(), true);
  }

  public long size() {
    return libtorrent_jni.file_slice_vector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtorrent_jni.file_slice_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.file_slice_vector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return libtorrent_jni.file_slice_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.file_slice_vector_clear(swigCPtr, this);
  }

  public void add(file_slice x) {
    libtorrent_jni.file_slice_vector_add(swigCPtr, this, file_slice.getCPtr(x), x);
  }

  public file_slice get(int i) {
    return new file_slice(libtorrent_jni.file_slice_vector_get(swigCPtr, this, i), false);
  }

  public void set(int i, file_slice val) {
    libtorrent_jni.file_slice_vector_set(swigCPtr, this, i, file_slice.getCPtr(val), val);
  }

}
