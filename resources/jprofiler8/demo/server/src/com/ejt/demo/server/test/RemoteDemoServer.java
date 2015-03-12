package com.ejt.demo.server.test;

import com.ejt.demo.server.DemoServer;
import com.ejt.demo.server.SimulatorType;
import com.ejt.demo.server.controls.NoEventsSimulatorControl;
import com.ejt.demo.server.controls.SimulatorControl;

import java.util.EnumSet;

public class RemoteDemoServer extends DemoServer<SimulatorControl> {

    public RemoteDemoServer(EnumSet<SimulatorType> simulatorTypes) {
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
        return new NoEventsSimulatorControl();
    }

    @Override
    public void stopServer() {
    }

    public static void main(String[] args) throws Exception {
        new RemoteDemoServer(EnumSet.of(SimulatorType.RMI, SimulatorType.WS)).start();
    }
}
