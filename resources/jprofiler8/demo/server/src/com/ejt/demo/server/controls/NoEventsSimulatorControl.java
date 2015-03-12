package com.ejt.demo.server.controls;

public class NoEventsSimulatorControl extends SimulatorControl {

    public NoEventsSimulatorControl() {
        setEnabled(false);
    }

    @Override
    protected double getRate() {
        return 0;
    }
}
