package org.mediasoup.droid.lib.model;

public class Info {

  public String getId() {
    return "";
  }

  public String getClientId() {
    return "";
  }

  public String getDisplayName() {
    return "";
  }

  public boolean isP2PMode() {
    return false;
  }

  public DeviceInfo getDevice() {
    return DeviceInfo.androidDevice();
  }
}
