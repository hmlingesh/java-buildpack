package com.ejt.mock.jpa;

import com.ejt.mock.MockHelper;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.*;

public class MockTypedQuery<T> implements TypedQuery<T> {

    private final MockEntityManager entityManager;
    private final String queryString;
    private final Class<T> clazz;

    private Random random = new Random(System.nanoTime());

    public MockTypedQuery(MockEntityManager entityManager, String queryString, Class<T> clazz) {
        this.entityManager = entityManager;
        this.queryString = queryString;
        this.clazz = clazz;
    }

    @Override
    public List<T> getResultList() {
        List<T> ret = new ArrayList<T>();
        try {
            entityManager.getConnection().setNextExecutionTimes(1000 + random.nextInt(400), 100000 + random.nextInt(100000));

            entityManager.getConnection().prepareStatement(queryString.replaceAll("select . from", "select * from").replaceAll(":.+? ", "? ").replaceAll(":.+?$", "?").toUpperCase()).executeQuery();

            MockHelper.runnable(400000 + random.nextInt(400000));

            for (int i=0; i<10; i++) {
                ret.add(clazz.newInstance());
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


        return ret;
    }

    @Override
    public T getSingleResult() {
        return null;
    }

    @Override
    public TypedQuery<T> setMaxResults(int i) {
        return null;
    }

    @Override
    public TypedQuery<T> setFirstResult(int i) {
        return null;
    }

    @Override
    public TypedQuery<T> setHint(String s, Object o) {
        return null;
    }

    @Override
    public <V> TypedQuery<T> setParameter(Parameter<V> tParameter, V t) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(Parameter<Calendar> calendarParameter, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(Parameter<Date> dateParameter, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Object o) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Object o) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setFlushMode(FlushModeType flushModeType) {
        return null;
    }

    @Override
    public TypedQuery<T> setLockMode(LockModeType lockModeType) {
        return null;
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public int getMaxResults() {
        return 0;
    }

    @Override
    public int getFirstResult() {
        return 0;
    }

    @Override
    public Map<String, Object> getHints() {
        return null;
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        return null;
    }

    @Override
    public Parameter<?> getParameter(String s) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(String s, Class<T> tClass) {
        return null;
    }

    @Override
    public Parameter<?> getParameter(int i) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(int i, Class<T> tClass) {
        return null;
    }

    @Override
    public boolean isBound(Parameter<?> parameter) {
        return false;
    }

    @Override
    public <T> T getParameterValue(Parameter<T> tParameter) {
        return null;
    }

    @Override
    public Object getParameterValue(String s) {
        return null;
    }

    @Override
    public Object getParameterValue(int i) {
        return null;
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public LockModeType getLockMode() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> tClass) {
        return null;
    }

    @Override
    public String toString() {
        return queryString;
    }
}
