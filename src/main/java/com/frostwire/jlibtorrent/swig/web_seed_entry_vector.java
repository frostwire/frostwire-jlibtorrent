/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class web_seed_entry_vector extends java.util.AbstractList<web_seed_entry> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected web_seed_entry_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(web_seed_entry_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(web_seed_entry_vector obj) {
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
        libtorrent_jni.delete_web_seed_entry_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public web_seed_entry_vector(web_seed_entry[] initialElements) {
    this();
    reserve(initialElements.length);

    for (web_seed_entry element : initialElements) {
      add(element);
    }
  }

  public web_seed_entry_vector(Iterable<web_seed_entry> initialElements) {
    this();
    for (web_seed_entry element : initialElements) {
      add(element);
    }
  }

  public web_seed_entry get(int index) {
    return doGet(index);
  }

  public web_seed_entry set(int index, web_seed_entry e) {
    return doSet(index, e);
  }

  public boolean add(web_seed_entry e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, web_seed_entry e) {
    modCount++;
    doAdd(index, e);
  }

  public web_seed_entry remove(int index) {
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

  public web_seed_entry_vector() {
    this(libtorrent_jni.new_web_seed_entry_vector__SWIG_0(), true);
  }

  public web_seed_entry_vector(web_seed_entry_vector other) {
    this(libtorrent_jni.new_web_seed_entry_vector__SWIG_1(web_seed_entry_vector.getCPtr(other), other), true);
  }

  public boolean isEmpty() {
    return libtorrent_jni.web_seed_entry_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.web_seed_entry_vector_clear(swigCPtr, this);
  }

  public web_seed_entry_vector(int count, web_seed_entry value) {
    this(libtorrent_jni.new_web_seed_entry_vector__SWIG_2(count, web_seed_entry.getCPtr(value), value), true);
  }

  private int doCapacity() {
    return libtorrent_jni.web_seed_entry_vector_doCapacity(swigCPtr, this);
  }

  private void doReserve(int n) {
    libtorrent_jni.web_seed_entry_vector_doReserve(swigCPtr, this, n);
  }

  private int doSize() {
    return libtorrent_jni.web_seed_entry_vector_doSize(swigCPtr, this);
  }

  private void doAdd(web_seed_entry x) {
    libtorrent_jni.web_seed_entry_vector_doAdd__SWIG_0(swigCPtr, this, web_seed_entry.getCPtr(x), x);
  }

  private void doAdd(int index, web_seed_entry x) {
    libtorrent_jni.web_seed_entry_vector_doAdd__SWIG_1(swigCPtr, this, index, web_seed_entry.getCPtr(x), x);
  }

  private web_seed_entry doRemove(int index) {
    return new web_seed_entry(libtorrent_jni.web_seed_entry_vector_doRemove(swigCPtr, this, index), true);
  }

  private web_seed_entry doGet(int index) {
    return new web_seed_entry(libtorrent_jni.web_seed_entry_vector_doGet(swigCPtr, this, index), false);
  }

  private web_seed_entry doSet(int index, web_seed_entry val) {
    return new web_seed_entry(libtorrent_jni.web_seed_entry_vector_doSet(swigCPtr, this, index, web_seed_entry.getCPtr(val), val), true);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtorrent_jni.web_seed_entry_vector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
