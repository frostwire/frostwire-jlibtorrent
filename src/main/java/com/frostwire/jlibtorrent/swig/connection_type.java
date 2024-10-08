/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.frostwire.jlibtorrent.swig;

public final class connection_type {
  public final static connection_type bittorrent = new connection_type("bittorrent");
  public final static connection_type url_seed = new connection_type("url_seed");
  public final static connection_type http_seed = new connection_type("http_seed");

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static connection_type swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + connection_type.class + " with value " + swigValue);
  }

  private connection_type(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private connection_type(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private connection_type(String swigName, connection_type swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static connection_type[] swigValues = { bittorrent, url_seed, http_seed };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}

