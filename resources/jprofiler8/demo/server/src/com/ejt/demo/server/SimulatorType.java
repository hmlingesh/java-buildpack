package com.ejt.demo.server;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum SimulatorType {
    WS("Web service"),
    RMI("RMI"),
    REQUEST("Servlet request simulator"),
    JDBC_JOB("JDBC Job Simulator"),
    JMS("JMS Simulator");

    public static EnumSet<SimulatorType> getFromArguments(String[] args) {
        List<SimulatorType> simulatorTypes = new ArrayList<SimulatorType>();
        for (String arg : args) {
            for (SimulatorType simulatorType : values()) {
                if (simulatorType.name().equals(arg)) {
                    simulatorTypes.add(simulatorType);
                }
            }
        }
        if (simulatorTypes.size() == 0) {
            return EnumSet.allOf(SimulatorType.class);
        } else {
            return EnumSet.copyOf(simulatorTypes);
        }
    }

    private String verbose;

    SimulatorType(String verbose) {
        this.verbose = verbose;
    }

    @Override
    public String toString() {
        return verbose;
    }
}
