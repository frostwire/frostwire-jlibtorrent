/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class ip_interface {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ip_interface(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ip_interface obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(ip_interface obj) {
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

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_ip_interface(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setInterface_address(address value) {
    libtorrent_jni.ip_interface_interface_address_set(swigCPtr, this, address.getCPtr(value), value);
  }

  public address getInterface_address() {
    long cPtr = libtorrent_jni.ip_interface_interface_address_get(swigCPtr, this);
    return (cPtr == 0) ? null : new address(cPtr, false);
  }

  public void setNetmask(address value) {
    libtorrent_jni.ip_interface_netmask_set(swigCPtr, this, address.getCPtr(value), value);
  }

  public address getNetmask() {
    long cPtr = libtorrent_jni.ip_interface_netmask_get(swigCPtr, this);
    return (cPtr == 0) ? null : new address(cPtr, false);
  }

  public void setName(byte_vector value) {
    libtorrent_jni.ip_interface_name_set(swigCPtr, this, byte_vector.getCPtr(value), value);
  }

  public byte_vector getName() {
    long cPtr = libtorrent_jni.ip_interface_name_get(swigCPtr, this);
    return (cPtr == 0) ? null : new byte_vector(cPtr, false);
  }

  public void setFriendly_name(byte_vector value) {
    libtorrent_jni.ip_interface_friendly_name_set(swigCPtr, this, byte_vector.getCPtr(value), value);
  }

  public byte_vector getFriendly_name() {
    long cPtr = libtorrent_jni.ip_interface_friendly_name_get(swigCPtr, this);
    return (cPtr == 0) ? null : new byte_vector(cPtr, false);
  }

  public void setDescription(byte_vector value) {
    libtorrent_jni.ip_interface_description_set(swigCPtr, this, byte_vector.getCPtr(value), value);
  }

  public byte_vector getDescription() {
    long cPtr = libtorrent_jni.ip_interface_description_get(swigCPtr, this);
    return (cPtr == 0) ? null : new byte_vector(cPtr, false);
  }

  public void setPreferred(boolean value) {
    libtorrent_jni.ip_interface_preferred_set(swigCPtr, this, value);
  }

  public boolean getPreferred() {
    return libtorrent_jni.ip_interface_preferred_get(swigCPtr, this);
  }

  public ip_interface() {
    this(libtorrent_jni.new_ip_interface(), true);
  }

}
