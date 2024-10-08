/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class web_seed_entry {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected web_seed_entry(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(web_seed_entry obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(web_seed_entry obj) {
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
        libtorrent_jni.delete_web_seed_entry(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public web_seed_entry(String url_, web_seed_entry.type_t type_, String auth_, string_string_pair_vector extra_headers_) {
    this(libtorrent_jni.new_web_seed_entry__SWIG_0(url_, type_.swigValue(), auth_, string_string_pair_vector.getCPtr(extra_headers_), extra_headers_), true);
  }

  public web_seed_entry(String url_, web_seed_entry.type_t type_, String auth_) {
    this(libtorrent_jni.new_web_seed_entry__SWIG_1(url_, type_.swigValue(), auth_), true);
  }

  public web_seed_entry(String url_, web_seed_entry.type_t type_) {
    this(libtorrent_jni.new_web_seed_entry__SWIG_2(url_, type_.swigValue()), true);
  }

  public boolean op_eq(web_seed_entry e) {
    return libtorrent_jni.web_seed_entry_op_eq(swigCPtr, this, web_seed_entry.getCPtr(e), e);
  }

  public boolean op_lt(web_seed_entry e) {
    return libtorrent_jni.web_seed_entry_op_lt(swigCPtr, this, web_seed_entry.getCPtr(e), e);
  }

  public void setUrl(String value) {
    libtorrent_jni.web_seed_entry_url_set(swigCPtr, this, value);
  }

  public String getUrl() {
    return libtorrent_jni.web_seed_entry_url_get(swigCPtr, this);
  }

  public void setAuth(String value) {
    libtorrent_jni.web_seed_entry_auth_set(swigCPtr, this, value);
  }

  public String getAuth() {
    return libtorrent_jni.web_seed_entry_auth_get(swigCPtr, this);
  }

  public void setExtra_headers(string_string_pair_vector value) {
    libtorrent_jni.web_seed_entry_extra_headers_set(swigCPtr, this, string_string_pair_vector.getCPtr(value), value);
  }

  public string_string_pair_vector getExtra_headers() {
    long cPtr = libtorrent_jni.web_seed_entry_extra_headers_get(swigCPtr, this);
    return (cPtr == 0) ? null : new string_string_pair_vector(cPtr, false);
  }

  public void setType(short value) {
    libtorrent_jni.web_seed_entry_type_set(swigCPtr, this, value);
  }

  public short getType() {
    return libtorrent_jni.web_seed_entry_type_get(swigCPtr, this);
  }

  public final static class type_t {
    public final static web_seed_entry.type_t url_seed = new web_seed_entry.type_t("url_seed");
    public final static web_seed_entry.type_t http_seed = new web_seed_entry.type_t("http_seed");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static type_t swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + type_t.class + " with value " + swigValue);
    }

    private type_t(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private type_t(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private type_t(String swigName, type_t swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static type_t[] swigValues = { url_seed, http_seed };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
