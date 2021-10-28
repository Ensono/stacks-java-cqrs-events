package com.amido.stacks.core.messaging.publish;

import com.amido.stacks.core.cqrs.command.ApplicationCommand;

public class TestApplicationCommand extends ApplicationCommand {

  public TestApplicationCommand(String correlationId) {
    super(correlationId);
  }

  @Override
  public int getOperationCode() {
    return 1000;
  }
}
