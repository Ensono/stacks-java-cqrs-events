package com.amido.stacks.core.azure.servicebus;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@NoArgsConstructor
@ConfigurationProperties("azure.servicebus")
public class ServiceBusProperties {
  /** Service Bus connection string. */
  @NotEmpty
  @Value("connectionString")
  private String connectionString;

  /** Topic name. Entity path of the topic. */
  @NotEmpty
  @Value("topicName")
  private String topicName;

  /** Subscription name. */
  @NotEmpty
  @Value("subscriptionName")
  private String subscriptionName;
}
