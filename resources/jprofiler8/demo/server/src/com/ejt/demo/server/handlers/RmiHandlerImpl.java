package com.ejt.demo.server.handlers;

import com.ejt.mock.MockHelper;
import com.ejt.mock.jdbc.MockConnection;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Random;

public class RmiHandlerImpl implements RmiHandler {

    private Random random = new Random(System.nanoTime());
    private Context context;

    public RmiHandlerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void remoteOperation() throws RemoteException {
        performWork();
    }

    protected void performWork() {
        MockHelper.runnable(random.nextInt(100000));
        makeWebServiceCalls();
        try {
            executeJdbcStatements();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void performWork2() {
        MockHelper.runnable(random.nextInt(100000));
    }

    protected void makeWebServiceCalls() {
        int count = random.nextInt(3);
        for (int i = 0; i < count; i++) {
            HandlerHelper.makeWebServiceCall();
        }
    }

    private void executeJdbcStatements() throws SQLException, NamingException {
        MockConnection connection = (MockConnection)((DataSource)context.lookup("jdbc/testDB")).getConnection();
        connection.setNextExecutionTimes(5000 + random.nextInt(2000), 500000 + random.nextInt(500000));
        connection.createStatement().executeQuery("SELECT i.id, i.availability, i.name FROM inventory i WHERE i.delayed = 1");
    }
}
