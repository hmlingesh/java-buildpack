package com.ejt.demo.server.controls;

public class RunOnceSimulatorControl extends SimulatorControl {

    private boolean complete = false;

    @Override
    public boolean isExecuteImmediately() {
        try {
            return !complete;
        } finally {
            complete = true;
        }
    }

    @Override
    protected double getRate() {
        return 0;
    }
}
