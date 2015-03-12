package com.ejt.mock.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class MockDataSource implements DataSource {

    private String uri;
    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>() {
        @Override
        protected Connection initialValue() {
            return new MockConnection(uri);
        }
    };

    public MockDataSource(String uri) {
        this.uri = uri;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionThreadLocal.get();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
