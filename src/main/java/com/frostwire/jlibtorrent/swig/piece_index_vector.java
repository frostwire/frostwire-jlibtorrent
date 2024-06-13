/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class piece_index_vector extends java.util.AbstractList<SWIGTYPE_p_libtorrent__piece_index_t> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected piece_index_vector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(piece_index_vector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(piece_index_vector obj) {
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
        libtorrent_jni.delete_piece_index_vector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public piece_index_vector(SWIGTYPE_p_libtorrent__piece_index_t[] initialElements) {
    this();
    reserve(initialElements.length);

    for (SWIGTYPE_p_libtorrent__piece_index_t element : initialElements) {
      add(element);
    }
  }

  public piece_index_vector(Iterable<SWIGTYPE_p_libtorrent__piece_index_t> initialElements) {
    this();
    for (SWIGTYPE_p_libtorrent__piece_index_t element : initialElements) {
      add(element);
    }
  }

  public SWIGTYPE_p_libtorrent__piece_index_t get(int index) {
    return doGet(index);
  }

  public SWIGTYPE_p_libtorrent__piece_index_t set(int index, SWIGTYPE_p_libtorrent__piece_index_t e) {
    return doSet(index, e);
  }

  public boolean add(SWIGTYPE_p_libtorrent__piece_index_t e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, SWIGTYPE_p_libtorrent__piece_index_t e) {
    modCount++;
    doAdd(index, e);
  }

  public SWIGTYPE_p_libtorrent__piece_index_t remove(int index) {
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

  public piece_index_vector() {
    this(libtorrent_jni.new_piece_index_vector__SWIG_0(), true);
  }

  public piece_index_vector(piece_index_vector other) {
    this(libtorrent_jni.new_piece_index_vector__SWIG_1(piece_index_vector.getCPtr(other), other), true);
  }

  public boolean isEmpty() {
    return libtorrent_jni.piece_index_vector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.piece_index_vector_clear(swigCPtr, this);
  }

  public piece_index_vector(int count, SWIGTYPE_p_libtorrent__piece_index_t value) {
    this(libtorrent_jni.new_piece_index_vector__SWIG_2(count, SWIGTYPE_p_libtorrent__piece_index_t.getCPtr(value)), true);
  }

  private int doCapacity() {
    return libtorrent_jni.piece_index_vector_doCapacity(swigCPtr, this);
  }

  private void doReserve(int n) {
    libtorrent_jni.piece_index_vector_doReserve(swigCPtr, this, n);
  }

  private int doSize() {
    return libtorrent_jni.piece_index_vector_doSize(swigCPtr, this);
  }

  private void doAdd(SWIGTYPE_p_libtorrent__piece_index_t x) {
    libtorrent_jni.piece_index_vector_doAdd__SWIG_0(swigCPtr, this, SWIGTYPE_p_libtorrent__piece_index_t.getCPtr(x));
  }

  private void doAdd(int index, SWIGTYPE_p_libtorrent__piece_index_t x) {
    libtorrent_jni.piece_index_vector_doAdd__SWIG_1(swigCPtr, this, index, SWIGTYPE_p_libtorrent__piece_index_t.getCPtr(x));
  }

  private SWIGTYPE_p_libtorrent__piece_index_t doRemove(int index) {
    return new SWIGTYPE_p_libtorrent__piece_index_t(libtorrent_jni.piece_index_vector_doRemove(swigCPtr, this, index), true);
  }

  private SWIGTYPE_p_libtorrent__piece_index_t doGet(int index) {
    return new SWIGTYPE_p_libtorrent__piece_index_t(libtorrent_jni.piece_index_vector_doGet(swigCPtr, this, index), false);
  }

  private SWIGTYPE_p_libtorrent__piece_index_t doSet(int index, SWIGTYPE_p_libtorrent__piece_index_t val) {
    return new SWIGTYPE_p_libtorrent__piece_index_t(libtorrent_jni.piece_index_vector_doSet(swigCPtr, this, index, SWIGTYPE_p_libtorrent__piece_index_t.getCPtr(val)), true);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtorrent_jni.piece_index_vector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
