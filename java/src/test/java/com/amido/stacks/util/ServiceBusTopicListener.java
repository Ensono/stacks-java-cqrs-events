package com.amido.stacks.util;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class ServiceBusTopicListener {

  static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) throws Exception {
    String connectionString =
        "Endpoint=sb://sb-menu-vejtxg.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=i7Rfyj1GMno4DywPIuudxaJLcS3imr/E3IKSm8o+ESg=";

    ServiceBusProcessorClient listener =
        new ServiceBusClientBuilder()
            .connectionString(connectionString)
            .processor()
            .topicName("sbt-menu-events")
            .subscriptionName("sbs-menu-events")
            .processMessage(
                context -> {
                  ServiceBusReceivedMessage message = context.getMessage();
                  try {
                    Map event =
                        objectMapper.readValue(new String(message.getBody().toString()), Map.class);
                    System.out.printf(
                        "Message received: \n\t\t\t\t\t\tMessageId = %s,"
                            + " \n\t\t\t\t\t\tSequenceNumber = %s,"
                            + " \n\t\t\t\t\t\tEnqueuedTimeUtc = %s,"
                            + " \n\t\t\t\t\t\tSubject = %s,"
                            + " \n\t\t\t\t\t\tContent: %s\n",
                        message.getMessageId(),
                        message.getSequenceNumber(),
                        message.getEnqueuedTime(),
                        message.getSubject(),
                        event.toString());
                  } catch (JsonProcessingException e) {
                    e.printStackTrace();
                  }
                })
            .processError(context -> context.getException().printStackTrace())
            .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
            .buildProcessorClient();

    listener.start();

    //    registerMessageHandlerOnClient(client);
  }

  //  static void registerMessageHandlerOnClient(SubscriptionClient receiveClient) throws Exception
  // {
  //
  //    // register the RegisterMessageHandler callback
  //    IMessageHandler messageHandler =
  //        new IMessageHandler() {
  //          // callback invoked when the message handler loop has obtained a message
  //          public CompletableFuture<Void> onMessageAsync(IMessage message) {
  //            // receives message is passed to callback
  //            try {
  //              if (message.getLabel() != null
  //                  && message.getContentType() != null
  //                  && message.getContentType().contentEquals("application/json")) {
  //                Map event = objectMapper.readValue(new String(message.getBody(), UTF_8),
  // Map.class);
  //                System.out.printf(
  //                    "Message received: \n\t\t\t\t\t\tMessageId = %s,"
  //                        + " \n\t\t\t\t\t\tSequenceNumber = %s,"
  //                        + " \n\t\t\t\t\t\tEnqueuedTimeUtc = %s,"
  //                        + " \n\t\t\t\t\t\tLabel = %s,"
  //                        + " \n\t\t\t\t\t\tContent: %s\n",
  //                    message.getMessageId(),
  //                    message.getSequenceNumber(),
  //                    message.getEnqueuedTimeUtc(),
  //                    message.getLabel(),
  //                    event.toString());
  //              }
  //            } catch (JsonProcessingException e) {
  //              e.printStackTrace();
  //            }
  //            return receiveClient.completeAsync(message.getLockToken());
  //          }
  //
  //          public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
  //            System.out.printf(exceptionPhase + "-" + throwable.getMessage());
  //          }
  //        };
  //
  //    receiveClient.registerMessageHandler(
  //        messageHandler,
  //        // callback invoked when the message handler has an exception to report
  //        // 1 concurrent call, messages aren't auto-completed, auto-renew duration
  //        new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));
  //  }
}
