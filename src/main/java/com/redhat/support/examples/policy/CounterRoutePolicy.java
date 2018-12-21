package com.redhat.support.examples.policy;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;


public class CounterRoutePolicy extends RoutePolicySupport implements ApplicationContextAware {

    private static int numExecutions = 1;
    private static int counter = 0;
    private Thread stop;
    private boolean shuttingDown = false;
    private ApplicationContext ctx = null;

    public CounterRoutePolicy (int numExecutions) {
        this.numExecutions = numExecutions;
    }

    private void shutdown(Route route, Exchange exchange) throws Exception {

        if (stop == null) {
            stop = new Thread() {
                @Override
                public void run() {
                    try {
                        exchange.getContext().stop();
                        ((ConfigurableApplicationContext) ctx).close();
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        stop.start();
    }

    @Override
    public void onExchangeBegin(Route route, Exchange exchange) {
        if(shuttingDown) {
            exchange.setProperty("shuttingDown", true);
        } else {
            exchange.setProperty("shuttingDown", false);
        }
    }

    @Override
    synchronized public void onExchangeDone(Route route, Exchange exchange) {

        counter++;

        if(counter >= numExecutions) {
            shuttingDown = true;
            try {
                shutdown(route, exchange);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
