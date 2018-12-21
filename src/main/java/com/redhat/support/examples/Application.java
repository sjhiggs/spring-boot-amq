/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.support.examples;

import com.redhat.support.examples.config.AMQDestinationConfiguration;
import com.redhat.support.examples.config.AMQSourceConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.logging.Logger;

/**
 * The Spring-boot main class.
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application {

    public static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean(name = "amq-source-component")
    public ActiveMQComponent amqSourceComponent(AMQSourceConfiguration config) {
        logger.fine("creating amqp-source-component");
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(config.getUsername(), config.getPassword(), "tcp://"+ config.getHost() + ":" + config.getPort());
        ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
        policy.setQueuePrefetch(0);
        factory.setPrefetchPolicy(policy);
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(factory);
        pooledConnectionFactory.setMaxConnections(1);
        ActiveMQComponent component = new ActiveMQComponent();
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(pooledConnectionFactory);
        component.setConfiguration(configuration);
        return component;
    }

    @Bean(name = "amq-destination-component")
    public ActiveMQComponent amqDestinationComponent(AMQDestinationConfiguration config) {
        logger.fine("creating amqp-destination-component");
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(config.getUsername(), config.getPassword(), "tcp://"+ config.getHost() + ":" + config.getPort());
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(factory);
        pooledConnectionFactory.setMaxConnections(1);
        ActiveMQComponent component = new ActiveMQComponent();
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(pooledConnectionFactory);
        component.setConfiguration(configuration);
        return component;
    }

}
