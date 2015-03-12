package com.ejt.demo.server.handlers;

import com.ejt.mock.MockHelper;
import com.ejt.mock.jms.MockMessage;

import javax.jms.Message;
import javax.jms.MessageListener;

public class JmsHandler implements MessageListener {

    private int rmiRegistryPort;

    public JmsHandler(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }

    @Override
    public void onMessage(Message message) {
        handleMessage((MockMessage)message);
    }

    protected void handleMessage(MockMessage message) {
        performWork(message);

        makeRmiCall();
        makeWebServiceCall();
    }

    protected void makeRmiCall() {
        HandlerHelper.makeRmiCall(rmiRegistryPort);
    }

    protected void makeWebServiceCall() {
        HandlerHelper.makeWebServiceCall();
    }

    private void performWork(MockMessage message) {
        MockHelper.runnable(message.getDuration());
    }

    public enum JmsType {
        ORDER("orderHandler", 100000),
        MAIL("mailService", 20000),
        PAYMENT("paymentProcessor", 300000),
        DELIVERY("deliveryService", 80000);

        private String destination;
        private int duration;

        JmsType(String destination, int duration) {
            this.destination = destination;
            this.duration = duration;
        }

        public String getDestination() {
            return destination;
        }

        public int getDuration() {
            return duration;
        }
    }

}
