package com.amido.stacks.core.messaging.listen;

import com.azure.messaging.servicebus.ServiceBusClientBuilder.ServiceBusProcessorClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "azure.servicebus.enabled", havingValue = "true")
public class ServiceBusListener implements ApplicationEventListener {

  private Logger logger = LoggerFactory.getLogger(ServiceBusListener.class);

  private ServiceBusProcessorClientBuilder clientBuilder;

  public ServiceBusListener(ServiceBusProcessorClientBuilder clientBuilder) {
    this.clientBuilder = clientBuilder;
  }

  @Override
  public void listen() {
    configureBuilder();
    ServiceBusProcessorClient client = configureBuilder();
    client.start();
  }

  private ServiceBusProcessorClient configureBuilder() {
    clientBuilder
        .processMessage(
            context -> {
              try {
                ServiceBusReceivedMessage message = context.getMessage();

                Map event = new ObjectMapper().readValue(message.getBody().toString(), Map.class);
                logger.info(
                    "Message received: \n\t\t\t\t\t\tMessageId = {},"
                        + " \n\t\t\t\t\t\tSequenceNumber = {},"
                        + " \n\t\t\t\t\t\tEnqueuedTimeUtc = {},"
                        + " \n\t\t\t\t\t\tSubject = {},"
                        + " \n\t\t\t\t\t\tContent: {}\n",
                    message.getMessageId(),
                    message.getSequenceNumber(),
                    message.getEnqueuedTime(),
                    message.getSubject(),
                    event.toString());
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
            })
        .processError(
            context ->
                logger.error(
                    "There was an error listening on " + context.getFullyQualifiedNamespace() + ":",
                    context.getException()));

    return clientBuilder.buildProcessorClient();
  }
}
