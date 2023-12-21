/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class string_entry_map {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected string_entry_map(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(string_entry_map obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_string_entry_map(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public string_entry_map() {
    this(libtorrent_jni.new_string_entry_map__SWIG_0(), true);
  }

  public string_entry_map(string_entry_map arg0) {
    this(libtorrent_jni.new_string_entry_map__SWIG_1(string_entry_map.getCPtr(arg0), arg0), true);
  }

  public long size() {
    return libtorrent_jni.string_entry_map_size(swigCPtr, this);
  }

  public boolean empty() {
    return libtorrent_jni.string_entry_map_empty(swigCPtr, this);
  }

  public void clear() {
    libtorrent_jni.string_entry_map_clear(swigCPtr, this);
  }

  public entry get(String key) {
    return new entry(libtorrent_jni.string_entry_map_get(swigCPtr, this, key), false);
  }

  public void set(String key, entry x) {
    libtorrent_jni.string_entry_map_set(swigCPtr, this, key, entry.getCPtr(x), x);
  }

  public void erase(String key) {
    libtorrent_jni.string_entry_map_erase(swigCPtr, this, key);
  }

  public boolean has_key(String key) {
    return libtorrent_jni.string_entry_map_has_key(swigCPtr, this, key);
  }

  public string_vector keys() {
    return new string_vector(libtorrent_jni.string_entry_map_keys(swigCPtr, this), true);
  }

}
