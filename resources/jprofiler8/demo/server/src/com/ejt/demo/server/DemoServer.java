package com.ejt.demo.server;

import com.ejt.demo.server.controls.SimulatorControl;
import com.ejt.demo.server.handlers.*;
import com.ejt.mock.MockCallable;
import com.ejt.mock.jms.MockMessage;
import com.ejt.mock.servlet.MockHttpServletRequest;
import com.ejt.mock.servlet.MockServlet;
import org.uncommons.maths.random.ExponentialGenerator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.ws.Endpoint;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Random;

public abstract class DemoServer<SC extends SimulatorControl> implements ServerControl<SC> {

    public static final int MILLISECONDS_TO_MINUTE = 60000;
    public static final int MAX_REQUEST_SIMULATORS = 5;
    public static final String VIEW_NAME = "demo/view";
    public static final String PROPERTY_DEMO_PERFINO = "demo.perfino";

    private EnumSet<SimulatorType> simulatorTypes;
    private Random random = new Random(System.nanoTime());

    private SC requestSimulatorControl;
    private SC jdbcJobSimulatorControl;
    private SC jmsSimulatorControl;
    private RmiHandlerImpl rmiHandler;

    public DemoServer(EnumSet<SimulatorType> simulatorTypes) {
        this.simulatorTypes = simulatorTypes;

        requestSimulatorControl = createRequestSimulatorControl();
        jdbcJobSimulatorControl = createJdbcJobSimulatorControl();
        jmsSimulatorControl = createJmsSimulatorControl();

    }

    protected abstract SC createJmsSimulatorControl();
    protected abstract SC createJdbcJobSimulatorControl();
    protected abstract SC createRequestSimulatorControl();

    @Override
    public SC getRequestSimulatorControl() {
        return requestSimulatorControl;
    }

    @Override
    public SC getJdbcJobSimulatorControl() {
        return jdbcJobSimulatorControl;
    }

    @Override
    public SC getJmsSimulatorControl() {
        return jmsSimulatorControl;
    }

    public void start() throws Exception {

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, DemoInitialContextFactory.class.getName());
        final Context context = new InitialContext(env);
        startSimulators(context);

        synchronized(this) {
            wait();
        }
    }

    protected void startSimulators(final Context context) throws RemoteException {
        if (simulatorTypes.contains(SimulatorType.WS)) {
            startWebService();
        }
        if (simulatorTypes.contains(SimulatorType.RMI)) {
            startRmiServer(context);
        }

        for (int i = 0; i < MAX_REQUEST_SIMULATORS; i++) {
            startSimulator(SimulatorType.REQUEST, i + 1, MAX_REQUEST_SIMULATORS,
                    requestSimulatorControl,
                    new MockCallable() {
                        @Override
                        public void run() throws Exception {
                            simulateRequest(context);
                        }
                    }
            );
        }

        startSimulator(SimulatorType.JDBC_JOB,
                jdbcJobSimulatorControl,
                createJdbcJobHandler(context)
        );

        startSimulator(SimulatorType.JMS,
                jmsSimulatorControl,
                new MockCallable() {
                    @Override
                    public void run() throws Exception {
                        simulateJmsMessage();
                    }
                }
        );
    }

    protected JdbcJobHandler createJdbcJobHandler(Context context) {
        return new JdbcJobHandler(context);
    }

    protected RequestHandler createRequestHandler(int viewNumber, Context context, int rmiRegistryPort) {
        return new RequestHandler(viewNumber, context, rmiRegistryPort);
    }

    protected JmsHandler createJmsHandler(int rmiRegistryPort) {
        return new JmsHandler(rmiRegistryPort);
    }

    protected RmiHandlerImpl createRmiHandlerImpl(Context context) {
        return new RmiHandlerImpl(context);
    }

    protected WsHandlerImpl createWsHandlerImpl() {
        return new WsHandlerImpl();
    }

    protected void startRmiServer(Context context) throws RemoteException {
        rmiHandler = createRmiHandlerImpl(context);
        RmiHandler stub = (RmiHandler)UnicastRemoteObject.exportObject(rmiHandler, 0);
        if (Boolean.getBoolean("perfino.logRmi")) {
            UnicastRemoteObject.setLog(System.err);
        }
        Registry registry = LocateRegistry.createRegistry(getRmiRegistryPort());
        registry.rebind(RmiHandler.NAME, stub);
    }

    protected int getRmiRegistryPort() {
        return RmiHandler.PORT;
    }

    protected void startWebService() {
        Endpoint.publish("http://localhost:" + WsHandlerImpl.PORT + "/" + WsHandlerImpl.NAME, createWsHandlerImpl());
    }

    private void simulateRequest(Context context) throws Exception {
        int viewNumber = random.nextInt(5) + 1;
        String queryString = "action=list&random=" + random.nextInt(1000);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("/" + VIEW_NAME + viewNumber, queryString);
        MockServlet servlet = new MockServlet(createRequestHandler(viewNumber, context, getRmiRegistryPort()));
        servlet.service(servletRequest, null);
    }

    private void simulateJmsMessage() {
        JmsHandler.JmsType jmsType = JmsHandler.JmsType.values()[random.nextInt(JmsHandler.JmsType.values().length)];
        int duration = jmsType.getDuration();
        createJmsHandler(getRmiRegistryPort()).onMessage(new MockMessage(jmsType.getDestination(), duration / 2 + random.nextInt(duration / 2)));
    }

    protected void startSimulator(SimulatorType simulatorType, SimulatorControl simulatorControl, MockCallable mockCallable) {
        startSimulator(simulatorType, 0, 1, simulatorControl, mockCallable);
    }

    protected void startSimulator(final SimulatorType simulatorType, final int number, final int simulatorCount, final SimulatorControl simulatorControl, final MockCallable mockCallable) {
        if (!simulatorTypes.contains(simulatorType)) {
            return;
        }

        new Thread() {
            {
                setName(simulatorType.toString() + (number > 0 ? " " + number : ""));
            }

            @Override
            public void run() {
                Random random = new Random(System.nanoTime());
                ExponentialGenerator gen = new ExponentialGenerator(simulatorControl, random);
                //noinspection InfiniteLoopStatement
                while (true) {
                    simulatorControl.waitForEnabled();

                    if (simulatorControl.isExecuteImmediately()) {
                        try {
                            mockCallable.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        long interval = Math.round(gen.nextValue() * simulatorCount * (long)MILLISECONDS_TO_MINUTE);
                        try {
                            Thread.sleep(interval);
                            mockCallable.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

}
