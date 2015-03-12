package com.ejt.demo.server.test;

import com.ejt.demo.server.DemoServer;
import com.ejt.demo.server.SimulatorType;
import com.ejt.demo.server.controls.NoEventsSimulatorControl;
import com.ejt.demo.server.controls.RunOnceSimulatorControl;
import com.ejt.demo.server.controls.SimulatorControl;

import java.util.EnumSet;

public class TestDemoServer extends DemoServer<SimulatorControl> {

    public TestDemoServer(EnumSet<SimulatorType> simulatorTypes) {
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
        return new RunOnceSimulatorControl();
    }

    @Override
    public void stopServer() {

    }

    public static void main(String[] args) throws Exception {
        new TestDemoServer(EnumSet.of(SimulatorType.REQUEST, SimulatorType.RMI, SimulatorType.WS)).start();
    }
}
