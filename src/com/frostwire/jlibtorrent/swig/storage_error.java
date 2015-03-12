/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.4
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class storage_error {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected storage_error(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(storage_error obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_storage_error(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public storage_error() {
    this(libtorrent_jni.new_storage_error__SWIG_0(), true);
  }

  public storage_error(error_code e) {
    this(libtorrent_jni.new_storage_error__SWIG_1(error_code.getCPtr(e), e), true);
  }

  public void setEc(error_code value) {
    libtorrent_jni.storage_error_ec_set(swigCPtr, this, error_code.getCPtr(value), value);
  }

  public error_code getEc() {
    long cPtr = libtorrent_jni.storage_error_ec_get(swigCPtr, this);
    return (cPtr == 0) ? null : new error_code(cPtr, false);
  }

  public void setFile(int value) {
    libtorrent_jni.storage_error_file_set(swigCPtr, this, value);
  }

  public int getFile() {
    return libtorrent_jni.storage_error_file_get(swigCPtr, this);
  }

  public void setOperation(long value) {
    libtorrent_jni.storage_error_operation_set(swigCPtr, this, value);
  }

  public long getOperation() {
    return libtorrent_jni.storage_error_operation_get(swigCPtr, this);
  }

  public String operation_str() {
    return libtorrent_jni.storage_error_operation_str(swigCPtr, this);
  }

  public enum file_operation_t {
    none,
    stat,
    mkdir,
    open,
    rename,
    remove,
    copy,
    read,
    write,
    fallocate,
    alloc_cache_piece,
    partfile_move,
    partfile_read,
    partfile_write;

    public final int swigValue() {
      return swigValue;
    }

    public static file_operation_t swigToEnum(int swigValue) {
      file_operation_t[] swigValues = file_operation_t.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (file_operation_t swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + file_operation_t.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private file_operation_t() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private file_operation_t(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private file_operation_t(file_operation_t swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

}
