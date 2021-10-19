package com.amido.stacks.core.messaging.publish;

import com.amido.stacks.core.messaging.event.ApplicationEvent;

public interface ApplicationEventPublisher {

  void publish(ApplicationEvent applicationEvent);
}
