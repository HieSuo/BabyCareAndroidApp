package com.example.babycareapp.data.model;

public class StatusDevice {
    public int fanStatus;
    public int swingStatus;
    public int ledStatus;

    public StatusDevice(int fanStatus, int swingStatus, int ledStatus) {
        this.fanStatus = fanStatus;
        this.swingStatus = swingStatus;
        this.ledStatus = ledStatus;
    }

    public int getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(int fanStatus) {
        this.fanStatus = fanStatus;
    }

    public int getSwingStatus() {
        return swingStatus;
    }

    public void setSwingStatus(int swingStatus) {
        this.swingStatus = swingStatus;
    }

    public int getLedStatus() {
        return ledStatus;
    }

    public void setLedStatus(int ledStatus) {
        this.ledStatus = ledStatus;
    }

    public StatusDevice() {
    }
}
