package com.amido.stacks.core.azure.servicebus.publisher;

import com.amido.stacks.core.messaging.event.ApplicationEvent;

public class TestApplicationEvent extends ApplicationEvent {

  public TestApplicationEvent(TestApplicationCommand applicationCommand, String correlationId) {
    super(applicationCommand.getOperationCode(), correlationId);
  }

  @Override
  public int getEventCode() {
    return 2000;
  }
}
