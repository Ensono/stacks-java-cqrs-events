package com.amido.stacks.core.azure.servicebus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ServiceBusProperties.class)
@TestPropertySource("classpath:azure.servicebus.properties")
public class ServiceBusPropertiesPropertiesTest {

  @Autowired private ServiceBusProperties serviceBusProperties;

  @Test
  void givenUserDefinedPOJO_whenBindingPropertiesFile_thenAllFieldsAreSet() {
    assertEquals("Test_Connection_String", serviceBusProperties.getConnectionString());
    assertEquals("Test_Topic_Name", serviceBusProperties.getTopicName());
    assertEquals("Test_Subscription_Name", serviceBusProperties.getSubscriptionName());
  }
}
