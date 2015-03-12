package com.ejt.demo.server.handlers;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HandlerHelper {

    private HandlerHelper() {
    }

    public static void makeRmiCall(int rmiRegistryPort) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", rmiRegistryPort);
            RmiHandler rmiHandler = (RmiHandler)registry.lookup(RmiHandler.NAME);
            for (int i = 0; i < 3; i++) {
                rmiHandler.remoteOperation();
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void makeWebServiceCall() {
        WsHandlerService handlerService = new WsHandlerService();
        WsHandler wsHandler = handlerService.getWsHandlerPort();
        for (int i = 0; i < 3; i++) {
            wsHandler.getExchangeRate("USD", "EUR");
        }
    }
}
