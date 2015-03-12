package com.ejt.demo.server;

import com.ejt.demo.server.controls.SimulatorControl;

public interface ServerControl<SC extends SimulatorControl> {
    void stopServer();
    SC getRequestSimulatorControl();
    SC getJdbcJobSimulatorControl();
    SC getJmsSimulatorControl();
}
