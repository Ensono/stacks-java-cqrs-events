package com.xxAMIDOxx.xxSTACKSxx.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServiceBusTopicListener {

  static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) throws Exception, ServiceBusException {
    String connectionString =
        "Endpoint=sb://amido-stacks-java.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=01D8OSZUUJfyD0B3vCqWsK9jkmzzmC8SjE9kGMlpLc8=";

    SubscriptionClient client =
        new SubscriptionClient(
            new ConnectionStringBuilder(
                connectionString, "menu-updates/subscriptions/Subscription1"),
            ReceiveMode.PEEKLOCK);

    registerMessageHandlerOnClient(client);
  }

  static void registerMessageHandlerOnClient(SubscriptionClient receiveClient) throws Exception {

    // register the RegisterMessageHandler callback
    IMessageHandler messageHandler =
        new IMessageHandler() {
          // callback invoked when the message handler loop has obtained a message
          public CompletableFuture<Void> onMessageAsync(IMessage message) {
            // receives message is passed to callback
            try {
              if (message.getLabel() != null
                  && message.getContentType() != null
                  && message.getContentType().contentEquals("application/json")) {
                Map event = objectMapper.readValue(new String(message.getBody(), UTF_8), Map.class);
                System.out.printf(
                    "Message received: \n\t\t\t\t\t\tMessageId = %s,"
                        + " \n\t\t\t\t\t\tSequenceNumber = %s,"
                        + " \n\t\t\t\t\t\tEnqueuedTimeUtc = %s,"
                        + " \n\t\t\t\t\t\tLabel = %s,"
                        + " \n\t\t\t\t\t\tContent: %s\n",
                    message.getMessageId(),
                    message.getSequenceNumber(),
                    message.getEnqueuedTimeUtc(),
                    message.getLabel(),
                    event.toString());
              }
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }
            return receiveClient.completeAsync(message.getLockToken());
          }

          public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
            System.out.printf(exceptionPhase + "-" + throwable.getMessage());
          }
        };

    receiveClient.registerMessageHandler(
        messageHandler,
        // callback invoked when the message handler has an exception to report
        // 1 concurrent call, messages aren't auto-completed, auto-renew duration
        new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));
  }
}
