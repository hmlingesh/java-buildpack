package com.ejt.demo.server.handlers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiHandler extends Remote {
    int PORT = Integer.getInteger("rmiPort", 10099);
    String NAME = "demoServer";

    void remoteOperation() throws RemoteException;
}
