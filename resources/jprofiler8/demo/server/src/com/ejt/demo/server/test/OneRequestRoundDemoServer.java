package com.ejt.demo.server.test;

import com.ejt.demo.server.DemoServer;
import com.ejt.demo.server.SimulatorType;
import com.ejt.demo.server.controls.NoEventsSimulatorControl;
import com.ejt.demo.server.controls.RunOncePerThreadSimulatorControl;
import com.ejt.demo.server.controls.SimulatorControl;

import java.util.EnumSet;

public class OneRequestRoundDemoServer extends DemoServer<SimulatorControl> {

    public OneRequestRoundDemoServer(EnumSet<SimulatorType> simulatorTypes) {
        super(simulatorTypes);
    }

    @Override
    protected SimulatorControl createJmsSimulatorControl() {
        return new NoEventsSimulatorControl();
    }

    @Override
    protected SimulatorControl createJdbcJobSimulatorControl() {
        return new NoEventsSimulatorControl();
    }

    @Override
    protected SimulatorControl createRequestSimulatorControl() {
        return new RunOncePerThreadSimulatorControl();
    }

    @Override
    public void stopServer() {

    }

    public static void main(String[] args) throws Exception {
        new OneRequestRoundDemoServer(EnumSet.of(SimulatorType.REQUEST)).start();
    }
}
