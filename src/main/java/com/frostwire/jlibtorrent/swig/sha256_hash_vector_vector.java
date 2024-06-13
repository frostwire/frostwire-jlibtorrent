/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class sha256_hash_vector_vector extends java.util.AbstractList<sha256_hash_vector> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected sha256_hash_vector_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(sha256_hash_vector_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(sha256_hash_vector_vector obj) {
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
        libtorrent_jni.delete_sha256_hash_vector_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public sha256_hash_vector_vector(sha256_hash_vector[] initialElements) {
    this();
    reserve(initialElements.length);

    for (sha256_hash_vector element : initialElements) {
      add(element);
    }
  }

  public sha256_hash_vector_vector(Iterable<sha256_hash_vector> initialElements) {
    this();
    for (sha256_hash_vector element : initialElements) {
      add(element);
    }
  }

  public sha256_hash_vector get(int index) {
    return doGet(index);
  }

  public sha256_hash_vector set(int index, sha256_hash_vector e) {
    return doSet(index, e);
  }

  public boolean add(sha256_hash_vector e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, sha256_hash_vector e) {
    modCount++;
    doAdd(index, e);
  }

  public sha256_hash_vector remove(int index) {
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

  public sha256_hash_vector_vector() {
    this(libtorrent_jni.new_sha256_hash_vector_vector__SWIG_0(), true);
  }

  public sha256_hash_vector_vector(sha256_hash_vector_vector other) {
    this(libtorrent_jni.new_sha256_hash_vector_vector__SWIG_1(sha256_hash_vector_vector.getCPtr(other), other), true);
  }

  public boolean isEmpty() {
    return libtorrent_jni.sha256_hash_vector_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.sha256_hash_vector_vector_clear(swigCPtr, this);
  }

  public sha256_hash_vector_vector(int count, sha256_hash_vector value) {
    this(libtorrent_jni.new_sha256_hash_vector_vector__SWIG_2(count, sha256_hash_vector.getCPtr(value), value), true);
  }

  private int doCapacity() {
    return libtorrent_jni.sha256_hash_vector_vector_doCapacity(swigCPtr, this);
  }

  private void doReserve(int n) {
    libtorrent_jni.sha256_hash_vector_vector_doReserve(swigCPtr, this, n);
  }

  private int doSize() {
    return libtorrent_jni.sha256_hash_vector_vector_doSize(swigCPtr, this);
  }

  private void doAdd(sha256_hash_vector x) {
    libtorrent_jni.sha256_hash_vector_vector_doAdd__SWIG_0(swigCPtr, this, sha256_hash_vector.getCPtr(x), x);
  }

  private void doAdd(int index, sha256_hash_vector x) {
    libtorrent_jni.sha256_hash_vector_vector_doAdd__SWIG_1(swigCPtr, this, index, sha256_hash_vector.getCPtr(x), x);
  }

  private sha256_hash_vector doRemove(int index) {
    return new sha256_hash_vector(libtorrent_jni.sha256_hash_vector_vector_doRemove(swigCPtr, this, index), true);
  }

  private sha256_hash_vector doGet(int index) {
    return new sha256_hash_vector(libtorrent_jni.sha256_hash_vector_vector_doGet(swigCPtr, this, index), false);
  }

  private sha256_hash_vector doSet(int index, sha256_hash_vector val) {
    return new sha256_hash_vector(libtorrent_jni.sha256_hash_vector_vector_doSet(swigCPtr, this, index, sha256_hash_vector.getCPtr(val), val), true);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtorrent_jni.sha256_hash_vector_vector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
