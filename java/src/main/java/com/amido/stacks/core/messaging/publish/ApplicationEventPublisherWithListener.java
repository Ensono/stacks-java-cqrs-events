package com.amido.stacks.core.messaging.publish;

import com.amido.stacks.core.messaging.listen.ApplicationEventListener;

public abstract class ApplicationEventPublisherWithListener implements ApplicationEventPublisher{

  protected ApplicationEventListener applicationEventListener;

  protected ApplicationEventPublisherWithListener(ApplicationEventListener applicationEventListener) {
    this.applicationEventListener = applicationEventListener;
  }

  protected void listen(){
    applicationEventListener.listen();
  }
}
