package com.example.babycareapp.data.model;

public class Enviroment {
    private double currentTemp;
    private double maxTemp;
    private double minTemp;
    private double currentHumidity;
    private double maxHumidity;
    private double minHumidity;

    public Enviroment(double currentTemp, double maxTemp, double minTemp, double currentHumidity, double maxHumidity, double minHumidity) {
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.currentHumidity = currentHumidity;
        this.maxHumidity = maxHumidity;
        this.minHumidity = minHumidity;
    }

    public Enviroment() {
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(double currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }
}
