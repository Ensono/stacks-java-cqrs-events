package com.amido.stacks.core.messaging.publish;

import com.amido.stacks.core.messaging.event.ApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    value = "azure.servicebus.enabled",
    havingValue = "false",
    matchIfMissing = true)
public class DefaultEventPublisher implements ApplicationEventPublisher {

  Logger logger = LoggerFactory.getLogger(DefaultEventPublisher.class);

  @Override
  public void publish(ApplicationEvent applicationEvent) {
    logger.info(applicationEvent.toString());
  }
}
