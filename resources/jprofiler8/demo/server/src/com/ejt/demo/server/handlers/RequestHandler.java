package com.ejt.demo.server.handlers;

import com.ejt.demo.server.entities.Customer;
import com.ejt.demo.server.entities.Order;
import com.ejt.mock.MockCallable;
import com.ejt.mock.MockHelper;
import com.ejt.mock.jpa.MockEntityManager;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@SuppressWarnings("JpaQlInspection")
public class RequestHandler implements MockCallable {

    private static final Object GLOBAL_LOCK = new Object();

    private int viewNumber;
    private Context context;
    private int rmiRegistryPort;

    private Random random = new Random(System.nanoTime());

    public RequestHandler(int viewNumber, Context context, int rmiRegistryPort) {
        this.viewNumber = viewNumber;
        this.context = context;
        this.rmiRegistryPort = rmiRegistryPort;
    }

    @Override
    public void run() throws Exception {
        workWithGlobalResource();
        performWork();
    }

    protected void workWithGlobalResource() {
        synchronized (GLOBAL_LOCK) {
            MockHelper.runnable(random.nextInt(20000));
        }
    }

    protected void performWork() {
        MockHelper.runnable(random.nextInt(20000) * viewNumber * viewNumber);
        try {
            makeJpaCall();
            makeRmiCall();
            makeWebServiceCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Random getRandom() {
        return random;
    }

    private void makeJpaCall() throws NamingException, SQLException {
        EntityManager entityManager = new MockEntityManager(Customer.class.getPackage().getName(), context, "jdbc/testDB");
        entityManager.getTransaction().begin();

        executeJpaQuery(entityManager);
        createOrder(entityManager);

        entityManager.getTransaction().commit();
        entityManager.flush();

    }

    private void executeJpaQuery(EntityManager entityManager) throws SQLException, NamingException {
        MockHelper.runnable(random.nextInt(50000));
        List<Order> resultList = entityManager.createQuery("select o from Order o where o.date >= :date", Order.class).getResultList();
        if (resultList.size() > 0 && random.nextInt(100) > 90) {
            entityManager.remove(resultList.get(0));
        }
    }

    private void createOrder(EntityManager entityManager) {
        Order order = new Order();
        order.setCustomer(new Customer());
        entityManager.persist(order);
    }

    protected void makeRmiCall() {
        HandlerHelper.makeRmiCall(rmiRegistryPort);
    }

    protected void makeWebServiceCall() {
        HandlerHelper.makeWebServiceCall();
    }

}
