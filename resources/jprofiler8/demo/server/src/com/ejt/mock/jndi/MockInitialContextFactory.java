package com.ejt.mock.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public abstract class MockInitialContextFactory implements InitialContextFactory {

    private Map<String, Object> map = new HashMap<String, Object>();
    private Map<String, Integer> durations = new HashMap<String, Integer>();

    public MockInitialContextFactory() {
        fillMap(map, durations);
    }

    protected abstract void fillMap(Map<String, Object> map, Map<String, Integer> durations);

    @Override
    public Context getInitialContext(Hashtable environment) throws NamingException {
        return new MockContext(map, durations);
    }

}
