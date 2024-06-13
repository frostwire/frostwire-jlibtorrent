/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class piece_index_bitfield {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected piece_index_bitfield(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(piece_index_bitfield obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(piece_index_bitfield obj) {
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
        libtorrent_jni.delete_piece_index_bitfield(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public piece_index_bitfield() {
    this(libtorrent_jni.new_piece_index_bitfield__SWIG_0(), true);
  }

  public piece_index_bitfield(int bits) {
    this(libtorrent_jni.new_piece_index_bitfield__SWIG_1(bits), true);
  }

  public piece_index_bitfield(int bits, boolean val) {
    this(libtorrent_jni.new_piece_index_bitfield__SWIG_2(bits, val), true);
  }

  public piece_index_bitfield(piece_index_bitfield rhs) {
    this(libtorrent_jni.new_piece_index_bitfield__SWIG_3(piece_index_bitfield.getCPtr(rhs), rhs), true);
  }

  public boolean get_bit(int index) {
    return libtorrent_jni.piece_index_bitfield_get_bit(swigCPtr, this, index);
  }

  public void clear_bit(int index) {
    libtorrent_jni.piece_index_bitfield_clear_bit(swigCPtr, this, index);
  }

  public void set_bit(int index) {
    libtorrent_jni.piece_index_bitfield_set_bit(swigCPtr, this, index);
  }

  public int end_index() {
    return libtorrent_jni.piece_index_bitfield_end_index(swigCPtr, this);
  }

  public boolean all_set() {
    return libtorrent_jni.piece_index_bitfield_all_set(swigCPtr, this);
  }

  public boolean none_set() {
    return libtorrent_jni.piece_index_bitfield_none_set(swigCPtr, this);
  }

  public int size() {
    return libtorrent_jni.piece_index_bitfield_size(swigCPtr, this);
  }

  public int num_words() {
    return libtorrent_jni.piece_index_bitfield_num_words(swigCPtr, this);
  }

  public boolean empty() {
    return libtorrent_jni.piece_index_bitfield_empty(swigCPtr, this);
  }

  public int count() {
    return libtorrent_jni.piece_index_bitfield_count(swigCPtr, this);
  }

  public int find_first_set() {
    return libtorrent_jni.piece_index_bitfield_find_first_set(swigCPtr, this);
  }

  public int find_last_clear() {
    return libtorrent_jni.piece_index_bitfield_find_last_clear(swigCPtr, this);
  }

  public void resize(int bits, boolean val) {
    libtorrent_jni.piece_index_bitfield_resize__SWIG_0(swigCPtr, this, bits, val);
  }

  public void resize(int bits) {
    libtorrent_jni.piece_index_bitfield_resize__SWIG_1(swigCPtr, this, bits);
  }

  public void set_all() {
    libtorrent_jni.piece_index_bitfield_set_all(swigCPtr, this);
  }

  public void clear_all() {
    libtorrent_jni.piece_index_bitfield_clear_all(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.piece_index_bitfield_clear(swigCPtr, this);
  }

}
