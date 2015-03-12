package com.ejt.mock.jpa;

import com.ejt.mock.MockHelper;
import com.ejt.mock.jdbc.MockConnection;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MockEntityManager implements EntityManager {

    private final String packageName;
    private final MockConnection connection;

    private List<Object> addedObjects = new ArrayList<Object>();
    private List<Object> removedObjects = new ArrayList<Object>();

    private Random random = new Random(System.nanoTime());

    public MockEntityManager(String packageName, Context context, String jndiQuery) throws NamingException, SQLException {
        this.packageName = packageName;
        connection = (MockConnection)((DataSource)context.lookup(jndiQuery)).getConnection();
    }

    public String getPackageName() {
        return packageName;
    }

    public MockConnection getConnection() {
        return connection;
    }

    @Override
    public void persist(Object o) {
        if (o == null) return;

        for (Method method : o.getClass().getMethods()) {
            if (isBeanMethod(method)) {
                try {
                    persist(method.invoke(o));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        addedObjects.add(o);
    }

    @Override
    public <T> T merge(T t) {
        return null;
    }

    @Override
    public void remove(Object o) {
        removedObjects.add(o);
    }

    @Override
    public <T> T find(Class<T> tClass, Object o) {
        return null;
    }

    @Override
    public <T> T find(Class<T> tClass, Object o, Map<String, Object> stringObjectMap) {
        return null;
    }

    @Override
    public <T> T find(Class<T> tClass, Object o, LockModeType lockModeType) {
        return null;
    }

    @Override
    public <T> T find(Class<T> tClass, Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> tClass, Object o) {
        return null;
    }

    @Override
    public void flush() {
        for (Object o : addedObjects) {
            addObject(o);
        }
        addedObjects.clear();
        for (Object o : removedObjects) {
            removeObject(o);
        }
        removedObjects.clear();
    }

    private void removeObject(Object o) {
        try {
            String originalClassName = o.getClass().getSimpleName().toUpperCase();

            connection.setNextExecutionTimes(1000 + random.nextInt(400), 20000 + random.nextInt(20000));
            connection.prepareStatement("DELETE FROM " + originalClassName + " WHERE ID = ?").execute();
            MockHelper.runnable(20000 + random.nextInt(20000));

            for (Method method : o.getClass().getMethods()) {
                if (isBeanMethod(method)) {
                    String containedClassName = method.getReturnType().getSimpleName().toUpperCase();
                    connection.setNextExecutionTimes(1000 + random.nextInt(400), 20000 + random.nextInt(20000));
                    connection.prepareStatement("DELETE FROM " + originalClassName + "_" + containedClassName  + " WHERE " + originalClassName + "_ID = ?").execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addObject(Object o) {
        try {
            String originalClassName = o.getClass().getSimpleName().toUpperCase();

            connection.setNextExecutionTimes(1000 + random.nextInt(400), 40000 + random.nextInt(40000));
            connection.prepareStatement("INSERT INTO " + originalClassName + " (ID, NAME, OPTIONS) VALUES (?, ?, ?)").execute();
            MockHelper.runnable(40000 + random.nextInt(40000));

            for (Method method : o.getClass().getMethods()) {
                if (isBeanMethod(method)) {
                    String containedClassName = method.getReturnType().getSimpleName().toUpperCase();
                    connection.setNextExecutionTimes(1000 + random.nextInt(400), 40000 + random.nextInt(40000));
                    connection.prepareStatement("INSERT INTO " + originalClassName + "_" + containedClassName  + " (" + originalClassName + "_ID, " + containedClassName + "_ID) VALUES (?, ?)").execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isBeanMethod(Method method) {
        return !method.getName().equals("getClass") && method.getName().startsWith("get") && Object.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
    }

    @Override
    public void refresh(Object o) {
    }

    @Override
    public void refresh(Object o, Map<String, Object> stringObjectMap) {
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void detach(Object o) {
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return null;
    }

    @Override
    public void setProperty(String s, Object o) {
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public Query createQuery(String s) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> tClass) {
        return new MockTypedQuery<T>(this, s, tClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> tClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return null;
    }

    @Override
    public void joinTransaction() {
    }

    @Override
    public <T> T unwrap(Class<T> tClass) {
        return null;
    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        return new EntityTransaction() {
            @Override
            public void begin() {
            }

            @Override
            public void commit() {
            }

            @Override
            public void rollback() {
            }

            @Override
            public void setRollbackOnly() {
            }

            @Override
            public boolean getRollbackOnly() {
                return false;
            }

            @Override
            public boolean isActive() {
                return false;
            }
        };
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }
}
