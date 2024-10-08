/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class piece_block {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected piece_block(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(piece_block obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(piece_block obj) {
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
        libtorrent_jni.delete_piece_block(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static piece_block getInvalid() {
    long cPtr = libtorrent_jni.piece_block_invalid_get();
    return (cPtr == 0) ? null : new piece_block(cPtr, false);
  }

  public piece_block() {
    this(libtorrent_jni.new_piece_block__SWIG_0(), true);
  }

  public piece_block(int p_index, int b_index) {
    this(libtorrent_jni.new_piece_block__SWIG_1(p_index, b_index), true);
  }

  public void setPiece_index(int value) {
    libtorrent_jni.piece_block_piece_index_set(swigCPtr, this, value);
  }

  public int getPiece_index() {
    return libtorrent_jni.piece_block_piece_index_get(swigCPtr, this);
  }

  public void setBlock_index(int value) {
    libtorrent_jni.piece_block_block_index_set(swigCPtr, this, value);
  }

  public int getBlock_index() {
    return libtorrent_jni.piece_block_block_index_get(swigCPtr, this);
  }

  public boolean op_lt(piece_block b) {
    return libtorrent_jni.piece_block_op_lt(swigCPtr, this, piece_block.getCPtr(b), b);
  }

  public boolean op_eq(piece_block b) {
    return libtorrent_jni.piece_block_op_eq(swigCPtr, this, piece_block.getCPtr(b), b);
  }

  public boolean op_ne(piece_block b) {
    return libtorrent_jni.piece_block_op_ne(swigCPtr, this, piece_block.getCPtr(b), b);
  }

}
