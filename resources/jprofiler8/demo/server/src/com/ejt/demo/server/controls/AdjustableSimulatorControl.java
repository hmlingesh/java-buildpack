package com.ejt.demo.server.controls;

public class AdjustableSimulatorControl extends SimulatorControl {

    private double rate;

    public AdjustableSimulatorControl(double rate) {
        this.rate = rate;
    }

    @Override
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
