package com.amido.stacks.core.messaging.publish;

import com.amido.stacks.core.messaging.event.ApplicationEvent;

public class TestApplicationEvent extends ApplicationEvent {

  public TestApplicationEvent(int operationCode, String correlationId, int eventCode) {
    super(operationCode, correlationId, eventCode);
  }
}
