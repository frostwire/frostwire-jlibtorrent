/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public class upnp {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected upnp(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(upnp obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_upnp(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public upnp(SWIGTYPE_p_io_service ios, address listen_interface, String user_agent, SWIGTYPE_p_boost__functionT_void_fint_boost__asio__ip__address_int_boost__system__error_code_const_RF_t cb, SWIGTYPE_p_boost__functionT_void_fchar_const_pF_t lcb, boolean ignore_nonrouters) {
    this(libtorrent_jni.new_upnp(SWIGTYPE_p_io_service.getCPtr(ios), address.getCPtr(listen_interface), listen_interface, user_agent, SWIGTYPE_p_boost__functionT_void_fint_boost__asio__ip__address_int_boost__system__error_code_const_RF_t.getCPtr(cb), SWIGTYPE_p_boost__functionT_void_fchar_const_pF_t.getCPtr(lcb), ignore_nonrouters), true);
  }

  public void start(SWIGTYPE_p_void state) {
    libtorrent_jni.upnp_start__SWIG_0(swigCPtr, this, SWIGTYPE_p_void.getCPtr(state));
  }

  public void start() {
    libtorrent_jni.upnp_start__SWIG_1(swigCPtr, this);
  }

  public SWIGTYPE_p_void drain_state() {
    long cPtr = libtorrent_jni.upnp_drain_state(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public int add_mapping(upnp.protocol_type p, int external_port, int local_port) {
    return libtorrent_jni.upnp_add_mapping(swigCPtr, this, p.swigValue(), external_port, local_port);
  }

  public void delete_mapping(int mapping_index) {
    libtorrent_jni.upnp_delete_mapping(swigCPtr, this, mapping_index);
  }

  public boolean get_mapping(int mapping_index, SWIGTYPE_p_int local_port, SWIGTYPE_p_int external_port, SWIGTYPE_p_int protocol) {
    return libtorrent_jni.upnp_get_mapping(swigCPtr, this, mapping_index, SWIGTYPE_p_int.getCPtr(local_port), SWIGTYPE_p_int.getCPtr(external_port), SWIGTYPE_p_int.getCPtr(protocol));
  }

  public void discover_device() {
    libtorrent_jni.upnp_discover_device(swigCPtr, this);
  }

  public void close() {
    libtorrent_jni.upnp_close(swigCPtr, this);
  }

  public String router_model() {
    return libtorrent_jni.upnp_router_model(swigCPtr, this);
  }

  public enum protocol_type {
    none(0),
    udp(1),
    tcp(2);

    public final int swigValue() {
      return swigValue;
    }

    public static protocol_type swigToEnum(int swigValue) {
      protocol_type[] swigValues = protocol_type.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (protocol_type swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + protocol_type.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private protocol_type() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private protocol_type(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private protocol_type(protocol_type swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

}
