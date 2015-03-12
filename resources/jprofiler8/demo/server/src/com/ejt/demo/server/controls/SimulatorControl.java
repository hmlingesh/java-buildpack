package com.ejt.demo.server.controls;

import org.uncommons.maths.number.NumberGenerator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SimulatorControl implements NumberGenerator<Double> {

    private Lock lock = new ReentrantLock();
    private Condition enabledCondition = lock.newCondition();

    private boolean enabled = true;

    protected abstract double getRate();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        withLock(new Runnable() {
            @Override
            public void run() {
                enabledCondition.signalAll();
            }
        });
    }

    @Override
    public Double nextValue() {
        return getRate();
    }

    public void waitForEnabled() {
        while (!enabled) {
            withLock(new Runnable() {
                @Override
                public void run() {
                    try {
                        enabledCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void withLock(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    public boolean isExecuteImmediately() {
        return false;
    }
}
