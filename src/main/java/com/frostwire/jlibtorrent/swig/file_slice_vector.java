/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class file_slice_vector extends java.util.AbstractList<file_slice> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected file_slice_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(file_slice_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(file_slice_vector obj) {
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
        libtorrent_jni.delete_file_slice_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public file_slice_vector(file_slice[] initialElements) {
    this();
    reserve(initialElements.length);

    for (file_slice element : initialElements) {
      add(element);
    }
  }

  public file_slice_vector(Iterable<file_slice> initialElements) {
    this();
    for (file_slice element : initialElements) {
      add(element);
    }
  }

  public file_slice get(int index) {
    return doGet(index);
  }

  public file_slice set(int index, file_slice e) {
    return doSet(index, e);
  }

  public boolean add(file_slice e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, file_slice e) {
    modCount++;
    doAdd(index, e);
  }

  public file_slice remove(int index) {
    modCount++;
    return doRemove(index);
  }

  protected void removeRange(int fromIndex, int toIndex) {
    modCount++;
    doRemoveRange(fromIndex, toIndex);
  }

  public int size() {
    return doSize();
  }

  public int capacity() {
    return doCapacity();
  }

  public void reserve(int n) {
    doReserve(n);
  }

  public file_slice_vector() {
    this(libtorrent_jni.new_file_slice_vector__SWIG_0(), true);
  }

  public file_slice_vector(file_slice_vector other) {
    this(libtorrent_jni.new_file_slice_vector__SWIG_1(file_slice_vector.getCPtr(other), other), true);
  }

  public boolean isEmpty() {
    return libtorrent_jni.file_slice_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.file_slice_vector_clear(swigCPtr, this);
  }

  public file_slice_vector(int count, file_slice value) {
    this(libtorrent_jni.new_file_slice_vector__SWIG_2(count, file_slice.getCPtr(value), value), true);
  }

  private int doCapacity() {
    return libtorrent_jni.file_slice_vector_doCapacity(swigCPtr, this);
  }

  private void doReserve(int n) {
    libtorrent_jni.file_slice_vector_doReserve(swigCPtr, this, n);
  }

  private int doSize() {
    return libtorrent_jni.file_slice_vector_doSize(swigCPtr, this);
  }

  private void doAdd(file_slice x) {
    libtorrent_jni.file_slice_vector_doAdd__SWIG_0(swigCPtr, this, file_slice.getCPtr(x), x);
  }

  private void doAdd(int index, file_slice x) {
    libtorrent_jni.file_slice_vector_doAdd__SWIG_1(swigCPtr, this, index, file_slice.getCPtr(x), x);
  }

  private file_slice doRemove(int index) {
    return new file_slice(libtorrent_jni.file_slice_vector_doRemove(swigCPtr, this, index), true);
  }

  private file_slice doGet(int index) {
    return new file_slice(libtorrent_jni.file_slice_vector_doGet(swigCPtr, this, index), false);
  }

  private file_slice doSet(int index, file_slice val) {
    return new file_slice(libtorrent_jni.file_slice_vector_doSet(swigCPtr, this, index, file_slice.getCPtr(val), val), true);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtorrent_jni.file_slice_vector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
