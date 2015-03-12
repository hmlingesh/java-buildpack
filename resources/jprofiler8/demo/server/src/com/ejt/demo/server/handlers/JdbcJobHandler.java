package com.ejt.demo.server.handlers;

import com.ejt.mock.MockCallable;
import com.ejt.mock.MockHelper;
import com.ejt.mock.jdbc.MockConnection;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class JdbcJobHandler implements MockCallable {

    private Random random = new Random(System.nanoTime());

    private Context context;

    public JdbcJobHandler(Context context) {
        this.context = context;
    }

    @Override
    public void run() throws Exception {

        lookupConnectorsInJndi();
        executeJdbcStatements();
    }

    protected void executeJdbcStatements() throws SQLException, NamingException {
        MockHelper.runnable(random.nextInt(500000));
        MockConnection connection = (MockConnection)((DataSource)context.lookup("jdbc/testDB")).getConnection();
        connection.setNextExecutionTimes(5000 + random.nextInt(2000), 500000 + random.nextInt(500000));
        connection.createStatement().executeQuery("SELECT SUM(o.price * o.quantity) FROM customers c LEFT JOIN order o WHERE o.customer_id = c.id ");

        connection.setNextExecutionTimes(5000 + random.nextInt(2000), 10000 + random.nextInt(10000));
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO order_report VALUES (?, ?, ?)");
        preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
        preparedStatement.setString(2, String.valueOf(random.nextInt(100)));
        preparedStatement.setInt(3, random.nextInt(10000));
        preparedStatement.executeQuery();
    }

    private void lookupConnectorsInJndi() throws NamingException {
        context.lookup("connector/remote");
        context.lookup("connector/local");
    }

    protected Random getRandom() {
        return random;
    }
}
