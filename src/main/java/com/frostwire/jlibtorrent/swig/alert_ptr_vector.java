/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class alert_ptr_vector extends java.util.AbstractList<SWIGTYPE_p_libtorrent__alert> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected alert_ptr_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(alert_ptr_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(alert_ptr_vector obj) {
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
        libtorrent_jni.delete_alert_ptr_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public alert_ptr_vector(SWIGTYPE_p_libtorrent__alert[] initialElements) {
    this();
    reserve(initialElements.length);

    for (SWIGTYPE_p_libtorrent__alert element : initialElements) {
      add(element);
    }
  }

  public alert_ptr_vector(Iterable<SWIGTYPE_p_libtorrent__alert> initialElements) {
    this();
    for (SWIGTYPE_p_libtorrent__alert element : initialElements) {
      add(element);
    }
  }

  public SWIGTYPE_p_libtorrent__alert get(int index) {
    return doGet(index);
  }

  public SWIGTYPE_p_libtorrent__alert set(int index, SWIGTYPE_p_libtorrent__alert e) {
    return doSet(index, e);
  }

  public boolean add(SWIGTYPE_p_libtorrent__alert e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, SWIGTYPE_p_libtorrent__alert e) {
    modCount++;
    doAdd(index, e);
  }

  public SWIGTYPE_p_libtorrent__alert remove(int index) {
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

  public alert_ptr_vector() {
    this(libtorrent_jni.new_alert_ptr_vector__SWIG_0(), true);
  }

  public alert_ptr_vector(alert_ptr_vector other) {
    this(libtorrent_jni.new_alert_ptr_vector__SWIG_1(alert_ptr_vector.getCPtr(other), other), true);
  }

  public long capacity() {
    return libtorrent_jni.alert_ptr_vector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtorrent_jni.alert_ptr_vector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return libtorrent_jni.alert_ptr_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.alert_ptr_vector_clear(swigCPtr, this);
  }

  public alert_ptr_vector(int count, SWIGTYPE_p_libtorrent__alert value) {
    this(libtorrent_jni.new_alert_ptr_vector__SWIG_2(count, SWIGTYPE_p_libtorrent__alert.getCPtr(value)), true);
  }

  private int doSize() {
    return libtorrent_jni.alert_ptr_vector_doSize(swigCPtr, this);
  }

  private void doAdd(SWIGTYPE_p_libtorrent__alert x) {
    libtorrent_jni.alert_ptr_vector_doAdd__SWIG_0(swigCPtr, this, SWIGTYPE_p_libtorrent__alert.getCPtr(x));
  }

  private void doAdd(int index, SWIGTYPE_p_libtorrent__alert x) {
    libtorrent_jni.alert_ptr_vector_doAdd__SWIG_1(swigCPtr, this, index, SWIGTYPE_p_libtorrent__alert.getCPtr(x));
  }

  private SWIGTYPE_p_libtorrent__alert doRemove(int index) {
    long cPtr = libtorrent_jni.alert_ptr_vector_doRemove(swigCPtr, this, index);
    return (cPtr == 0) ? null : new SWIGTYPE_p_libtorrent__alert(cPtr, false);
  }

  private SWIGTYPE_p_libtorrent__alert doGet(int index) {
    long cPtr = libtorrent_jni.alert_ptr_vector_doGet(swigCPtr, this, index);
    return (cPtr == 0) ? null : new SWIGTYPE_p_libtorrent__alert(cPtr, false);
  }

  private SWIGTYPE_p_libtorrent__alert doSet(int index, SWIGTYPE_p_libtorrent__alert val) {
    long cPtr = libtorrent_jni.alert_ptr_vector_doSet(swigCPtr, this, index, SWIGTYPE_p_libtorrent__alert.getCPtr(val));
    return (cPtr == 0) ? null : new SWIGTYPE_p_libtorrent__alert(cPtr, false);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtorrent_jni.alert_ptr_vector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
