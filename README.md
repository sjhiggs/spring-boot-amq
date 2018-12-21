# Spring Boot, Camel and ActiveMQ QuickStart (MODIFIED)

This application demonstrates Red Hat Fuse on Spring-Boot by sending a batch of messages from one broker to another.  After the batch of messages is sent, the application terminates.

### Configuration
See src/main/resources/application.properties

1. Configure 'from' broker amq.source.*
2. Configure 'to' broker amq.destination.*
3. Configure number of messages to move


### Building

The example can be built with

    mvn clean install

### Running

java -jar target/spring-boot-amq-1.0.0-SNAPSHOT.jar

-or-

java -jar target/spring-boot-amq-1.0.0-SNAPSHOT.jar --amq.num-messages-to-move=2


