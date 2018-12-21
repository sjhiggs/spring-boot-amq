package com.redhat.support.examples.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ShutdownGuard implements Processor {


    @Override
    public void process(final Exchange exchange) throws Exception {
        if((boolean) exchange.getProperty("shuttingDown")) {
            throw new Exception("cannot complete exchange, route is shutting down");
        }
    }

}
