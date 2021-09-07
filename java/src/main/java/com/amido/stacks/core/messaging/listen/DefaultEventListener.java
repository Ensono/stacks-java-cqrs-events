package com.amido.stacks.core.messaging.listen;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    value = "azure.servicebus.enabled",
    havingValue = "false",
    matchIfMissing = true)
public class DefaultEventListener implements ApplicationEventListener{

  @Override
  public void listen() {
//Nothing to listen to for default listener
  }
}
