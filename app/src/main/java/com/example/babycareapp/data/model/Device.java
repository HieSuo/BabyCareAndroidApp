package com.example.babycareapp.data.model;

public class Device {
    private String deviceId;
    private boolean isMain;

    public Device(String deviceId, boolean isMain) {
        this.deviceId = deviceId;
        this.isMain = isMain;
    }

    public Device() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setOn(boolean main){
        isMain = main;
    }
}
