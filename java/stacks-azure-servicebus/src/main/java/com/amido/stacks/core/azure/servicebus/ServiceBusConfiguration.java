package com.amido.stacks.core.azure.servicebus;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder.ServiceBusProcessorClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceBusProperties.class)
@ConditionalOnProperty(
    value = "azure.servicebus.enabled",
    havingValue = "true",
    matchIfMissing = false)
public class ServiceBusConfiguration {

  private final ServiceBusProperties properties;

  public ServiceBusConfiguration(ServiceBusProperties properties) {
    this.properties = properties;
  }

  @Bean
  public ServiceBusProcessorClientBuilder serviceBusProcessorClientBuilder() {

    return new ServiceBusClientBuilder()
        .connectionString(properties.getConnectionString())
        .processor()
        .topicName(properties.getTopicName())
        .subscriptionName(properties.getSubscriptionName())
        .processError(context -> context.getException().printStackTrace())
        .receiveMode(ServiceBusReceiveMode.PEEK_LOCK);
  }

  @Bean
  public ServiceBusSenderAsyncClient serviceBusAsyncSender() {
    return new ServiceBusClientBuilder()
        .connectionString(properties.getConnectionString())
        .sender()
        .topicName(properties.getTopicName())
        .buildAsyncClient();
  }

  @Bean
  public JsonMapper jsonMapper() {
    return JsonMapper.builder()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .addModule(new JavaTimeModule())
        .build();
  }
}
