/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class ip_filter {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ip_filter(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ip_filter obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_ip_filter(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void add_rule(address first, address last, long flags) {
    libtorrent_jni.ip_filter_add_rule(swigCPtr, this, address.getCPtr(first), first, address.getCPtr(last), last, flags);
  }

  public long access(address addr) {
    return libtorrent_jni.ip_filter_access(swigCPtr, this, address.getCPtr(addr), addr);
  }

  public ip_filter() {
    this(libtorrent_jni.new_ip_filter(), true);
  }

  public final static class access_flags {
    public final static ip_filter.access_flags blocked = new ip_filter.access_flags("blocked", libtorrent_jni.ip_filter_blocked_get());

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static access_flags swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + access_flags.class + " with value " + swigValue);
    }

    private access_flags(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private access_flags(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private access_flags(String swigName, access_flags swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static access_flags[] swigValues = { blocked };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
