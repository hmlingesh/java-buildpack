package com.ejt.demo.server;

import com.ejt.mock.jdbc.MockDataSource;
import com.ejt.mock.jndi.MockInitialContextFactory;

import java.util.Map;

public class DemoInitialContextFactory extends MockInitialContextFactory {
    @Override
    protected void fillMap(Map<String, Object> map, Map<String, Integer> durations) {
        map.put("jdbc/testDB", new MockDataSource("jdbc:demo://remote_host/test"));

        map.put("connector/remote", "");
        durations.put("connector/remote", 1000);
        map.put("connector/local", "");
        durations.put("connector/local", 500);
    }
}
