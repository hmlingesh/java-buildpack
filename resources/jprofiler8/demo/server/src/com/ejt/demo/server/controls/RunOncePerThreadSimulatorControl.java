package com.ejt.demo.server.controls;

public class RunOncePerThreadSimulatorControl extends SimulatorControl {

    private ThreadLocal<Boolean> complete = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    @Override
    public boolean isExecuteImmediately() {
        try {
            return !complete.get();
        } finally {
            complete.set(Boolean.TRUE);
        }
    }

    @Override
    protected double getRate() {
        return 0;
    }
}
