package com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.event.ApplicationEvent;

public interface ApplicationEventPublisher {

  void publish(ApplicationEvent applicationEvent);
}
