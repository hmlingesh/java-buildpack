package com.ejt.mock;

import com.ejt.demo.server.DemoServer;
import com.jprofiler.agent.Agent;
import com.jprofiler.agent.AgentConstants;

public class MockHelper {

    private static final int MICRO_TO_MILLI = 1000;
    private static final int NANO_TO_MICRO = 1000;

    private MockHelper() {
    }

    public static void runnable(long microSeconds) {
        if (Boolean.getBoolean(DemoServer.PROPERTY_DEMO_PERFINO)) {
            waiting(microSeconds);
        } else {
            Agent.setOverrideThreadStatus(AgentConstants.TIME_TYPE_RUNNING);
            waiting(microSeconds);
            Agent.resetOverrideThreadStatus();
        }
    }

    public static void waiting(long microSeconds) {
        long milliSeconds = microSeconds / MICRO_TO_MILLI;
        int nanoSeconds = (int)((microSeconds - milliSeconds * MICRO_TO_MILLI) * NANO_TO_MICRO);

        try {
            Thread.sleep(milliSeconds, nanoSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void netIo(long microSeconds) {
        if (Boolean.getBoolean(DemoServer.PROPERTY_DEMO_PERFINO)) {
            waiting(microSeconds);
        } else {
            Agent.setOverrideThreadStatus(AgentConstants.TIME_TYPE_NET_IO);
            waiting(microSeconds);
            Agent.resetOverrideThreadStatus();
        }
    }
}
