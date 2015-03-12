package com.ejt.demo.server.handlers;

import com.ejt.mock.MockHelper;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Random;

@WebService(serviceName = "WsHandlerService", portName = "WsHandlerPort")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WsHandlerImpl {

    public static final int PORT = Integer.getInteger("wsPort", 10091);
    public static final String NAME = "ws";

    private Random random = new Random(System.nanoTime());

    public double getExchangeRate(String from, String to) {
        return lookupExchangeRate();
    }

    private double lookupExchangeRate() {
        MockHelper.runnable(50000 + random.nextInt(50000));
        return 1.5;
    }

}
